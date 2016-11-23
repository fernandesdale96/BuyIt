package com.example.dayle_fernandes.final_project;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.braintreepayments.cardform.OnCardFormSubmitListener;
import com.braintreepayments.cardform.utils.CardType;
import com.braintreepayments.cardform.view.CardEditText;
import com.braintreepayments.cardform.view.CardForm;
import com.braintreepayments.cardform.view.SupportedCardTypesView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dayle_fernandes on 09-Nov-16.
 */


public class CreditCardForm extends AppCompatActivity implements OnCardFormSubmitListener, CardEditText.OnCardTypeChangedListener {
    private static final CardType[] SUPPORTED_CARD_TYPES = { CardType.VISA, CardType.MASTERCARD, CardType.DISCOVER,
            CardType.AMEX, CardType.DINERS_CLUB, CardType.JCB, CardType.MAESTRO, CardType.UNIONPAY };
    private SupportedCardTypesView mSupportedCardTypesView;

    protected CardForm mCardForm;
    private String email = LoginActivity.getEmail();
    private static String url = "http://10.0.2.2/FinalProject/ccard.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creditcard_form);

        mSupportedCardTypesView = (SupportedCardTypesView) findViewById(R.id.supported_card_types);
        mSupportedCardTypesView.setSupportedCardTypes(SUPPORTED_CARD_TYPES);

        mCardForm = (CardForm) findViewById(R.id.card_form);
        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .postalCodeRequired(true)
                .actionLabel(getString(R.string.purchase))
                .setup(this);
        mCardForm.setOnCardFormSubmitListener(this);
        mCardForm.setOnCardTypeChangedListener(this);

        // Warning: this is for development purposes only and should never be done outside of this example app.
        // Failure to set FLAG_SECURE exposes your app to screenshots allowing other apps to steal card information.
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

    }

    @Override
    public void onCardTypeChanged(CardType cardType) {
        if (cardType == CardType.EMPTY) {
            mSupportedCardTypesView.setSupportedCardTypes(SUPPORTED_CARD_TYPES);
        } else {
            mSupportedCardTypesView.setSelected(cardType);
        }
    }

    @Override
    public void onBackPressed(){
        finish();

    }

    @Override
    public void onCardFormSubmit() {
        if (mCardForm.isValid()) {
            Toast.makeText(this, "Payment Method Added", Toast.LENGTH_SHORT).show();
            String cnum = mCardForm.getCardNumber();
            //String emonth = mCardForm.getExpirationMonth();
            //String eyear = mCardForm.getExpirationYear();
            //String cvv = mCardForm.getCvv();
            //String mnum = mCardForm.getMobileNumber();

            //CCInfo cc = new CCInfo(cnum,eyear,cvv,mnum,emonth);
            //testCCList.addCC(cc);

            new AddCC().execute(cnum,email);


            finish();
        } else {
            mCardForm.validate();
            Toast.makeText(this, R.string.invalid, Toast.LENGTH_SHORT).show();
        }
    }

    private class AddCC extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            Toast.makeText(CreditCardForm.this, "Adding to basket", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... args){
            org.apache.http.params.HttpParams httpParameters = new org.apache.http.params.BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
            HttpConnectionParams.setSoTimeout(httpParameters, 5000);
            HttpClient httpClient = new DefaultHttpClient(httpParameters);
            org.apache.http.client.methods.HttpPost httpPost = new org.apache.http.client.methods.HttpPost(url);
            String jsonresult = "";

            String uemail = args[1];
            String cnum = args[0];

            try{
                List params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("cnum", cnum));
                params.add(new BasicNameValuePair("email", uemail));
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
                Toast.makeText(CreditCardForm.this, "Server connection failed", Toast.LENGTH_LONG).show();
                return;
            }
            String jsonResult = returnParsedJsonObject(result);
            if (jsonResult == "false") {
                Toast.makeText(CreditCardForm.this, "Error registering card", Toast.LENGTH_LONG).show();
                return;
            }
            if(jsonResult == "true"){
                Toast.makeText(CreditCardForm.this, "Card has been successfully registered", Toast.LENGTH_LONG).show();
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
