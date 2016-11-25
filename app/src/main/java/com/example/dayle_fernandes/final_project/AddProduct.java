package com.example.dayle_fernandes.final_project;

/**
 * Created by aksharma2 on 05-11-2016.
 */

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
public class AddProduct extends Activity {

    private ProgressDialog pDialog;

    private static String url_create_product = "http://175.159.69.203/FinalProject/create_product.php";

    private static final String TAG_SUCCESS = "success";
    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    private String imageString;

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
        this.imageView = (ImageView)this.findViewById(R.id.imageView1);
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        imageString=BitMapToString(bitmap);
        imageString = getIntent().getStringExtra("str");
        Toast.makeText(this,imageString,Toast.LENGTH_SHORT).show();
//        Log.d("src", imageString);

        // Edit Text
        inputName = (EditText) findViewById(R.id.inputName);
        inputPrice = (EditText) findViewById(R.id.inputPrice);
        inputDesc = (EditText) findViewById(R.id.inputDesc);
        inputLocation = (EditText) findViewById(R.id.inputLocation);

        // Create button
        Button btnCreateProduct = (Button) findViewById(R.id.btnCreateProduct);
        Button photoButton = (Button) this.findViewById(R.id.btnCamera);

        photoButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });



        // button click event
        btnCreateProduct.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // creating new product in background thread
                String yourURL;
                String name = inputName.getText().toString();
                String price = inputPrice.getText().toString();
                String desc = inputDesc.getText().toString();
                String loc = inputLocation.getText().toString();
                String img = imageString;
                String[] args = {name,price,desc,loc,img};
                new CreateNewProduct().execute(args);
            }


        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            this.imageView.setImageBitmap(photo);
            BitmapDrawable drawable = (BitmapDrawable) this.imageView.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            imageString=BitMapToString(bitmap);
            data.putExtra("str",imageString);

        }
    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp=Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    //helper function
    public static String encodeTobase64(Bitmap image)
    {
        Bitmap immagex=image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b,Base64.DEFAULT);
        return imageEncoded;
    }

    //helper function
    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }



    private class CreateNewProduct extends AsyncTask<String, Void, String>{
        @Override
        protected void onPreExecute() {
            Toast.makeText(AddProduct.this, "Adding to basket", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... args){
            org.apache.http.params.HttpParams httpParameters = new org.apache.http.params.BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
            HttpConnectionParams.setSoTimeout(httpParameters, 5000);
            HttpClient httpClient = new DefaultHttpClient(httpParameters);
            org.apache.http.client.methods.HttpPost httpPost = new org.apache.http.client.methods.HttpPost(url_create_product);
            String jsonresult = "";

            String name = args[0];
            String price = args[1];
            String description = args[2];
            String location = args[3];


            try{
                List params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("name", name));
                params.add(new BasicNameValuePair("price", price));
                params.add(new BasicNameValuePair("description",description));
                params.add(new BasicNameValuePair("location",location));

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
                Toast.makeText(AddProduct.this, "Server connection failed", Toast.LENGTH_LONG).show();
                return;
            }
            String jsonResult = returnParsedJsonObject(result);
            if (jsonResult == "false") {
                Toast.makeText(AddProduct.this, "Error adding product to basket", Toast.LENGTH_LONG).show();
                return;
            }
            if(jsonResult == "true"){
                Toast.makeText(AddProduct.this, "Product added to basket", Toast.LENGTH_LONG).show();
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
