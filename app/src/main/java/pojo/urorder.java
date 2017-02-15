package pojo;

/**
 * Created by GMSoft on 1/23/2017.
 */

public class urorder {

    String id,name,mThumbnailUrl,prodname,shopname,orderid;
    String qty,totalprice;
    public urorder()
    {
        this.id=id;
        this.name=name;
        this.mThumbnailUrl=mThumbnailUrl;
        this.prodname=prodname;
        this.shopname=shopname;
        this.qty=qty;
        this.orderid=orderid;
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
    public String getProdname()
    {
        return prodname;
    }
    public void setProdname(String prodname)
    {

        this.prodname=prodname;
    }


    public String getShopname(){return  shopname;}
    public void setShopname(String shopname){
        this.shopname=shopname;

    }

    public String getorderid()
    {


        return  orderid;
    }
    public void setorderid(String orderid)
    {


        this.orderid=orderid;
    }

    public String getqty(){


        return  qty;
    }
    public void setqty(String qty){
        this.qty=qty;
    }

    public String gettotalprice()
    {
        return totalprice;
    }
    public void settotalprice(String totalprice)
    {

        this.totalprice=totalprice;
    }

}
