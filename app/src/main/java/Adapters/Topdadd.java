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
import pojo.category;
import sample.com.closebuy.R;

/**
 * Created by Hp on 1/2/2017.
 */

public class Topdadd extends RecyclerView.Adapter<Topdadd.ViewHolder>
{
    private List<advertisment> itemList;
    private Context context;


    public Topdadd(Context context ,List<advertisment>itemList)
    {
        this.context=context;
        this.itemList=itemList;
    }


    @Override
    public Topdadd.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View layoutview= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_adds,null);
        Topdadd.ViewHolder add=new Topdadd.ViewHolder(layoutview);
        return add;
    }

    @Override
    public void onBindViewHolder(Topdadd.ViewHolder holder, int position)
    {
        holder.AddidTv.setText(itemList.get(position).getId());
        Glide.with(context).load(itemList.get(position).getThumbnailUrl()).into(holder.iconImg);
    }

    @Override
    public int getItemCount()
    {
        return this.itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        TextView AddidTv;
        ImageView iconImg;

        public ViewHolder(View itemView)
        {

            super(itemView);

            iconImg = (ImageView) itemView.findViewById(R.id.add);
            AddidTv   = (TextView) itemView.findViewById(R.id.idvalue);


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

