package com.rkbapps.atm;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class registerActivity extends AppCompatActivity {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://atm-project-1dbee-default-rtdb.firebaseio.com/");
    ProgressBar loadingRegister;
    Spinner listGender;
    TextView alreadyHaveAccount;
    EditText txtFirstName, txtLastName, txtMobileNumber, txtEmail, txtPin, txtConfirmPin;
    Button btnRegister;
    String firstName, lastName, mobileNumber, email, pin, confirmPin, Gender;
    TextView genarateOtpReg, otpStatusReg;
    EditText enterOtpReg;
    String enterOtpForReg, originalOtp;
    int genarateOtpClicked = 0;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //spiner data set up
        listGender = findViewById(R.id.listGender);
        ArrayList<String> gender = new ArrayList<>();
        gender.add("Select Gender");
        gender.add("Male");
        gender.add("Female");
        gender.add("Other");
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, gender);
        listGender.setAdapter(genderAdapter);
        //finding ids for views
        txtFirstName = findViewById(R.id.textFirstName);
        txtLastName = findViewById(R.id.txtLastName);
        txtMobileNumber = findViewById(R.id.txtMobileNumber);
        txtEmail = findViewById(R.id.txtEmailId);
        txtPin = findViewById(R.id.txtCreatePassword);
        txtConfirmPin = findViewById(R.id.txtConfirmPassword);
        loadingRegister = findViewById(R.id.registerProgressBar);
        btnRegister = findViewById(R.id.buttonSubmit);
        alreadyHaveAccount = findViewById(R.id.txtAlreadyHaveAccount);
        genarateOtpReg = findViewById(R.id.txtGenarateOtpReg);
        enterOtpReg = findViewById(R.id.txtEnterOtpReg);
        otpStatusReg = findViewById(R.id.otpStatusReg);
        loadingRegister.setVisibility(View.INVISIBLE);

        //generate and sending otp;
        genarateOtpReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstName = txtFirstName.getText().toString();
                lastName = txtLastName.getText().toString();
                email = txtEmail.getText().toString();
                Gender = listGender.getSelectedItem().toString();
                mobileNumber = txtMobileNumber.getText().toString();
                pin = txtPin.getText().toString();
                confirmPin = txtConfirmPin.getText().toString();
                genarateOtpClicked++;
                loadingRegister.setVisibility(View.VISIBLE);
                if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || Gender.equals("Select Gender") || mobileNumber.length() != 10 || pin.isEmpty() || confirmPin.isEmpty()) {
                    Toast.makeText(registerActivity.this, "Give all details Properly", Toast.LENGTH_SHORT).show();
                    loadingRegister.setVisibility(View.INVISIBLE);
                } else {
                    //Sending otp
                    PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + mobileNumber,
                            60, TimeUnit.SECONDS,
                            registerActivity.this,
                            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                @Override
                                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                }

                                @SuppressLint("SetTextI18n")
                                @Override
                                public void onVerificationFailed(@NonNull FirebaseException e) {
                                    otpStatusReg.setTextColor(Color.parseColor("#D20808"));
                                    otpStatusReg.setText("" + e.getMessage());
                                    loadingRegister.setVisibility(View.INVISIBLE);
                                }

                                @Override
                                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                    super.onCodeSent(s, forceResendingToken);
                                    originalOtp = s;
                                    loadingRegister.setVisibility(View.INVISIBLE);
                                    otpStatusReg.setTextColor(Color.parseColor("#00FF00"));
                                    otpStatusReg.setText("OTP Send Successfully");
                                    genarateOtpReg.setText("Resend OTP");

                                }
                            });
                }

            }
        });

        //registering process
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstName = txtFirstName.getText().toString();
                lastName = txtLastName.getText().toString();
                email = txtEmail.getText().toString();
                Gender = listGender.getSelectedItem().toString();
                mobileNumber = txtMobileNumber.getText().toString();
                pin = txtPin.getText().toString();
                confirmPin = txtConfirmPin.getText().toString();
                enterOtpForReg = enterOtpReg.getText().toString();
                loadingRegister.setVisibility(View.VISIBLE);
                if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()
                        || mobileNumber.isEmpty() || pin.isEmpty() || confirmPin.isEmpty() ||
                        Gender.equals("Select Gender")
                ) {
                    Toast.makeText(registerActivity.this, "Please give us the proper details", Toast.LENGTH_SHORT).show();
                    loadingRegister.setVisibility(View.INVISIBLE);
                } else if (enterOtpForReg.isEmpty()) {
                    Toast.makeText(registerActivity.this, "Enter OTP", Toast.LENGTH_SHORT).show();
                    loadingRegister.setVisibility(View.INVISIBLE);
                } else {
                    if (genarateOtpClicked == 0) {
                        AlertDialog.Builder otpAlert = new AlertDialog.Builder(registerActivity.this);
                        otpAlert.setIcon(R.drawable.ic_baseline_cancel_24).setTitle("OTP Warning").setMessage("Please click on the generate otp and enter The OTP");
                        otpAlert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //dismiss the dialog
                            }
                        });
                        otpAlert.show();
                        loadingRegister.setVisibility(View.INVISIBLE);
                    } else {
                        //checking otp
                        PhoneAuthCredential phoneAuthProvider = PhoneAuthProvider.getCredential(originalOtp, enterOtpForReg);
                        FirebaseAuth.getInstance().signInWithCredential(phoneAuthProvider).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    //if otp is correct then put the data to database
                                    databaseReference.child("user").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.hasChild(mobileNumber)) {
                                                //check if already have account
                                                Toast.makeText(registerActivity.this, "Already Have account with this Mobile Number", Toast.LENGTH_SHORT).show();
                                                loadingRegister.setVisibility(View.INVISIBLE);
                                            } else {
                                                if (mobileNumber.length() != 10 || pin.length() != 4 || !pin.equals(confirmPin)) {
                                                    Toast.makeText(registerActivity.this, "Please Recheck details and enter correctly", Toast.LENGTH_SHORT).show();
                                                    loadingRegister.setVisibility(View.INVISIBLE);
                                                } else {
                                                    databaseReference.child("user").child(mobileNumber).child("firstName").setValue(firstName);
                                                    databaseReference.child("user").child(mobileNumber).child("lastName").setValue(lastName);
                                                    databaseReference.child("user").child(mobileNumber).child("mobile-number").setValue(mobileNumber);
                                                    databaseReference.child("user").child(mobileNumber).child("gender").setValue(Gender);
                                                    databaseReference.child("user").child(mobileNumber).child("email").setValue(email);
                                                    databaseReference.child("user").child(mobileNumber).child("pin").setValue(confirmPin);
                                                    databaseReference.child("user").child(mobileNumber).child("balance").setValue("0");
                                                    AlertDialog.Builder registationAlert = new AlertDialog.Builder(registerActivity.this);
                                                    registationAlert.setIcon(R.drawable.ic_baseline_done_24).setTitle("Your Account is Ready");
                                                    registationAlert.setMessage("Your Account Number is " + mobileNumber);
                                                    registationAlert.setPositiveButton("Log in Now", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                            Intent Inextlogin = new Intent(registerActivity.this, loginActivity.class);
                                                            startActivity(Inextlogin);
                                                            finish();
                                                        }
                                                    });
                                                    registationAlert.setCancelable(false);
                                                    loadingRegister.setVisibility(View.INVISIBLE);
                                                    registationAlert.show();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Toast.makeText(registerActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                } else {
                                    otpStatusReg.setTextColor(Color.parseColor("#D20808"));
                                    otpStatusReg.setText("Wrong OTP");
                                }
                            }
                        });
                    }
                }
            }
        });

        alreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(registerActivity.this, loginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    public void onBackPressed() {
        AlertDialog.Builder build = new AlertDialog.Builder(registerActivity.this);
        build.setMessage("Do you want to exit ?");
        build.setTitle("Alert !");
        build.setCancelable(false);
        build.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent noBtn = new Intent(registerActivity.this, loginActivity.class);
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