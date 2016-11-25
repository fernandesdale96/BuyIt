package com.example.dayle_fernandes.final_project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.SignInButton;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.params.BasicHttpParams;
import cz.msebera.android.httpclient.params.HttpParams;

import static android.R.attr.name;
import static android.R.attr.password;
import static android.R.attr.start;
import static android.R.id.input;
import static com.example.dayle_fernandes.final_project.ServiceHandler.response;


public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";
    private EditText user_name;
    private EditText user_email;
    private EditText user_password;
    private EditText password_confirm;
    private Button signup_button;
    private TextView signin_link;
    private ProgressDialog mProgressDialog;
    private SessionManager session;

    String uname, upass, uemail;
    SignUpActivity selfref = this;


    private static String url_register = "http://175.159.69.203//FinalProject/register.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);




        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        user_name = (EditText) findViewById(R.id.input_name);
        user_email = (EditText) findViewById(R.id.input_email);
        user_password = (EditText) findViewById(R.id.input_password);
        password_confirm = (EditText) findViewById(R.id.input_password_confirm);
        signup_button = (Button) findViewById(R.id.btn_signup);
        signin_link = (TextView) findViewById(R.id.link_login);

        signin_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }

        });


    }

    public void signup() {
        if (!validate()) {
            onSignupFailed();
            return;
        }

        signup_button.setEnabled(false);

        uname = user_name.getText().toString();
        uemail = user_email.getText().toString();
        upass = user_password.getText().toString();
        //Log.d("signup name check",uname);




        if (!uname.isEmpty() && !uemail.isEmpty() && !upass.isEmpty()) {

            new RegisterUser().execute(uname, uemail, upass);


        } else {
            Toast.makeText(getApplicationContext(),
                    "Please enter your details!", Toast.LENGTH_LONG)
                    .show();
            signup_button.setEnabled(true);
        }


    }

    private class RegisterUser extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.setMessage("Creating Account");
            mProgressDialog.setIndeterminate(false);
            showDialog();
        }

        @Override
        protected String doInBackground(String... args) {

            org.apache.http.params.HttpParams httpParameters = new org.apache.http.params.BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
            HttpConnectionParams.setSoTimeout(httpParameters, 5000);
            HttpClient httpClient = new DefaultHttpClient(httpParameters);
            org.apache.http.client.methods.HttpPost httpPost = new org.apache.http.client.methods.HttpPost(url_register);
            String jsonresult = "";


            String aemail = args[1];
            String auname = args[0];
            Log.d("Name Check",uname);
            String upass = args[2];


            //ServiceHandler sh = new ServiceHandler();

            try {
                List params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("name", auname));
                Log.d("sending name check",uname);
                params.add(new BasicNameValuePair("email", aemail));
                params.add(new BasicNameValuePair("password", upass));

                httpPost.setEntity(new UrlEncodedFormEntity(params));
                HttpResponse response = httpClient.execute(httpPost);
                jsonresult = inputStreamToString(response.getEntity().getContent()).toString();
                Log.d("Sending data",jsonresult.toString());
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

                Toast.makeText(SignUpActivity.this, "Server connection failed", Toast.LENGTH_LONG).show();
                hideDialog();

                return;

            }

            String jsonResult = returnParsedJsonObject(result);

            if (jsonResult == "false") {

                Toast.makeText(SignUpActivity.this, "User already exists", Toast.LENGTH_LONG).show();
                hideDialog();
                onSignupFailed();

                return;
            }

            if(jsonResult == "true"){
                hideDialog();

                onSignupSuccess();
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

    public void onSignupFailed() {
        //Toast.makeText(getBaseContext(), "Signup failed", Toast.LENGTH_LONG).show();

        signup_button.setEnabled(true);
    }

    public void onSignupSuccess() {
        signup_button.setEnabled(true);

        Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(i);
        finish();

    }

    public boolean validate() {
        boolean valid = true;

        String name = user_name.getText().toString();
        String email = user_email.getText().toString();
        String password = user_password.getText().toString();
        String pwd_confirm = password_confirm.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            user_name.setError("at least 3 characters");
            valid = false;
        } else {
            user_name.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            user_email.setError("enter a valid email address");
            valid = false;
        } else {
            user_email.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            user_password.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            user_password.setError(null);
        }
        if (pwd_confirm.isEmpty() || pwd_confirm.length() < 4 || pwd_confirm.length() > 10) {
            password_confirm.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else if (!pwd_confirm.matches(password)) {
            password_confirm.setError("Passwords do not match");
            valid = false;
        } else {
            password_confirm.setError(null);
        }

        return valid;
    }

}
