##Prerequisite
###1. [SmartView SDK Android Library](http://www.samsungdforum.com/AddLibrary/SmartViewDownload):  Android Package(Mobile)
	
	added source /libs/android-smartview-sdk-2.0.16.jar

###2. [Picasso](http://repo1.maven.org/maven2/com/squareup/picasso/picasso/2.5.2/picasso-2.5.2.jar):  To handle Image (thumbnail) download
	
	added source /libs/picasso-2.5.2.jar


###3. Android Permmissions
    <!-- Required for fetching feed data. -->
    <uses-permission android:name="android.permission.INTERNET"/>
    <!-- Required because we're use msf lib -->
    <uses-permission android:name="android.permission.CHANGE_WIFI_MULTICAST_STATE"/>
    <!-- Required because we're use msf lib -->
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
    <!-- Required because we're use msf lib (discovery over BLE)-->
    <uses-permission android:name="android.permission.BLUETOOTH"/>
 
 
## Discover : Search devices around your mobile.
1. If you push search button in ActionBar, it starts search API.
2. You can configure the device list to override onFound(), onLost() listener.
3. Search stop API is called when you click a item of devices list.

 


	MainActivity.java
	
        private void notifyDataChange()
        {
            mTVLsitHandler.post(new Runnable() {
                @Override
                public void run() {
                    mTVListAdapter.notifyDataSetChanged();
                }
            });
        }

        private void updateTVList(Service service)
        {
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
        private void startDiscovery()
        {
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


## Create MediaPlayer object and launch a TV app.

1. Get a Service instance from devices list.
2. And create a application instance using MediaPlayer.
3. Now, you are ready to launch the content on TV. Call PlayContent(String uri).



	MainActivity.java
	
        lstConnectedTv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*Get selected TV's service object*/
                final Service service = (Service) parent.getItemAtPosition(position);
                /*Set service for the app*/
                MediaLauncherSingleton.getInstance().setService(service);
                /*TV Connected, Show Video List*/
                deviceConnected(true);

                /*Stop discovery*/
                stopDiscovery();

                /*Dismiss TV List Dialog*/
                lstDialog.dismiss();
            }
        });


    MediaLauncherSingleton.java
    
        mMediaPlayer.playContent(Uri.parse(uri), new Result<Boolean>() {
            @Override
            public void onSuccess(Boolean r) {
                deviceConnectionObserver(true);
                Log.v(TAG, "playContent(): onSuccess.");
            }

            @Override
            public void onError(com.samsung.multiscreen.Error error) {
                deviceConnectionObserver(false);
                Log.v(TAG, "playContent(): onError.");
            }
        });
 

 
 


##Event Handling
1. You can check to connect, disconnect event and to join other devices, also to catch a error.
2. To handle playback key events (like play, pause, stop, mute, etc), use respective MediaPlayer APIs.

 

    MediaLauncherSingleton.java

        mMediaPlayer.setOnConnectListener(new Channel.OnConnectListener() {
            @Override
            public void onConnect(Client client) {
                deviceConnectionObserver(true);
                Log.v(TAG, "setOnConnectListener() called!");
            }
        });

        mMediaPlayer.setOnDisconnectListener(new Channel.OnDisconnectListener() {
            @Override
            public void onDisconnect(Client client) {
                deviceConnectionObserver(false);
                Log.v(TAG, "setOnDisconnectListener() called!");

            }
        });


        mMediaPlayer.setOnErrorListener(new Channel.OnErrorListener() {
            @Override
            public void onError(com.samsung.multiscreen.Error error) {
                Log.v(TAG, "setOnErrorListener() called: Error: " + error.toString());
            }
        });
        
        
        
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
 

## Use YouTube API
1. You must acquire a YouTube API KEY. Check-out this link for more information (https://developers.google.com/youtube/v3/getting-started).
2. To create video URLs, you may use either of the following URLs
    a. "http://www.youtube.com/watch?v=" + "videoId"
    b. "http://www.youtube.com/embed/" + "videoId"
3. To fetch video thumbnail, use following URL
    "http://img.youtube.com/vi/" + "videoId" + "/0.jpg"
4. To fetch video title, use following URL
    "https://www.googleapis.com/youtube/v3/videos?part=snippet&id=" + videoId + "&key=" + API_KEY


## Stream Video/YouTube Data
Video & YouTube Data is stored in a json file under assets folder.


    videolist.json
    
        {
		  "movies": [
			{
			  "id": 1,
			  "title": "Big Buck Bunny",
			  "imgUrl": "http://www.samsungdforum.com/smartview/sample/image/BigBuckBunny.jpg",
			  "url": "http://www.samsungdforum.com/smartview/sample/video/BigBuckBunny.mp4"
			},
			{
			  "id": 2,
			  "title": "Elephant Dream",
			  "imgUrl": "http://www.samsungdforum.com/smartview/sample/image/ElephantsDream.jpg",
			  "url": "http://www.samsungdforum.com/smartview/sample/video/ElephantsDream.mp4"
			},
			{
			  "id": 3,
			  "title": "Sintel",
			  "imgUrl": "http://www.samsungdforum.com/smartview/sample/image/Sintel.jpg",
			  "url": "http://www.samsungdforum.com/smartview/sample/video/Sintel.mp4"
			},
			{
			  "id": 4,
			  "title": "Tears of Steel",
			  "imgUrl": "http://www.samsungdforum.com/smartview/sample/image/TearsOfSteel.jpg",
			  "url": "http://www.samsungdforum.com/smartview/sample/video/TearsOfSteel.mp4"
			},
			{
			  "id": 5,
			  "title": "SmartView SDK Demo",
			  "imgUrl": "http://www.samsungdforum.com/smartview/sample/image/SamsungSamrtViewSDKDemo.jpg",
			  "url": "http://www.samsungdforum.com/smartview/sample/video/SamsungSamrtViewSDKDemo.mp4"
			},
			{
			  "id": 6,
			  "title": "Samsung SmartView SDK",
			  "imgUrl": "http://www.samsungdforum.com/smartview/sample/image/SamsungSmartViewSDK.jpg",
			  "url": "http://www.samsungdforum.com/smartview/sample/video/SamsungSmartViewSDK.mp4"
			}
		  ]
		}
        
    youtubelist.json

        {
          "movies": [
            {
              "id": 1,
              "url": "fwkJtgVswgM"
            },
            {
              "id": 2,
              "url": "_DfL0zM4TJ8"
            },
            {
              "id": 3,
              "url": "jZvC7NWkeA0"
            },
            {
              "id": 4,
              "url": "3bEtJgnd86M"
            },
            {
              "id": 5,
              "url": "WEjbnQj6BrY"
            },
            {
              "id": 6,
              "url": "6jiNS_4CEug"
            },
            {
              "id": 7,
              "url": "onXpKXbnbE0"
            }
          ]
        }