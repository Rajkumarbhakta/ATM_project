package com.rkbapps.atm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Deposit extends AppCompatActivity {
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://atm-project-1dbee-default-rtdb.firebaseio.com/");
   EditText txtEnterAmount;
   Button btnProceed;
   ProgressBar loaddingDiposit;
   TextView presentBalance;
    String accNumInDiposit,enterAmount,totalAmountF,previousBalance;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);
        txtEnterAmount=findViewById(R.id.txtEnterAmount);
        btnProceed=findViewById(R.id.btnProceed);
        presentBalance=findViewById(R.id.txtCurrentAccountBalance);
        loaddingDiposit=findViewById(R.id.dipositPageProgressBar);
        Intent getDetailsFromMainActivity=getIntent();
        accNumInDiposit=getDetailsFromMainActivity.getStringExtra("accountNumberToDiposit");
        loaddingDiposit.setVisibility(View.VISIBLE);
        //fetching account balance details
        databaseReference.child("user").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(accNumInDiposit)){
                    previousBalance=snapshot.child(accNumInDiposit).child("balance").getValue(String.class);
                    presentBalance.setText("A/C bal:  ₹"+previousBalance);
                    loaddingDiposit.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loaddingDiposit.setVisibility(View.VISIBLE);
                enterAmount =txtEnterAmount.getText().toString();
                int previousAmount=Integer.parseInt(previousBalance);
                int depAmount=Integer.parseInt(enterAmount);
                int totalAmount=previousAmount+depAmount;
                totalAmountF=Integer.toString(totalAmount);
                //new balance update in database
                databaseReference.child("user").child(accNumInDiposit).child("balance").setValue(totalAmountF);
                loaddingDiposit.setVisibility(View.INVISIBLE);
                //Toast.makeText(Deposit.this, "Balance deposit successfully.", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder dipositStatusDialog=new AlertDialog.Builder(Deposit.this);
                dipositStatusDialog.setIcon(R.drawable.ic_baseline_done_24);
                dipositStatusDialog.setTitle("Deposit Amount Successfully").setMessage("₹"+enterAmount+" deposit in your account Successfully and the Account balance is ₹"+totalAmountF);
                dipositStatusDialog.setPositiveButton("Deposit More", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        txtEnterAmount.setText("");
                        presentBalance.setText("A/C bal:  ₹"+totalAmountF);
                    }
                });
                dipositStatusDialog.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent=new Intent(Deposit.this,loginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                dipositStatusDialog.setCancelable(false);
               dipositStatusDialog.show();
            }
        });
    }

    // when back pressed
    public void onBackPressed(){
        AlertDialog.Builder build= new AlertDialog.Builder(Deposit.this);
        build.setMessage("Do you want to exit ?");

        build.setTitle("Alert !");

        build.setCancelable(false);

        build.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(Deposit.this, "Yes clicked", Toast.LENGTH_SHORT).show();
                Intent noBtn=new Intent(Deposit.this, loginActivity.class);
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