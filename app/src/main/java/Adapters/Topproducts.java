package Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import pojo.products;
import sample.com.closebuy.ProductDetails;
import sample.com.closebuy.R;

/**
 * Created by Android on 29-12-2016.
 */

public class Topproducts extends RecyclerView.Adapter<Topproducts.ViewHolder>
{
    private List<products> itemList;
    private Context context;


    public Topproducts(Context context ,List<products>itemList)
    {
        this.context=context;
        this.itemList=itemList;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View layoutview = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_topproducts,null);
        ViewHolder prod = new ViewHolder(layoutview);
        return prod;

    }

    @Override
    public void onBindViewHolder(Topproducts.ViewHolder holder, int position) {
        holder.prdid.setText(itemList.get(position).getid());
        holder.prdname.setText(itemList.get(position).getName());
        holder.price.setText(itemList.get(position).getprice());
        holder.imagepath.setText(itemList.get(position).getThumbnailUrl());
        Glide.with(context).load(itemList.get(position).getThumbnailUrl()).into(holder.icon);


    }



    @Override
    public int getItemCount() {
        return
                this.itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView prdname,prdid,price,imagepath;
        ImageView icon;
        public ViewHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.prodimg);
            prdname = (TextView) itemView.findViewById(R.id.uname);
            prdid   = (TextView) itemView.findViewById(R.id.idvalue1);
            price   = (TextView) itemView.findViewById(R.id.price);
           imagepath= (TextView) itemView.findViewById(R.id.imagepath);


            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                 Intent in=new Intent(view.getContext(), ProductDetails.class);
                    in.putExtra("shopprdid",prdid.getText().toString());
                    in.putExtra("pimage",imagepath.getText().toString());
                    view.getContext().startActivity(in);
                }
            });
        }

    }

}
