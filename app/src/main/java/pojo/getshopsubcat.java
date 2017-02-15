package pojo;

/**
 * Created by GMSoft on 1/24/2017.
 */

public class getshopsubcat {

    String id,name,mThumbnailUrl,categoryid;

    public getshopsubcat()
    {
        this.id=id;
        this.name=name;
        this.mThumbnailUrl=mThumbnailUrl;
        this.categoryid=categoryid;
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
    public String getcategoryid()
    {
        return categoryid;
    }
    public void setcategoryid(String categoryid)
    {

        this.categoryid=categoryid;
    }
}
