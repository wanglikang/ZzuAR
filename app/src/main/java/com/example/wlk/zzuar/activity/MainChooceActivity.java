package com.example.wlk.zzuar.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.wlk.zzuar.R;
import com.example.wlk.zzuar.obj.ObjLoadActivity;

public class MainChooceActivity extends AppCompatActivity {

    private Button bt1 ;
    private Button bt2 ;
    private Button bt3 ;
    private Button bt4 ;
    private Button bt5 ;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_chooce);
        context= this;
        bt1 = findViewById(R.id.c_bt1);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ObjLoadActivity.class);
                startActivity(intent);
            }
        });
        bt2 = findViewById(R.id.c_bt2);
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SensorActivity.class);
                startActivity(intent);
            }
        });

        bt3 = findViewById(R.id.c_bt3);
        bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetectorActivity.class);
                startActivity(intent);
            }
        });
        bt4 = findViewById(R.id.c_bt4);
        bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, testFrameActivity.class);
                startActivity(intent);
            }
        });

    }
}
