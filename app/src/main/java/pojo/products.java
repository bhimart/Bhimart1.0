package pojo;

/**
 * Created by Android on 29-12-2016.
 */

public class products {


    String id,name,mThumbnailUrl,price;

    public products()
    {
        this.id=id;
        this.name=name;
        this.mThumbnailUrl=mThumbnailUrl;
        this.price=price;
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
    public String getprice()
    {
        return price;
    }

    public void setprice(String price)
    {

        this.price = price;
    }
}
