package com.example.wlk.zzuar.obj;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.SeekBar;

import com.example.wlk.zzuar.activity.BaseActivity;
import com.example.wlk.zzuar.R;
import com.example.wlk.zzuar.utils.Gl2Utils;

import java.io.IOException;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class ObjLoadActivity extends BaseActivity {

    private GLSurfaceView mGLView;
    private ObjFilter mFilter;
    private Obj3D obj;
    private Context context;
    private SeekBar seekbar1;
    private SeekBar seekbar2;
    private SeekBar seekbar3;
    private SeekBar seekbar4;
    private SeekBar seekbar5;
    private SeekBar seekbar6;
    private SeekBar seekbar7;
    private SeekBar seekbar8;
    private SeekBar seekbar9;
    private SeekBar seekbar10;
    private SeekBar seekbar11;
    private SeekBar seekbar12;
    private float[] diyxyz = new float[12];

class OnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener{

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        float newxyz;
        if(progress<50){
            newxyz= -(float) ((50-progress)*1.0/100);
        }else {
            newxyz = (float) ((progress - 50) * 1.0 / 100);
        }

        switch (seekBar.getId()){

            case R.id.seekBar1:
                diyxyz[0] = newxyz;
                break;
            case R.id.seekBar2:
                diyxyz[1] = newxyz;
                break;
            case R.id.seekBar3:
                diyxyz[2] = newxyz;
                break;
            case R.id.seekBar4:
                diyxyz[3] = newxyz;
                break;
            case R.id.seekBar5:
                diyxyz[4] = newxyz;
                break;
            case R.id.seekBar6:
                diyxyz[5] = newxyz;
                break;
            case R.id.seekBar7:
                diyxyz[6] = newxyz;
                break;
            case R.id.seekBar8:
                diyxyz[7] = newxyz;
                break;
            case R.id.seekBar9:
                diyxyz[8] = (float) (1-(50-progress)*1.0/1000);Log.i("learning", seekBar.getId()+" :"+progress+" ;set　newxyz:"+diyxyz[11]);
                break;
            case R.id.seekBar10:
                diyxyz[9] = (float) (1-(50-progress)*1.0/1000);Log.i("learning", seekBar.getId()+" :"+progress+" ;set　newxyz:"+diyxyz[11]);
                break;
            case R.id.seekBar11:
                diyxyz[10] = (float) (1-(50-progress)*1.0/1000);Log.i("learning", seekBar.getId()+" :"+progress+" ;set　newxyz:"+diyxyz[10]);
                break;
            case R.id.seekBar12:
                diyxyz[11] = (float) (1-(50-progress)*1.0/1000);Log.i("learning", seekBar.getId()+" :"+progress+" ;set　newxyz:"+diyxyz[11]);
                break;
        }
        Log.i("learning", seekBar.getId()+" :"+progress+" ;set　newxyz:"+newxyz);

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_obj);
        mGLView= (GLSurfaceView) findViewById(R.id.mGLView);
        seekbar1 = findViewById(R.id.seekBar1);
        seekbar2 = findViewById(R.id.seekBar2);
        seekbar3 = findViewById(R.id.seekBar3);
        seekbar4 = findViewById(R.id.seekBar4);
        seekbar5 = findViewById(R.id.seekBar5);
        seekbar6 = findViewById(R.id.seekBar6);
        seekbar7 = findViewById(R.id.seekBar7);
        seekbar8 = findViewById(R.id.seekBar8);
        seekbar9 = findViewById(R.id.seekBar9);
        seekbar10 = findViewById(R.id.seekBar10);
        seekbar11 = findViewById(R.id.seekBar11);
        seekbar12 = findViewById(R.id.seekBar12);

        OnSeekBarChangeListener osbcl = new OnSeekBarChangeListener();
        seekbar1.setOnSeekBarChangeListener(osbcl);
        seekbar2.setOnSeekBarChangeListener(osbcl);
        seekbar3.setOnSeekBarChangeListener(osbcl);
        seekbar4.setOnSeekBarChangeListener(osbcl);
        seekbar5.setOnSeekBarChangeListener(osbcl);
        seekbar6.setOnSeekBarChangeListener(osbcl);
        seekbar7.setOnSeekBarChangeListener(osbcl);
        seekbar8.setOnSeekBarChangeListener(osbcl);
        seekbar9.setOnSeekBarChangeListener(osbcl);
        seekbar10.setOnSeekBarChangeListener(osbcl);
        seekbar11.setOnSeekBarChangeListener(osbcl);
        seekbar12.setOnSeekBarChangeListener(osbcl);


        mGLView.setEGLContextClientVersion(2);
        mFilter=new ObjFilter(getResources());
        obj=new Obj3D();
        try {
            ObjReader.read(getAssets().open("3dres/hat.obj"),obj);
            mFilter.setObj3D(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mGLView.setRenderer(new GLSurfaceView.Renderer() {

            @Override
            public void onSurfaceCreated(GL10 gl, EGLConfig config) {
                mFilter.create();
                Matrix.rotateM(mFilter.getMatrix(),0,0.3f,0,1,0);
                Log.i("learning","mFilter.create()" );
                diyxyz[0] = (float) 0.001;
                diyxyz[1] = (float) 0.001;
                diyxyz[2] = (float) 0.001;
                diyxyz[3] = (float) 0.001;
                diyxyz[4] = (float) 0.001;
                diyxyz[5] = (float) 0.001;
                diyxyz[6] = (float) 0.001;
                diyxyz[7] = (float) 0.001;
                diyxyz[8] = (float) 0.001;
                diyxyz[9] = (float) 1;
                diyxyz[10] = (float) 1;
                diyxyz[11] = (float) 1;


            }

            @Override
            public void onSurfaceChanged(GL10 gl, int width, int height) {

                reSurfaceChanged(width,height);
                Log.i("learning",width+":"+height);
                Log.i("learning","onSurfaceChanged");

            }

            @Override
            public void onDrawFrame(GL10 gl) {

                //Matrix.rotateM(mFilter.getMatrix(),0,0.3f,0,1,0);
                Matrix.rotateM(mFilter.getMatrix(),0,diyxyz[0],diyxyz[1],diyxyz[2],diyxyz[3]);
                Matrix.translateM(mFilter.getMatrix(), 0,diyxyz[5],diyxyz[6],diyxyz[7]);
                Matrix.scaleM(mFilter.getMatrix(), 0,diyxyz[9],diyxyz[10],diyxyz[11]);
                //Matrix.setLookAtM(, , , , , , , , , , );

                mFilter.draw();

            }
        });

        mGLView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
//        mGLView.setOnClickListener((v)->{
//            float[] fa = mFilter.getMatrix();
//
//            Log.i("ObjLoadActivity","setOnClickListener");
//
//            Matrix.rotateM(fa,0,0.3f,0,1,0);
//            //Log.i("ObjLoadActivity",floatArr2String(fa));
//            Toast.makeText(context,floatArr2String(fa),Toast.LENGTH_SHORT).show();
////            mFilter.draw();
//
//        });
    }
    public String floatArr2String(float[] fa){
        StringBuilder stringBuilder = new StringBuilder();
        int len = fa.length;
        int i = (int)Math.sqrt(len*1.0);
        stringBuilder.append('\n');
        for(int a = 0;a<i;a++){
           for(int j = 0;j<i;j++){
               stringBuilder.append(fa[a*i+j]+" ");
           }
           stringBuilder.append('\n');
        }

        return stringBuilder.toString();


    }


    public void reSurfaceChanged(int width, int height){
        mFilter.onSizeChanged(width, height);
        float[] matrix= Gl2Utils.getOriginalMatrix();
        Matrix.scaleM(matrix,0,0.2f,0.2f*width/height,0.2f);
        mFilter.setMatrix(matrix);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGLView.onPause();
    }
}
