package com.example.dayle_fernandes.final_project;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.cast.framework.*;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.vision.text.Text;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.client.ClientProtocolException;

import static android.app.Activity.RESULT_OK;
import static com.example.dayle_fernandes.final_project.R.id.btn_login;
import static com.example.dayle_fernandes.final_project.R.id.intent_action;
import static com.example.dayle_fernandes.final_project.R.id.start;
import static com.example.dayle_fernandes.final_project.R.string.valid;


/**
 * Created by dayle_fernandes on 04-Nov-16.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    private static final String Tag = "LoginActivity";
    private static final int REQUEST_SIGNIN = 007;

    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;

    private SignInButton btngoogle;
    private Button btnlogin;
    private EditText email;
    private EditText pass;
    private TextView signup;
    private static String name;
    private static String profile_url;
    private static String email_id;
    private String password;
    private boolean google_flag = false;
    private SessionManager session;


    private static String url_log_in = "http://10.0.2.2/FinalProject/login.php";
    private static final String TAG = LoginActivity.class.getSimpleName();


    public static String getName(){
        return name;
    }

    public static String getProfile(){
        return profile_url;
    }

    public static String getEmail(){
        return email_id;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.input_email);
        pass = (EditText) findViewById(R.id.input_password);
        btnlogin = (Button) findViewById(btn_login);
        btngoogle = (SignInButton) findViewById(R.id.btn_google);
        signup = (TextView) findViewById(R.id.link_signup);

        btngoogle.setOnClickListener(this);
        btnlogin.setOnClickListener(this);
        signup.setOnClickListener(this);


        session = new SessionManager(getApplicationContext());
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);






        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }






    private void google_login() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, REQUEST_SIGNIN);
        google_flag = true;
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount act = result.getSignInAccount();
            name = act.getDisplayName();
            //profile_url = act.getPhotoUrl().toString();
            email_id = act.getEmail();

            Context context = getApplicationContext();
            String text = "Name: " + name + " Email id " + email_id;
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();

        }
    }

    private void btn_login() {
        if (!validate()) {
            onLoginFailed();
            return;
        }
        btnlogin.setEnabled(false);

        String email_text = email.getText().toString().trim();
        String password_text = pass.getText().toString().trim();

        if(!email_text.isEmpty() && !password_text.isEmpty()) {
            new LoginUser().execute(email_text,password_text);
        }
        else{
            Toast.makeText(getApplicationContext(),
                    "Please enter your details!", Toast.LENGTH_LONG)
                    .show();
            btnlogin.setEnabled(true);
        }



    }

    private class LoginUser extends AsyncTask<String,Void,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.setMessage("Logging Account");
            mProgressDialog.setIndeterminate(false);
            showDialog();
        }

        @Override
        protected String doInBackground(String... args){
            org.apache.http.params.HttpParams httpParameters = new org.apache.http.params.BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
            HttpConnectionParams.setSoTimeout(httpParameters, 5000);
            HttpClient httpClient = new DefaultHttpClient(httpParameters);
            org.apache.http.client.methods.HttpPost httpPost = new org.apache.http.client.methods.HttpPost(url_log_in);
            String jsonresult = "";

            String aemail = args[0];
            email_id = aemail;
            String upass = args[1];

            try {
                List params = new ArrayList<NameValuePair>();

                //Log.d("sending name check",uname);
                params.add(new BasicNameValuePair("email", aemail));
                params.add(new BasicNameValuePair("password", upass));

                httpPost.setEntity(new UrlEncodedFormEntity(params));
                HttpResponse response = httpClient.execute(httpPost);
                jsonresult = inputStreamToString(response.getEntity().getContent()).toString();
                //jsonStr = sh.makeServiceCall(url_register,ServiceHandler.POST,params);
            } catch (ClientProtocolException e) {
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

                Toast.makeText(LoginActivity.this, "Server connection failed", Toast.LENGTH_LONG).show();
                hideDialog();

                return;

            }

            String jsonResult = returnParsedJsonObject(result);

            if (jsonResult == "false") {

                Toast.makeText(LoginActivity.this, "User does not exist. Please sign up", Toast.LENGTH_LONG).show();
                hideDialog();
                onLoginFailed();

                return;
            }

            if(jsonResult == "true"){
                hideDialog();

                onLoginSuccess();
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



    private void showDialog() {
        if (!mProgressDialog.isShowing())
            mProgressDialog.show();
    }

    private void hideDialog() {
        if (mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }


    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login Failed", Toast.LENGTH_LONG).show();
        btnlogin.setEnabled(true);
    }

    public void onLoginSuccess() {
        btnlogin.setEnabled(true);

        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(i);
        finish();

    }

    public boolean validate(){
        boolean valid = true;

        String email_text = email.getText().toString();
        String password_text = pass.getText().toString();

        if(email_text.isEmpty() /*|| Patterns.EMAIL_ADDRESS.matcher(email_text).matches()*/){
            email.setError("Please Enter a Valid Email ID");
            valid = false;
        }
        else{
            email.setError(null);
        }

        if(password_text.isEmpty() || password_text.length() < 4 || password_text.length() > 10){
            pass.setError("Must be between 4 and 10 alphanumeric characters");
            valid = false;
        }
        else{
            pass.setError(null);
        }
        return valid;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (google_flag == true) {
            if (requestCode == REQUEST_SIGNIN) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(result);

            }
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    /*@Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }*/

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Authenticating with Google");
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Context context = getApplicationContext();
        String text = "Gogle Sign In Failed";
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

    }

    public void signUp(){
        Intent intent = new Intent(getApplicationContext(),SignUpActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case btn_login:
                btn_login();
                break;
            case R.id.btn_google:
                google_login();
                break;
            case R.id.link_signup:
                signUp();
                break;

        }
    }


}
