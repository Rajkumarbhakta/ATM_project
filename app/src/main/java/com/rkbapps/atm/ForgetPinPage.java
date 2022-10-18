package com.rkbapps.atm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ForgetPinPage extends AppCompatActivity {
Toolbar forgetPinToolbar;
Button resetPin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pin_page);
        forgetPinToolbar=findViewById(R.id.toolbarForgetPin);
        setSupportActionBar(forgetPinToolbar);
        getSupportActionBar().setTitle("Reset PIN");
        resetPin=findViewById(R.id.btnResetPin);
        resetPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //reset custom alart dialog
                Dialog dialog=new Dialog(ForgetPinPage.this);
                dialog.setContentView(R.layout.reset_pin_custom_alart_dialog);
                Button done =dialog.findViewById(R.id.btnPinResetDone);
                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        //done code here
                    }
                });
                dialog.show();
            }
        });


    }
}