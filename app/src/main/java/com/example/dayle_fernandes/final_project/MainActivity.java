package com.example.dayle_fernandes.final_project;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
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

import static com.example.dayle_fernandes.final_project.R.id.profile_image;
import static com.example.dayle_fernandes.final_project.R.id.user;
import static com.google.android.gms.analytics.internal.zzy.n;
import static java.lang.Math.*;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.dayle_fernandes.final_project.R;
import com.google.android.gms.analytics.ecommerce.Product;
import com.google.android.gms.vision.text.Text;

import android.widget.Adapter;
import android.widget.ImageView;
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
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView aRecyclerView;
    RecyclerView.LayoutManager aLayoutManager;
    RecyclerView.Adapter aAdapter;
    testProductList list;
    MainActivity selfRef = this;

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
        String email = LoginActivity.getEmail();
        String name = LoginActivity.getName();
        String profile = LoginActivity.getProfile();

        NavigationView navigationView = (NavigationView) findViewById(com.example.dayle_fernandes.final_project.R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View v = navigationView.getHeaderView(0);
        TextView nav_user = (TextView) v.findViewById(user);
        final ImageView user_profile = (ImageView) v.findViewById(profile_image);

        if (name == null) {
            nav_user.setText(email);
        } else {
            nav_user.setText(name);
        }

        if(profile != null) {
            Glide.with(getApplicationContext()).load(profile).asBitmap().into(new BitmapImageViewTarget(user_profile) {
                @Override
                protected void setResource(Bitmap resource){
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(getApplicationContext().getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    user_profile.setImageDrawable(circularBitmapDrawable);
                }
            });
        }


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
            Intent intent = new Intent(MainActivity.this,CreditCardFragmentActivity.class);
            startActivity(intent);
            finish();
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
