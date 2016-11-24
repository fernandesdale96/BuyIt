package com.example.dayle_fernandes.final_project;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

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

import static android.os.Build.ID;
import static com.google.firebase.analytics.FirebaseAnalytics.Param.PRICE;

/**
 * Created by dayle_fernandes on 23-Nov-16.
 */

public class PurchasedActivity extends AppCompatActivity{

    RecyclerView aRecyclerView;
    RecyclerView.LayoutManager aLayoutManager;
    RecyclerView.Adapter aAdapter;

    ArrayList<ProductInfo> productsList;

    private ProgressDialog pDialog;
    String url = "http://10.0.2.2/FinalProject/show_purchase.php";
    String url_delete = "http://10.0.2.2/FinalProject/delete_basket.php";
    String aemail = LoginActivity.getEmail();
    JSONArray products = null;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_view);

        aRecyclerView = (RecyclerView) findViewById(R.id.productlist_recyclerView);
        aRecyclerView.setHasFixedSize(true);

        productsList = new ArrayList<ProductInfo>();

        aLayoutManager = new LinearLayoutManager(this);
        aRecyclerView.setLayoutManager(aLayoutManager);

        new GetBasket().execute();
        new DeleteOrder().execute(aemail);
    }


    private class DeleteOrder extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... args){
            org.apache.http.params.HttpParams httpParameters = new org.apache.http.params.BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
            HttpConnectionParams.setSoTimeout(httpParameters, 5000);
            HttpClient httpClient = new DefaultHttpClient(httpParameters);
            org.apache.http.client.methods.HttpPost httpPost = new org.apache.http.client.methods.HttpPost(url_delete);
            String jsonresult = "";

            String email = args[0];



            try{
                List params = new ArrayList<NameValuePair>();

                params.add(new BasicNameValuePair("email",email));
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

                //Toast.makeText(PurchasedActivity.this, "Server connection failed", Toast.LENGTH_LONG).show();


                return;

            }

            String jsonResult = returnParsedJsonObject(result);

            if (jsonResult == "false") {

                //Toast.makeText(PurchasedActivity.this, "Products could not be removed from Basket", Toast.LENGTH_LONG).show();

                return;
            }

            if(jsonResult == "true"){
                //Toast.makeText(PurchasedActivity.this, "Product Removed from Basket", Toast.LENGTH_LONG).show();


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











    private class GetBasket extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            /*pDialog = new ProgressDialog(getApplicationContext());
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(true);
            pDialog.show();*/

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);
            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    products = jsonObj.getJSONArray("products");
                    // looping through All Questions
                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);

                        if (c.getString("email").equals(aemail)) {
                            String id = c.getString("pid");
                            String name = c.getString("name");
                            String price = c.getString("price");
                            String loc = c.getString("location");


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
            /*if (pDialog.isShowing())
                pDialog.dismiss();*/
            /**
             * Updating parsed JSON data into ListView
             * */
            aAdapter = new ProductAdapter(productsList);
            aRecyclerView.setAdapter(aAdapter);


        }
    }


}



