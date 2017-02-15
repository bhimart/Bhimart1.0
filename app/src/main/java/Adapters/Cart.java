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
    public Cart(Context context, List<cartvalues> itemList) {
        this.context=context;
        this.itemList=itemList;
        helper = new DbHelper(context);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        holder.prdid.setText(itemList.get(position).getid());
        holder.prdname.setText(itemList.get(position).getProdname());
        holder.price.setText(itemList.get(position).getprice()+"");
        Glide.with(context).load(itemList.get(position).getThumbnailUrl()).into(holder.icon);
        holder.qty.setText(itemList.get(position).getqty()+"");
        holder.prdtotal.setText(itemList.get(position).gettotalprice()+"");
        System.out.println("holder---"+holder.prdname.getText().toString());


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

        TextView prdname,prdid,price,qty,prdtotal;
        ImageView icon,subtractQty,addQty;
        private int itmQty=1;
        private int SELECTED_POS;
        String prodid;
        Button remove;
        int tot;
        public ViewHolder(View itemView)
        {

            super(itemView);

            icon = (ImageView) itemView.findViewById(R.id.prdimg);
            prdname = (TextView) itemView.findViewById(R.id.prdname);
            prdid   = (TextView) itemView.findViewById(R.id.pid);
            price   = (TextView) itemView.findViewById(R.id.prdprice);
            qty     =  (TextView) itemView.findViewById(R.id.qtyval);
            addQty=(ImageView)itemView.findViewById(R.id.add);
            remove=(Button) itemView.findViewById(R.id.remove);
            subtractQty=(ImageView)itemView.findViewById(R.id.sub);
            prdtotal=(TextView) itemView.findViewById(R.id.prdtotal);
            itemView.setOnClickListener(this);
            addQty.setOnClickListener(this);
            subtractQty.setOnClickListener(this);
            remove.setOnClickListener(this);



   }

        @Override
        public void onClick(View v) {




            if(v==addQty)
            {
             ++itmQty;
            cartvalues item = itemList.get(SELECTED_POS);
            item.setqty(itmQty);
            qty.setText(String.valueOf(itmQty));
            prodid=prdid.getText().toString();
            helper.updateqty(prodid,itmQty);
             tot=(Integer.parseInt(price.getText().toString())* itmQty);
                prdtotal.setText(tot+"");

                fullprdtotal+=tot;
                System.out.println("full---"+fullprdtotal);

            }

            if(v==subtractQty)
            {
              if(itmQty > 1) {
                  --itmQty;
                  cartvalues item = itemList.get(SELECTED_POS);
                  item.setqty(itmQty);
                  qty.setText(String.valueOf(itmQty));
                  prodid = prdid.getText().toString();
                  helper.updateqty(prodid, itmQty);
                  tot=(Integer.parseInt(price.getText().toString())* itmQty);
                  prdtotal.setText(tot+"");
              }else{

                  qty.setText("1");
              }
            }
            if(v==remove){
                prodid=prdid.getText().toString();
                 helper.delete(prodid);
                  Intent in=new Intent(v.getContext(),Yourcart.class);

                  v.getContext().startActivity(in);

            }


        }
    }
}
