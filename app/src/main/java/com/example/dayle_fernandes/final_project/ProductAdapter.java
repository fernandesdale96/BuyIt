package com.example.dayle_fernandes.final_project;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;


import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import DB.ProductHandler;


/**
 * Created by dayle_fernandes on 31-Oct-16.
 */


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private ArrayList<ProductInfo> inf;
    ProductAdapter selfRef = this;
    String url_add_product = "http://10.0.2.2/FinalProject/add_basket_product.php";
    private ProgressDialog pDialog;
    private static final String TAG_SUCCESS = "success";

    public String nm, st, pr, dis;

    //helper method to get ProductInfo object
    private ProductInfo getInfo(String name) {
        ProductInfo pinfo = null;
        for (ProductInfo x : inf) {
            if (x.getName().equalsIgnoreCase(name))
                pinfo = x;
        }
        return pinfo;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView pName;
        TextView pPrice;
        TextView aDistance;
        TextView aStore;
        private Context ctx = null;
        OnSwipeTouchListener onSwipeTouchListener;
        ProductInfo p;

        public ViewHolder(final View view) {
            super(view);
            ctx = view.getContext();
            pName = (TextView) view.findViewById(R.id.selected_prod_name);
            pPrice = (TextView) view.findViewById(R.id.selected_prod_price);
            aStore = (TextView) view.findViewById(R.id.product_store);

            onSwipeTouchListener = (new OnSwipeTouchListener(ctx) {
                public void onSwipeTop() {
                    Toast.makeText(ctx, "top", Toast.LENGTH_SHORT).show();
                }

                public void onSwipeRight() {
                    Toast.makeText(ctx, "right", Toast.LENGTH_SHORT).show();
                }

                public void onSwipeLeft() {
                    HttpClient client = new DefaultHttpClient();
                    String yourURL;
                    Toast.makeText(ctx, "Added to cart", Toast.LENGTH_SHORT).show();
                    //  p.setName(pName.getText().toString());
                    nm = (pName.getText().toString());
                    // p.setPrice(Double.parseDouble(pPrice.getText().toString()));
                    pr = (pPrice.getText().toString());
                    pr = pr.replaceAll("[^\\d.]", "");
                    // p.setDistance(Double.parseDouble(aDistance.getText().toString()));
                    dis = (aDistance.getText().toString());
                    dis = dis.replaceAll("[^\\d.]", "");
                    // p.setStore(aStore.getText().toString());
                    st = aStore.getText().toString();

                    yourURL = "http://10.0.2.2/FinalProject/add_basket_product.php?name=" + nm + "&price=" + pr + "&distance=" + dis + "&location=" + st + "&description=x";
                    Log.d("url: ", yourURL);

                    new CreateNewProduct().execute(yourURL);


                }

                public void onSwipeBottom() {
                    Toast.makeText(ctx, "bottom", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onClick() {
                    super.onClick();
                    Intent i;
                    ProductInfo pinfo = getInfo(pName.getText().toString());
                    //Toast.makeText(view.getContext(),pName.getText().toString(),Toast.LENGTH_LONG).show();
                    String name = pinfo.getName();
                    String price = Double.toString(pinfo.getPrice());
                    String store = pinfo.getStore();

                    i = new Intent(ctx, LocationActivity.class);
                    Bundle b = new Bundle();
                    b.putString("PROD_NAME", name);
                    b.putString("PROD_PRICE", price);
                    b.putString("PROD_STORE", store);


                    i.putExtras(b);
                    view.getContext().startActivity(i);

                }
            });

            //To set onClick Listener for each item which is held by ViewHolder
            view.setOnTouchListener(onSwipeTouchListener);
        }

        class CreateNewProduct extends AsyncTask<String, String, String> {

            String name=ViewHolder.this.pName.toString();
                       String price=ViewHolder.this.pPrice.toString();;
                        String location=ViewHolder.this.aStore.toString();;
                       String distance=ViewHolder.this.aDistance.toString();;
                        String description="x";

            /**
             * Before starting background thread Show Progress Dialog
             */
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pDialog = new ProgressDialog(ctx);
                pDialog.setMessage("Creating Product..");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(true);
                pDialog.show();

            }

            /**
             * Creating product
             */
            @Override
            protected String doInBackground(String... args) {

                String jsonStr = "";
                try {
                    DefaultHttpClient client = new DefaultHttpClient();
                                        HttpResponse res = client.execute(new HttpGet(args[0]));
                    // Building Parameters

                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("name", "banana"));
                                        params.add(new BasicNameValuePair("price", "banana"));
                                        params.add(new BasicNameValuePair("location", "banana"));
                                        params.add(new BasicNameValuePair("distance", "banana"));
                                        params.add(new BasicNameValuePair("description", "banana"));

                    Log.d("Create Response", distance);
                    //jsonStr = sh.makeServiceCall(url_add_product, ServiceHandler.POST, params);
                    // check log cat fro response
                    Log.d("Create Response", jsonStr.toString());
                } catch (Exception e) {

                }
                // check for success tag
                return "success";

            }

            /**
             * After completing background task Dismiss the progress dialog
             **/
            protected void onPostExecute(String result) {
                // dismiss the dialog once done

                // Dismiss the progress dialog
                if (pDialog.isShowing())
                    pDialog.dismiss();
            }
        }

    }

    public ProductAdapter(ArrayList<ProductInfo> prinfo) {
        this.inf = prinfo;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_card, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ProductInfo product = inf.get(position);
        holder.pName.setText(product.getName());
        double n = product.getPrice();
        String s = String.valueOf(n) + " HKD";
        holder.pPrice.setText(s);
        holder.aStore.setText(product.getStore());
    }

    @Override
    public int getItemCount() {
        return inf.size();
    }

}
