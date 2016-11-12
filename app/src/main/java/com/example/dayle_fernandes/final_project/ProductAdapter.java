package com.example.dayle_fernandes.final_project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * Created by dayle_fernandes on 31-Oct-16.
 */



public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder>  {

    private ArrayList<ProductInfo> inf;
    ProductAdapter selfRef;

    //helper method to get ProductInfo object
    private ProductInfo getInfo(String name){
        ProductInfo pinfo=null;
        for(ProductInfo x:inf){
            if(x.getName().equalsIgnoreCase(name))
                pinfo=x;
        }
        return pinfo;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView pName;
        TextView pPrice;
        TextView aDistance;
        TextView aStore;
        private Context ctx=null;


        public ViewHolder(final View view){
            super(view);
            pName = (TextView) view.findViewById(R.id.selected_prod_name);
            pPrice = (TextView) view.findViewById(R.id.selected_prod_price);
            aDistance = (TextView) view.findViewById(R.id.product_distance);
            aStore = (TextView) view.findViewById(R.id.product_store);
            ctx=view.getContext();

            //To set onClick Listener for each item which is held by ViewHolder
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i;
                    ProductInfo pinfo= getInfo(pName.getText().toString());
                    //Toast.makeText(view.getContext(),pName.getText().toString(),Toast.LENGTH_LONG).show();
                    String name=pinfo.getName();
                    String price=Double.toString(pinfo.getPrice());
                    String store=pinfo.getStore();
                    String dist=Double.toString(pinfo.getDistance());
                    i=new Intent(ctx,LocationActivity.class);
                    Bundle b=new Bundle();
                    b.putString("PROD_NAME",name);
                    b.putString("PROD_PRICE",price);
                    b.putString("PROD_STORE",store);
                    b.putString("PROD_DIST",dist);

                    i.putExtras(b);
                    //  f.putExtra(PROD_PRICE,price);
                    //  i.putExtra(PROD_STORE,store);
                    //  i.putExtra(PROD_PRICE,Double.toString(price));
                    //  i.putExtra(PROD_DIST,Double.toString(dist));
                    v.getContext().startActivity(i);

                }
            });

        }

    }

    public ProductAdapter(ArrayList<ProductInfo> prinfo){
        this.inf = prinfo;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_card,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        ProductInfo product = inf.get(position);
        holder.pName.setText(product.getName());
        double n = product.getPrice();
        String s = String.valueOf(n) + " HKD";
        holder.pPrice.setText(s);
        double m = product.getDistance();
        String q = String.valueOf(m) + " KM";
        holder.aDistance.setText(q);
        holder.aStore.setText(product.getStore());


    }

    @Override
    public int getItemCount(){
        return inf.size();

    }




}
