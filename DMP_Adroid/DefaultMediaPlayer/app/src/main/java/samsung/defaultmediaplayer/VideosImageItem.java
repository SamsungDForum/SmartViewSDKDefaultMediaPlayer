package samsung.defaultmediaplayer;

/**
 * @author Ankit Saini
 * Class to define properties of Video Item.
 */
public class VideosImageItem {
    private Integer id;
    private String title;
    private String imageUrl;
    private String url;

    public VideosImageItem(Integer id, String title, String imageUrl, String url)
    {
        super();
        this.id         = id;
        this.title      = title;
        this.imageUrl   = imageUrl;
        this.url        = url;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
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

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String image)
    {
        this.url = url;
    }
}
