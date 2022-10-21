package com.rkbapps.atm;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class FundTransfer extends AppCompatActivity {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl
            ("https://atm-project-1dbee-default-rtdb.firebaseio.com/");
    EditText receiverAcc, receiverName, quantity;
    Button send;
    int receiverPreviousBalanceInt;
    String receiveAccString, receiverNameString, quantityString, senderAccount, senderPreviousBalance, totalAmountF, receiverPreviousBalance, senderName;
    String getFName, getLName, getFullName;
    ProgressBar loaddingFundTransfer;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fund_transfer);
        receiverAcc = findViewById(R.id.receiverAcc);
        receiverName = findViewById(R.id.receiverName);
        quantity = findViewById(R.id.quantity);
        send = findViewById(R.id.btnSend);
        loaddingFundTransfer = findViewById(R.id.fundTransferProgress);
        Intent get = getIntent();
        senderAccount = get.getStringExtra("FundTransferAccount");
        senderName = get.getStringExtra("senderName");
        loaddingFundTransfer.setVisibility(View.INVISIBLE);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loaddingFundTransfer.setVisibility(View.VISIBLE);
                receiveAccString = receiverAcc.getText().toString();
                receiverNameString = receiverName.getText().toString();
                quantityString = quantity.getText().toString();
                if (receiveAccString.length() != 10 || receiveAccString.equals(senderAccount) || quantityString.isEmpty() || quantityString.equals("0")) {
                    Toast.makeText(FundTransfer.this, "Invalid account no or invalid amount.", Toast.LENGTH_SHORT).show();
                    loaddingFundTransfer.setVisibility(View.INVISIBLE);
                } else {
//                    fetching sender details
                    loaddingFundTransfer.setVisibility(View.VISIBLE);
                    databaseReference.child("user").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(senderAccount)) {
                                senderPreviousBalance = snapshot.child(senderAccount).child("balance").getValue(String.class);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(FundTransfer.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                            loaddingFundTransfer.setVisibility(View.INVISIBLE);
                        }
                    });
//                    receiver details
                    databaseReference.child("user").addListenerForSingleValueEvent(new ValueEventListener() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(receiveAccString)) {
                                receiverPreviousBalance = snapshot.child(receiveAccString).child("balance").getValue(String.class);
                                getFName = snapshot.child(receiveAccString).child("firstName").getValue(String.class);
                                getLName = snapshot.child(receiveAccString).child("lastName").getValue(String.class);
                                receiverPreviousBalanceInt = Integer.parseInt(receiverPreviousBalance);
                                getFullName = getFName + " " + getLName;
                                if (Objects.equals(getFullName, receiverNameString)) {
//                                    Toast.makeText(FundTransfer.this, "Thikthak", Toast.LENGTH_SHORT).show();
                                    Dialog sendMonyDialog = new Dialog(FundTransfer.this);
                                    sendMonyDialog.setContentView(R.layout.send_confirm_alart);
                                    //alert dialog components
                                    Button confirmSend = sendMonyDialog.findViewById(R.id.sendConfirm);
                                    Button cancelSend = sendMonyDialog.findViewById(R.id.sendCancel);
                                    TextView amountToSend, reciverAccountToSend, reciverNameToSend, senderAccountToSend, senderNameToSend;
                                    amountToSend = sendMonyDialog.findViewById(R.id.sendAmount);
                                    reciverAccountToSend = sendMonyDialog.findViewById(R.id.reciverAccount);
                                    reciverNameToSend = sendMonyDialog.findViewById(R.id.txtReciverName);
                                    senderAccountToSend = sendMonyDialog.findViewById(R.id.senderAccount);
                                    senderNameToSend = sendMonyDialog.findViewById(R.id.senderName);
                                    amountToSend.setText("Ammount : ₹" + quantityString);
                                    reciverAccountToSend.setText("Account Number :" + receiveAccString);
                                    reciverNameToSend.setText("Account hoalder Name : " + receiverNameString);
                                    senderAccountToSend.setText("Account Number :" + senderAccount);
                                    senderNameToSend.setText("Account hoalder Name : " + senderName);
                                    //confirm
                                    confirmSend.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            loaddingFundTransfer.setVisibility(View.VISIBLE);
                                            Intent i = new Intent(FundTransfer.this, TransactionDetails.class);
                                            int previousAmount = Integer.parseInt(senderPreviousBalance);
                                            int credAmount = Integer.parseInt(quantityString);
                                            if (previousAmount >= credAmount) {
                                                int totalAmount = previousAmount - credAmount;
                                                totalAmountF = Integer.toString(totalAmount);
                                                //new balance update in database
                                                databaseReference.child("user").child(senderAccount).child("balance").setValue(totalAmountF);
                                                int totalMoney = receiverPreviousBalanceInt + credAmount;
                                                String totalMoneyF = Integer.toString(totalMoney);
                                                databaseReference.child("user").child(receiveAccString).child("balance").setValue(totalMoneyF);
                                                i.putExtra("money", quantityString);
                                                loaddingFundTransfer.setVisibility(View.INVISIBLE);
                                                startActivity(i);
                                                finish();
                                            } else {
                                                Toast.makeText(FundTransfer.this, "Aukaat K bahar", Toast.LENGTH_SHORT).show();
                                                loaddingFundTransfer.setVisibility(View.INVISIBLE);
                                                sendMonyDialog.dismiss();
                                            }
                                        }
                                    });
                                    //cancel
                                    cancelSend.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            sendMonyDialog.dismiss();
                                        }
                                    });
                                    sendMonyDialog.setCancelable(false);
                                    loaddingFundTransfer.setVisibility(View.INVISIBLE);
                                    sendMonyDialog.show();
                                } else {
                                    Toast.makeText(FundTransfer.this, "Please,Check Your details.", Toast.LENGTH_SHORT).show();
                                    loaddingFundTransfer.setVisibility(View.INVISIBLE);
                                }

                            }else {
                                Toast.makeText(FundTransfer.this, "Account Dose not exist", Toast.LENGTH_SHORT).show();
                                loaddingFundTransfer.setVisibility(View.INVISIBLE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(FundTransfer.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                            loaddingFundTransfer.setVisibility(View.INVISIBLE);
                        }
                    });

                }
            }
        });
    }

    public void onBackPressed() {
        AlertDialog.Builder build = new AlertDialog.Builder(FundTransfer.this);
        build.setMessage("Do you want to exit ?");

        build.setTitle("Alert !");

        build.setCancelable(false);

        build.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent noBtn = new Intent(FundTransfer.this, loginActivity.class);
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