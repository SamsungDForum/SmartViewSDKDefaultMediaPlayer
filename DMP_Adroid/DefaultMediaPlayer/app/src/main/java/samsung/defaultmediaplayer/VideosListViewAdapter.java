package samsung.defaultmediaplayer;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * @author Ankit Saini
 * Adapter class to populate video Items in Videos tab (Listview).
 */
public class VideosListViewAdapter extends ArrayAdapter<VideosImageItem> {
    private Context mContext;
    private int mLayoutResourceId;
    private ArrayList<VideosImageItem> mData = new ArrayList<VideosImageItem>();

    public VideosListViewAdapter(Context context, int layoutResourceId, ArrayList<VideosImageItem> data)
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

    private static int VideoCount;

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
            holder.image = (ImageView)rowView.findViewById(R.id.video_thumbnail);
            holder.title = (TextView)rowView.findViewById(R.id.video_label);
            rowView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) rowView.getTag();
        }

        VideosImageItem item = mData.get(position);
        /*Download the image from url and display on image view..*/
        Picasso.with(mContext).load(item.getImageUrl()).into(holder.image);
        holder.title.setText(item.getTitle());
        return rowView;
    }

    /**
     * Updates grid data and refresh grid items..
     * @param data
     */
    public void setData(ArrayList<VideosImageItem> data) {
        this.mData = data;
        notifyDataSetChanged();
    }
}
