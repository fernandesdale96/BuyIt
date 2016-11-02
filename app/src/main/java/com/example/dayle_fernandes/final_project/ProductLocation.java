package com.example.dayle_fernandes.final_project;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;


/**
 * Created by aksharma2 on 01-11-2016.
 */
public class ProductLocation extends AppCompatActivity{

    //private void prod

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_location);
        Intent i=getIntent();
        Bundle b=i.getExtras();
        String name=b.getString("PROD_NAME");
        String price=b.getString("PROD_PRICE");
        String dist=b.getString("PROD_DIST");
        String store=b.getString("PROD_STORE");

        TextView tv=(TextView)findViewById(R.id.selected_prod_name);
        tv.setText(name);
        TextView tv2=(TextView)findViewById(R.id.selected_prod_price);
        tv2.setText(price+ " HKD");
        TextView tv3=(TextView)findViewById(R.id.selected_prod_distance);
        tv3.setText(dist +" KM");
        TextView tv4=(TextView)findViewById(R.id.selected_prod_store);
        tv4.setText(store);

      // Google Map Implementation still left

      //  MapFragment mapFragment = (MapFragment) getFragmentManager()
      //          .findFragmentById(R.id.fragment);
      //  mapFragment.getMapAsync(this);
    }

   // @Override
   // public void onMapReady(GoogleMap map) {
     //   LatLng sydney = new LatLng(-33.867, 151.206);

       // map.setMyLocationEnabled(true);

      //  map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));

      //  map.addMarker(new MarkerOptions()
        //        .title("Sydney")
         //       .snippet("The most populous city in Australia.")
          //      .position(sydney));

    }

