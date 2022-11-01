package com.rkbapps.atm;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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

import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;

public class ForgetPinPage extends AppCompatActivity {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://atm-project-1dbee-default-rtdb.firebaseio.com/");
    Toolbar forgetPinToolbar;
    ProgressBar loaddingForgetPin;
    EditText resetPinEmail, resetPinAccNum, Otp;
    TextView genarateOtp, otpStatus;
    Button resetPin;
    String email, accNum, sendOtp, enteredOtp;
    int genarateOtpClickedCount = 0;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pin_page);
        forgetPinToolbar = findViewById(R.id.toolbarForgetPin);
        // setSupportActionBar(forgetPinToolbar);
        resetPinEmail = findViewById(R.id.txtEnterYourEmail);
        resetPinAccNum = findViewById(R.id.txtForgetPinEnterAccountNumber);
        Otp = findViewById(R.id.txtEnterOtp);
        genarateOtp = findViewById(R.id.txtGenarateOtp);
        otpStatus = findViewById(R.id.txtOtpStatus);
        resetPin = findViewById(R.id.btnResetPin);
        loaddingForgetPin = findViewById(R.id.forgetPinProgressBar);
        loaddingForgetPin.setVisibility(View.INVISIBLE);
        //otp generation
        genarateOtp.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                accNum = resetPinAccNum.getText().toString();
                email = resetPinEmail.getText().toString();
                genarateOtpClickedCount++;
                loaddingForgetPin.setVisibility(View.VISIBLE);
                if (accNum.length() == 10) {
                    //Sending OTP
                    PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + accNum, 60, TimeUnit.SECONDS,
                            ForgetPinPage.this,
                            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                @Override
                                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                                }

                                @Override
                                public void onVerificationFailed(@NonNull FirebaseException e) {
                                    otpStatus.setTextColor(Color.parseColor("#D20808"));
                                    otpStatus.setText(e.getMessage());
                                    loaddingForgetPin.setVisibility(View.INVISIBLE);
                                }

                                @Override
                                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                    super.onCodeSent(s, forceResendingToken);
                                    sendOtp = s;
                                    loaddingForgetPin.setVisibility(View.INVISIBLE);
                                    otpStatus.setTextColor(Color.parseColor("#00FF00"));
                                    otpStatus.setText("OTP Send Successfully");
                                    genarateOtp.setText("Resend OTP"); // after click generate otp change the text to resend otp
                                }
                            }
                    );
                } else {
                    Toasty.warning(ForgetPinPage.this, "Please Enter Proper Details", Toast.LENGTH_SHORT).show();
                    loaddingForgetPin.setVisibility(View.INVISIBLE);
                }
            }
        });


        resetPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accNum = resetPinAccNum.getText().toString();
                email = resetPinEmail.getText().toString();
                enteredOtp = Otp.getText().toString();
                loaddingForgetPin.setVisibility(View.VISIBLE);
                if (accNum.isEmpty() && email.isEmpty()) {
                    Toasty.warning(ForgetPinPage.this, "Please enter your account number and register email id.", Toast.LENGTH_SHORT).show();
                    loaddingForgetPin.setVisibility(View.INVISIBLE);
                } else if (email.isEmpty()) {
                    Toasty.warning(ForgetPinPage.this, "Please enter your register email id.", Toast.LENGTH_SHORT).show();
                    loaddingForgetPin.setVisibility(View.INVISIBLE);
                } else if (accNum.isEmpty()) {
                    Toasty.warning(ForgetPinPage.this, "Please enter your account number", Toast.LENGTH_SHORT).show();
                    loaddingForgetPin.setVisibility(View.INVISIBLE);
                } else if (enteredOtp.isEmpty()) {
                    Toasty.error(ForgetPinPage.this, "Please enter OTP", Toast.LENGTH_SHORT).show();
                    loaddingForgetPin.setVisibility(View.INVISIBLE);
                } else {
                    //check if otp generated
                    if (genarateOtpClickedCount == 0) {
                        AlertDialog.Builder otpAlert = new AlertDialog.Builder(ForgetPinPage.this);
                        otpAlert.setIcon(R.drawable.ic_baseline_cancel_24).setTitle("OTP Warning").setMessage("Please click on the generate otp and enter The OTP");
                        otpAlert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //dismiss the dialog
                            }
                        });
                        otpAlert.show();
                        loaddingForgetPin.setVisibility(View.INVISIBLE);
                    } else {
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
                                            if (snapshot.hasChild(accNum)) {
                                                String getEmail = snapshot.child(accNum).child("email").getValue(String.class);
                                                if (getEmail.equals(email)) {
                                                    //reset custom alert dialog
                                                    Dialog dialog = new Dialog(ForgetPinPage.this);
                                                    dialog.setContentView(R.layout.reset_pin_custom_alart_dialog);
                                                    Button done = dialog.findViewById(R.id.btnPinResetDone);
                                                    EditText newPin = dialog.findViewById(R.id.txtChangePinEnterNewPin);
                                                    EditText confirmNewPin = dialog.findViewById(R.id.txtChangePinConfirmPin);
                                                    done.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            dialog.dismiss();
                                                            //done code here
                                                            loaddingForgetPin.setVisibility(View.VISIBLE);
                                                            String enterPin, confirmPin;
                                                            enterPin = newPin.getText().toString();
                                                            confirmPin = confirmNewPin.getText().toString();
                                                            if (enterPin.length() == 4 && confirmPin.equals(enterPin)) {
                                                                databaseReference.child("user").child(accNum).child("pin").setValue(confirmPin);
                                                                androidx.appcompat.app.AlertDialog.Builder pinChangeDoneAlert = new androidx.appcompat.app.AlertDialog.Builder(ForgetPinPage.this);
                                                                pinChangeDoneAlert.setIcon(R.drawable.ic_baseline_done_24).setTitle("PIN Change Successfully");
                                                                pinChangeDoneAlert.setPositiveButton("Log in Now", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                                        Intent Inextlogin = new Intent(ForgetPinPage.this, loginActivity.class);
                                                                        startActivity(Inextlogin);
                                                                        finish();
                                                                    }
                                                                });
                                                                pinChangeDoneAlert.setCancelable(false);
                                                                loaddingForgetPin.setVisibility(View.INVISIBLE);
                                                                pinChangeDoneAlert.show();
                                                            } else {
                                                                Toasty.error(ForgetPinPage.this, "Recheck PIN", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                                    loaddingForgetPin.setVisibility(View.INVISIBLE);
                                                    dialog.show();
                                                } else {
                                                    Toasty.error(ForgetPinPage.this, "Wrong credential", Toast.LENGTH_SHORT).show();
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
                                    otpStatus.setTextColor(Color.parseColor("#D20808"));
                                    loaddingForgetPin.setVisibility(View.INVISIBLE);
                                }
                            }
                        });
                    }
                    //else ends hare
                }
            }
        });


    }

    public void onBackPressed() {
        AlertDialog.Builder build = new AlertDialog.Builder(ForgetPinPage.this);
        build.setMessage("Do you want to exit ?");

        build.setTitle("Alert !");

        build.setCancelable(false);

        build.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent noBtn = new Intent(ForgetPinPage.this, loginActivity.class);
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