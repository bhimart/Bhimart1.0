package Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import pojo.advertisment;
import pojo.urorder;
import sample.com.closebuy.R;

/**
 * Created by GMSoft on 1/23/2017.
 */

public class yourorderlist extends RecyclerView.Adapter<yourorderlist.ViewHolder>
{
    private List<urorder> itemList;
    private Context context;


    public yourorderlist(Context context ,List<urorder>itemList)
    {
        this.context=context;
        this.itemList=itemList;
    }

    @Override
    public yourorderlist.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutview= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_yourorders,parent,false);
        ViewHolder add=new ViewHolder(layoutview);
        return add;
    }

    @Override
    public void onBindViewHolder(yourorderlist.ViewHolder holder, int position) {
        holder.productnameTv.setText(itemList.get(position).getProdname());
        Glide.with(context).load(itemList.get(position).getThumbnailUrl()).into(holder.iconImg);
        holder.quantityTv.setText(itemList.get(position).getqty());
        holder.totalpriceTv.setText(itemList.get(position).gettotalprice());
        holder.orderidTv.setText(itemList.get(position).getorderid());
        holder.sellerTv.setText(itemList.get(position).getShopname());

        System.out.println("holder---"+holder.productnameTv.getText().toString());
    }

    @Override
    public int getItemCount()
    {
        return this.itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        TextView orderidTv,productnameTv,quantityTv,totalpriceTv,sellerTv;
        ImageView iconImg;

        public ViewHolder(View itemView)
        {

            super(itemView);

            iconImg = (ImageView) itemView.findViewById(R.id.prdimg);
            orderidTv= (TextView) itemView.findViewById(R.id.orderid);
            productnameTv=(TextView) itemView.findViewById(R.id.prdname);
            quantityTv=(TextView) itemView.findViewById(R.id.qty);
            totalpriceTv=(TextView) itemView.findViewById(R.id.totalprice);
            sellerTv=(TextView) itemView.findViewById(R.id.seller);
            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {

                }
            });

        }
    }
}
