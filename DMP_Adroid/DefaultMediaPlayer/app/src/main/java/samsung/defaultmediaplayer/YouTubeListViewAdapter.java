package samsung.defaultmediaplayer;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * @author Ankit Saini
 * Adapter class to populate youtube Items in YouTube tab (Listview).
 */
public class YouTubeListViewAdapter extends ArrayAdapter<YouTubeImageItem> {
    private Context mContext;
    private int mLayoutResourceId;
    private ArrayList<YouTubeImageItem> mData = new ArrayList<YouTubeImageItem>();
    public static final String TAG = "YouTubeListViewAdapter";

    public YouTubeListViewAdapter(Context context, int layoutResourceId, ArrayList<YouTubeImageItem> data)
    {
        super(context, layoutResourceId, data);
        this.mContext = context;
        this.mLayoutResourceId = layoutResourceId;
        this.mData = data;
    }

    static class ViewHolder {
        ImageView image;
        TextView title;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View rowView = convertView;
        ViewHolder holder = null;

        if(null == rowView)
        {
            LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
            rowView = inflater.inflate(mLayoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.image = (ImageView)rowView.findViewById(R.id.youtube_thumbnail);
            holder.title = (TextView)rowView.findViewById(R.id.youtube_label);
            rowView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) rowView.getTag();
        }

        YouTubeImageItem item = mData.get(position);

        /*Set YouTube Video Title & Thumbnail..*/
        String ytThumbnailURL = "http://img.youtube.com/vi/" + item.getVideoId() + "/0.jpg";
        AsyncTaskParams param = new AsyncTaskParams(holder, item.getVideoId());
        new getYouTubeVideoTitle().execute(param); /*Load Video title..*/
        try { /*Load Video thumbnail..*/
            Picasso.with(mContext).load(ytThumbnailURL).memoryPolicy(MemoryPolicy.NO_STORE).into(holder.image);
        }catch(Exception e){
            e.printStackTrace();
        }

        return rowView;
    }

    /*Class to hold asyncTask parameters..*/
    private static class AsyncTaskParams{
        ViewHolder mHolder;
        String mTitle;
        String mVideoId;

        AsyncTaskParams(ViewHolder holder, String videoId){
            this.mHolder = holder;
            this.mTitle = null;
            this.mVideoId = videoId;
        }
    }

    /*AsyncTask class - Load Video Title..*/
    private class getYouTubeVideoTitle extends AsyncTask<AsyncTaskParams/*Params*/, Void/*Progress*/, AsyncTaskParams/*Result*/> {
        @Override
        protected AsyncTaskParams doInBackground(AsyncTaskParams... params) {
            try {
                return downloadUrl(params[0]);
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(AsyncTaskParams param) {
            param.mHolder.title.setText(param.mTitle);
        }

        private AsyncTaskParams downloadUrl(AsyncTaskParams param) throws IOException
        {
            URL url = null;
            String buffer;
            JSONObject reader;
            String API_KEY = "AIzaSyCidon5OI7lwe1abm_H9aDpvCmgX-D4Yus";
            if (null == param.mVideoId/*video Id*/) {
                return param;
            }
            try
            {
                url = new URL("https://www.googleapis.com/youtube/v3/videos?part=snippet&id=" + param.mVideoId + "&key=" + API_KEY);
                buffer = IOUtils.toString(url);
                reader = new JSONObject(buffer);
                /*Parse Json buffer to search for "title"*/
                JSONArray itemsArray = reader.optJSONArray("items");
                JSONObject obj = null;
                obj = itemsArray.getJSONObject(0);
                obj = obj.getJSONObject("snippet");
                param.mTitle = obj.optString("title");
            } catch (Exception e)
            {
                Log.w(TAG, "Error: JSONObject crashed.");
                e.printStackTrace();
            }
            return param;
        }
    }

    /* Updates grid data and refresh grid items..*/
    public void setData(ArrayList<YouTubeImageItem> data) {
        this.mData = data;
        notifyDataSetChanged();
    }
}
