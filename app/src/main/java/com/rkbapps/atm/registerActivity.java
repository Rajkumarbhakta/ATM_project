package com.rkbapps.atm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Guideline;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class registerActivity extends AppCompatActivity {
DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://atm-project-1dbee-default-rtdb.firebaseio.com/");

TextView alreadyHaveAccount;
EditText txtFirstName,txtLastName,txtMobileNumber,txtEmail,txtPin,txtConfirmPin;
Button btnRegister;
String firstName,lastName,mobileNumber,email,pin,confirmPin,Gender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        txtFirstName=findViewById(R.id.textFirstName);
        txtLastName=findViewById(R.id.txtLastName);
        txtMobileNumber=findViewById(R.id.txtMobileNumber);
        txtEmail=findViewById(R.id.txtEmailId);
        txtPin=findViewById(R.id.txtCreatePassword);
        txtConfirmPin=findViewById(R.id.txtConfirmPassword);
        btnRegister=findViewById(R.id.buttonSubmit);
        alreadyHaveAccount=findViewById(R.id.txtAlreadyHaveAccount);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstName=txtFirstName.getText().toString();
                lastName=txtLastName.getText().toString();
                email=txtEmail.getText().toString();
                mobileNumber=txtMobileNumber.getText().toString();
                pin=txtPin.getText().toString();
               confirmPin=txtConfirmPin.getText().toString();
               databaseReference.child("user").addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                       if(snapshot.hasChild(mobileNumber)){
                           Toast.makeText(registerActivity.this, "Already Have account with this Mobile Number", Toast.LENGTH_SHORT).show();
                       }
                       else {
                           if(mobileNumber.length() != 10 || pin.length()!=4 || !pin.equals(confirmPin)){
                               Toast.makeText(registerActivity.this, "Please Recheck details and enter correctly", Toast.LENGTH_SHORT).show();
                           }else {
                               databaseReference.child("user").child(mobileNumber).child("firstName").setValue(firstName);
                               databaseReference.child("user").child(mobileNumber).child("lastName").setValue(lastName);
                               databaseReference.child("user").child(mobileNumber).child("mobile-number").setValue(mobileNumber);
                               databaseReference.child("user").child(mobileNumber).child("email").setValue(email);
                               databaseReference.child("user").child(mobileNumber).child("pin").setValue(confirmPin);
                               databaseReference.child("user").child(mobileNumber).child("balance").setValue("0");
                               AlertDialog.Builder registationAlert=new AlertDialog.Builder(registerActivity.this);
                               registationAlert.setIcon(R.drawable.ic_baseline_done_24).setTitle("Your Account is Ready");
                               registationAlert.setMessage("Your Account Number is "+mobileNumber);
                               registationAlert.setPositiveButton("Log in Now", new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialogInterface, int i) {
                                       Intent Inextlogin=new Intent(registerActivity.this,loginActivity.class);
                                       startActivity(Inextlogin);
                                       finish();
                                   }
                               });
                               registationAlert.setCancelable(false);
                               registationAlert.show();
                           }
                       }
                   }
                   @Override
                   public void onCancelled(@NonNull DatabaseError error) {

                   }
               });
            }
        });

        alreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(registerActivity.this,loginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
    public void onBackPressed(){
        AlertDialog.Builder build= new AlertDialog.Builder(registerActivity.this);
        build.setMessage("Do you want to exit ?");

        build.setTitle("Alert !");

        build.setCancelable(false);

        build.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(registerActivity.this, "Yes clicked", Toast.LENGTH_SHORT).show();
                Intent noBtn=new Intent(registerActivity.this, loginActivity.class);
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