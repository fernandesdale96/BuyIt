package com.example.dayle_fernandes.final_project;

import android.location.Location;
import android.widget.Switch;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.Serializable;
import java.util.ArrayList;

import static android.os.Build.VERSION_CODES.DONUT;
import static android.os.Build.VERSION_CODES.M;

/**
 * Created by dayle_fernandes on 13-Nov-16.
 */

public class testLocation implements Serializable{

    private testLocation() {
        Taste = new ArrayList<LatLng>();
        populateTaste();
        Welcome = new ArrayList<LatLng>();
        populateWelcome();
        ParknShop = new ArrayList<LatLng>();
        populateParknShop();
        CircleK = new ArrayList<LatLng>();
        populateCircleK();
        MarketPlace = new ArrayList<LatLng>();
        populateMarketPlace();
        S11 = new ArrayList<LatLng>();
        populateS11();
    }
    public static testLocation getInstance(){
        return instance;
    }

    private static final testLocation instance = new testLocation();
    private ArrayList<LatLng> Taste;
    private ArrayList<LatLng> Welcome;
    private ArrayList<LatLng> ParknShop;
    private ArrayList<LatLng> MarketPlace;
    private ArrayList<LatLng> CircleK;
    private ArrayList<LatLng> S11;


    private void populateTaste(){
        LatLng t1 = new LatLng(22.337009, 114.174907);
        Taste.add(t1);
        LatLng t2 = new LatLng(22.340621, 114.202373);
        Taste.add(t2);
        LatLng t3 = new LatLng(22.316441, 114.161577);
        Taste.add(t3);
        LatLng t4 = new LatLng(22.319066, 114.186108);
        Taste.add(t4);
        LatLng t5 = new LatLng(22.325179, 114.216664);
    }

    private void populateWelcome(){
        LatLng w1 = new LatLng(22.327675, 114.188866);
        Welcome.add(w1);
        LatLng w2 = new LatLng(22.331499, 114.175699);
        Welcome.add(w2);
        LatLng w3 = new LatLng(22.333968, 114.167554);
        Welcome.add(w3);
        LatLng w4 = new LatLng(22.328915, 114.165460);
        Welcome.add(w4);
        LatLng w5 = new LatLng(22.320765, 114.166159);
        Welcome.add(w5);
    }

    private void populateParknShop(){
        LatLng p1 = new LatLng(22.334480, 114.168133);
        ParknShop.add(p1);
        LatLng p2 = new LatLng(22.336485, 114.167704);
        ParknShop.add(p2);
        LatLng p3 = new LatLng(22.330556, 114.192244);
        ParknShop.add(p3);
        LatLng p4 = new LatLng(22.324024, 114.188839);
        ParknShop.add(p4);
        LatLng p5 = new LatLng(22.320460, 114.177331);
        ParknShop.add(p5);
    }

    private void populateCircleK(){
        LatLng c1 = new LatLng(22.328829, 114.188057);
        CircleK.add(c1);
        LatLng c2 = new LatLng(22.333522, 114.171708);
        CircleK.add(c2);
        LatLng c3 = new LatLng(22.332861, 114.161909);
        CircleK.add(c3);
        LatLng c4 = new LatLng(22.331823, 114.163389);
        CircleK.add(c4);
        LatLng c5 = new LatLng(22.338995, 114.188962);
        CircleK.add(c5);
    }

    private void populateMarketPlace(){
        LatLng m1 = new LatLng(22.331135, 114.181666);
        MarketPlace.add(m1);
        LatLng m2 = new LatLng(22.318507, 114.159203);
        MarketPlace.add(m2);
        LatLng m3 = new LatLng(22.309860, 114.166061);
        MarketPlace.add(m3);
        LatLng m4 = new LatLng(22.340233, 114.197014);
        MarketPlace.add(m4);
        LatLng m5 = new LatLng(22.322100, 114.212672);
        MarketPlace.add(m5);
    }

    private void populateS11(){
        LatLng s1 = new LatLng(22.325102, 114.187318);
        S11.add(s1);
        LatLng s2 = new LatLng(22.332501, 114.166618);
        S11.add(s2);
        LatLng s3 = new LatLng(22.337278, 114.174666);
        S11.add(s3);
        LatLng s4 = new LatLng(22.328970, 114.187752);
        S11.add(s4);
        LatLng s5 = new LatLng(22.339457, 114.187642);
        S11.add(s5);

    }

    public LatLng nearestStore(ArrayList<LatLng> alist, LatLng cpos){
        float length = 100000;
        LatLng apos = null;

        for(LatLng e: alist){
            Location a1 = new Location("");
            a1.setLatitude(cpos.latitude);
            a1.setLongitude(cpos.longitude);
            Location a2 = new Location("");
            a2.setLatitude(cpos.latitude);
            a2.setLongitude(cpos.longitude);
            float distance = a1.distanceTo(a2);

            if(length > distance){
                length = distance;
                apos = e;
            }
        }
        return  apos;
    }

    public LatLng returnNearestStore(String store, LatLng cpos){
        LatLng pos = new LatLng(cpos.latitude,cpos.longitude);
        if(store.equals("Taste")){
            pos = nearestStore(Taste,cpos);
        }
        else if(store.equals("Welcome")){
            pos = nearestStore(Welcome, cpos);
        }
        else if(store.equals("Park n Shop")){
            pos = nearestStore(ParknShop,cpos);
        }
        else if(store.equals("7-11")){
            pos = nearestStore(S11,cpos);
        }
        else if(store.equals("Circle K")){
            pos = nearestStore(CircleK,cpos);
        }
        else if(store.equals("Market Place")){
            pos = nearestStore(MarketPlace,cpos);
        }
        return pos;
    }


}
