package com.example.dayle_fernandes.final_project;

import android.widget.ArrayAdapter;

import java.io.Serializable;

/**
 * Created by dayle_fernandes on 31-Oct-16.
 */

public class ProductInfo implements Serializable{
    private long id;
    private String name;
    private double price;
    private double distance;
    private String store;

    public ProductInfo(){}

    public ProductInfo(String aname, double aprice, double adistance, String aStore){
        this.name = aname;
        this.price = aprice;
        this.distance = adistance;
        this.store = aStore;
    }

    public ProductInfo(long id,String aname, double aprice, double adistance, String aStore){
        this.id=id;
        this.name = aname;
        this.price = aprice;
        this.distance = adistance;
        this.store = aStore;
    }



    public String getName(){
        return name;
    }

    public double getPrice(){
        return price;
    }

    public double getDistance(){
        return distance;
    }

    public String getStore(){
        return store;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public void setId(long id){
        this.id=id;
    }

    public long getId(){
        return this.id;
    }

}
