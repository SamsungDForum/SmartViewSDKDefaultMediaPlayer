/*

Copyright (c) 2014 Samsung Electronics

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.

*/

import Foundation
import AssetsLibrary
import SmartView

class PhotoShareController: NSObject, ServiceSearchDelegate, ChannelDelegate, MediaPlayerDelegate {
    /// The service discovery
    let search = Service.search()
    //var app: Application?
    var appURL: String = "http://prod-multiscreen-examples.s3-website-us-west-1.amazonaws.com/examples/photoshare/tv/"
    //var appURL: String = "http://www.google.com"
    var channelId: String = "samsung.default.media.player"
    var isConnecting: Bool = false
    var isConnected: Bool = false
    var services = [Service]()
    
    var selectedService: Service?
    var mediaplayer:MediaPlayer? = nil
    var connectStatus:Bool = false
    
    var totalDurationOfVideo = 0
    var currentPage:Int = 0
    var mediaStyleDict = [String:String] ()
    var currentElement:String?
    
    class var sharedInstance : PhotoShareController {
    struct Static {
        static var onceToken : dispatch_once_t = 0
        static var instance : PhotoShareController? = nil
        }
        dispatch_once(&Static.onceToken) {
            Static.instance = PhotoShareController()
        }
        return Static.instance!
    }

    override init () {
        super.init()
        search.delegate = self
        
    }

    func searchServices() {
        search.start()
        updateCastStatus()
    }

    func connect(service: Service) {
        search.stop()
        
        mediaplayer = service.createMediaPlayer()
        
        mediaplayer?.delegate = self
        
        mediaplayer!.connect
            { (error) -> Void in
                
                if error != nil
                {
                    self.connectStatus = false
                    UIAlertView(title: "Error", message: "Your TV doesn't support DMP", delegate: nil, cancelButtonTitle: "OK").show()
                }
                else
                {
                    self.connectStatus = true
                }
           }
        
        self.isConnecting = true
        self.updateCastStatus()
     
    }

    func getCastStatus() -> CastStatus {
        var castStatus = CastStatus.notReady
        if isConnected {
            castStatus = CastStatus.connected
        } else if isConnecting {
            castStatus = CastStatus.connecting
        } else if services.count > 0 {
            castStatus = CastStatus.readyToConnect
        }
        return castStatus
    }

    func castImage(imageURL: NSURL) {
        dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), {
            let assetLib = ALAssetsLibrary()
            assetLib.assetForURL(imageURL, resultBlock: {
                (asset: ALAsset!) in
                if asset != nil {
                    let iref = asset.defaultRepresentation().fullResolutionImage().takeUnretainedValue()
                    let image = UIImage(CGImage: iref)
                    let imageData = UIImageJPEGRepresentation(image,0.6)
                  //  PhotoShareController.sharedInstance.app?.publish(event: "showPhoto", message: nil, data: imageData!, target: MessageTarget.Host.rawValue)
                }
                }, failureBlock: { (error: NSError!) in
            })
        })
    }

    // MARK: Private Methods

    private func updateCastStatus() {
        // Update the cast button status: Since they may be many cast buttons and
        // the PhotoShareController does not need to be coupled to the view controllers 
        // the use of Notifications seems appropriate.
        NSNotificationCenter.defaultCenter().postNotificationName("CastStatusDidChange", object: self, userInfo: ["status":getCastStatus().rawValue])
    }

    // MARK: - ChannelDelegate -

    @objc func onConnect(error: NSError?)
    {
        if (error != nil) {
            search.start()
            print(error?.localizedDescription)
        }
        isConnecting = false
        isConnected = true
        updateCastStatus()
    }
    
    @objc func onDisconnect(error: NSError?)
    {
        if (isConnected)
        {
            print("disconnect")
            search.start()
            isConnecting = false
            isConnected = false
            updateCastStatus()
        }
    }
    
 /*   func disconnectTV()
    {
        if(mediaplayer != nil)
        {
            isConnected = false
            mediaplayer?.disconnect()

        }
    }
*/
    
    // MARK: - ServiceDiscoveryDelegate Methods -

    // These two delegate method will help us know when to change the cast button status

    @objc func onServiceFound(service: Service) {
        services.append(service)
        updateCastStatus()
    }

    @objc func onServiceLost(service: Service) {
        removeObject(&services,object: service)
        updateCastStatus()
    }

    @objc func onStop() {
        services.removeAll(keepCapacity: false)
    }

    func removeObject<T:Equatable>(inout arr:Array<T>, object:T) -> T? {
        if let found = arr.indexOf(object) {
            return arr.removeAtIndex(found)
        }
        return nil
    }
    
    @objc func onMessage(message: Message)
    {
        NSLog("Message Received")
        print("message is \(message.data) from \(message.from)")
        let item:NSString = message.data as! NSString
        
        print(item)
        
        let jsonData = item.dataUsingEncoding(NSUTF8StringEncoding)
        
        do {
            let json: AnyObject? = try NSJSONSerialization.JSONObjectWithData(jsonData!, options:NSJSONReadingOptions.MutableContainers)
            
           
            }
        catch let error as NSError
        {
            print("error \(error)")
        }
    }
    
    @objc func onData(message: Message, payload: NSData)
    {
        NSLog("Data Received")
        print("data is \(message.data) from \(message.from) with payload \(payload)")
    }

   
   /* @objc func onPlayerNotice(message: [String : AnyObject]) {
        print("rahul hello")
        
        if (message["Video State"] != nil)
        {
            let temp :String = (message["Video State"] as? String)!
            
            print(temp)
        }
        
    }
    
    @objc func onBufferingStart()
    {
        print("app onBufferingStart")
    }
    
    @objc func OnBufferingComplete()
    {
        print("app OnBufferingComplete")
    }
    
    @objc func OnVideoStream(totalDuration: Int) {
        print("OnVideoStream duration is \(totalDuration)")
        
        totalDurationOfVideo = totalDuration
    }
    
    @objc func OnBufferingProgress(progressValue: Int)
    {
       
        print("progressValue is \(progressValue)")
       
    }
  

    @objc func OnCurrentPlayTime(currentTime: Int)
    {
        var tempPlayTime:Float = Float(currentTime)
        
        print("app oncurrentplaytime tempPlayTime is \(tempPlayTime)")
        
        print("total duration is \(totalDurationOfVideo)")
        
        if(totalDurationOfVideo != 0)
        {
            tempPlayTime = tempPlayTime/Float(totalDurationOfVideo)
        }
        
        print("tempPlayTime is \(tempPlayTime)")
        
        if (currentPage == 1)
        {
            NSNotificationCenter.defaultCenter().postNotificationName("updateProgressBarStatus", object: self, userInfo: ["progressStatus":tempPlayTime])
        }
        else if(currentPage == 2)
        {
            NSNotificationCenter.defaultCenter().postNotificationName("updateProgressBarYoutubeStatus", object: self, userInfo: ["progressStatus":tempPlayTime])
        }
        
    }
    
    @objc func OnStreamCompleted()
    {
        print("app OnStreamCompleted")
    }
    
    @objc func OnPlay()
    {
        print("app onplay")
    }
    
    @objc func OnPause()
    {
        print("app onpause")
    }
    
    @objc func OnStop()
    {
        print("app onstop")
    }
    
    @objc func OnForward()
    {
        print("app onforward")
    }
    
    @objc func OnRewind()
    {
        print("app onrewind")
    }
    
    @objc func OnMute()
    {
        print("app onmute")
    }
    
    @objc func OnUnMute()
    {
        print("app OnUnMute")
    }

   */
  }