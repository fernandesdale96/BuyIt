package com.example.dayle_fernandes.final_project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.os.Bundle;

/**
 * Created by dayle_fernandes on 26-Oct-16.
 */
public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        Intent newIntent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(newIntent);
        finish();
    }
}
