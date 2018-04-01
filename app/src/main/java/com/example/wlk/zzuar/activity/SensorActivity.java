package com.example.wlk.zzuar.activity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.wlk.zzuar.R;

public class SensorActivity extends AppCompatActivity  implements SensorEventListener {
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;

    private static final String TAG = "SensorActivity";
    //记录rotationMatrix矩阵值
    private float[] r = new float[9];
    //记录通过getOrientation()计算出来的方位横滚俯仰值
    private float[] values = new float[3];
    private float[] gravity = null;
    private float[] geomagnetic = null;
    // 定义真机的Sensor管理器
    private SensorManager mSensorManager;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Sensor.TYPE_ACCELEROMETER:
                    float[] valuse=(float[]) msg.obj;
                    tv1.setText("手机沿Yaw轴转过的角度为："+Float.toString(valuse[0]));
                    tv2.setText("手机沿Pitch轴转过的角度为："+Float.toString(valuse[1]));
                    tv3.setText("手机沿Roll轴转过的角度为："+Float.toString(valuse[2]));
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //注册加速度传感器监听
        Sensor acceleSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, acceleSensor, mSensorManager.SENSOR_DELAY_NORMAL);
        //注册磁场传感器监听
        Sensor magSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mSensorManager.registerListener(this, magSensor, mSensorManager.SENSOR_DELAY_NORMAL);
    }

    //@Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    //@Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER: //加速度传感器

                gravity = event.values;
                float[] valuse=event.values;
                Message message=new Message();
                message.obj=valuse;
                message.what=Sensor.TYPE_ACCELEROMETER;
                handler.sendMessage(message);
                break;
            case Sensor.TYPE_MAGNETIC_FIELD://磁场传感器
                geomagnetic = event.values;
                handler.sendEmptyMessage(0);
                break;
        }
    }
}
