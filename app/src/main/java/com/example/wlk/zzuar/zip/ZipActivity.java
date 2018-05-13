/*
 *
 * ZipActivity.java
 * 
 * Created by Wuwang on 2016/12/8
 * Copyright © 2016年 深圳哎吖科技. All rights reserved.
 */
package com.example.wlk.zzuar.zip;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.example.wlk.zzuar.R;
import com.example.wlk.zzuar.utils.Gl2Utils;


/**
 * Description:
 */
public class ZipActivity extends Activity {

    private ZipAniView mAniView;
    private String nowMenu="assets/etczip/cc.zip";

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zip);
        mAniView= (ZipAniView)findViewById(R.id.mAni);
        mAniView.setScaleType(Gl2Utils.TYPE_CENTERINSIDE);
        mAniView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mAniView.isPlay()){
                    mAniView.setAnimation(nowMenu,50);
                    mAniView.start();
                }
            }
        });
        mAniView.setStateChangeListener(new StateChangeListener() {
            @Override
            public void onStateChanged(int lastState, int nowState) {
                if(nowState==STOP){
                    if(!mAniView.isPlay()){
                        mAniView.setAnimation(nowMenu,50);
                        mAniView.start();
                    }
                }
            }
        });
    }

}
