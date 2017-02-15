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
import pojo.shops;
import sample.com.closebuy.Checkin;
import sample.com.closebuy.R;
import sample.com.closebuy.Shopsubcat;

/**
 * Created by Hp on 1/2/2017.
 */

public class Topshops extends RecyclerView.Adapter<Topshops.ViewHolder>
{
    private List<shops> itemList;
    private Context context;


    public Topshops(Context context ,List<shops>itemList)
    {
        this.context=context;
        this.itemList=itemList;
    }


    @Override
    public Topshops.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View layoutview= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_topshops,null);
        Topshops.ViewHolder shop=new Topshops.ViewHolder(layoutview);
        return shop;
    }

    @Override
    public void onBindViewHolder(Topshops.ViewHolder holder, int position)
    {
        holder.id.setText(itemList.get(position).getid());
        holder.name.setText(itemList.get(position).getName());
        holder.distance.setText(itemList.get(position).getdistance());
        Glide.with(context).load(itemList.get(position).getThumbnailUrl()).into(holder.icon);
    }

    @Override
    public int getItemCount()
    {
        return this.itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        TextView name,id,distance;
        ImageView icon;

        public ViewHolder(View itemView)
        {

            super(itemView);

            icon = (ImageView) itemView.findViewById(R.id.shops);
            name = (TextView) itemView.findViewById(R.id.shopname);
            id   = (TextView) itemView.findViewById(R.id.idval);
            distance   = (TextView) itemView.findViewById(R.id.distance);
            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Intent in=new Intent(view.getContext(), Shopsubcat.class);
                    in.putExtra("shopid",id.getText().toString());
                    view.getContext().startActivity(in);
                }
            });

        }
    }

}
