package com.example.dayle_fernandes.final_project;

import android.app.ProgressDialog;
import android.content.Intent;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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
    private SQLiteHandler db;

    private static String url_register = "http://10.0.2.2/FinalProject/android_register.php";

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
                Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
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

        String name = user_name.getText().toString();
        String email = user_email.getText().toString();
        String password = user_password.getText().toString();

        if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
            registerUser(name, email, password);
        } else {
            Toast.makeText(getApplicationContext(),
                    "Please enter your details!", Toast.LENGTH_LONG)
                    .show();
        }




    }

    private void registerUser(final String name, final String email,
                              final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        mProgressDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url_register, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        String uid = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");
                        String aname = user.getString("name");
                        String aemail = user.getString("email");
                        String acreated_at = user
                                .getString("created_at");

                        // Inserting row in users table
                        db.addUser(aname, aemail, uid, acreated_at);

                        Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();

                        // Launch login activity
                        Intent intent = new Intent(
                                SignUpActivity.this,
                                LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
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
        if(pwd_confirm.isEmpty() || pwd_confirm.length() < 4 || pwd_confirm.length() > 10){
            password_confirm.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        }
        else if(!pwd_confirm.matches(password)){
            password_confirm.setError("Passwords do not match");
            valid = false;
        }
        else{
            password_confirm.setError(null);
        }

        return valid;
    }

}
