package com.rkbapps.atm;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.SurfaceControl;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class WithDrawalPage extends AppCompatActivity {
   Button withDraw;
   EditText moneyAmount;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_drawal_page);
        withDraw=findViewById(R.id.withdrawBtn);
        moneyAmount=findViewById(R.id.editTextNumber);


        withDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int amount=Integer.parseInt(moneyAmount.getText().toString());
                if (amount % 100 != 0) {
                    AlertDialog.Builder moneyAlert = new AlertDialog.Builder(WithDrawalPage.this);
                    moneyAlert.setTitle("Sorry,Invalid Money Format.");
                    moneyAlert.setMessage("Please,Enter amount in multiple of 100/-");
                    moneyAlert.setPositiveButton("Ok", null);
                    AlertDialog t = moneyAlert.create();
                    t.show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(WithDrawalPage.this);
                    builder.setTitle("Alert!");
                    builder.setMessage("Are you sure want to withdraw the money ?");
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(WithDrawalPage.this, "YES clicked", Toast.LENGTH_SHORT).show();
                            Intent y = new Intent(WithDrawalPage.this, TransactionDetails.class);
                            startActivity(y);
                            finish();
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(WithDrawalPage.this, "No clicked", Toast.LENGTH_SHORT).show();
                            Intent noBtn = new Intent(WithDrawalPage.this, loginActivity.class);
                            startActivity(noBtn);
                            finish();
                        }
                    });
                    AlertDialog aler = builder.create();
                    aler.show();

                }
            }
        });


    }
    public void onBackPressed(){
        AlertDialog.Builder build= new AlertDialog.Builder(WithDrawalPage.this);
        build.setMessage("Do you want to exit ?");

        build.setTitle("Alert !");

        build.setCancelable(false);

        build.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(WithDrawalPage.this, "Yes clicked", Toast.LENGTH_SHORT).show();
                Intent noBtn=new Intent(WithDrawalPage.this, loginActivity.class);
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