package com.example.wlk.zzuar;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        /setContentView(R.layout.activity_main);

        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);

        GLSurfaceView surfaceview = new GLSurfaceView(this);
        surfaceview.setEGLContextClientVersion(2);

        surfaceview.setRenderer(new MyRender());

        setContentView(surfaceview);
        ActivityManager activitymanager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo cfg = activitymanager.getDeviceConfigurationInfo();
        int glversion = cfg.reqGlEsVersion;





        //tv.setText(stringFromJNI());
        int a = (glversion>>16)&0x0000f;
       // tv.setText(a+"");


    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
