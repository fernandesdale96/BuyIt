package com.example.dayle_fernandes.final_project;

import android.widget.ArrayAdapter;

import java.util.ArrayList;

import static android.R.attr.name;

/**
 * Created by dayle_fernandes on 31-Oct-16.
 */

public class testProductList {
    private static testProductList instance = new testProductList();

    private static ArrayList<ProductInfo> products;


    public static testProductList getInstance(){
        return instance;
    }

    private testProductList(){
       products = new ArrayList<>();
        String[] name = {"Colgate Toothpaste", "Snapple", "Haribo Gummy Bears", "Tylenol Cold Tablets", "Cleanex Tissue", "Doritos Nacho Cheese", "Cheetos Cheese Sticks", "Eclipse Mints",
                            "Ham", "Cream Cheese", "White Rice", "Jacob Creek Wine"};
        double[] prices = {5,11,10.50,20.20,5.40,16.40,15.20,9.81,12.50,32.50,85.20, 98.00};
        double[] distances = {1.2,3.2,5.2,3.6,1.5,2.0,0.5,0.2,0.9,1.8,2.3,2.4};
        String[] stores = {"Welcome", "Taste", "Park n Shop", "Taste", "Market Place", "Taste", "Welcome", "7-11", "Circle K", "Park n Shop", "7-11", "Welcome"};

        for(int i = 0; i < name.length; i++){
            ProductInfo pinf = new ProductInfo(name[i], prices[i], distances[i], stores[i]);
            products.add(pinf);
        }


    }

    public static ArrayList<ProductInfo> getProducts(){
        return products;
    }


}
