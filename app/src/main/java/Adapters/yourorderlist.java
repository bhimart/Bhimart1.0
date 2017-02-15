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
        holder.prdname.setText(itemList.get(position).getProdname());
        Glide.with(context).load(itemList.get(position).getThumbnailUrl()).into(holder.icon);
        holder.qty.setText(itemList.get(position).getqty());
        holder.totalprice.setText(itemList.get(position).gettotalprice());
        holder.orderid.setText(itemList.get(position).getorderid());
        holder.seller.setText(itemList.get(position).getShopname());

        System.out.println("holder---"+holder.prdname.getText().toString());
    }

    @Override
    public int getItemCount()
    {
        return this.itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        TextView name,id,orderid,prdname,qty,totalprice,seller;
        ImageView icon;

        public ViewHolder(View itemView)
        {

            super(itemView);

            icon = (ImageView) itemView.findViewById(R.id.prdimg);
            orderid= (TextView) itemView.findViewById(R.id.orderid);
            prdname=(TextView) itemView.findViewById(R.id.prdname);
            qty=(TextView) itemView.findViewById(R.id.qty);
            totalprice=(TextView) itemView.findViewById(R.id.totalprice);
            seller=(TextView) itemView.findViewById(R.id.seller);
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
