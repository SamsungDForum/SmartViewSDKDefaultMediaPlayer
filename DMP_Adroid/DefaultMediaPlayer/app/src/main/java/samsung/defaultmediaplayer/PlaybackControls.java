package samsung.defaultmediaplayer;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

/**
 * @author Ankit Saini
 * This class manages the playback controls for videos & youtube.
 */
public class PlaybackControls extends Dialog implements View.OnClickListener{
    private static final String TAG = "PlaybackControls";
    private static PlaybackControls mInstance = null;

    private static Dialog mDialog;
    private View mDialogView;
    private Button btnPlayPause;
    private Button btnStop;
    private Button btnForward;
    private Button btnRewind;
    private Button btnMuteUnmute;
    private ImageView thumbnail;

    private Boolean isMute;
    private Boolean isPlaying;

    public void showPlayBackControls(String thumbnailImageUrl) {
        mDialog = new Dialog(getContext());
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater playbackControls = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mDialogView = playbackControls.inflate(R.layout.layout_playback_controls, null, false);

        mDialog.setContentView(mDialogView);
        mDialog.setCancelable(true);


        btnPlayPause    = (Button)mDialogView.findViewById(R.id.btnPlay);
        btnStop         = (Button)mDialogView.findViewById(R.id.btnStop);
        btnForward      = (Button)mDialogView.findViewById(R.id.btnForward);
        btnRewind       = (Button)mDialogView.findViewById(R.id.btnRewind);
        btnMuteUnmute   = (Button)mDialogView.findViewById(R.id.btnMute);
        thumbnail       = (ImageView)mDialogView.findViewById(R.id.thumbnail);

        btnPlayPause.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnForward.setOnClickListener(this);
        btnRewind.setOnClickListener(this);
        btnMuteUnmute.setOnClickListener(this);

        if(null != thumbnailImageUrl) {
            thumbnail.setBackgroundColor(getContext().getResources().getColor(R.color.BLACK));
            Picasso.with(getContext()).load(thumbnailImageUrl).memoryPolicy(MemoryPolicy.NO_STORE).into(thumbnail);
        }

        isPlaying   = true;
        isMute      = false;

        mDialog.show();
    }

    public void dismissPlayBackControls() {
        if(null != mDialog && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        mInstance = null;
    }

    private PlaybackControls(Context context) {
        super(context);
    }

    public static PlaybackControls getInstance(Context context){
        /*create a new instance each time.
         *Memory management is taken care of (by java's garbage collector)*/
        mInstance = new PlaybackControls(context);
        return mInstance;
    }

    @Override
    public void onClick(View view){
        Log.v(TAG, "onClick() called..");
        if(!MediaLauncherSingleton.getInstance().isConnected()) {
            Log.v(TAG, "onClick(): isConnected: false");
            return;
        }
        if(view == view.findViewById(R.id.btnPlay)){
            if(isPlaying){
                Log.v(TAG, "onClick(): Pause");
                MediaLauncherSingleton.getInstance().pause();
                btnPlayPause.setBackground(getContext().getResources().getDrawable(R.drawable.play));
                isPlaying   = false;
            }
            else {
                Log.v(TAG, "onClick(): Play");
                MediaLauncherSingleton.getInstance().play();
                btnPlayPause.setBackground(getContext().getResources().getDrawable(R.drawable.pause));
                isPlaying   = true;
            }
        }
        else if(view == view.findViewById(R.id.btnStop)){
            Log.v(TAG, "onClick(): Stop");
            MediaLauncherSingleton.getInstance().stop();
            btnPlayPause.setBackground(getContext().getResources().getDrawable(R.drawable.play));
            isPlaying = false;
        }
        else if(view == view.findViewById(R.id.btnForward)){
            Log.v(TAG, "onClick(): Forward");
            MediaLauncherSingleton.getInstance().forward();
        }
        else if(view == view.findViewById(R.id.btnRewind)){
            Log.v(TAG, "onClick(): Rewind");
            MediaLauncherSingleton.getInstance().rewind();
        }
        else if(view == view.findViewById(R.id.btnMute)){
            if(isMute){
                Log.v(TAG, "onClick(): Unmute");
                MediaLauncherSingleton.getInstance().unmute();
                btnMuteUnmute.setBackground(getContext().getResources().getDrawable(R.drawable.mute));
                isMute      = false;
            }
            else {
                Log.v(TAG, "onClick(): Mute");
                MediaLauncherSingleton.getInstance().mute();
                btnMuteUnmute.setBackground(getContext().getResources().getDrawable(R.drawable.unmute));
                isMute      = true;
            }
        }
    }
}

