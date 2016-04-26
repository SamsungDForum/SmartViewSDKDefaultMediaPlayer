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

import UIKit

class FirstViewController: UIViewController, UIPopoverPresentationControllerDelegate, UIScrollViewDelegate {
    
    weak var castItem: CastButtonItem?
    @IBOutlet weak var photoButton: UIButton!
    @IBOutlet weak var youtubeButton: UIButton!
    
    @IBOutlet weak var lineView: UIView!
    
    var videoView:VideoViewController?
    var youtubeView:YoutubeViewController?
    
    @IBOutlet weak var scrollView: UIScrollView!
    
    var imageView: UIImageView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        
        let castItem = CastButtonItem(buttonFrame: CGRectMake(0, 0, 43, 40))
        navigationItem.rightBarButtonItem = castItem
        //self.navigationController?.navigationBar.translucent = false
        
        let label = UILabel(frame: CGRect(x: 0, y: 0, width: (self.navigationController?.navigationBar.frame.size.width)!, height: (self.navigationController?.navigationBar.frame.size.height)!))
        label.text = "Default Media Player"
        label.textAlignment = NSTextAlignment.Left
        
        self.navigationController?.navigationBar.barTintColor = UIColor.orangeColor()
       // self.navigationController?.navigationBar.titleTextAttributes = [NSForegroundColorAttributeName: UIColor.whiteColor()]
        self.navigationItem.titleView = label
        
        //self.navigationController?.navigationBar.tintColor = UIColor.whiteColor()
        
        navigationItem.titleView?.tintColor = UIColor.blackColor()
        castItem.castButton.addTarget(self, action: Selector("cast"), forControlEvents: UIControlEvents.TouchUpInside)
        castItem.castStatus = PhotoShareController.sharedInstance.getCastStatus()
        self.castItem = castItem
        
        automaticallyAdjustsScrollViewInsets = true
        
        let sb:UIStoryboard = UIStoryboard(name:"Main", bundle: nil)
        
       
        lineView.frame.size.width = scrollView.frame.width/2
        
        scrollView.frame = CGRect(x:0, y:125, width:view.bounds.size.width, height: view.bounds.size.height - 125) // height: scrollView.bounds.height
        scrollView.backgroundColor = UIColor.whiteColor()
        scrollView.contentSize = CGSizeMake((view.bounds.size.width)*2, scrollView.bounds.height)
        scrollView.autoresizingMask = UIViewAutoresizing.FlexibleWidth
        
        videoView = sb.instantiateViewControllerWithIdentifier("videoView") as? VideoViewController
        videoView?.view.frame = CGRect(x:0, y:0, width: self.view.bounds.size.width, height: scrollView.bounds.height)
        videoView?.view.backgroundColor = UIColor.whiteColor()
        
        scrollView.addSubview(videoView!.view)
       
        youtubeView = sb.instantiateViewControllerWithIdentifier("youtubeView") as? YoutubeViewController
        youtubeView?.view.frame = CGRect(x:(self.view.bounds.size.width), y:0, width: self.view.bounds.size.width, height: scrollView.bounds.height)
        youtubeView?.view.backgroundColor = UIColor.whiteColor()
        scrollView.addSubview(youtubeView!.view)
    
        scrollView.delegate = self
        
        photoButton.backgroundColor = UIColor.clearColor()
        photoButton.setTitleColor(UIColor.blackColor(), forState:UIControlState.Normal)
       
        youtubeButton.backgroundColor = UIColor.clearColor()
        youtubeButton.setTitleColor(UIColor.darkGrayColor(), forState:UIControlState.Normal)
        
