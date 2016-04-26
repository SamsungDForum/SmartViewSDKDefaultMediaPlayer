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

class YoutubeViewController : UIViewController, UICollectionViewDataSource, UICollectionViewDelegate, UICollectionViewDelegateFlowLayout, /*cellYoutubeDataTransferDelegate,*/ UIPopoverPresentationControllerDelegate
{
    var imageView: UIImageView!
    
    var playStatus:Bool = false
    
    @IBOutlet var youtubeController: UICollectionView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.youtubeController.delegate = self
        self.youtubeController.dataSource = self
        
    }
    
    override func viewWillAppear(animated: Bool) {
        
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
        let items:Int = 7
        return items
    }
    
    func collectionView(collectionView: UICollectionView, cellForItemAtIndexPath indexPath: NSIndexPath) -> UICollectionViewCell
    {
        let cell: YoutubeThumbnail = collectionView.dequeueReusableCellWithReuseIdentifier("youtubeCell", forIndexPath: indexPath) as! YoutubeThumbnail
        
        if (indexPath.row == 0)
        {
            cell.setThumbnailImage(UIImage(named:"youtube1.jpg")!)
            cell.setTitleText("Demo Samsung - From the Blue - FULL HD 1080P")
        }
        else if (indexPath.row == 1)
        {
            cell.setThumbnailImage(UIImage(named:"youtube2.jpg")!)
            cell.setTitleText("Samsung HD Demo - Colour China 1080p")
        }
        else if (indexPath.row == 2)
        {
            cell.setThumbnailImage(UIImage(named:"youtube3.jpg")!)
            cell.setTitleText("Samsung Curved TV commercial 60 seconds")
        }
        if (indexPath.row == 3)
        {
            cell.setThumbnailImage(UIImage(named:"youtube4.jpg")!)
            cell.setTitleText("Full HD Samsung LED Color Demo 1080p")
        }
        else if (indexPath.row == 4)
        {
            cell.setThumbnailImage(UIImage(named:"youtube5.jpg")!)
            cell.setTitleText("Samsung HD Color Demo ")
        }
        else if (indexPath.row == 5)
        {
            cell.setThumbnailImage(UIImage(named:"youtube6.jpg")!)
            cell.setTitleText("Samsung 4K UHD demo video")
        }
        else if (indexPath.row == 6)
        {
            cell.setThumbnailImage(UIImage(named:"youtube7.jpg")!)
            cell.setTitleText("Samsung Galaxy S6 4K Ultra HD Video Sample")
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
        
        let contentURL0 = NSURL(string: "https://www.youtube.com/embed/_DfL0zM4TJ8")
        let contentURL1 = NSURL(string: "https://www.youtube.com/embed/3bEtJgnd86M")
        let contentURL2 = NSURL(string: "https://www.youtube.com/embed/kbZYNqL6grQ")
        let contentURL3 = NSURL(string: "https://www.youtube.com/embed/jZvC7NWkeA0")
        let contentURL4 = NSURL(string: "https://www.youtube.com/embed/WEjbnQj6BrY")
        let contentURL5 = NSURL(string: "https://www.youtube.com/embed/6jiNS_4CEug")
        let contentURL6 = NSURL(string: "https://www.youtube.com/embed/onXpKXbnbE0")
        
        let sb:UIStoryboard = UIStoryboard(name:"Main", bundle: nil)
        let playView = sb.instantiateViewControllerWithIdentifier("playView") as? PlayViewController
        
        presentPopover(playView!)
        
        if (indexPath.row == 0)
        {
            playView!.setBackgroundImg(UIImage(named:"youtube1.jpg")!)
            PhotoShareController.sharedInstance.mediaplayer?.playContent(contentURL0!)
        }
        else if(indexPath.row == 1)
        {
            playView!.setBackgroundImg(UIImage(named:"youtube2.jpg")!)
            PhotoShareController.sharedInstance.mediaplayer?.playContent(contentURL1!)
        }
        else if(indexPath.row == 2)
        {
            playView!.setBackgroundImg(UIImage(named:"youtube3.jpg")!)
            PhotoShareController.sharedInstance.mediaplayer?.playContent(contentURL2!)
        }
        else if(indexPath.row == 3)
        {
            playView!.setBackgroundImg(UIImage(named:"youtube4.jpg")!)
            PhotoShareController.sharedInstance.mediaplayer?.playContent(contentURL3!)
        }
        else if(indexPath.row == 4)
        {
            playView!.setBackgroundImg(UIImage(named:"youtube5.jpg")!)
            PhotoShareController.sharedInstance.mediaplayer?.playContent(contentURL4!)
        }
        else if(indexPath.row == 5)
        {
            playView!.setBackgroundImg(UIImage(named:"youtube6.jpg")!)
            PhotoShareController.sharedInstance.mediaplayer?.playContent(contentURL5!)
        }
        else if(indexPath.row == 6)
        {
            playView!.setBackgroundImg(UIImage(named:"youtube7.jpg")!)
            PhotoShareController.sharedInstance.mediaplayer?.playContent(contentURL6!)
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
        
        presentationController!.sourceRect =  CGRect(x:20, y:(200 + self.view.bounds.origin.y), width:0, height:0)
        viewController.popoverPresentationController!.delegate = self
        presentViewController(viewController, animated: false, completion: {})
    }
    
  }