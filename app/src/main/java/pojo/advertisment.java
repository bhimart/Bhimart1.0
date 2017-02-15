package pojo;

/**
 * Created by Android on 29-12-2016.
 */

public class advertisment {

    String id,name,mThumbnailUrl;

    public advertisment()
    {

        this.mThumbnailUrl=mThumbnailUrl;
        this.id=id;
    }

    public String getThumbnailUrl()
    {

        return mThumbnailUrl;
    }
    public void setThumbnailUrl(String thumbnailUrl)
    {

        this.mThumbnailUrl = thumbnailUrl;
    }
    public String getId()
    {

        return id;
    }
    public void setid(String id)
    {

        this.id = id;
    }
}
