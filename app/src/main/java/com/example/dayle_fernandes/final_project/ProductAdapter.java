package com.example.dayle_fernandes.final_project;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import static android.os.Build.VERSION_CODES.M;


/**
 * Created by dayle_fernandes on 31-Oct-16.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private ArrayList<ProductInfo> inf;

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView pName;
        TextView pPrice;
        TextView aDistance;
        TextView aStore;

        public ViewHolder(View view){
            super(view);
            pName = (TextView) view.findViewById(R.id.product_name);
            pPrice = (TextView) view.findViewById(R.id.product_price);
            aDistance = (TextView) view.findViewById(R.id.product_distance);
            aStore = (TextView) view.findViewById(R.id.product_store);
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
