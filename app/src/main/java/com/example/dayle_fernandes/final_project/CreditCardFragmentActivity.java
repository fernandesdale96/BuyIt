package com.example.dayle_fernandes.final_project;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import movile.com.creditcardguide.ActionOnPayListener;
import movile.com.creditcardguide.CreditCardFragment;
import movile.com.creditcardguide.model.CreditCardPaymentMethod;
import movile.com.creditcardguide.model.IssuerCode;
import movile.com.creditcardguide.model.PaymentMethod;
import movile.com.creditcardguide.model.PurchaseOption;

import android.support.v7.widget.Toolbar;


/**
 * Created by dayle_fernandes on 08-Nov-16.
 */

public class CreditCardFragmentActivity extends AppCompatActivity implements ActionOnPayListener {
    private CreditCardFragment inputCardFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card_fragment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Payment Options");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Window window =  this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimaryDark));

        inputCardFragment = (CreditCardFragment) getFragmentManager().findFragmentById(R.id.frg_pay);


        inputCardFragment.setPagesOrder(CreditCardFragment.Step.FLAG, CreditCardFragment.Step.NUMBER,
                CreditCardFragment.Step.EXPIRE_DATE, CreditCardFragment.Step.CVV, CreditCardFragment.Step.NAME);

        //TODO: Add product price below
        inputCardFragment.setListPurchaseOptions(getList(), 230.0);
    }

    private List<PurchaseOption> getList() {
        List<PurchaseOption> list = new ArrayList<>();
        list.add(new PurchaseOption(PaymentMethod.Type.CREDIT_CARD, IssuerCode.MASTERCARD, 6));
        list.add(new PurchaseOption(PaymentMethod.Type.CREDIT_CARD, IssuerCode.VISACREDITO, 6));
        list.add(new PurchaseOption(PaymentMethod.Type.CREDIT_CARD, IssuerCode.AMEX, 6));
        list.add(new PurchaseOption(PaymentMethod.Type.CREDIT_CARD, IssuerCode.PAYPAL, 6));
        list.add(new PurchaseOption(PaymentMethod.Type.CREDIT_CARD, IssuerCode.DINERS, 6));
        list.add(new PurchaseOption(PaymentMethod.Type.CREDIT_CARD, IssuerCode.NUBANK, 6));
        list.add(new PurchaseOption(PaymentMethod.Type.CREDIT_CARD, IssuerCode.AURA, 6));
        list.add(new PurchaseOption(PaymentMethod.Type.CREDIT_CARD, IssuerCode.ELO, 6));
        list.add(new PurchaseOption(PaymentMethod.Type.CREDIT_CARD, IssuerCode.HIPERCARD, 6));
        list.add(new PurchaseOption(PaymentMethod.Type.CREDIT_CARD, IssuerCode.OTHER, 6));

        return list;
    }

    @Override
    public void onChangedPage(CreditCardFragment.Step page) {

    }

    @Override
    public void onComplete(CreditCardPaymentMethod purchaseOption, boolean saveCard) {
        Toast.makeText(this, "Product has been added to cart. Please collect at Supermarket", Toast.LENGTH_LONG).show();

        if (saveCard) {
            // TODO: Persist the card
            purchaseOption.setSecurityCode(null);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        inputCardFragment.backPressed();
    }
}
