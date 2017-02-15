package pojo;

/**
 * Created by pc on 4/12/15.
 */
public class ShoppingCartResults
{
    String id,prdname,shopname,prdimg;
    public int qty,price,vendorqty;


    public ShoppingCartResults(String id, Integer qty, Integer price,Integer vendorqty, String prdname, String prdimg, String shopname)
    {
        this.id = id;
        this.price = price;
        this.qty = qty;
        this.prdname=prdname;
        this.prdimg=prdimg;
        this.shopname=shopname;
        this.vendorqty=vendorqty;
    }



    public String getid() {
        return id;
    }

    public void setid(String id) {
        id = id;
    }

    public int getprice() {
        return price;
    }

    public void setprice(int price) {
        price = price;
    }

    public int getqty()
    {

        return qty;
    }

    public void setqty(int qty)
    {

        qty = qty;
    }
    public int getvendorqty() {
        return vendorqty;
    }

    public void setvendorqty(int vendorqty) {
        vendorqty = vendorqty;
    }

    public String getprdname() {
        return prdname;
    }

    public void setprdname(String prdname) {
        prdname = prdname;
    }


    public String getprdimg() {
        return prdimg;
    }

    public void setprdimg(String prdimg) {
        prdimg = prdimg;
    }

    public String getshopname() {
        return shopname;
    }

    public void setshopname(String shopname) {
        shopname = shopname;
    }
    // String representation
    public String toString() {
        return  this.id + "" + this.qty+""+this.price+""+this.prdname+""+this.prdimg+""+this.shopname;    }
}
