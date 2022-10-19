package com.rkbapps.atm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class ForgetPinPage extends AppCompatActivity {
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://atm-project-1dbee-default-rtdb.firebaseio.com/");
Toolbar forgetPinToolbar;
ProgressBar loaddingForgetPin;
EditText resetPinEmail,resetPinAccNum,Otp;
TextView genarateOtp,otpStatus;
Button resetPin;
String email,accNum,sendOtp,enteredOtp;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pin_page);
        forgetPinToolbar=findViewById(R.id.toolbarForgetPin);
        setSupportActionBar(forgetPinToolbar);
        getSupportActionBar().setTitle("Reset PIN");
        resetPinEmail=findViewById(R.id.txtEnterYourEmail);
        resetPinAccNum=findViewById(R.id.txtForgetPinEnterAccountNumber);
        Otp=findViewById(R.id.txtEnterOtp);
        genarateOtp=findViewById(R.id.txtGenarateOtp);
        otpStatus=findViewById(R.id.txtOtpStatus);
        resetPin=findViewById(R.id.btnResetPin);
        loaddingForgetPin=findViewById(R.id.forgetPinProgressBar);
        loaddingForgetPin.setVisibility(View.INVISIBLE);
        //otp generation
        genarateOtp.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                accNum=resetPinAccNum.getText().toString();
                email=resetPinEmail.getText().toString();
                loaddingForgetPin.setVisibility(View.VISIBLE);
                genarateOtp.setText("Resend OTP"); // after click generate otp change the text to resend otp
                if(accNum.length()==10){
                    //Sending OTP
                    PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + accNum, 60, TimeUnit.SECONDS,
                            ForgetPinPage.this,
                            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                @Override
                                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                                }

                                @Override
                                public void onVerificationFailed(@NonNull FirebaseException e) {

                                }

                                @Override
                                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                    super.onCodeSent(s, forceResendingToken);
                                    sendOtp=s;
                                    loaddingForgetPin.setVisibility(View.INVISIBLE);
                                    otpStatus.setText("OTP Send Successfully");
                                }
                            }
                    );
                }
                else {
                    Toast.makeText(ForgetPinPage.this, "Please Enter Proper Details", Toast.LENGTH_SHORT).show();
                    loaddingForgetPin.setVisibility(View.INVISIBLE);
                }
            }
        });


        resetPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accNum=resetPinAccNum.getText().toString();
                email=resetPinEmail.getText().toString();
                enteredOtp=Otp.getText().toString();
                loaddingForgetPin.setVisibility(View.VISIBLE);
                //verifying otp
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(sendOtp, enteredOtp);
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @SuppressLint("ResourceAsColor")
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                // Update UI
                                databaseReference.child("user").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.hasChild(accNum)){
                                            String getEmail=snapshot.child(accNum).child("email").getValue(String.class);
                                            if(getEmail.equals(email)){
                                                //reset custom alert dialog
                                                Dialog dialog=new Dialog(ForgetPinPage.this);
                                                dialog.setContentView(R.layout.reset_pin_custom_alart_dialog);
                                                Button done =dialog.findViewById(R.id.btnPinResetDone);
                                                EditText newPin = dialog.findViewById(R.id.txtChangePinEnterNewPin);
                                                EditText confirmNewPin = dialog.findViewById(R.id.txtChangePinConfirmPin);
                                                done.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        dialog.dismiss();
                                                        //done code here
                                                        loaddingForgetPin.setVisibility(View.VISIBLE);
                                                        String enterPin,confirmPin;
                                                        enterPin=newPin.getText().toString();
                                                        confirmPin=confirmNewPin.getText().toString();
                                                        if(enterPin.length()==4 && confirmPin.equals(enterPin)){
                                                            databaseReference.child("user").child(accNum).child("pin").setValue(confirmPin);
                                                            androidx.appcompat.app.AlertDialog.Builder pinChangeDoneAlert=new androidx.appcompat.app.AlertDialog.Builder(ForgetPinPage.this);
                                                            pinChangeDoneAlert.setIcon(R.drawable.ic_baseline_done_24).setTitle("PIN Change Successfully");
                                                            pinChangeDoneAlert.setPositiveButton("Log in Now", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                    Intent Inextlogin=new Intent(ForgetPinPage.this,loginActivity.class);
                                                                    startActivity(Inextlogin);
                                                                    finish();
                                                                }
                                                            });
                                                            pinChangeDoneAlert.setCancelable(false);
                                                            loaddingForgetPin.setVisibility(View.INVISIBLE);
                                                            pinChangeDoneAlert.show();
                                                        }else {
                                                            Toast.makeText(ForgetPinPage.this, "Recheck PIN", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                                loaddingForgetPin.setVisibility(View.INVISIBLE);
                                                dialog.show();
                                            }else {
                                                Toast.makeText(ForgetPinPage.this, "Wrong credential", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            } else {
                                // Sign in failed, display a message and update the UI
                                //if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                    // The verification code entered was invalid
                                otpStatus.setText("Wrong OTP");
                                otpStatus.setTextColor(R.color.red);
                                loaddingForgetPin.setVisibility(View.INVISIBLE);
                                }
                        }
                    });


//            //reset custom alert dialog
//                Dialog dialog=new Dialog(ForgetPinPage.this);
//                dialog.setContentView(R.layout.reset_pin_custom_alart_dialog);
//                Button done =dialog.findViewById(R.id.btnPinResetDone);
//                done.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        dialog.dismiss();
//                        //done code here
//                    }
//                });
//                dialog.show();
            }
        });


    }
}