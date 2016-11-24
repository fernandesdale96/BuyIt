package com.example.dayle_fernandes.final_project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.github.ajalt.reprint.core.AuthenticationFailureReason;
import com.github.ajalt.reprint.core.AuthenticationListener;
import com.github.ajalt.reprint.core.Reprint;
import com.google.android.gms.vision.text.Text;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.client.ClientProtocolException;

import static android.content.pm.PackageManager.PERMISSION_DENIED;
import static com.google.android.gms.analytics.internal.zzy.em;

/**
 * Created by aksharma2 on 11-11-2016.
 */
public class BasketActivity extends AppCompatActivity {

    RecyclerView aRecyclerView;
    RecyclerView.LayoutManager aLayoutManager;
    RecyclerView.Adapter aAdapter;
    testProductList list;
    BasketActivity selfRef = this;

    ArrayList<ProductInfo> productsList;
    // url to get all products list


    private ProgressDialog pDialog;
    private static final String PRODUCTS = "products";
    private static final String ID = "pid";
    private static final String NAME = "name";
    private static final String PRICE = "price";
    private static final String LOCATION = "location";
    private static final String DISTANCE = "distance";
    private static final String DESCRIPTION = "description";
    private static final String CREATED_AT = "created_at";
    private static final String UPDATED_AT = "updated_at";
    private static final String EMAIL = "email";

    private double tprice;
    private int titem_num;
    private TextView total_price;
    private TextView total_num;
    private Button purchase;

    JSONArray products = null;
    JSONObject obj;
    String aemail = LoginActivity.getEmail();
    private String url_all_products = "http://10.0.2.2/FinalProject/show_basket_products.php";
    private String url_purchase = "http://10.0.2.2/FinalProject/purchase.php";
    boolean running;
    // ArrayList<HashMap<String, String>> productList = new ArrayList<HashMap<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basket_view);

        aRecyclerView = (RecyclerView) findViewById(R.id.productlist_recyclerView);
        aRecyclerView.setHasFixedSize(true);
        total_num = (TextView) findViewById(R.id.item_number);
        total_price = (TextView) findViewById(R.id.total_amount);
        purchase = (Button) findViewById(R.id.btn_purchase);

        purchase.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){



                for(ProductInfo p : productsList){
                    String name = p.getName();
                    String price = Double.toString(p.getPrice());
                    String location = p.getStore();
                    String email = aemail;
                    new BuyProduct().execute(name,price,location,email);
                }



                pDialog = new ProgressDialog(selfRef);
                pDialog.show(selfRef,"","Purchasing...",true);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {

                        Intent i = new Intent(BasketActivity.this,PurchasedActivity.class);
                        pDialog.dismiss();
                        startActivity(i);

                        finish();
                    }
                }, 3000);





            }
        });
        running = false;


        // Hashmap for ListView
        productsList = new ArrayList<ProductInfo>();

        aLayoutManager = new LinearLayoutManager(this);
        aRecyclerView.setLayoutManager(aLayoutManager);
        tprice = 0;
        titem_num = 0;

        new GetQuestions().execute();
    }






    private class BuyProduct extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... args){
            org.apache.http.params.HttpParams httpParameters = new org.apache.http.params.BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
            HttpConnectionParams.setSoTimeout(httpParameters, 5000);
            HttpClient httpClient = new DefaultHttpClient(httpParameters);
            org.apache.http.client.methods.HttpPost httpPost = new org.apache.http.client.methods.HttpPost(url_purchase);
            String jsonresult = "";

            String name = args[0];
            String price = args[1];
            String store = args[2];
            String aemail = args[3];


            try{
                List params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("name", name));
                params.add(new BasicNameValuePair("price", price));
                params.add(new BasicNameValuePair("location",store));
                params.add(new BasicNameValuePair("email",aemail));
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

                Toast.makeText(BasketActivity.this, "Server connection failed", Toast.LENGTH_LONG).show();


                return;

            }

            String jsonResult = returnParsedJsonObject(result);

            if (jsonResult == "false") {

                Toast.makeText(BasketActivity.this, "Product could not be purchased", Toast.LENGTH_LONG).show();

                return;
            }

            if(jsonResult == "true"){
                Toast.makeText(BasketActivity.this, "Product Purchased", Toast.LENGTH_LONG).show();


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

    private class GetQuestions extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(selfRef);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(true);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            String jsonStr = sh.makeServiceCall(url_all_products, ServiceHandler.GET);
            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    products = jsonObj.getJSONArray(PRODUCTS);
                    // looping through All Questions
                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);

                        if (c.getString(EMAIL).equals(aemail)) {
                            String id = c.getString(ID);
                            String name = c.getString(NAME);
                            String price = c.getString(PRICE);
                            String loc = c.getString(LOCATION);

                            tprice = tprice + Double.parseDouble(price);
                            titem_num++;


                            ProductInfo p = new ProductInfo(name, Double.parseDouble(price), loc);


                            productsList.add(p);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            aAdapter = new ProductAdapter(productsList);
            aRecyclerView.setAdapter(aAdapter);
            tprice = Math.round(tprice);
            String astring = Double.toString(tprice) + "HKD";
            total_price.setText(astring);
            String bstring = Integer.toString(titem_num);
            total_num.setText(bstring);


        }
    }



}