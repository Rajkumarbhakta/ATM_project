package com.rkbapps.atm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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

public class Deposit extends AppCompatActivity {
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://atm-project-1dbee-default-rtdb.firebaseio.com/");
   EditText txtEnterAmount,accountNumber;
   Button btnProceed;
    String accNum,enterAmount;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);
        txtEnterAmount=findViewById(R.id.txtEnterAmount);
        btnProceed=findViewById(R.id.btnProceed);
        accountNumber = findViewById(R.id.account_number);

        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accNum=accountNumber.toString();
                enterAmount =txtEnterAmount.getText().toString();
                databaseReference.child("accNum").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild(accNum)){
                            String getBalance=snapshot.child(accNum).child("balance").getValue(String.class);
                            int previousAmount=Integer.parseInt(getBalance);
                            int depAmount=Integer.parseInt(enterAmount);
                            int totalAmount=previousAmount+depAmount;
                            String totalAmountF=Integer.toString(totalAmount);

                            databaseReference.child(accNum).child("balance").setValue(totalAmountF);
                            Toast.makeText(Deposit.this, "Balance deposit successfully.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}