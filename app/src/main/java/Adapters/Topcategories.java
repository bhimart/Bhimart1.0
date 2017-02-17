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
        holder.categoryidTv.setText(itemList.get(position).getid()+"");
        holder.categorynameTv.setText(itemList.get(position).getName());
        holder.latitudeTv.setText(itemList.get(position).getlat());
        holder.langitudeTv .setText(itemList.get(position).getlang());

        cd = new ConnectionDetector(context);
        Isinternetpresent = cd.isConnectingToInternet();
        if (Isinternetpresent)
        {
            Glide.with(context).load(itemList.get(position).getThumbnailUrl()).into(holder.iconImg);
        }
        else
        {

            byte[] decodedString = Base64.decode(itemList.get(position).getimg(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.iconImg.setImageBitmap(decodedByte);

        }

    }

    @Override
    public int getItemCount()
    {
        return this.itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

     TextView categorynameTv,categoryidTv,latitudeTv,langitudeTv ;
     ImageView iconImg;

        public ViewHolder(View itemView)
        {

            super(itemView);

            iconImg = (ImageView) itemView.findViewById(R.id.category);
            categorynameTv = (TextView) itemView.findViewById(R.id.nametext);
            categoryidTv   = (TextView) itemView.findViewById(R.id.idvalue);
            latitudeTv=(TextView) itemView.findViewById(R.id.lat);
            langitudeTv =(TextView) itemView.findViewById(R.id.lang);

            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Intent in=new Intent(view.getContext(), Subcategory.class);
                    in.putExtra("categoryid", categoryidTv.getText().toString());
                    in.putExtra("lat",latitudeTv.getText().toString());
                    in.putExtra("lang",langitudeTv .getText().toString());
                    view.getContext().startActivity(in);


                }
            });

        }
    }
}