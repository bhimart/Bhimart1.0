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
        holder.id.setText(itemList.get(position).getId());
        Glide.with(context).load(itemList.get(position).getThumbnailUrl()).into(holder.icon);
    }

    @Override
    public int getItemCount()
    {
        return this.itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        TextView name,id;
        ImageView icon;

        public ViewHolder(View itemView)
        {

            super(itemView);

            icon = (ImageView) itemView.findViewById(R.id.add);
            id   = (TextView) itemView.findViewById(R.id.idvalue);


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

