package com.rkbapps.atm;

import android.annotation.SuppressLint;
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

public class WithDrawalPage extends AppCompatActivity {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://atm-project-1dbee-default-rtdb.firebaseio.com/");
    Button withDraw;
    EditText moneyAmount;
    ProgressBar loaddingWithdrawl;
    String accNumC, previousBalance, enterAmount, totalAmountF;
    TextView presentBalance;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_drawal_page);
        withDraw = findViewById(R.id.withdrawBtn);
        presentBalance = findViewById(R.id.bal);
        moneyAmount = findViewById(R.id.txtWidthdrawlAmount);
        loaddingWithdrawl = findViewById(R.id.withdrawl_progress);
        Intent getDetailsFromMainActivity = getIntent();
        accNumC = getDetailsFromMainActivity.getStringExtra("account no for credit");
        loaddingWithdrawl.setVisibility(View.VISIBLE);
        databaseReference.child("user").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(accNumC)) {
                    previousBalance = snapshot.child(accNumC).child("balance").getValue(String.class);
                    presentBalance.setText("A/C bal:  ₹" + previousBalance);
                    loaddingWithdrawl.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        withDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enterAmount = moneyAmount.getText().toString();
                loaddingWithdrawl.setVisibility(View.VISIBLE);
                if (enterAmount.isEmpty() || enterAmount.equals("0")) {
                    Toast.makeText(WithDrawalPage.this, "Please,Enter amount in multiple of 100/-", Toast.LENGTH_SHORT).show();
                    loaddingWithdrawl.setVisibility(View.INVISIBLE);
                } else {
                    // int amm=Integer.parseInt(moneyAmount.getText().toString());
                    int amount = Integer.parseInt(moneyAmount.getText().toString());
                    if (amount % 100 != 0) {
                        AlertDialog.Builder moneyAlert = new AlertDialog.Builder(WithDrawalPage.this);
                        moneyAlert.setTitle("Sorry,Invalid Money Format.");
                        moneyAlert.setMessage("Please,Enter amount in multiple of 100/-");
                        moneyAlert.setPositiveButton("Ok", null);
                        AlertDialog t = moneyAlert.create();
                        t.setCancelable(false);
                        loaddingWithdrawl.setVisibility(View.INVISIBLE);
                        t.show();

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(WithDrawalPage.this);
                        builder.setTitle("Alert!");
                        builder.setMessage("Are you sure want to withdraw ₹" + enterAmount);
                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int previousAmount = Integer.parseInt(previousBalance);
                                int credAmount = Integer.parseInt(enterAmount);
                                if (previousAmount >= credAmount) {
                                    int totalAmount = previousAmount - credAmount;
                                    totalAmountF = Integer.toString(totalAmount);
                                    //new balance update in database
                                    databaseReference.child("user").child(accNumC).child("balance").setValue(totalAmountF);
                                    Intent y = new Intent(WithDrawalPage.this, TransactionDetails.class);
                                    y.putExtra("debitedAmmount", enterAmount);
                                    loaddingWithdrawl.setVisibility(View.INVISIBLE);
                                    startActivity(y);
                                    finish();
                                } else {
                                    Toast.makeText(WithDrawalPage.this, "Aukaat hai tere itne money account mein rakhne ke liye.", Toast.LENGTH_SHORT).show();
                                    loaddingWithdrawl.setVisibility(View.INVISIBLE);
                                }
//                            Toast.makeText(WithDrawalPage.this, "YES clicked", Toast.LENGTH_SHORT).show();
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(WithDrawalPage.this, "No clicked", Toast.LENGTH_SHORT).show();
                                Intent noBtn = new Intent(WithDrawalPage.this, loginActivity.class);
                                loaddingWithdrawl.setVisibility(View.INVISIBLE);
                                startActivity(noBtn);
                                finish();
                            }
                        });
                        AlertDialog aler = builder.create();
                        aler.show();

                    }
                }
            }
        });


    }

    public void onBackPressed() {
        AlertDialog.Builder build = new AlertDialog.Builder(WithDrawalPage.this);
        build.setMessage("Do you want to exit ?");

        build.setTitle("Alert !");

        build.setCancelable(false);

        build.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(WithDrawalPage.this, "Yes clicked", Toast.LENGTH_SHORT).show();
                Intent noBtn = new Intent(WithDrawalPage.this, loginActivity.class);
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