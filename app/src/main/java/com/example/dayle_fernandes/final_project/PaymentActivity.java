package com.example.dayle_fernandes.final_project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.os.Build.ID;


/**
 * Created by dayle_fernandes on 09-Nov-16.
 */

public class PaymentActivity extends AppCompatActivity {

    RecyclerView aRecyclerView;
    RecyclerView.LayoutManager aLayoutManager;
    RecyclerView.Adapter aAdapter;
    String email = LoginActivity.getEmail();
    String url = "http://175.159.69.203/FinalProject/show_cc.php";
    ProgressDialog pDialog;

    ArrayList<String> cclist;
    JSONArray cards = null;

    @Override
    public void onCreate(Bundle savedBundleInstance) {
        super.onCreate(savedBundleInstance);
        setContentView(R.layout.activity_payment);

        cclist = new ArrayList<>();

        aRecyclerView = (RecyclerView) findViewById(R.id.payment_recyclerview);
        aRecyclerView.hasFixedSize();

        aLayoutManager = new LinearLayoutManager(this);
        aRecyclerView.setLayoutManager(aLayoutManager);

        aRecyclerView.setAdapter(aAdapter);

        new GetCC().execute();
    }


    private class GetCC extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(PaymentActivity.this,"Fetching Card Data",Toast.LENGTH_LONG);

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
                    cards = jsonObj.getJSONArray("cards");
                    // looping through All Questions
                    for (int i = 0; i < cards.length(); i++) {
                        JSONObject c = cards.getJSONObject(i);

                        if (c.getString("email").equals(email)) {


                            String ccnum = c.getString("cnum");





                            cclist.add(ccnum);
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

            aAdapter = new PaymentAdapter(cclist);
            aRecyclerView.setAdapter(aAdapter);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_payment, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.new_credit_card) {
            Intent intent = new Intent(this,CreditCardForm.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
