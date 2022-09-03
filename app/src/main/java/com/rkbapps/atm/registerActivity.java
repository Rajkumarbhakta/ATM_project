package com.rkbapps.atm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class registerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Sign Up");
        setContentView(R.layout.activity_register);
    }
}