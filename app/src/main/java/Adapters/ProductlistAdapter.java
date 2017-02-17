package Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import pojo.prodlist;
import sample.com.closebuy.Checkin;
import sample.com.closebuy.ProductDetails;
import sample.com.closebuy.R;

/**
 * Created by GMSoft on 2/17/2017.
 */
 public class ProductlistAdapter extends RecyclerView.Adapter<ProductlistAdapter.ViewHolder>

{
    private List<prodlist> itemList;
    private Context context;
    DbHelper helper;
    int total;

    public ProductlistAdapter(Context context, List<prodlist> itemList)
    {
        this.context = context;
        this.itemList = itemList;
        helper = new DbHelper(context);
    }


    @Override
    public ProductlistAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View layoutview = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_prodctlist, parent, false);
        ProductlistAdapter.ViewHolder product = new ProductlistAdapter.ViewHolder(layoutview);
        return product;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        holder.productidTv.setText(itemList.get(position).getid());
        holder.productnameTv.setText(itemList.get(position).getName());
        holder.shopnameTv.setText(itemList.get(position).getShopname());
        holder.productpriceTv.setText(itemList.get(position).getprice() + "");
        holder.productimagepathTv.setText(itemList.get(position).getThumbnailUrl());
        holder.vendorqtyTv.setText(itemList.get(position).getVendorQty());
        Glide.with(context).load(itemList.get(position).getThumbnailUrl()).into(holder.productImg);
    }



    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView productnameTv, productidTv, productpriceTv, shopnameTv, productimagepathTv,vendorqtyTv;
        ImageView productImg, checkinImg, checkoutImg;
        Integer pricevalue,vendorquant;


        public ViewHolder(View itemView)
        {

            super(itemView);

            productImg         = (ImageView)itemView.findViewById(R.id.prodimg);
            productnameTv      = (TextView) itemView.findViewById(R.id.prodname);
            productidTv        = (TextView) itemView.findViewById(R.id.pid);
            shopnameTv         = (TextView) itemView.findViewById(R.id.shopname);
            productpriceTv     = (TextView) itemView.findViewById(R.id.sellprice);
            vendorqtyTv        = (TextView) itemView.findViewById(R.id.vendorqty);
            productimagepathTv = (TextView) itemView.findViewById(R.id.prdimage);

            checkinImg  = (ImageView) itemView.findViewById(R.id.checkin);
            checkoutImg = (ImageView) itemView.findViewById(R.id.checkout);

            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view) {

                    String productidtext = productidTv.getText().toString();
                    Intent in = new Intent(view.getContext(), ProductDetails.class);
                    in.putExtra("shopprdid", productidtext);
                    in.putExtra("qty", "1");
                    in.putExtra("pimage", productimagepathTv.getText().toString());

                    view.getContext().startActivity(in);


                }
            });

            checkinImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {



                    Intent incheck = new Intent(v.getContext(), Checkin.class);
                    incheck.putExtra("shoppid", productidTv.getText().toString());
                    incheck.putExtra("shopid","0");
                    v.getContext().startActivity(incheck);

                }
            });
            checkoutImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {


                    boolean value = helper.CheckIsDataAlreadyInDBorNot(productidTv.getText().toString());
                    if (value != true)
                    {
                        pricevalue = Integer.parseInt(productpriceTv.getText().toString());
                        String pimageval = productimagepathTv.getText().toString();
                        vendorquant= Integer.parseInt(vendorqtyTv.getText().toString());
                        System.out.println("subvendq--"+vendorquant);
                        helper.insert(productidTv.getText().toString(), 1, pricevalue,vendorquant, productnameTv.getText().toString(), pimageval, shopnameTv.getText().toString());
                        Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {

                        Toast.makeText(context, "Already Exists", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }

}


