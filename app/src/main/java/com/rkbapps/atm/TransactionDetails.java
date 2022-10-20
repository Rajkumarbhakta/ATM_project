package com.rkbapps.atm;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TransactionDetails extends AppCompatActivity {
   TextView TransactionID,Amount,localDate,localTime;
   EditText txtMoney;
   private TextView dateDisplay;
   private Calendar calendar;
   private SimpleDateFormat dateFormat;
   private SimpleDateFormat timeFormat,transactionIdFormat;
   private String date,time,id,debitAmmount,debitAmountFastCash;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_details);
        TransactionID=findViewById(R.id.transactionID);
        Amount=findViewById(R.id.Amount);
        txtMoney=findViewById(R.id.txtWidthdrawlAmount);
//        String mon=txtMoney.getText().toString();
//        Amount.setText("100");
        localDate=(TextView) findViewById(R.id.LocalDate);
        localTime=findViewById(R.id.LocalTime);
//        calendar=Calendar.getInstance();
        Intent d=getIntent();
        debitAmmount=d.getStringExtra("debitedAmmount");
        debitAmountFastCash=d.getStringExtra("debitedAmmountFastCash");
        if(debitAmmount!=null) {
            Amount.setText("₹" + debitAmmount);
        }else{
            Amount.setText("₹" +debitAmountFastCash);
        }
        dateFormat =new SimpleDateFormat("dd/MM/yyyy");
        transactionIdFormat =new SimpleDateFormat("yyyyHHmmMMdd");
        id=transactionIdFormat.format(new Date());
        TransactionID.setText(id);
        date = dateFormat.format(new Date());
        localDate.setText(date);
        timeFormat = new SimpleDateFormat("HH:mm:ss");
        time=timeFormat.format(new Date());
        localTime.setText(time);
    }



    public void onBackPressed(){
        AlertDialog.Builder build= new AlertDialog.Builder(TransactionDetails.this);
        build.setMessage("Do you want to exit ?");

        build.setTitle("Alert !");

        build.setCancelable(false);

        build.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent noBtn=new Intent(TransactionDetails.this, loginActivity.class);
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