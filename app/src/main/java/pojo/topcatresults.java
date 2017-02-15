package pojo;

/**
 * Created by GMSoft on 2/1/2017.
 */

public class topcatresults {

    int catid;
    String catname;
    String catimg;
    String lat;
    String lang;
    public topcatresults(int catid, String catname, String catimg, String lat, String lang)
    {
        this.catid = catid;
        this.catname=catname;
        this.catimg=catimg;
        this.lat=lat;
        this.lang=lang;
    }

    public int getcatid() {
        return catid;
    }

    public void setcatid(int catid) {
        catid = catid;
    }


    public String getcatname() {
        return catname;
    }

    public void setcatname(String catname) {
        catname = catname;
    }


    public String getcatimg() {
        return catimg;
    }

    public void setcatimgimg(String catimg) {
        catimg = catimg;
    }


    public String gelat() {
        return lat;
    }

    public void setlat(String lat) {
        lat = lat;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang)
    {
        lang = lang;
    }

}
