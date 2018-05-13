package com.example.wlk.zzuar;

import android.opengl.Matrix;
import android.util.Log;

import java.util.Arrays;
import java.util.Stack;
import java.util.logging.Level;


public class VaryTools {

    private float[] mMatrixCamera=new float[16];    //相机矩阵
    private float[] mMatrixProjection=new float[16];    //投影矩阵
    private float[] mMatrixCurrent=     //原始矩阵
            {1,1,0,0,
            0,1,0,0,
            0,0,1,0,
            0,0,0,1};

    private Stack<float[]> mStack;      //变换矩阵堆栈

    public VaryTools(){
        mStack=new Stack<>();
    }

    //保护现场
    public void pushMatrix(){
        mStack.push(Arrays.copyOf(mMatrixCurrent,16));
    }

    //恢复现场
    public void popMatrix(){
        mMatrixCurrent=mStack.pop();
    }

    public void clearStack(){
        mStack.clear();
    }

    //平移变换
    public void translate(float x,float y,float z){
        Matrix.translateM(mMatrixCurrent,0,x,y,z);
    }

    //旋转变换
    public void rotate(float angle,float x,float y,float z){
        Matrix.rotateM(mMatrixCurrent,0,angle,x,y,z);
    }

    //缩放变换
    public void scale(float x,float y,float z){
        Matrix.scaleM(mMatrixCurrent,0,x,y,z);
    }

    //设置相机
    public void setCamera(float ex,float ey,float ez,float cx,float cy,float cz,float ux,float uy,float uz){
        Matrix.setLookAtM(mMatrixCamera,0,ex,ey,ez,cx,cy,cz,ux,uy,uz);
    }
    public void changeCamera(int index,float value){
        if(index==1||index==2||index==3)
            mMatrixCamera[index-1] = value;
        else if(index==4||index==5||index==6)
            mMatrixCamera[index] = value;
        else if(index==7||index==8||index==9)
            mMatrixCamera[index+1] = value;

//        Matrix.setLookAtM(mMatrixCamera,0,mMatrixCamera[0],mMatrixCamera[1],mMatrixCamera[2],
//                mMatrixCamera[4],mMatrixCamera[5],mMatrixCamera[6],
//                mMatrixCamera[8],mMatrixCamera[9],mMatrixCamera[10]);
        Log.i("mmatrixCamera",arr2Str(mMatrixCamera));
    }

    public String arr2Str(float[] arr){
        String result = "[";
        for (int i = 0;i<arr.length-1;i++) {
            result+="\r"+arr[i]+",";
            if(i==3 ||i==7||i==11)
                result+="\n";
        }
        result+=arr[arr.length-1]+"]";
        return result;
    }
    public void frustum(float left,float right,float bottom,float top,float near,float far){
        Matrix.frustumM(mMatrixProjection,0,left,right,bottom,top,near,far);
    }

    public void ortho(float left,float right,float bottom,float top,float near,float far){
        Matrix.orthoM(mMatrixProjection,0,left,right,bottom,top,near,far);
    }

    public float[] getFinalMatrix(){
        float[] ans=new float[16];
        Matrix.multiplyMM(ans,0,mMatrixCamera,0,mMatrixCurrent,0);
        Matrix.multiplyMM(ans,0,mMatrixProjection,0,ans,0);
        Log.i("getFinalMatrix",arr2Str(ans));
        return ans;
    }

}
