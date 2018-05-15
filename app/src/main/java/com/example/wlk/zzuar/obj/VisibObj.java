package com.example.wlk.zzuar.obj;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.util.Log;

import com.example.wlk.zzuar.utils.MatrixUtils;
import com.example.wlk.zzuar.utils.ObjUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class VisibObj {
    private List<Obj3D> bindObjs;
    private GLGod god ;
    public String className;
    private String modelName;
    public static Context context;
    private float[] matrix= MatrixUtils.getOriginalMatrix();
    private int textureId=0;
    /**
     * 此栈用于保存位置矩阵使用
     */
    private Stack<float[]> matrixStack;

    public VisibObj(String className){
        this.className = className;
        matrixStack = new Stack<float[]>();
    }
    public VisibObj bindGod(GLGod god){
        this.god = god;
        this.context = god.getContext();
        this.god.addVisibObj(className,this);
        return this;
    }

    public String getClassName() {
        return className;
    }

    public void setObjNameAndReadObj(String modelName){
        this.modelName = modelName;
        //ObjReader.read(context.getAssets().open("3dres/hat.obj"),bindObj);
        //    ObjReader.read(context.getAssets().open("3dres/"+modelName+".obj"),bindObj);
        bindObjs = ObjReader.readMultiObj(context,"assets/3dres/"+modelName+".obj");
        if(bindObjs.size()==0){
            try {
                ObjReader.readObj(context.getAssets().open("3dres/"+modelName+".obj"),bindObjs);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //ObjReader.readMultiObj(context,"assets/3dres/pikachu.obj");
        Log.i("3dobj", "load .obj from "+modelName);

//        if(bindObj.vertTexture!=null){
//            try {
//                Log.i("3dobj", "start to load Texture for class:"+className+":with modelName:"+modelName);
//                textureId= ObjUtil.createTexture(BitmapFactory.decodeStream(ObjUtil.mRes.getAssets().open("3dres/"+bindObj.mtl.map_Kd)));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
    }

    public void requestDraw(){
        god.drawObj(this);
    }

    public void drawSelf(){
        requestDraw();
    }

    public List<Obj3D> getBindObjs() {
        return bindObjs;
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


    public float[] popMatrix(){
        float[] peekele = matrixStack.peek();
        for(int i = 0;i<16;i++){
            this.matrix[i] = peekele[i];
        }
        return matrixStack.pop();
    }
    public void pushMatrix(){
        float[] pushEle = Arrays.copyOf(this.matrix, 16);
        matrixStack.push(pushEle);
    }
}
