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
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import cz.msebera.android.httpclient.client.ClientProtocolException;

import static android.R.attr.description;


/**
 * Created by dayle_fernandes on 31-Oct-16.
 */


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private ArrayList<ProductInfo> inf;
    ProductAdapter selfRef = this;
    String url_basket = "http://175.159.69.203/FinalProject/basket.php";
    private ProgressDialog pDialog;
    private static final String TAG_SUCCESS = "success";
    private String email = LoginActivity.getEmail();

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

                    nm = (pName.getText().toString());

                    pr = (pPrice.getText().toString());
                    pr = pr.replaceAll("[^\\d.]", "");

                    st = aStore.getText().toString();


                    new CreateNewProduct().execute(nm,pr,st,email);


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

        class CreateNewProduct extends AsyncTask<String, Void, String>{

            protected void onPreExecute() {
                Toast.makeText(ctx,"Adding to basket",Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... args){
                org.apache.http.params.HttpParams httpParameters = new org.apache.http.params.BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
                HttpConnectionParams.setSoTimeout(httpParameters, 5000);
                HttpClient httpClient = new DefaultHttpClient(httpParameters);
                org.apache.http.client.methods.HttpPost httpPost = new org.apache.http.client.methods.HttpPost(url_basket);
                String jsonresult = "";

                String name = args[0];
                String price = args[1];
                String store = args[2];
                String description = "x";
                String uemail = args[3];

                try{
                    List params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("name", name));
                    params.add(new BasicNameValuePair("price", price));
                    params.add(new BasicNameValuePair("location",store));
                    params.add(new BasicNameValuePair("description", description));
                    params.add(new BasicNameValuePair("email",uemail));
                    Log.d("Sending data", params.toString());

                    httpPost.setEntity(new UrlEncodedFormEntity(params));
                    HttpResponse response = httpClient.execute(httpPost);
                    jsonresult = inputStreamToString(response.getEntity().getContent()).toString();
                }catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Log.d("Register Response", jsonresult.toString());

                return jsonresult;

            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                System.out.println("Resulted Value: " + result);

                if (result.equals("") || result == null) {

                    Toast.makeText(ctx, "Server connection failed", Toast.LENGTH_LONG).show();


                    return;

                }

                String jsonResult = returnParsedJsonObject(result);

                if (jsonResult == "false") {

                    Toast.makeText(ctx, "Error adding product to basket", Toast.LENGTH_LONG).show();

                    return;
                }

                if(jsonResult == "true"){
                    Toast.makeText(ctx, "Product added to basket", Toast.LENGTH_LONG).show();


                }
            }

            private StringBuilder inputStreamToString(InputStream is) {

                String rLine = "";

                StringBuilder answer = new StringBuilder();

                BufferedReader br = new BufferedReader(new InputStreamReader(is));

                try {

                    while ((rLine = br.readLine()) != null) {

                        answer.append(rLine);

                    }

                } catch (IOException e) {


                    e.printStackTrace();

                }

                return answer;

            }

            private String returnParsedJsonObject(String result) {

                JSONObject resultObject = null;

                String returnedResult = "";

                try {

                    resultObject = new JSONObject(result);

                    returnedResult = resultObject.getString("success");

                } catch (JSONException e) {

                    e.printStackTrace();

                }

                return returnedResult;

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
