package com.rkbapps.atm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class activity_login extends AppCompatActivity {
    EditText accountNumber,pin;
    TextView openAccount;
    Button submit;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("Sign in");
        accountNumber = findViewById(R.id.account_number);
        pin=findViewById(R.id.pin);
        openAccount=findViewById(R.id.newaccount);
        submit = findViewById(R.id.login);


    }
}