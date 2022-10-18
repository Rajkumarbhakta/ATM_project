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
Button checkBalance,btnWithdrawal,btnChangePin;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar=findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("ATM");
        btnWithdrawal=findViewById(R.id.btnWithdrawl);
        checkBalance=findViewById(R.id.btnCheckBalance);
        btnChangePin=findViewById(R.id.btnChangePin);
        btnWithdrawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent withDrawl=new Intent(MainActivity.this,WithDrawalPage.class);
                startActivity(withDrawl);
                finish();
            }
        });
        btnChangePin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this,ForgetPinPage.class);
                startActivity(i);
            }
        });
       checkBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder checkBalanceAlart=new AlertDialog.Builder(MainActivity.this);
                checkBalanceAlart.setIcon(R.drawable.ic_baseline_account_balance_wallet_24).setTitle("Balance");
                checkBalanceAlart.setMessage("Balance in your account ₹"+"0.00");
                checkBalanceAlart.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    //Do nothing
                    }
                });
                checkBalanceAlart.show();
            }
        });

     //04063
    }

    // when back pressed
    public void onBackPressed(){
        AlertDialog.Builder build= new AlertDialog.Builder(MainActivity.this);
        build.setMessage("Do you want to exit ?");

        build.setTitle("Alert !");

        build.setCancelable(false);

        build.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(MainActivity.this, "Yes clicked", Toast.LENGTH_SHORT).show();
                Intent noBtn=new Intent(MainActivity.this, loginActivity.class);
                startActivity(noBtn);
                finish();
            }
        });
        build.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
            dialog.cancel();
        });
        AlertDialog alertDialog = build.create();
        alertDialog.show();
    }
}