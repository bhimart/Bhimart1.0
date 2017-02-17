package Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import pojo.cartvalues;

import pojo.category;
import sample.com.closebuy.R;
import sample.com.closebuy.Yourcart;

/**
 * Created by GMSoft on 1/9/2017.
 */

public class Cart extends RecyclerView.Adapter<Cart.ViewHolder> {
    private List<cartvalues> itemList;
    private Context context;
    DbHelper helper;
    int fullprdtotal=0;
    public Cart(Context context, List<cartvalues> itemList)
    {
        this.context=context;
        this.itemList=itemList;
        helper = new DbHelper(context);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        holder.productidTv.setText(itemList.get(position).getid());
        holder.productnameTv .setText(itemList.get(position).getProdname());
        holder.priceTv.setText(itemList.get(position).getprice()+"");
        Glide.with(context).load(itemList.get(position).getThumbnailUrl()).into(holder.iconImg);
        holder.quantityTv.setText(itemList.get(position).getqty()+"");
        holder.producttotalTv.setText(itemList.get(position).gettotalprice()+"");



    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View layoutview = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_cart,null);
        ViewHolder cart = new ViewHolder(layoutview);
        return cart;
    }



    @Override
    public int getItemCount()
    {
        return this.itemList.size();
    }


public class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener
{

    TextView productnameTv ,productidTv, priceTv,quantityTv, producttotalTv;
    ImageView iconImg, subtractQtyImg, addQtyImg;
    private int itmQty=1;
    private int SELECTED_POS;
    String productidtext;
    Button removeBtn;
    int tot;
    public ViewHolder(View itemView)
    {

        super(itemView);

        iconImg = (ImageView) itemView.findViewById(R.id.prdimg);
        productnameTv  = (TextView) itemView.findViewById(R.id.prdname);
        productidTv   = (TextView) itemView.findViewById(R.id.pid);
        priceTv   = (TextView) itemView.findViewById(R.id.prdprice);
        quantityTv     =  (TextView) itemView.findViewById(R.id.qtyval);
        addQtyImg=(ImageView)itemView.findViewById(R.id.add);
        removeBtn=(Button) itemView.findViewById(R.id.remove);
        subtractQtyImg=(ImageView)itemView.findViewById(R.id.sub);
        producttotalTv=(TextView) itemView.findViewById(R.id.prdtotal);
        itemView.setOnClickListener(this);
        addQtyImg.setOnClickListener(this);
        subtractQtyImg.setOnClickListener(this);
        removeBtn.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {




        if(v==addQtyImg)
        {
            ++itmQty;
            cartvalues item = itemList.get(SELECTED_POS);
            item.setqty(itmQty);
            quantityTv.setText(String.valueOf(itmQty));
            productidtext=productidTv.getText().toString();
            helper.updateqty(productidtext,itmQty);
            tot=(Integer.parseInt(priceTv.getText().toString())* itmQty);
            producttotalTv.setText(tot+"");

            fullprdtotal+=tot;
            System.out.println("full---"+fullprdtotal);

        }

        if(v==subtractQtyImg)
        {
            if(itmQty > 1) {
                --itmQty;
                cartvalues item = itemList.get(SELECTED_POS);
                item.setqty(itmQty);
                quantityTv.setText(String.valueOf(itmQty));
                productidtext = productidTv.getText().toString();
                helper.updateqty(productidtext, itmQty);
                tot=(Integer.parseInt(priceTv.getText().toString())* itmQty);
                producttotalTv.setText(tot+"");
            }else{

                quantityTv.setText("1");
            }
        }
        if(v==removeBtn){
            productidtext=productidTv.getText().toString();
            helper.delete(productidtext);
            Intent in=new Intent(v.getContext(),Yourcart.class);

            v.getContext().startActivity(in);

        }


    }
}
}
