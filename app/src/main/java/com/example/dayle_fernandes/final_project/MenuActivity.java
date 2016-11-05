package com.example.dayle_fernandes.final_project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

/**
 * Created by aksharma2 on 06-11-2016.
 */
public class MenuActivity extends Activity {

    Button addButton;
    Button viewButton;
    MenuActivity selfRef=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menuscreen);
        addButton=(Button)findViewById(R.id.addButton);
        viewButton=(Button)findViewById(R.id.viewButton);

        addButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i;
                        i=new Intent(selfRef,AddProduct.class);
                        startActivity(i);
                    }
                }
        );

        viewButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i;
                        i=new Intent(selfRef,MainActivity.class);
                        startActivity(i);
                    }
                }
        );
    }
}
