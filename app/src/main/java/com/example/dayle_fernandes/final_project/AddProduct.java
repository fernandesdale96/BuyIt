package com.example.dayle_fernandes.final_project;

/**
 * Created by aksharma2 on 05-11-2016.
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
public class AddProduct extends Activity {

    private ProgressDialog pDialog;

    private static String url_create_product = "http://10.0.2.2/FinalProject/create_product.php";

    private static final String TAG_SUCCESS = "success";

    //JSONParser jsonParser = new JSONParser();
    EditText inputName;
    EditText inputPrice;
    EditText inputDesc;
    EditText inputLocation;
    EditText inputDistance;
    AddProduct selfRef=this;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product);

        // Edit Text
        inputName = (EditText) findViewById(R.id.inputName);
        inputPrice = (EditText) findViewById(R.id.inputPrice);
        inputDesc = (EditText) findViewById(R.id.inputDesc);
        inputLocation = (EditText) findViewById(R.id.inputLocation);
        inputDistance = (EditText) findViewById(R.id.inputDistance);

        // Create button
        Button btnCreateProduct = (Button) findViewById(R.id.btnCreateProduct);

        // button click event
        btnCreateProduct.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // creating new product in background thread
                new CreateNewProduct().execute();
            }


        });
    }

    class CreateNewProduct extends AsyncTask<String, String, String>  {

        String name;
        String price;
        String location;
        String distance;
        String description;
        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AddProduct.this);
            pDialog.setMessage("Creating Product..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
            name = inputName.getText().toString();
            price = inputPrice.getText().toString();
            location=inputLocation.getText().toString();
            distance=inputDistance.getText().toString();
            description = inputDesc.getText().toString();
        }

        /**
         * Creating product
         */

        protected String doInBackground(String... args) {
            String jsonStr="";
            try {
                ServiceHandler sh = new ServiceHandler();
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("name", name));
                params.add(new BasicNameValuePair("price", price));
                params.add(new BasicNameValuePair("description", description));
                params.add(new BasicNameValuePair("location", location));
                params.add(new BasicNameValuePair("distance", distance));

                // getting JSON Object
                // Note that create product url accepts POST method
                //  JSONObject json = jsonParser.makeHttpRequest(url_create_product,
                //          "POST", params);

                 jsonStr = sh.makeServiceCall(url_create_product, ServiceHandler.POST, params);

                // check log cat fro response
                Log.d("Create Response", jsonStr.toString());
            }catch(Exception e){

            }
                // check for success tag
                {
                    int success = jsonStr.compareTo(TAG_SUCCESS);

                    if (success == 0) {
                        // successfully created product
                        Intent i = new Intent(getApplicationContext(), AddProduct.class);
                        startActivity(i);
                        Toast.makeText(selfRef, "Success", Toast.LENGTH_SHORT);

                        // closing this screen
                        finish();
                    } else {
                        // failed to create product
                    }
                }

                return null;

        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            selfRef.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pDialog.dismiss();
                }
            });

        }
    }
}