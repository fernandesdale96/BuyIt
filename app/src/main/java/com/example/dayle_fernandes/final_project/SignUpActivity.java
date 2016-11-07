package com.example.dayle_fernandes.final_project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;




public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignupActivity";
    private EditText user_name;
    private EditText user_email;
    private EditText user_password;
    private EditText password_confirm;
    private Button signup_button;
    private TextView signin_link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);




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

        final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String name = user_name.getText().toString();
        String email = user_email.getText().toString();
        String password = user_password.getText().toString();
        String pword_confirm = password_confirm.getText().toString();

        //TODO: Add Signup Logic

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);


    }

    public void onSignupSuccess() {
        signup_button.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
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
