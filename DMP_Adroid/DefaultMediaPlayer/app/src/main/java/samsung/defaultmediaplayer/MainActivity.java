package samsung.defaultmediaplayer;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.samsung.multiscreen.Search;
import com.samsung.multiscreen.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ankit Saini
 * Main Activity class. Handles the "Videos" & "YouTube" tabs & Cast menu button.
 */
public class MainActivity
        extends AppCompatActivity
        implements VideosFragment.OnFragmentInteractionListener,
        YouTubeFragment.OnFragmentInteractionListener,
        Observer {

    public static final String TAG = "MainActivity";

    private Service mService = null;
    private Search mTVSearch = null;
    private TVListAdapter mTVListAdapter;
    private Handler mTVLsitHandler = new Handler();

    private Menu mMenu = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Toolbar toolbar;
        TabLayout tabLayout;
        ViewPager viewPager;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*ActionBar & ViewPager setup*/
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e);
        }

        //Show Dialog to display TV List.
        populateDeviceList();

        //Setting up viewpager for tabbed layout
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        //Registering function for CastStateMachine Observer..
        CastStateMachineSingleton.getInstance().registerObserver(this);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        /*De-registering CastStateMachine observer..*/
        CastStateMachineSingleton.getInstance().removeObserver(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_connectTV) {
            if(CastStateMachineSingleton.getInstance().getCurrentCastState() == CastStates.CONNECTED) {
                displayConnectedDeviceInfo();
            } else if(CastStateMachineSingleton.getInstance().getCurrentCastState() == CastStates.IDLE){
                //Show Dialog to display TV List.
                populateDeviceList();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri)
    {
        //nothing to do here!
    }

    /**
     * Handle cast icon changes as per change in device connection status.
     * @param currentCastState
     */
    @Override
    public void onCastStatusChange(CastStates currentCastState){
        if(mMenu != null) {
            MenuItem menuItem = (MenuItem) mMenu.findItem(R.id.action_connectTV);
            if(menuItem != null) {
                if (currentCastState == CastStates.IDLE) {
                    menuItem.setIcon(MainActivity.this.getResources().getDrawable(R.drawable.cast_black_idle));
                    PlaybackControls.getInstance(MainActivity.this).dismissPlayBackControls();
                } else if (currentCastState == CastStates.CONNECTING) {
                    AnimationDrawable castButtonAnimation = (AnimationDrawable) MainActivity.this.getResources().getDrawable(R.drawable.casting_menu_item_animation);
                    menuItem.setIcon(castButtonAnimation);
                    if(castButtonAnimation != null) {
                        castButtonAnimation.start();
                    }
                } else if (currentCastState == CastStates.CONNECTED) {
                    menuItem.setIcon(MainActivity.this.getResources().getDrawable(R.drawable.cast_blue_connected));
                }
            }
        }
    }

    /*
     * Setting up ViewPager (fragments) on MainAcitivty..
     */
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new VideosFragment(), "Videos");
        adapter.addFragment(new YouTubeFragment(), "YouTube");
        viewPager.setAdapter(adapter);
    }

    /*
     * Adapter class to add fragments (video & youtube tabs).
     */
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    /*
     * Check network connectivity..
     */
    private boolean checkNetworkConnectivity() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected())
        {
            return true;
        } else
        {
            return false;
        }
    }

    /**
     *
     * [DISCOVERY] Populate Connected TV List..
     *
     */
    private void populateDeviceList() {
        if(!checkNetworkConnectivity()) {
            Toast.makeText(this, "No Network Connection", Toast.LENGTH_SHORT).show();
        } else {
            final Dialog lstDialog = new Dialog(this);
            lstDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            LayoutInflater serviceList = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = serviceList.inflate(R.layout.layout_tvlist, null, false);
            lstDialog.setContentView(view);
            lstDialog.setCancelable(true);
            ImageView castingIcon = (ImageView)lstDialog.findViewById(R.id.imgCastIcon);
            AnimationDrawable castButtonAnimation = (AnimationDrawable) MainActivity.this.getResources().getDrawable(R.drawable.casting_icon_animation);
            castingIcon.setBackground(castButtonAnimation);
            if(castButtonAnimation != null) {
                castButtonAnimation.start();
            }

            mTVListAdapter = new TVListAdapter(MainActivity.this, R.layout.layout_tvlist_item);

            //fill tvList..
            final ListView lstConnectedTv = (ListView) lstDialog.findViewById(R.id.tvList);
            lstConnectedTv.setAdapter(mTVListAdapter);

            /*start discovery..*/
            new Thread(new Runnable() {
                @Override
                public void run() {
                    startDiscovery();
                }
            }).start();

            //display dialog (list view)..
            lstDialog.show();

            //set cast state as Connecting..
            CastStateMachineSingleton.getInstance().setCurrentCastState(CastStates.CONNECTING);

            lstConnectedTv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    /*Get selected TV's service object*/
                    mService = (Service) parent.getItemAtPosition(position);

                    /*Set service for the app*/
                    MediaLauncherSingleton.getInstance().setService(MainActivity.this, mService);

                    /*Stop discovery*/
                    stopDiscovery();

                    /*Dismiss TV List Dialog*/
                    lstDialog.dismiss();
                }
            });

            lstDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    if (mTVSearch.isSearching()) {
                        stopDiscovery();
                    }
                    if(CastStateMachineSingleton.getInstance().getCurrentCastState() != CastStates.CONNECTED) {
                        CastStateMachineSingleton.getInstance().setCurrentCastState(CastStates.IDLE);
                    }
                }
            });
        }
    }

    /*
     * Method to duisplay connected device information & show "Disconnect" button.
     */
    private void displayConnectedDeviceInfo() {
        final Dialog lstDialog = new Dialog(this);
        lstDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater serviceList = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = serviceList.inflate(R.layout.layout_disconnect_tv, null, false);
        lstDialog.setContentView(view);
        lstDialog.setCancelable(true);

        TextView connectedTVName = (TextView)lstDialog.findViewById(R.id.txtConnectedTVName);
        connectedTVName.setText(mService.getName());
        lstDialog.show();

        Button btnDisconnectTV = (Button) lstDialog.findViewById(R.id.btnDisconnect);
        btnDisconnectTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MediaLauncherSingleton.getInstance().disconnect();
                CastStateMachineSingleton.getInstance().setCurrentCastState(CastStates.IDLE);
                lstDialog.dismiss();
            }
        });
    }

    /*
     * Method to notify TV List data change.
     */
    private void notifyDataChange() {
        mTVLsitHandler.post(new Runnable() {
            @Override
            public void run() {
                mTVListAdapter.notifyDataSetChanged();
            }
        });
    }

    /*
     * Method to update (add) new service (tv) to ListView adapter.
     */
    private void updateTVList(Service service) {
        if(null == service)
        {
            Log.w(TAG, "updateTVList(): NULL service!!!");
            return;
        }

        /*If service already doesn't exist in TVListAdapter, add it*/
        if(!mTVListAdapter.contains(service))
        {
            mTVListAdapter.add(service);
            Log.v(TAG, "TVListAdapter.add(service): " + service);
            notifyDataChange();
        }
    }

    /*Start TV Discovery..*/
    private void startDiscovery() {
        Log.v(TAG, "startDicovery() execution started..");

        if(null == mTVSearch)
        {
            mTVSearch = Service.search(MainActivity.this);
            Log.v(TAG, "Device (" + mTVSearch + ") Search instantiated..");
            mTVSearch.setOnServiceFoundListener(new Search.OnServiceFoundListener() {
                @Override
                public void onFound(Service service) {
                    Log.v(TAG, "setOnServiceFoundListener(): onFound(): Service Added: " + service);
                    updateTVList(service);
                }
            });

            mTVSearch.setOnStartListener(new Search.OnStartListener() {
                @Override
                public void onStart() {
                    Log.v(TAG, "Starting Discovery.");
                }
            });

            mTVSearch.setOnStopListener(new Search.OnStopListener() {
                @Override
                public void onStop() {
                    Log.v(TAG, "Discovery Stopped.");
                }
            });

            mTVSearch.setOnServiceLostListener(new Search.OnServiceLostListener() {
                @Override
                public void onLost(Service service) {
                    Log.v(TAG, "Discovery: Service Lost!!!");
                    /*remove TV*/
                    if (null == service) {
                        return;
                    }
                    mTVListAdapter.remove(service);
                    notifyDataChange();
                }
            });
        }

        boolean bStartDiscovery = mTVSearch.start();
        if(bStartDiscovery)
        {
            Log.v(TAG, "Discovery Already Started..");
        }
        else
        {
            Log.v(TAG, "New Discovery Started..");
        }
    }

    /* Stop TV Discovery*/
    private void stopDiscovery() {
        if (null != mTVSearch)
        {
            mTVSearch.stop();
            mTVSearch = null;
            Log.v(TAG, "Stopping Discovery.");
        }
    }
}
