package com.rkbapps.atm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

public class FastCashPage extends AppCompatActivity {
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl
            ("https://atm-project-1dbee-default-rtdb.firebaseio.com/");
    String accNum,previousBalanceString,enterAmountString,TotalAmountF;
    TextView SelectedAmount,text100,text200,text500,text1000,text1500,text2000;
    Button btnConfirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fast_cash_page);

        Intent getDetailsFromMain=getIntent();
        accNum=getDetailsFromMain.getStringExtra("FastCashAccount");
        databaseReference.child("user").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                previousBalanceString=snapshot.child(accNum).child("balance").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        text100=findViewById(R.id.text100);
        text100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectedAmount.setText("100");
            }
        });
        text200=findViewById(R.id.text200);
        text200.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectedAmount.setText("200");
            }
        });
        text500=findViewById(R.id.text500);
        text500.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectedAmount.setText("500");
            }
        });
        text1000=findViewById(R.id.text1000);
        text1000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SelectedAmount.setText("1000");
            }
        });
        text1500=findViewById(R.id.text1500);
        text1500.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SelectedAmount.setText("1500");
            }
        });
        text2000=findViewById(R.id.text2000);
        text2000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SelectedAmount.setText("2000");
            }
        });

        SelectedAmount=findViewById(R.id.SelectedAmount);
        btnConfirm=findViewById(R.id.btnConfirm);

//        int previousAmount = Integer.parseInt(previousBalanceString);
//        int credAmount = Integer.parseInt(enterAmountString);
//        int totalAmount = previousAmount + credAmount;
//        TotalAmountF = Integer.toString(totalAmount);
//        //new balance update in database
//        databaseReference.child("user").child(accNum).child("balance").setValue(TotalAmountF);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enterAmountString=SelectedAmount.getText().toString();
                AlertDialog.Builder builder = new AlertDialog.Builder(FastCashPage.this);
                builder.setTitle("Alert!");
                builder.setMessage("Are you sure want to withdraw the money ?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int previousAmount = Integer.parseInt(previousBalanceString);
                        int credAmount = Integer.parseInt(enterAmountString);
                        if (previousAmount >= credAmount) {
                            int totalAmount = previousAmount - credAmount;
                            TotalAmountF = Integer.toString(totalAmount);
                            //new balance update in database
                            databaseReference.child("user").child(accNum).child("balance").setValue(TotalAmountF);
                            Intent y = new Intent(FastCashPage.this, TransactionDetails.class);
                            y.putExtra("debitedAmmountFastCash",enterAmountString);
//                            loaddingWithdrawl.setVisibility(View.INVISIBLE);
                            startActivity(y);
                            finish();
                        } else {
                            Toast.makeText(FastCashPage.this, "Aukaat hai tere itne money account mein rakhne ke liye.", Toast.LENGTH_SHORT).show();
//                            loaddingWithdrawl.setVisibility(View.INVISIBLE);
                        }
//                            Toast.makeText(WithDrawalPage.this, "YES clicked", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(FastCashPage.this, "No clicked", Toast.LENGTH_SHORT).show();
                        Intent noBtn = new Intent(FastCashPage.this, loginActivity.class);
//                        loaddingWithdrawl.setVisibility(View.INVISIBLE);
                        startActivity(noBtn);
                        finish();
                    }
                });
                AlertDialog aler = builder.create();
                aler.show();
            }
        });
    }
    public void onBackPressed(){
        AlertDialog.Builder build= new AlertDialog.Builder(FastCashPage.this);
        build.setMessage("Do you want to exit ?");

        build.setTitle("Alert !");

        build.setCancelable(false);

        build.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(FastCashPage.this, "Yes clicked", Toast.LENGTH_SHORT).show();
                Intent noBtn=new Intent(FastCashPage.this, loginActivity.class);
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