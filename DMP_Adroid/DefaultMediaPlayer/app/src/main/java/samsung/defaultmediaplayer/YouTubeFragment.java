package samsung.defaultmediaplayer;

import android.app.Activity;
import android.app.Dialog;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.samsung.multiscreen.MediaPlayer;
import com.samsung.multiscreen.Service;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

//import android.app.Fragment;


/**
 * @author Ankit Saini
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link YouTubeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link YouTubeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class YouTubeFragment extends Fragment {
    public static final String TAG = "ML-YouTubeFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Button btnPlayPause;
    private Button btnRewind;
    private Button btnForward;
    private Button btnStop;
    private Button btnMute;
    private boolean isMute = false;
    private boolean isPlaying = false;
    private Dialog dlgVideoControls;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private ListView listView;
    private YouTubeListViewAdapter listAdapter;

    Service service;
    //private static Application application;
    private static MediaPlayer mediaPlayer;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment YouTubeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static YouTubeFragment newInstance(String param1, String param2) {
        YouTubeFragment fragment = new YouTubeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public YouTubeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        Log.v(TAG, "onCreate() called.");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView() called.");
        if(null == container)
        {
            Log.w(TAG, "NULL container!");
            return null;
        }
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_youtube, container, false);
        listAdapter = new YouTubeListViewAdapter(container.getContext(),
                R.layout.layout_youtube,
                getData());
        if(0 == listAdapter.getCount())
        {
            Log.w(TAG, "No YouTube Videos Found!");
            return view;
        }

        listView = (ListView) view.findViewById(R.id.youtubeListView);
        if(null == listView)
        {
            Log.w(TAG, "Empty listView!!!");
            return view;
        }

        listView.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();

        /*On Click Item Listener implementation for videos ListView..*/
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (CastStateMachineSingleton.getInstance().getCurrentCastState() == CastStates.CONNECTED) {

                    /*Get position of the clicked image..*/
                    YouTubeImageItem item = (YouTubeImageItem) parent.getItemAtPosition(position);

                    /*Create video Url from youtube id*/
                    String ytVideoURL = "http://www.youtube.com/embed/" + item.getVideoId();
                    /*Create thumbnail URL from youtube id*/
                    String thumbnailUrl = "http://img.youtube.com/vi/" + item.getVideoId() + "/0.jpg";

                    /*Send this url to TV..*/
                    MediaLauncherSingleton.getInstance().playContent(ytVideoURL, thumbnailUrl);
                } else {
                    Toast.makeText(getActivity(), "Please connect to a TV.", Toast.LENGTH_SHORT).show();
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
    private ArrayList<YouTubeImageItem> getData()
    {
        final ArrayList<YouTubeImageItem> imageItems = new ArrayList<YouTubeImageItem>();
        try {
            JSONObject obj = new JSONObject(AssetJSONFile("youtubelist.json", getActivity()/*container.getContext()*/));
            JSONArray jarray = obj.getJSONArray("movies");

            for (int i = 0; i < jarray.length(); i++) {
                JSONObject json = jarray.getJSONObject(i);
                imageItems.add(new YouTubeImageItem(json.getInt("id"), json.getString("url")));
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
