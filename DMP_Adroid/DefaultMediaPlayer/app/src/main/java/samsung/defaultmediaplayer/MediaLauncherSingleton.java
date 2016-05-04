package samsung.defaultmediaplayer;

/**
 * @author Ankit Saini
 * Singleton class to store selected service & other related objects
 * It also handles all the callbacks from MediaPlayer and sets respective
 * callbacks for playbackControls to handle playback events.
 */

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.samsung.multiscreen.Channel;
import com.samsung.multiscreen.Client;
import com.samsung.multiscreen.MediaPlayer;
import com.samsung.multiscreen.Result;
import com.samsung.multiscreen.Service;

import java.util.ArrayList;
import java.util.List;


public class MediaLauncherSingleton
{
    public static final String TAG = "MediaLauncherSingleton";
    private static MediaLauncherSingleton mInstance = null;
    private Service mService;
    private static MediaPlayer mMediaPlayer = null;
    private List<Observer> listners = new ArrayList<Observer>();
    private Context mContext = null;
    private boolean mIsDMPSupported = false;

    private MediaLauncherSingleton(){
    }

    private void initMediaPlayer()
    {
        mMediaPlayer = this.mService.createMediaPlayer();

        /*Get MSF info from TV.*/
        getAppInfo();
    }

    public static MediaLauncherSingleton getInstance() {
        if(null == mInstance){
            mInstance = new MediaLauncherSingleton();
        }
        return mInstance;
    }

    public void setService(final Context context, final Service service){
        this.mService = service;
        this.mContext = context;
        initMediaPlayer();
        CastStateMachineSingleton.getInstance().setCurrentCastState(CastStates.CONNECTED);
    }

    private boolean isDMPSupported() {
        return mIsDMPSupported;
    }

    private void setDMPSupported(boolean status) {
        mIsDMPSupported = status;
    }

    /**
     * Fetch MSF information from TV & check if MSF version is 1.0.0,
     * then, Default Media Player is not supported by T.V.
     */
    private void getAppInfo() {
        mMediaPlayer.getInfo(new Result<com.samsung.multiscreen.ApplicationInfo>() {
            @Override
            public void onSuccess(com.samsung.multiscreen.ApplicationInfo appInfo) {
                if (appInfo != null) {
                    Log.d(TAG, "App Info: " + appInfo.toString());
                    if (appInfo.getVersion().contains("1.0.0")) {
                        setDMPSupported(false);
                    } else {
                        setDMPSupported(true);
                    }
                }
            }

            @Override
            public void onError(com.samsung.multiscreen.Error error) {
                Log.d(TAG, "Error: " + error.toString());
            }
        });
    }

    private void resetService(){
        this.mService = null;
        this.mContext = null;
        CastStateMachineSingleton.getInstance().setCurrentCastState(CastStates.IDLE);
    }

    public Boolean isConnected() {
        return mMediaPlayer.isConnected();
    }

    public void disconnect() {
        mMediaPlayer.disconnect(new Result<Client>() {
            @Override
            public void onError(com.samsung.multiscreen.Error error) {
                Log.v(TAG, "disconnect(): Error: " + error);
            }

            @Override
            public void onSuccess(Client client) {
                Log.v(TAG, "disconnect(): Success: " + client);
                CastStateMachineSingleton.getInstance().setCurrentCastState(CastStates.IDLE);
            }

        });
    }

    public Service getService(){
        return this.mService;
    }

    /**
     * Method to play content on T.V.
     * @param uri : Url of content which has to be launched on TV.
     * @param thumbnail : Thumbnail url.
     */
    public void playContent(final String uri, final String thumbnail) {
        if (null != mMediaPlayer && null != mService) {
            Log.v(TAG, "Playing Content: " + uri);
            if(isDMPSupported()) {
                mMediaPlayer.playContent(Uri.parse(uri), new Result<Boolean>() {
                    @Override
                    public void onSuccess(Boolean r) {
                        Log.v(TAG, "playContent(): onSuccess.");
                        PlaybackControls.getInstance(mContext).showPlayBackControls(thumbnail);
                    }

                    @Override
                    public void onError(com.samsung.multiscreen.Error error) {
                        Log.v(TAG, "playContent(): onError.");
                        Toast.makeText(mContext, "Error in Launching Content!", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(mContext, "TV doesn't support DefaultMediaPlayer.\n" +
                        "Please connect another TV.", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Log.v(TAG, "playContent(): un-initialized mMediaPlayer.");
        }

        mMediaPlayer.setOnConnectListener(new Channel.OnConnectListener() {
            @Override
            public void onConnect(Client client) {
                Log.v(TAG, "setOnConnectListener() called!");
            }
        });

        mMediaPlayer.setOnDisconnectListener(new Channel.OnDisconnectListener() {
            @Override
            public void onDisconnect(Client client) {
                PlaybackControls.getInstance(mContext).dismissPlayBackControls();
                resetService();
                Log.v(TAG, "setOnDisconnectListener() called!");
            }
        });


        mMediaPlayer.setOnErrorListener(new Channel.OnErrorListener() {
            @Override
            public void onError(com.samsung.multiscreen.Error error) {
                Log.v(TAG, "setOnErrorListener() called: Error: " + error.toString());
            }
        });
    }

    /*playback controls*/
    public void play(){
        mMediaPlayer.play();
    }

    public void pause(){
        mMediaPlayer.pause();
    }

    public void stop(){
        mMediaPlayer.stop();
    }

    public void forward(){
        mMediaPlayer.forward();
    }

    public void rewind(){
        mMediaPlayer.rewind();
    }

    public void mute(){
        mMediaPlayer.mute();
    }

    public void unmute(){
        mMediaPlayer.unMute();
    }
}