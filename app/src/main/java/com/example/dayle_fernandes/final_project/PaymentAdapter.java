package com.example.dayle_fernandes.final_project;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;



/**
 * Created by dayle_fernandes on 09-Nov-16.
 */

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.ViewHolder>{


    private ArrayList<String> cc;
    PaymentAdapter selfRef;

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView cnum;

        public ViewHolder(final View view) {
            super(view);
            cnum = (TextView) view.findViewById(R.id.cc_num);
        }


    }

    public PaymentAdapter(ArrayList<String> cc) {
        this.cc = cc;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewtype){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cc_card,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position){
        String ccinfo = cc.get(position);
        ccinfo = ccinfo.replaceAll("....","$0");
        viewHolder.cnum.setText(ccinfo);
    }

    @Override
    public int getItemCount(){
        return cc.size();
    }
}
