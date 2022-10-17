package com.rkbapps.atm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Guideline;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class registerActivity extends AppCompatActivity {

Spinner listGender;
TextView alreadyHaveAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().setTitle("Sign Up");
        setContentView(R.layout.activity_register);
        listGender=findViewById(R.id.listGender);
        ArrayList<String> gender=new ArrayList<>();

        gender.add("Select Gender");
        gender.add("Male");
        gender.add("Female");
        gender.add("Other");

        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,gender);
        listGender.setAdapter(genderAdapter);

        alreadyHaveAccount=findViewById(R.id.txtAlreadyHaveAccount);
        alreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(registerActivity.this,loginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}