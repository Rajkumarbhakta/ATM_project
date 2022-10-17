package com.rkbapps.atm;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
Toolbar toolbar;
Button checkBalance;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar=findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("ATM");
        checkBalance.findViewById(R.id.btnCheckBalance);
       /* checkBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog checkBalanceAlart=new AlertDialog.Builder(MainActivity.this).create();
                checkBalanceAlart.setTitle("Balance");
                checkBalanceAlart.setMessage("₹000000");
//                checkBalanceAlart.setButton("ok", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
////                        Intent checkBalanceAlertDialogOk = new Intent(MainActivity.this,loginActivity.class);
////                        startActivity(checkBalanceAlertDialogOk);
////                        finish();
//                        Toast.makeText(MainActivity.this, "Ok clicked", Toast.LENGTH_SHORT).show();
//                    }
//                });
                checkBalanceAlart.show();
            }
        });*/

     //04063
    }
}