package com.example.dayle_fernandes.final_project;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by aksharma2 on 11-11-2016.
 */
public class BasketActivity extends AppCompatActivity{

    RecyclerView aRecyclerView;
    RecyclerView.LayoutManager aLayoutManager;
    RecyclerView.Adapter aAdapter;
    testProductList list;
    BasketActivity selfRef=this;

    ArrayList<ProductInfo> productsList;
    // url to get all products list
    private static String url_all_products = "http://10.0.2.2/FinalProject/show_basket_products.php";

    private ProgressDialog pDialog;
    private static final String PRODUCTS = "products";
    private static final String ID= "pid";
    private static final String NAME= "name";
    private static final String PRICE= "price";
    private static final String LOCATION= "location";
    private static final String DISTANCE= "distance";
    private static final String DESCRIPTION= "description";
    private static final String CREATED_AT= "created_at";
    private static final String UPDATED_AT= "updated_at";

    JSONArray products=null;
    JSONObject obj;
    // ArrayList<HashMap<String, String>> productList = new ArrayList<HashMap<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basket_view);

        aRecyclerView = (RecyclerView) findViewById(R.id.productlist_recyclerView);
        aRecyclerView.setHasFixedSize(true);

        // Hashmap for ListView
        productsList = new ArrayList<ProductInfo>();

        aLayoutManager = new LinearLayoutManager(this);
        aRecyclerView.setLayoutManager(aLayoutManager);

        new GetQuestions().execute();
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
                try{
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    products = jsonObj.getJSONArray(PRODUCTS);
                    // looping through All Questions
                    for(int i = 0; i < products.length(); i++){
                        JSONObject c = products.getJSONObject(i);

                        String id = c.getString(ID);
                        String name= c.getString(NAME);
                        String price = c.getString(PRICE);
                        String loc = c.getString(LOCATION);

                        ProductInfo p=new ProductInfo(name,Double.parseDouble(price),loc);


                        productsList.add(p);
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
            aAdapter=new ProductAdapter(productsList);
            aRecyclerView.setAdapter(aAdapter);
        }
    }

}

