package com.example.dayle_fernandes.final_project;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.example.dayle_fernandes.final_project.R.id.btn_login;
import static com.example.dayle_fernandes.final_project.R.id.intent_action;
import static com.example.dayle_fernandes.final_project.R.id.start;



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


    private static String url_log_in = "http://10.0.2.2/FinalProject/android_login.php";
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
            profile_url = act.getPhotoUrl().toString();
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

        checkLogin(email_text,password_text);



    }

    private void checkLogin(final String email, final String pass){
        String tag_string_req = "req_login";



        //mProgressDialog.setMessage("Loggig in...");
        //showProgressDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, url_log_in, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jso = new JSONObject(response);
                    boolean error = jso.getBoolean("error");

                    if(!error){
                        session.setLogin(true);

                        String uid = jso.getString("uid");

                        JSONObject user = jso.getJSONObject("user");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        email_id = email;
                        String created_at = user.getString("created_at");



                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                    }else{
                        String errormsg = jso.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errormsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
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
