package com.example.dayle_fernandes.final_project;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import static com.example.dayle_fernandes.final_project.R.id.profile_image;
import static com.example.dayle_fernandes.final_project.R.id.start;
import static com.example.dayle_fernandes.final_project.R.id.user;
import android.util.Log;

import static java.lang.Math.*;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.NameValuePair;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.dayle_fernandes.final_project.R;
import com.google.android.gms.analytics.ecommerce.Product;
import com.google.android.gms.vision.text.Text;

import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.dayle_fernandes.final_project.R.id.toolbar;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TextView;
import org.apache.http.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView aRecyclerView;
    RecyclerView.LayoutManager aLayoutManager;
    RecyclerView.Adapter aAdapter;
    testProductList list;
    MainActivity selfRef=this;

    ArrayList<ProductInfo> productsList;
    // url to get all products list
    private static String url_all_products = "http://10.0.2.2/FinalProject/get_all_products.php";

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
        setContentView(com.example.dayle_fernandes.final_project.R.layout.activity_main);

        list = testProductList.getInstance();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton search = (FloatingActionButton) findViewById(com.example.dayle_fernandes.final_project.R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Add Search Function
                Snackbar.make(view, "Implement Search Function", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(com.example.dayle_fernandes.final_project.R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, com.example.dayle_fernandes.final_project.R.string.navigation_drawer_open, com.example.dayle_fernandes.final_project.R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        String email = LoginActivity.getEmail();
        String name = LoginActivity.getName();
        String profile = LoginActivity.getProfile();

        NavigationView navigationView = (NavigationView) findViewById(com.example.dayle_fernandes.final_project.R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View v = navigationView.getHeaderView(0);
        TextView nav_user = (TextView) v.findViewById(user);
        final ImageView user_profile = (ImageView) v.findViewById(profile_image);

        if (name == null) {
            nav_user.setText(email);
        } else {
            nav_user.setText(name);
        }

        if(profile != null) {
            Glide.with(getApplicationContext()).load(profile).asBitmap().into(new BitmapImageViewTarget(user_profile) {
                @Override
                protected void setResource(Bitmap resource){
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    user_profile.setImageDrawable(circularBitmapDrawable);
                }
            });
        }


        aRecyclerView = (RecyclerView) findViewById(R.id.productlist_recyclerView);
        aRecyclerView.setHasFixedSize(true);


        // Hashmap for ListView
        productsList = new ArrayList<ProductInfo>();

        aLayoutManager = new LinearLayoutManager(this);
        aRecyclerView.setLayoutManager(aLayoutManager);

        new GetQuestions().execute();

        // Loading products in Background Thread
        //new LoadAllProducts().execute();

        // Creating JSON Parser object

        //aAdapter = new ProductAdapter(list.getProducts());

       // aRecyclerView.setAdapter(aAdapter);
       // aAdapter.notifyDataSetChanged();
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
                        String dist = c.getString(DISTANCE);
                        ProductInfo p=new ProductInfo(name,Double.parseDouble(price),loc);


                        productsList.add(p);
                       /* // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // adding each child node to HashMap key => value
                        map.put(ID, id);
                        map.put(NAME, name);
                        map.put(PRICE, price);
                        map.put(LOCATION, loc);
                        map.put(DISTANCE, dist);
                        // adding HashList to ArrayList
                        productList.add(map);
                       */
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
          //  aAdapter.notifyDataSetChanged();
          /*  ListAdapter adapter = new SimpleAdapter(
                    getActivity(),
                    questionsList,
                    ask.code.R.layout.question_item,
                    new String[] {
                            TAG_NAME,
                            TAG_TITLE,
                            TAG_TIME},
                    new int[] {
                            ask.code.R.id.asker_name,
                            ask.code.R.id.question_title,
                            ask.code.R.id.question_rating});


            setListAdapter(adapter);  */



        }



    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(com.example.dayle_fernandes.final_project.R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(com.example.dayle_fernandes.final_project.R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == com.example.dayle_fernandes.final_project.R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == com.example.dayle_fernandes.final_project.R.id.nav_home) {

        } else if (id == com.example.dayle_fernandes.final_project.R.id.nav_payment) {
            Intent intent = new Intent(this,PaymentActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_basket) {
            Intent intent = new Intent(this,BasketActivity.class);
            startActivity(intent);

        }else if (id == R.id.add_product){
            Intent i = new Intent(this,AddProduct.class);
            startActivity(i);

        }else if (id == com.example.dayle_fernandes.final_project.R.id.nav_exit) {
            finish();
            System.exit(0);
        }else if(id == R.id.nav_history){
            Intent i = new Intent(this,PurchasedActivity.class);
            startActivity(i);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(com.example.dayle_fernandes.final_project.R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
