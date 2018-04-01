package com.example.wlk.zzuar.obj;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.wlk.zzuar.activity.BaseActivity;
import com.example.wlk.zzuar.R;
import com.example.wlk.zzuar.zip.Gl2Utils;

import java.io.IOException;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by wuwang on 2017/1/7
 */

public class ObjLoadActivity extends BaseActivity {

    private GLSurfaceView mGLView;
    private ObjFilter mFilter;
    private Obj3D obj;
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_obj);
        mGLView= (GLSurfaceView) findViewById(R.id.mGLView);
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
            }

            @Override
            public void onSurfaceChanged(GL10 gl, int width, int height) {
                reSurfaceChanged(width,height);
                Log.i("SurfaceChange",width+":"+height);
                Log.i("SurfaceChange","onSurfaceChanged");
            }

            @Override
            public void onDrawFrame(GL10 gl) {
//                Log.i("SurfaceChange","onDrawFrame");
//                Matrix.rotateM(mFilter.getMatrix(),0,0.3f,0,1,0);
                mFilter.draw();

            }
        });

        mGLView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        mGLView.setOnClickListener((v)->{
            float[] fa = mFilter.getMatrix();

            Log.i("ObjLoadActivity","setOnClickListener");

            Matrix.rotateM(fa,0,0.3f,0,1,0);
            //Log.i("ObjLoadActivity",floatArr2String(fa));
            Toast.makeText(context,floatArr2String(fa),Toast.LENGTH_SHORT).show();
//            mFilter.draw();

        });
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
