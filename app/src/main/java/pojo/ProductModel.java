package pojo;


/**
 * Created by GMSoft2 on 1/9/2017.
 */

public class ProductModel {
    String id,name,mThumbnailUrl,prodname,shopname,price,mVendorQty;

    public ProductModel()
    {
        this.id=id;
        this.name=name;
        this.mThumbnailUrl=mThumbnailUrl;
        this.prodname=prodname;
        this.shopname=shopname;
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
    public String getVendorQty()
    {

        return mVendorQty;
    }
    public void setVendorQty(String VendorQty)
    {

        this.mVendorQty = VendorQty;
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

    public String getprice(){


        return  price;
    }
    public void setPrice(String price){
        this.price=price;
    }
}
