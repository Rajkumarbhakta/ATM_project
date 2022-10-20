package com.rkbapps.atm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
//DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://atm-project-1dbee-default-rtdb.firebaseio.com/");
Toolbar toolbar;
Button checkBalance,btnWithdrawal,btnChangePin,btnDeposit,btnTransfer,btnFastCash;
TextView greetings;
String recAccountNum,recFirstName,recLastName,recMobileNumber,recBalance;
    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
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
        btnDeposit=findViewById(R.id.btnDeposit);
        btnFastCash=findViewById(R.id.btnFastCash);
        greetings=findViewById(R.id.txtGreetings);
        //getting details from login activity
        Intent getDetails = getIntent();
        recAccountNum=getDetails.getStringExtra("accountNum");
        recFirstName=getDetails.getStringExtra("firstName");
        recLastName=getDetails.getStringExtra("lastName");
        recMobileNumber=getDetails.getStringExtra("mobileNumber");
        recBalance=getDetails.getStringExtra("balance");

        greetings.setText("Hi "+recFirstName);


        btnWithdrawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent withDrawl=new Intent(MainActivity.this,WithDrawalPage.class);
                withDrawl.putExtra("account no for credit",recAccountNum);
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
                checkBalanceAlart.setMessage("Balance in your account ₹"+recBalance);
                checkBalanceAlart.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    //Do nothing
                    }
                });
                checkBalanceAlart.show();
            }
        });
       btnDeposit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent i=new Intent(MainActivity.this,Deposit.class);
               i.putExtra("accountNumberToDiposit",recAccountNum);
               startActivity(i);
               finish();
           }
       });
       btnTransfer=findViewById(R.id.btnStatement);
       btnTransfer.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent i=new Intent(MainActivity.this,FundTransfer.class);
               startActivity(i);
               finish();
           }
       });
        btnFastCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this,FastCashPage.class);
                startActivity(i);
                finish();
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