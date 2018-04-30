package com.example.wlk.zzuar.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;



public class testFrameActivity extends AppCompatActivity {

    private float alpha = (float)1.0;
    private TextView texture;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testframe);
        texture = findViewById(R.id.texture);
        texture.setAlpha(alpha);
        texture.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                alpha+=0.05;
                alpha = alpha>1?1: (float) 0.05;
                texture.setAlpha(alpha);
            }
        });
    }
}
