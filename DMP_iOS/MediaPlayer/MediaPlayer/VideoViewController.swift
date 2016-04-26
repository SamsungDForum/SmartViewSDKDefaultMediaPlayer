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
import UIKit
import SmartView

class VideoViewController : UIViewController, UICollectionViewDataSource, UICollectionViewDelegate, UICollectionViewDelegateFlowLayout, UIPopoverPresentationControllerDelegate
{
    @IBOutlet var videoController: UICollectionView!
    
    var imageView: UIImageView!
    
    var playStatus:Bool = false
    
    var mediaPlayer:MediaPlayer?
    
    var currentItemIndex:Int = -1
    var currentItemSection:Int = -1
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.videoController.delegate = self
        self.videoController.dataSource = self
        
        //NSNotificationCenter.defaultCenter().addObserver(self, selector: "updateProgressBar:", name: "updateProgressBarStatus", object: nil)
    }
    
    override func viewWillAppear(animated: Bool) {
        
    }
    
    override func viewDidAppear(animated: Bool)
    {
        super.viewDidAppear(animated)
        
    }

    override func viewWillDisappear(animated: Bool) {
        
    }
    
    func numberOfSectionsInCollectionView(collectionView: UICollectionView) -> Int
    {
        let sections:Int = 1
        return sections
    }
    
    func collectionView(collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int
    {
        let items:Int = 6
        return items
    }
    
    func collectionView(collectionView: UICollectionView, cellForItemAtIndexPath indexPath: NSIndexPath) -> UICollectionViewCell
    {
        let cell: VideoThumbnail = collectionView.dequeueReusableCellWithReuseIdentifier("videoCell", forIndexPath: indexPath) as! VideoThumbnail
        
        //let cell = collectionView.dequeueReusableCellWithReuseIdentifier("photoCell", forIndexPath: indexPath)
        //cell.myDelegate = self
        
        cell.itemIndex = indexPath.row
        cell.itemSection = indexPath.section
        
        if (indexPath.row == 0)
        {
             cell.setThumbnailImage(UIImage(named:"BigBuckBunny.jpg")!)
             cell.setTitleText("Big buck Bunny")
        }
        else if (indexPath.row == 1)
        {
            cell.setThumbnailImage(UIImage(named:"ElephantsDream.jpg")!)
            cell.setTitleText("Elephant Dream")
        }
        else if (indexPath.row == 2)
        {
            cell.setThumbnailImage(UIImage(named:"Sintel.jpg")!)
            cell.setTitleText("Sintel")
        }
        else if (indexPath.row == 3)
        {
            cell.setThumbnailImage(UIImage(named:"TearsOfSteel.jpg")!)
            cell.setTitleText("TearsOfSteel")
        }
        else if (indexPath.row == 4)
        {
            cell.setThumbnailImage(UIImage(named:"SDKDemo.jpg")!)
            cell.setTitleText("Samsung Smart View SDK Demo")
        }
        else if (indexPath.row == 5)
        {
            cell.setThumbnailImage(UIImage(named:"ViewSDK.jpg")!)
            cell.setTitleText("Samsung Smart View SDK")
        }
        return cell
    }
    
    func collectionView(collectionView: UICollectionView, didSelectItemAtIndexPath indexPath: NSIndexPath)
    {
        print("didSelectItemAtIndexPath")
        
        if (PhotoShareController.sharedInstance.mediaplayer == nil || PhotoShareController.sharedInstance.mediaplayer?.connected == false)
        {
            UIAlertView(title: "TV not connected", message: "Please connect with TV and then play video", delegate: nil, cancelButtonTitle: "OK").show()
            return;
        }
        
        let contentURL0 = NSURL(string: "http://samsungdforum.com/smartview/sample/video/BigBuckBunny.mp4")
        let contentURL1 = NSURL(string: "http://samsungdforum.com/smartview/sample/video/ElephantsDream.mp4")
        let contentURL2 = NSURL(string: "http://samsungdforum.com/smartview/sample/video/Sintel.mp4")
        let contentURL3 = NSURL(string: "http://samsungdforum.com/smartview/sample/video/TearsOfSteel.mp4")
        let contentURL4 = NSURL(string: "http://samsungdforum.com/smartview/sample/video/SamsungSamrtViewSDKDemo.mp4")
        let contentURL5 = NSURL(string: "http://www.samsungdforum.com/smartview/sample/video/SamsungSmartViewSDK.mp4")

        let sb:UIStoryboard = UIStoryboard(name:"Main", bundle: nil)
        let playView = sb.instantiateViewControllerWithIdentifier("playView") as? PlayViewController
        
        presentPopover(playView!)
        
        if (indexPath.row == 0)
        {
            playView!.setBackgroundImg(UIImage(named:"BigBuckBunny.jpg")!)
            PhotoShareController.sharedInstance.mediaplayer?.playContent(contentURL0!)
        }
        else if(indexPath.row == 1)
        {
            playView!.setBackgroundImg(UIImage(named:"ElephantsDream.jpg")!)
            PhotoShareController.sharedInstance.mediaplayer?.playContent(contentURL1!)
        }
        else if(indexPath.row == 2)
        {
            playView!.setBackgroundImg(UIImage(named:"Sintel.jpg")!)
            PhotoShareController.sharedInstance.mediaplayer?.playContent(contentURL2!)
        }
        else if(indexPath.row == 3)
        {
            playView!.setBackgroundImg(UIImage(named:"TearsOfSteel.jpg")!)
            PhotoShareController.sharedInstance.mediaplayer?.playContent(contentURL3!)
        }
        else if(indexPath.row == 4)
        {
            playView!.setBackgroundImg(UIImage(named:"SDKDemo.jpg")!)
            PhotoShareController.sharedInstance.mediaplayer?.playContent(contentURL4!)
        }
        else if(indexPath.row == 5)
        {
            playView!.setBackgroundImg(UIImage(named:"ViewSDK.jpg")!)
            PhotoShareController.sharedInstance.mediaplayer?.playContent(contentURL5!)
        }
        
        playStatus = true
        
        playView!.setRewindImg(UIImage(named:"rewind.png")!)
        playView?.setForwardImg(UIImage(named:"forward")!)
        playView!.setPlayPauseImg(UIImage(named:"pause.png")!)
        playView!.setMuteUnmuteImg(UIImage(named:"mute.png")!)
        playView!.setStopImg(UIImage(named:"stop.png")!)
      
    }
    
    func collectionView(collectionView: UICollectionView, didDeselectItemAtIndexPath indexPath: NSIndexPath)
    {
        
    }
    
    func collectionView(collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAtIndexPath indexPath: NSIndexPath) -> CGSize
    {
        let screenRect = UIScreen.mainScreen().bounds
        let screenWidth = screenRect.size.width
        
        return CGSizeMake(screenWidth, 100)

    }
    
    func adaptivePresentationStyleForPresentationController(controller: UIPresentationController) -> UIModalPresentationStyle {
        // Return no adaptive presentation style, use default presentation behaviour
        return .None
    }
    
    func presentPopover(viewController: UIViewController) {
        viewController.preferredContentSize = CGSize(width: 280, height: 200)
        viewController.modalPresentationStyle = UIModalPresentationStyle.Popover
        let presentationController = viewController.popoverPresentationController
        presentationController!.sourceView = self.view
        presentationController!.sourceRect =  CGRect(x:20, y:(200 + self.view.bounds.origin.y), width:0, height:0)  //self.view.bounds//castItem!.castButton.bounds
        viewController.popoverPresentationController!.delegate = self
        presentViewController(viewController, animated: false, completion: {})
    }

}