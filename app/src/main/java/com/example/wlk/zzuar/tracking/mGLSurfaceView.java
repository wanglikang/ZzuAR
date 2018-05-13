package com.example.wlk.zzuar.tracking;

import android.content.Context;
import android.graphics.RectF;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.List;


public class mGLSurfaceView extends GLSurfaceView {
    /**
    * 需要绘制的位置框信息，包含位置信息和时间戳（时间戳是为了让３Ｄ模型显示一会儿）
    */
    private java.util.Queue<android.util.Pair<RectF,Integer>> objList ;


    public mGLSurfaceView(Context context) {
        super(context);
        objList = new java.util.LinkedList<>();
    }

    public mGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        objList = new java.util.LinkedList<>();
    }

    public java.util.Queue<android.util.Pair<RectF,Integer>> getObjList() {
        java.util.Queue<android.util.Pair<RectF,Integer>> result = new java.util.LinkedList<>();
        synchronized(objList){
            while(!objList.isEmpty()){
                result.add(objList.remove());
            }

        }
        return result;
    }

    public void setObjList(java.util.Queue<android.util.Pair<RectF,Integer>> objs){
        while(!objList.isEmpty()){
            objList.remove(0);
        }
        objList.addAll(objs);
    }

    public synchronized void addObjList(android.util.Pair<RectF,Integer> trackedPos) {
        if(objList!=null){
            objList.add(trackedPos);
        }else objList = new java.util.LinkedList<>();
    }
}