        self.view.backgroundColor = UIColor.orangeColor()
    }
    
    override func viewWillAppear(animated: Bool) {
       // title = "Default Media Player"
    }
    
    override func viewWillDisappear(animated: Bool) {
       // title = nil
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    // 2: Add the cast button action
    func cast() {
        switch castItem!.castStatus {
    
        case .notReady:
            return
        case .connecting:
            return
        case .connected:
            let termiateApp = TerminateAppViewController(nibName: "TerminateAppViewController", bundle: nil)
            presentPopover(termiateApp)
        case .readyToConnect:
            let deviceList = DeviceListViewController(style: UITableViewStyle.Plain)
            presentPopover(deviceList)
        }
    }

    func presentPopover(viewController: UIViewController) {
        viewController.preferredContentSize = CGSize(width: 320, height: 186)
        viewController.modalPresentationStyle = UIModalPresentationStyle.Popover
        let presentationController = viewController.popoverPresentationController
        presentationController!.sourceView = castItem!.castButton
        presentationController!.sourceRect = castItem!.castButton.bounds
        viewController.popoverPresentationController!.delegate = self
        presentViewController(viewController, animated: false, completion: {})
    }
    
    func adaptivePresentationStyleForPresentationController(controller: UIPresentationController) -> UIModalPresentationStyle {
        // Return no adaptive presentation style, use default presentation behaviour
        return .None
    }
    
    func scrollViewWillBeginDragging(scrollView: UIScrollView)
    {
        print("begin dragging")
      //  let x:CGFloat = scrollView.frame.origin.x
        
      //  print("x is \(x)")
    }
    
    func scrollViewWillEndDragging(scrollView: UIScrollView, withVelocity velocity: CGPoint, targetContentOffset: UnsafeMutablePointer<CGPoint>)
    {
        print("will end dragging")
    }
    
    func scrollViewDidEndDragging(scrollView: UIScrollView, willDecelerate decelerate: Bool)
    {
        print("did end dragging")
        
        let page = (scrollView.contentOffset.x) / (scrollView.frame.size.width)
        
        
        print("page is \(page)")
    }
    
    func scrollViewDidScroll(scrollView: UIScrollView)
    {
        let x:CGFloat = scrollView.contentOffset.x
        print("did scroll x is \(x)")
        
        lineView.frame.origin.x = scrollView.contentOffset.x/2
        lineView.frame.size.width = scrollView.frame.width/2
    
    }
    
    func scrollViewWillBeginDecelerating(scrollView: UIScrollView)
    {
        
    }
   
    func scrollViewDidEndDecelerating(scrollView: UIScrollView) // called when scroll view grinds to a halt
    {
        let page = (scrollView.contentOffset.x) / (scrollView.frame.size.width)
        
         if page == 0
        {
            photoButton.setTitleColor(UIColor.blackColor(), forState:UIControlState.Normal)
            youtubeButton.setTitleColor(UIColor.darkGrayColor(), forState:UIControlState.Normal)
            
            PhotoShareController.sharedInstance.currentPage = 0
        }
        else if page == 1
        {
            youtubeButton.setTitleColor(UIColor.blackColor(), forState:UIControlState.Normal)
            photoButton.setTitleColor(UIColor.darkGrayColor(), forState:UIControlState.Normal)
         
            PhotoShareController.sharedInstance.currentPage = 1
        }
      
        print("didenddecelerating is \(page)")
    }
    
    
    @IBAction func videoBtnAction(sender: AnyObject) {
    
        photoButton.setTitleColor(UIColor.blackColor(), forState:UIControlState.Normal)
        youtubeButton.setTitleColor(UIColor.darkGrayColor(), forState:UIControlState.Normal)
        
        PhotoShareController.sharedInstance.currentPage = 0
        
        scrollView.setContentOffset(CGPoint(x: 0, y: 0), animated: true)
        
    }
    
    @IBAction func youtubeBtnAction(sender: AnyObject) {
        
            youtubeButton.setTitleColor(UIColor.blackColor(), forState:UIControlState.Normal)
            photoButton.setTitleColor(UIColor.darkGrayColor(), forState:UIControlState.Normal)
            
            PhotoShareController.sharedInstance.currentPage = 1
            scrollView.setContentOffset(CGPoint(x: self.view.bounds.size.width, y: 0), animated: true)
        }

}

