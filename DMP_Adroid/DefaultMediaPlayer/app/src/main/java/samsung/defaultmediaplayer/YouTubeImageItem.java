package samsung.defaultmediaplayer;

/**
 * @author Ankit Saini
 * Class to define properties of YouTube Item.
 */
public class YouTubeImageItem {

    private Integer id;
    private String title;
    private String imageUrl;
    private String videoId;

    public YouTubeImageItem(Integer id, String videoId)
    {
        super();
        this.id = id;
        this.videoId = videoId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getImageUrl()
    {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl)
    {
        this.imageUrl = imageUrl;
    }

    public String getVideoId()
    {
        return videoId;
    }

    public void setVideoId(String videoId)
    {
        this.videoId = videoId;
    }
}
