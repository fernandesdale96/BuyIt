package com.example.dayle_fernandes.final_project;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import static java.lang.Math.*;

import com.example.dayle_fernandes.final_project.R;
import com.google.android.gms.analytics.ecommerce.Product;

import android.widget.Adapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import static com.example.dayle_fernandes.final_project.R.id.toolbar;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView aRecyclerView;
    RecyclerView.LayoutManager aLayoutManager;
    RecyclerView.Adapter aAdapter;
    testProductList list;
    MainActivity selfRef=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.dayle_fernandes.final_project.R.layout.activity_main);

        list = testProductList.getInstance();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton search = (FloatingActionButton) findViewById(com.example.dayle_fernandes.final_project.R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Add Search Function
                Snackbar.make(view, "Implement Search Function", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(com.example.dayle_fernandes.final_project.R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, com.example.dayle_fernandes.final_project.R.string.navigation_drawer_open, com.example.dayle_fernandes.final_project.R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(com.example.dayle_fernandes.final_project.R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        aRecyclerView = (RecyclerView) findViewById(R.id.productlist_recyclerView);
        aRecyclerView.setHasFixedSize(true);
        aAdapter = new ProductAdapter(list.getProducts());
        aLayoutManager = new LinearLayoutManager(this);
        aRecyclerView.setLayoutManager(aLayoutManager);

       // AdapterView.OnItemClickListener flag= new AdapterView.OnItemClickListener() {
          //  @Override
         //   public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
          //      String name=(ProductInfo)adapterView.getAdapter().getItem(i);
          //      Intent f=new Intent(selfRef,ProductLocation.class);
          //      f.
          //  }
     //   };


        aRecyclerView.setAdapter(aAdapter);
        aAdapter.notifyDataSetChanged();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(com.example.dayle_fernandes.final_project.R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(com.example.dayle_fernandes.final_project.R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == com.example.dayle_fernandes.final_project.R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == com.example.dayle_fernandes.final_project.R.id.nav_home) {

        } else if (id == com.example.dayle_fernandes.final_project.R.id.nav_payment) {
            //TODO: Add intent to add/edit payment options
        } else if (id == com.example.dayle_fernandes.final_project.R.id.nav_market) {
            //TODO: Add intent to list of all markets
        } else if (id == com.example.dayle_fernandes.final_project.R.id.nav_exit) {
            finish();
            System.exit(0);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(com.example.dayle_fernandes.final_project.R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
