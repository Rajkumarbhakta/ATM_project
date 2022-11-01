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
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class loginActivity extends AppCompatActivity {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://atm-project-1dbee-default-rtdb.firebaseio.com/");
    Toolbar toolbarLogin;
    EditText accountNumber, pin;
    TextView openAccount, forgetPassword;
    Button submit;
    ProgressBar loginProgress;
    String accNum, pinNum;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        toolbarLogin = findViewById(R.id.toolbarLogin);
//        setSupportActionBar(toolbarLogin);
//        getSupportActionBar().setTitle("Sign in");
        accountNumber = findViewById(R.id.account_number);
        forgetPassword = findViewById(R.id.forget_pin);
        pin = findViewById(R.id.pin);
        openAccount = findViewById(R.id.sign_up);
        submit = findViewById(R.id.login);
        loginProgress = findViewById(R.id.google_progress);
        loginProgress.setVisibility(View.INVISIBLE);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginProgress.setVisibility(View.VISIBLE);
                accNum = accountNumber.getText().toString();
                pinNum = pin.getText().toString();
                databaseReference.child("user").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //check if number is exist
                        if (snapshot.hasChild(accNum)) {
                            //fetching data
                            String getPin = snapshot.child(accNum).child("pin").getValue(String.class);
                            String getFirstName = snapshot.child(accNum).child("firstName").getValue(String.class);
                            String getLastName = snapshot.child(accNum).child("lastName").getValue(String.class);
                            String getMobileNumber = snapshot.child(accNum).child("mobile-number").getValue(String.class);
                            String getBalance = snapshot.child(accNum).child("balance").getValue(String.class);
                            //check if pin is correct
                            if (Objects.equals(getPin, pinNum)) {
                                Toasty.success(loginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                Intent inextMain = new Intent(loginActivity.this, MainActivity.class);
                                inextMain.putExtra("accountNum", accNum);
                                inextMain.putExtra("firstName", getFirstName);
                                inextMain.putExtra("lastName", getLastName);
                                inextMain.putExtra("mobileNumber", getMobileNumber);
                                inextMain.putExtra("balance", getBalance);
                                startActivity(inextMain);
                                finish();
                                loginProgress.setVisibility(View.INVISIBLE);
                            } else {
                                Toasty.error(loginActivity.this, "Wrong Account number or PIN", Toast.LENGTH_SHORT).show();
                                loginProgress.setVisibility(View.INVISIBLE);
                            }
                        } else if (pinNum.isEmpty()) {
                            Toasty.warning(loginActivity.this, "Please Enter PIN", Toast.LENGTH_SHORT).show();
                            loginProgress.setVisibility(View.INVISIBLE);
                        } else {
                            AlertDialog.Builder donthaveAccountAlert = new AlertDialog.Builder(loginActivity.this);
                            donthaveAccountAlert.setIcon(R.drawable.ic_baseline_cancel_24).setTitle("Don't Have a account?");
                            donthaveAccountAlert.setMessage("Try to open a Free Bank Account");
                            donthaveAccountAlert.setPositiveButton("Register Now", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent InextReg = new Intent(loginActivity.this, registerActivity.class);
                                    startActivity(InextReg);
                                    finish();
                                }
                            });
                            loginProgress.setVisibility(View.INVISIBLE);
                            donthaveAccountAlert.show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toasty.error(loginActivity.this, "" + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

            }
        });


        openAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(loginActivity.this, registerActivity.class);
                startActivity(i);
                finish();
            }
        });
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(loginActivity.this, ForgetPinPage.class);
                accountNumber.setText("");
                pin.setText("");
                loginProgress.setVisibility(View.INVISIBLE);
                startActivity(i);
                finish();
            }
        });

    }
}