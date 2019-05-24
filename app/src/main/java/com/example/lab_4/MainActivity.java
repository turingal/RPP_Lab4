package com.example.lab_4;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

import static java.lang.System.currentTimeMillis;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();

        long date = intent.getLongExtra("date", 0);

        String days = "Дата пока не установлена";

        if(date != 0){

            long cur_date = System.currentTimeMillis();
            System.out.println(cur_date + " " + date);

            int days1 = (int) Math.floor((date - cur_date)/(1000*60*60*24));
            System.out.println("lasbvlajbhdl"+(date - cur_date));
            days = String.valueOf(days1) + " полных суток осталось до оповещения";
        }

        TextView tv = findViewById(R.id.amount_days);
        tv.setText(days);

        final Context ctx = this;

        TextView btn = findViewById(R.id.amount_days);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ctx, ChoosingDate.class);
                ctx.startActivity(intent);
                finish();
            }
        });
    }
}
