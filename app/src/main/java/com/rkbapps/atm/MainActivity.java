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
Button checkBalance,btnWithdrawal;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar=findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbar);
        btnWithdrawal=findViewById(R.id.btnWithdrawl);

        getSupportActionBar().setTitle("ATM");
        checkBalance=findViewById(R.id.btnCheckBalance);
        btnWithdrawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent withDrawl=new Intent(MainActivity.this,WithDrawalPage.class);
                startActivity(withDrawl);
                finish();
            }
        });
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