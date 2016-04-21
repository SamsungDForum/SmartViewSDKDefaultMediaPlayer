package samsung.defaultmediaplayer;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.samsung.multiscreen.MediaPlayer;
import com.samsung.multiscreen.Service;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * @author Ankit Saini
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link VideosFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link VideosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideosFragment extends Fragment{
    public static final String TAG = "VideosFragment";

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private OnFragmentInteractionListener mListener;

    private ListView listView;
    private VideosListViewAdapter listAdapter;

    Service service;
    //private static Application application;
    private static MediaPlayer mediaPlayer;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VideosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VideosFragment newInstance(String param1, String param2) {
        VideosFragment fragment = new VideosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public VideosFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        if (null == container) {
            Log.w(TAG, "NULL container!");
            return null;
        }
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_videos, container, false);
        listAdapter = new VideosListViewAdapter(container.getContext(),
                R.layout.layout_videos,
                getData());
        if (0 == listAdapter.getCount()) {
            Log.w(TAG, "No Videos Found!");
            return view;
        }

        listView = (ListView) view.findViewById(R.id.videosListView);
        if (null == listView) {
            Log.w(TAG, "Empty listView!!!");
            return view;
        }

        listView.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();

        /*On Click Item Listener implementation for videos ListView..*/
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*Check if DMP is supported.*/
                if (!MediaLauncherSingleton.getInstance().isDMPSupported()) {
                    Toast.makeText(getActivity(), "TV doesn't support DefaultMediaPlayer.\nPlease connect another TV.", Toast.LENGTH_SHORT).show();
                } else {
                    if (MediaLauncherSingleton.getInstance().getService() != null &&
                            CastStateMachineSingleton.getInstance().getCurrentCastState() == CastStates.CONNECTED) {
                        /*Get position of the clicked image..*/
                        final VideosImageItem item = (VideosImageItem) parent.getItemAtPosition(position);

                        /*Send this url to TV..*/
                        Log.v(TAG, "Sending video url to TV..");
                        MediaLauncherSingleton.getInstance().playContent(item.getUrl());

                        /*Open Controls Dialogue..*/
                        Toast.makeText(getActivity(), "Launching..", Toast.LENGTH_SHORT).show();
                        PlaybackControls.getInstance(getActivity()).showPlayBackControls(item.getImageUrl());
                    } else {
                        Toast.makeText(getActivity(), "Please connect to a TV.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        return view;
    }

    private String AssetJSONFile(String filename, Context context) {
        try {
            AssetManager manager = context.getAssets();
            InputStream is = manager.open(filename);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();

            return new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //prepare data for listview..
    private ArrayList<VideosImageItem> getData()
    {
        final ArrayList<VideosImageItem> imageItems = new ArrayList<VideosImageItem>();
        try {
            JSONObject obj = new JSONObject(AssetJSONFile("videolist.json", getActivity()/*container.getContext()*/));
            JSONArray jarray = obj.getJSONArray("movies");

            for (int i = 0; i < jarray.length(); i++) {
                JSONObject json = jarray.getJSONObject(i);
                imageItems.add(new VideosImageItem(json.getInt("id"), json.getString("title"),
                        json.getString("imgUrl"), json.getString("url")));
            }
        } catch(Exception e){
            Log.d(TAG, "Exception: " + e);
        }

        return imageItems;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }
}
