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



class PlayViewController : UIViewController
{
    @IBOutlet weak var bgImage: UIImageView!
    
    @IBOutlet weak var rewindImage: UIImageView!
    
    @IBOutlet weak var playPauseImage: UIImageView!
    
    @IBOutlet weak var muteUnmuteImage: UIImageView!
    
    @IBOutlet weak var forwardImage: UIImageView!
    
    @IBOutlet weak var stopImage: UIImageView!
    
    var playStatus:Bool = true
    var mute:Bool = false
    
    override func viewDidLoad() {
    
     super.viewDidLoad()
        
        self.view.backgroundColor = UIColor.whiteColor()
        
        rewindImage.userInteractionEnabled = true
        let rewindRecognizer = UITapGestureRecognizer(target: self, action: Selector("rewindAction:"))
        rewindImage.addGestureRecognizer(rewindRecognizer)
        
        playPauseImage.userInteractionEnabled = true
        let playPauseRecognizer = UITapGestureRecognizer(target: self, action: Selector("playPauseAction:"))
        playPauseImage.addGestureRecognizer(playPauseRecognizer)
        
        forwardImage.userInteractionEnabled = true
        let forwardRecognizer = UITapGestureRecognizer(target: self, action: Selector("forwardAction:"))
        forwardImage.addGestureRecognizer(forwardRecognizer)
        
        stopImage.userInteractionEnabled = true
        let stopRecognizer = UITapGestureRecognizer(target: self, action: Selector("stopAction:"))
        stopImage.addGestureRecognizer(stopRecognizer)
        
        muteUnmuteImage.userInteractionEnabled = true
        let muteUnmuteRecognizer = UITapGestureRecognizer(target: self, action: Selector("muteUnmuteAction:"))
        muteUnmuteImage.addGestureRecognizer(muteUnmuteRecognizer)
    }
    
     override func viewWillAppear(animated: Bool) {
    }
    
    override func viewWillDisappear(animated: Bool) {
    }
    
    func setBackgroundImg(backgroundImg:UIImage)
    {
        self.bgImage.image = backgroundImg
    }
    
    func setRewindImg(rewindImg:UIImage)
    {
        self.rewindImage.image = rewindImg
    }
    
    func setPlayPauseImg(playPauseImg:UIImage)
    {
        self.playPauseImage.image = playPauseImg
    }
    
    func setMuteUnmuteImg(muteUnmuteImg:UIImage)
    {
        self.muteUnmuteImage.image = muteUnmuteImg
    }
    
    func setForwardImg(forwardImg:UIImage)
    {
        self.forwardImage.image = forwardImg
    }
    
    func setStopImg(stopImg:UIImage)
    {
        self.stopImage.image = stopImg
    }
    
    func rewindAction(gestureRecognizer:UITapGestureRecognizer)
    {
        print("rewind test")
        
        PhotoShareController.sharedInstance.mediaplayer?.rewind()
    }
    
    func playPauseAction(gestureRecognizer:UITapGestureRecognizer)
    {
        print("playPause test")
        
        if(playStatus)
        {
            PhotoShareController.sharedInstance.mediaplayer?.pause()
            playStatus = false
            self.playPauseImage.image = UIImage(named:"play.png")
        }
        else
        {
            PhotoShareController.sharedInstance.mediaplayer?.play()
            playStatus = true
            self.playPauseImage.image = UIImage(named:"pause.png")
        }
    }
    
    func forwardAction(gestureRecognizer:UITapGestureRecognizer)
    {
        print("forward test")
        
        PhotoShareController.sharedInstance.mediaplayer?.fastForward()
    }
    
    func stopAction(gestureRecognizer:UITapGestureRecognizer)
    {
        print("stop test")
        
        PhotoShareController.sharedInstance.mediaplayer?.stop()
    }
    
    func muteUnmuteAction(gestureRecognizer:UITapGestureRecognizer)
    {
        print("muteUnmute test")
        
        if(!mute)
        {
            PhotoShareController.sharedInstance.mediaplayer?.mute(true)
            mute = true
            self.muteUnmuteImage.image = UIImage(named:"unmute.png")
        }
        else
        {
            PhotoShareController.sharedInstance.mediaplayer?.mute(false)
            mute = false
            self.muteUnmuteImage.image = UIImage(named:"mute.png")
        }
    }
}