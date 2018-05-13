package com.example.wlk.zzuar.obj;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.util.Log;

import com.example.wlk.zzuar.utils.MatrixUtils;
import com.example.wlk.zzuar.utils.ObjUtil;

import java.io.IOException;

public class VisibObj {
    private Obj3D bindObj;
    private GLGod god ;
    public String objName;
    public static Context context;
    private float[] matrix= MatrixUtils.getOriginalMatrix();
    private int textureId=0;

    public VisibObj(GLGod god){
        this.god = god;
        this.context = god.getContext();
    }

    public void setObjNameAndReadObj(String name){
        this.objName = name;
        bindObj = new Obj3D();
        try {
            //ObjReader.read(context.getAssets().open("3dres/hat.obj"),bindObj);
            ObjReader.read(context.getAssets().open("3dres/"+name+".obj"),bindObj);
            Log.i("3dobj", "load .obj from "+name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(bindObj.vertTexture!=null){
            try {
                Log.i("3dobj", "start to load Texture for:"+name);
                textureId= ObjUtil.createTexture(BitmapFactory.decodeStream(ObjUtil.mRes.getAssets().open("3dres/"+bindObj.mtl.map_Kd)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void requestDraw(){
        god.drawObj(this);
    }

    public void drawSelf(){
        requestDraw();
    }

    public Obj3D getBindObj() {
        return bindObj;
    }

    public int getTextureId() {
        return textureId;
    }

    public float[] getMatrix() {
        return matrix;
    }

    public void setMatrix(float[] matrix) {
        this.matrix = matrix;
    }
}
