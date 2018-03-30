package com.example.wlk.zzuar.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.wlk.zzuar.R;
import com.example.wlk.zzuar.VaryRender;

public class MainActivity extends Activity {
    private String TAG ="MainActivity";
    private GLSurfaceView mGLView;
    private VaryRender render;
    private SeekBar seekbar1;
    private SeekBar seekbar2;
    private SeekBar seekbar3;
    private SeekBar seekbar4;
    private SeekBar seekbar5;
    private SeekBar seekbar6;
    private SeekBar seekbar7;
    private SeekBar seekbar8;
    private SeekBar seekbar9;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    class myseekbarListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            progress  = progress-50;
            switch (seekBar.getId()){
                case R.id.seekBar1:
                    render.changeCamara(1,(float)(progress*0.01));
                    break;
                case R.id.seekBar2:
                    render.changeCamara(2,(float)(progress*0.01));
                    break;
                case R.id.seekBar3:
                    render.changeCamara(3,(float)(progress*0.01));
                    break;
                case R.id.seekBar4:
                    render.changeCamara(4,(float)(progress*0.01));
                    break;
                case R.id.seekBar5:
                    render.changeCamara(5,(float)(progress*0.01));
                    break;
                case R.id.seekBar6:
                    render.changeCamara(6,(float)(progress*0.01));
                    break;
                case R.id.seekBar7:
                    render.changeCamara(7,(float)(progress*0.01));
                    break;
                case R.id.seekBar8:
                    render.changeCamara(8,(float)(progress*0.01));
                    break;
                case R.id.seekBar9:
                    render.changeCamara(9,(float)(progress*0.01));
                    break;
            }
            mGLView.requestRender();
            Log.i(TAG,progress+"");
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {}
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        seekbar1 = findViewById(R.id.seekBar1);
        seekbar2 = findViewById(R.id.seekBar2);
        seekbar3 = findViewById(R.id.seekBar3);
        seekbar4 = findViewById(R.id.seekBar4);
        seekbar5 = findViewById(R.id.seekBar5);
        seekbar6 = findViewById(R.id.seekBar6);
        seekbar7 = findViewById(R.id.seekBar7);
        seekbar8 = findViewById(R.id.seekBar8);
        seekbar9 = findViewById(R.id.seekBar9);
        myseekbarListener listener = new myseekbarListener();
        seekbar1.setOnSeekBarChangeListener(listener);
        seekbar2.setOnSeekBarChangeListener(listener);
        seekbar3.setOnSeekBarChangeListener(listener);
        seekbar4.setOnSeekBarChangeListener(listener);
        seekbar5.setOnSeekBarChangeListener(listener);
        seekbar6.setOnSeekBarChangeListener(listener);
        seekbar7.setOnSeekBarChangeListener(listener);
        seekbar8.setOnSeekBarChangeListener(listener);
        seekbar9.setOnSeekBarChangeListener(listener);

        render=new VaryRender(getResources());
        initGL();
        // Example of a call to a native method
//        TextView tv = (TextView) findViewById(R.id.sample_text);
//
//        GLSurfaceView surfaceview = new GLSurfaceView(this);
//        surfaceview.setEGLContextClientVersion(2);
//
//        surfaceview.setRenderer(new MyRender());
//
//        setContentView(surfaceview);
//        ActivityManager activitymanager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//        ConfigurationInfo cfg = activitymanager.getDeviceConfigurationInfo();
//        int glversion = cfg.reqGlEsVersion;





        //tv.setText(stringFromJNI());
       // tv.setText(a+"");


    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    //public native String stringFromJNI();

    public void initGL(){
        mGLView= (GLSurfaceView) findViewById(R.id.mGLView);
        mGLView.setEGLContextClientVersion(2);
        mGLView.setRenderer(render);
        mGLView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

}
