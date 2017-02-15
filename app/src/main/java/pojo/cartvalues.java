package pojo;

/**
 * Created by GMSoft on 1/9/2017.
 */

public class cartvalues {

    String id,name,mThumbnailUrl,prodname,shopname;
         int qty,price,totalprice,mVendorQty;
    public cartvalues()
    {
        this.id=id;
        this.name=name;
        this.mThumbnailUrl=mThumbnailUrl;
        this.prodname=prodname;
        this.shopname=shopname;
        this.qty=qty;
        this.price=price;
        this.mVendorQty=mVendorQty;
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

    public int getprice(){


        return  price;
    }
    public void setPrice(int price){
        this.price=price;
    }

    public int getqty(){


        return  qty;
    }
    public void setqty(int qty){
        this.qty=qty;
    }

    public int gettotalprice()
    {
        return totalprice;
    }
    public void settotalprice(int totalprice)
    {

        this.totalprice=totalprice;
    }
    public int getVendorQty()
    {

        return mVendorQty;
    }
    public void setVendorQty(int VendorQty)
    {

        this.mVendorQty = VendorQty;
    }

}
