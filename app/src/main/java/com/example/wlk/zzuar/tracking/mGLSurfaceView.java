package com.example.wlk.zzuar.tracking;

import android.content.Context;
import android.graphics.RectF;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.wlk.zzuar.myapp;
import com.example.wlk.zzuar.utils.ObjViewDistributor;


public class mGLSurfaceView extends GLSurfaceView implements GLSurfaceView.OnClickListener{
    private boolean isShowRect = false;
    @Override
    public void onClick(View v) {
        Toast.makeText(myapp.getContext(), "GlSurefaceView clicked", Toast.LENGTH_SHORT).show();
        Log.i("gl", "GlSurefaceView clicked");
        isShowRect = !isShowRect;
    }
    public boolean getIsShowRect(){
        return isShowRect;
    }

    public class ObjInfo{
        public String objname;
        public RectF location;
        public int lifetime;

    }
    /**
    * 需要绘制的位置框信息，包含位置信息和时间戳（时间戳是为了让３Ｄ模型显示一会儿）
    */
    private java.util.Queue<ObjInfo> objList ;
    private ObjViewDistributor distributor;

    public ObjViewDistributor getDistributor() {
        return distributor;
    }

    public void setDistributor(ObjViewDistributor distributor) {
        this.distributor = distributor;
    }

    public mGLSurfaceView(Context context) {
        super(context);
        objList = new java.util.LinkedList<>();
    }

    public mGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        objList = new java.util.LinkedList<>();
    }

    public java.util.Queue<ObjInfo> getObjList() {
        java.util.Queue<ObjInfo> result = new java.util.LinkedList<>();
        synchronized(objList){
            while(!objList.isEmpty()){
                result.add(objList.remove());
            }

        }
        return result;
    }

    public void setObjList(java.util.Queue<ObjInfo> objs){
        while(!objList.isEmpty()){
            objList.remove(0);
        }
        objList.addAll(objs);
    }

    public synchronized void addObjList(ObjInfo trackedPos) {
        if(objList!=null){
            objList.add(trackedPos);
        }else objList = new java.util.LinkedList<>();
    }
}
