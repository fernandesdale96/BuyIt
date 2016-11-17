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
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

import static android.R.attr.name;
import static android.R.attr.password;
import static android.R.id.input;


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

    private static String url_register = "http://10.0.2.2/FinalProject/register.php";

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
        HttpClient client = new DefaultHttpClient();
        //String my_url;
        //Toast.makeText(getApplicationContext(), "Trying to sign in", Toast.LENGTH_LONG).show();

        //my_url = "http://10.0.2.2/FinalProject/android_register.php?name=" + uname + "&email" + uemail + "&password" + upass;
        //Log.d("url: ",my_url);


        if (!uname.isEmpty() && !uemail.isEmpty() && !upass.isEmpty()) {

            new RegisterUser().execute(uname,uemail,upass);

            /*RequestParams rp = new RequestParams();
            rp.add("username", uname);
            rp.add("password", upass);
            rp.add("email", uemail);

            Log.i("Running Request", uemail + " " + upass);

            RESTClient.post("register.php", rp, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                    try {
                        String reqResult = response.getString("success");
                        if (reqResult.equals("true")) {
                            signup_button.setEnabled(true);
                            Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
                            startActivity(i);
                            finish();
                        }

                    } catch (JSONException e) {
                        Log.e(TAG, "onSuccess: " + e.getStackTrace().toString());
                    }

                }
            });*/

        } else {
            Toast.makeText(getApplicationContext(),
                    "Please enter your details!", Toast.LENGTH_LONG)
                    .show();
            signup_button.setEnabled(true);
        }


    }

    class RegisterUser extends AsyncTask<String,String,String>{


        //Dialog before starting background thread

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            mProgressDialog.setMessage("Creating Account");
            mProgressDialog.setIndeterminate(false);
            showDialog();
        }

        @Override
        protected String doInBackground(String... args){

            String aemail = args[1];
            String auname = args[0];
            String upass = args[2];

            String jsonStr="";


            try {
                ServiceHandler sh = new ServiceHandler();

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("name",auname));
                params.add(new BasicNameValuePair("email",aemail));
                params.add(new BasicNameValuePair("password",upass));

                jsonStr = sh.makeServiceCall(url_register,ServiceHandler.POST,params);

                Log.d("Register Response",jsonStr.toString());




            }catch (Exception e){
                e.printStackTrace();
            }
            {
                int success = jsonStr.compareTo("success");
                if(success==0){
                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(i);
                    finish();

                }
                else{
                    Toast.makeText(getApplicationContext(), "User already exists, Please choose a different Email", Toast.LENGTH_SHORT).show();
                }
            }
            return null;

        }

        @Override
        protected void onPostExecute(String result){
            selfref.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mProgressDialog.dismiss();
                }
            });
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
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        signup_button.setEnabled(true);
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
