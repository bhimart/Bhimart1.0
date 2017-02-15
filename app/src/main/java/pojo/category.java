package pojo;

/**
 * Created by Android on 29-12-2016.
 */

public class category {

    String name,mThumbnailUrl,lat,lang;
    String img;
    int id;

    public category()
    {
        this.id=id;
        this.name=name;
        this.mThumbnailUrl=mThumbnailUrl;
    }
    public int getid()
    {
        return id;
    }
    public void setId(int id)
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
    public String getlat(){

        return lat;
    }

        public void setlat(String lat)
        {

            this.lat = lat;
        }
    public String getlang(){

        return lang;
    }

    public void setlang(String lang)
    {

        this.lang = lang;
    }
    public String getimg(){

        return img;
    }

    public void setimg(String img)
    {

        this.img = img;
    }

}
