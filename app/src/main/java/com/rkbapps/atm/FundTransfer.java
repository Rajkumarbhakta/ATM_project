package com.rkbapps.atm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class FundTransfer extends AppCompatActivity {
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl
            ("https://atm-project-1dbee-default-rtdb.firebaseio.com/");
    EditText receiverAcc,receiverName,quantity;
    Button send;
    int receiverPreviousBalanceInt;
    String receiveAccString,receiverNameString,quantityString,senderAccount,senderPreviousBalance,totalAmountF
            ,receiverPreviousBalance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fund_transfer);
        receiverAcc=findViewById(R.id.receiverAcc);
        receiverName=findViewById(R.id.receiverName);
        quantity=findViewById(R.id.quantity);
        send=findViewById(R.id.btnSend);
        Intent get=getIntent();
        senderAccount=get.getStringExtra("FundTransferAccount");


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                receiveAccString=receiverAcc.getText().toString();
                receiverNameString=receiverName.getText().toString();
                quantityString=quantity.getText().toString();
                if(receiveAccString.length()!=10){
                    Toast.makeText(FundTransfer.this, "Invalid account no format.", Toast.LENGTH_SHORT).show();
                }else {

//                    this is for previous balance of sender//
                    databaseReference.child("user").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(senderAccount)){
                                senderPreviousBalance=snapshot.child(senderAccount).child("balance").getValue(String.class);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
//                    this is for receiver previous balance
                    databaseReference.child("user").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(receiveAccString)){
                                receiverPreviousBalance=snapshot.child(receiveAccString).child("balance").getValue(String.class);
                                receiverPreviousBalanceInt =Integer.parseInt(receiverPreviousBalance);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });



//                    this is for transaction details as well as receiver
                    databaseReference.child("user").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.hasChild(receiveAccString)) {
                                String getFName = snapshot.child(receiveAccString).child("firstName").getValue(String.class);
                                String getLName=snapshot.child(receiveAccString).child("lastName").getValue(String.class);
                                String getFullName =getFName+" "+getLName;

                                if (Objects.equals(getFullName, receiverNameString)) {
//                                    Toast.makeText(FundTransfer.this, "Thikthak", Toast.LENGTH_SHORT).show();

                                    Intent i = new Intent(FundTransfer.this, TransactionDetails.class);
                                    int previousAmount = Integer.parseInt(senderPreviousBalance);
                                    int credAmount = Integer.parseInt(quantityString);
                                    if (previousAmount >= credAmount) {
                                        int totalAmount = previousAmount - credAmount;
                                        totalAmountF = Integer.toString(totalAmount);
                                         //new balance update in database
                                        databaseReference.child("user").child(senderAccount).child("balance").setValue(totalAmountF);
                                    }
                                    int totalMoney=receiverPreviousBalanceInt+credAmount;
                                    String totalMoneyF=Integer.toString(totalMoney);
                                    databaseReference.child("user").child(receiveAccString).child("balance").setValue(totalMoneyF);
                                    i.putExtra("money", quantityString);
                                    startActivity(i);
                                    finish();
                                } else {
                                    Toast.makeText(FundTransfer.this, "Please,Check Your details.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        });

    }
    public void onBackPressed(){
        AlertDialog.Builder build= new AlertDialog.Builder(FundTransfer.this);
        build.setMessage("Do you want to exit ?");

        build.setTitle("Alert !");

        build.setCancelable(false);

        build.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent noBtn=new Intent(FundTransfer.this,loginActivity.class);
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