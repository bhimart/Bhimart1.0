package pojo;

/**
 * Created by Android on 29-12-2016.
 */

public class shops {


    String id,name,mThumbnailUrl,distance;

    public shops()
    {
        this.id=id;
        this.name=name;
        this.mThumbnailUrl=mThumbnailUrl;
        this.distance=distance;
    }
    public String getid()
    {
        return id;
    }
    public void setId(String id)
    {

        this.id=id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {

        this.name = name;
    }
    public String getThumbnailUrl()
    {

        return mThumbnailUrl;
    }
    public void setThumbnailUrl(String thumbnailUrl)
    {

        this.mThumbnailUrl = thumbnailUrl;
    }



    public String getdistance()
    {
        return distance;
    }

    public void setdistance(String distance)
    {

        this.distance = distance;
    }

}
