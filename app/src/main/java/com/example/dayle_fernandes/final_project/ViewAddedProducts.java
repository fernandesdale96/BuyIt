package com.example.dayle_fernandes.final_project;

import android.app.ListActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import DB.ProductHandler;

/**
 * Created by aksharma2 on 09-11-2016.
 */
public class ViewAddedProducts extends ListActivity{
    private ProductHandler productHandler;
    ArrayList<ProductInfo> products;
    RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_added_products);
        productHandler = new ProductHandler(ViewAddedProducts.this);
        productHandler.open();
        testProductList atestProductList = testProductList.getInstance();
        products = atestProductList.getProducts();
        productHandler.close();
        ArrayAdapter<ProductInfo> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, products);
        setListAdapter(adapter);
    }
}
