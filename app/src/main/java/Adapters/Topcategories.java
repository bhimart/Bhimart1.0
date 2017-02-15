package Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import pojo.category;
import sample.com.closebuy.ConnectionDetector;
import sample.com.closebuy.Homepage;
import sample.com.closebuy.R;
import sample.com.closebuy.Subcategory;

/**
 * Created by Android on 29-12-2016.
 */

public class Topcategories extends RecyclerView.Adapter<Topcategories.ViewHolder>
{
    private List<category>itemList;
    private Context context;
    private Boolean Isinternetpresent = false;
    ConnectionDetector cd;

    public Topcategories(Context context ,List<category>itemList)
    {
        this.context=context;
        this.itemList=itemList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {

       View layoutview= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_topcategory,null);
        ViewHolder cat=new ViewHolder(layoutview);
        return cat;
    }

    @Override
    public void onBindViewHolder(Topcategories.ViewHolder holder, int position)
    {
        holder.id.setText(itemList.get(position).getid()+"");
        holder.name.setText(itemList.get(position).getName());
        holder.lat.setText(itemList.get(position).getlat());
        holder.lang.setText(itemList.get(position).getlang());

        cd = new ConnectionDetector(context);
        Isinternetpresent = cd.isConnectingToInternet();
        if (Isinternetpresent)
        {
            Glide.with(context).load(itemList.get(position).getThumbnailUrl()).into(holder.icon);
        }
        else
        {

            byte[] decodedString = Base64.decode(itemList.get(position).getimg(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.icon.setImageBitmap(decodedByte);

        }

    }

    @Override
    public int getItemCount()
    {
        return this.itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

     TextView name,id,lat,lang;
     ImageView icon;

        public ViewHolder(View itemView)
        {

            super(itemView);

            icon = (ImageView) itemView.findViewById(R.id.category);
            name = (TextView) itemView.findViewById(R.id.nametext);
            id   = (TextView) itemView.findViewById(R.id.idvalue);
            lat=(TextView) itemView.findViewById(R.id.lat);
            lang=(TextView) itemView.findViewById(R.id.lang);
            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Intent in=new Intent(view.getContext(), Subcategory.class);
                    in.putExtra("categoryid", id.getText().toString());
                    in.putExtra("lat",lat.getText().toString());
                    in.putExtra("lang",lang.getText().toString());
                    view.getContext().startActivity(in);


                }
            });

        }
    }
}