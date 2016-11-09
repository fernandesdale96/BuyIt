package com.example.dayle_fernandes.final_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;


/**
 * Created by dayle_fernandes on 09-Nov-16.
 */

public class PaymentActivity extends AppCompatActivity {

    RecyclerView aRecyclerView;
    RecyclerView.LayoutManager aLayoutManager;
    RecyclerView.Adapter aAdapter;
    testCCList ccList;
    @Override
    public void onCreate(Bundle savedBundleInstance) {
        super.onCreate(savedBundleInstance);
        setContentView(R.layout.activity_payment);

        aRecyclerView = (RecyclerView) findViewById(R.id.payment_recyclerview);
        aRecyclerView.hasFixedSize();
        aAdapter = new PaymentAdapter(ccList.getCC());
        aLayoutManager = new LinearLayoutManager(this);
        aRecyclerView.setLayoutManager(aLayoutManager);

        aRecyclerView.setAdapter(aAdapter);
        aAdapter.notifyDataSetChanged();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_payment, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.new_credit_card) {
            Intent intent = new Intent(this,CreditCardForm.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
