package com.example.all_letters;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    public CardView c1,c2,c3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        c1=(CardView) findViewById(R.id.card1);
        c2=(CardView) findViewById(R.id.card2);
        c3=(CardView) findViewById(R.id.card3);

        c1.setOnClickListener(this);
        c2.setOnClickListener(this);
        c3.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent i;

        switch (v.getId()){

            case R.id.card1:
                i=new Intent(this,templates.class);
                startActivity(i);
                break;

            case R.id.card2:
                i=new Intent(this,plain.class);
                startActivity(i);
                break;

            case R.id.card3:
                i=new Intent(this,notes.class);
                startActivity(i);
                break;
        }
    }
}