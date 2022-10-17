package com.rkbapps.atm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class loginActivity extends AppCompatActivity {
    EditText accountNumber,pin;
    TextView openAccount,forgetPassword;
    Button submit;
    String accNum,pinNum;
    Toolbar toolbarLogin;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        toolbarLogin=findViewById(R.id.toolbarLogin);
        setSupportActionBar(toolbarLogin);
        getSupportActionBar().setTitle("Sign in");
        accountNumber = findViewById(R.id.account_number);
        forgetPassword=findViewById(R.id.forget_pin);
        pin=findViewById(R.id.pin);
        openAccount=findViewById(R.id.sign_up);
        submit = findViewById(R.id.login);
        accNum=accountNumber.getText().toString();
        pinNum=pin.getText().toString();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if(Objects.equals(accNum, "8373001874") && Objects.equals(pinNum, "1234")){
                    Intent intent =new Intent(loginActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
               // }
//                else {
//                    Toast.makeText(loginActivity.this, "Wrong Account Number or Password", Toast.LENGTH_SHORT).show();
//                }
            }
        });
        openAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(loginActivity.this,registerActivity.class);
                startActivity(i);
            }
        });


    }
}