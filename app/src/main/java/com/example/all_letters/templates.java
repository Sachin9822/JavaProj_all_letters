package com.example.all_letters;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class templates extends AppCompatActivity implements View.OnClickListener {
    public  CardView c4,c5,c6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_templates);
        c4=(CardView) findViewById(R.id.appology_card);
        c5=(CardView) findViewById(R.id.leave_card);
        c6=(CardView) findViewById(R.id.request_card);

        c4.setOnClickListener(this);
        c5.setOnClickListener(this);
        c6.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Intent i;

        switch (view.getId()){

            case R.id.appology_card:
                i=new Intent(this,apology_page.class);
                startActivity(i);
                break;

            case R.id.leave_card:
                i=new Intent(this,leave_page.class);
                startActivity(i);
                break;

            case R.id.request_card:
                i=new Intent(this,request_page.class);
                startActivity(i);
                break;
        }

    }
}