package com.example.wlk.zzuar.obj;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.SparseArray;

import com.example.wlk.zzuar.utils.Gl2Utils;
import com.example.wlk.zzuar.utils.MatrixUtils;
import com.example.wlk.zzuar.utils.ObjUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class GLGod {
    private static final String TAG ="GLGod" ;
    private static Context context;
    private static GLGod THEGod=null;
    public static boolean DEBUG=true;
    /**
     * 单位矩阵
     */
    public static final float[] OM= MatrixUtils.getOriginalMatrix();
    /**
     * 程序句柄
     */
    protected int mProgram;
    /**
     * 顶点坐标句柄
     */
    protected int mHPosition;
    /**
     * 纹理坐标句柄
     */
    protected int mHCoord;
    /**
     * 总变换矩阵句柄
     */
    protected int mHMatrixHandler;
    /**
     * 默认纹理贴图句柄
     */
    protected int mHTexture;

    protected Resources mRes;


    /**
     * 顶点坐标Buffer
     */
    protected FloatBuffer mVerBuffer;

    /**
     * 纹理坐标Buffer
     */
    protected FloatBuffer mTexBuffer;

    /**
     * 索引坐标Buffer
     */
    protected ShortBuffer mindexBuffer;

    protected int mFlag=0;

    //private float[] matrix= Arrays.copyOf(OM,16);
    private float[] cameraMatrix =MatrixUtils.getOriginalMatrix();
    private float[] projMatrix = MatrixUtils.getOriginalMatrix();
    private float[] finalMatrix ;

    private int textureType=0;      //默认使用Texture2D0

    //顶点坐标
    private float pos[] = {
            -1.0f,  1.0f,
            -1.0f, -1.0f,
            1.0f, 1.0f,
            1.0f,  -1.0f,
    };
    //纹理坐标
    private float[] coord={
            0.0f, 0.0f,
            0.0f,  1.0f,
            1.0f,  0.0f,
            1.0f, 1.0f,
    };


    private SparseArray<boolean[]> mBools;
    private SparseArray<int[]> mInts;
    private SparseArray<float[]> mFloats;
    private int mHNormal;


    private Map<String,VisibObj> visibobjs  = new HashMap<>();
    public Context getContext() {
        return context;
    }

    public float[] getCameraMatrix() {
        return cameraMatrix;
    }

    public void setCameraMatrix(float[] cameraMatrix) {
        this.cameraMatrix = cameraMatrix;
    }

    public float[] getProjMatrix() {
        return projMatrix;
    }

    public void setProjMatrix(float[] projMatrix) {
        this.projMatrix = projMatrix;
    }

    private GLGod(Context context){
        this.context = context;
        this.mRes = context.getResources();
        /**
         * 将初始化分开进行
         */
       // initGod();

    }

    public void initGod(){
        initBuffer();
        createProgramByAssetsFile("3dres/obj.vert","3dres/obj.frag");
        mHNormal= GLES20.glGetAttribLocation(mProgram,"vNormal");
        //打开深度检测
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);


    }
    public synchronized static GLGod getTHEGod(Context mcontext){
        if(THEGod==null){
            THEGod = new GLGod(mcontext);
        }
        return THEGod;
    }

    public final void createProgramByAssetsFile(String vertex,String fragment){
        createProgram(ObjUtil.uRes(mRes,vertex),ObjUtil.uRes(mRes,fragment));
    }


    public final void createProgram(String vertex,String fragment){
        mProgram= ObjUtil.uCreateGlProgram(vertex,fragment);
        mHPosition= GLES20.glGetAttribLocation(mProgram, "vPosition");
        mHCoord=GLES20.glGetAttribLocation(mProgram,"vCoord");
        mHMatrixHandler=GLES20.glGetUniformLocation(mProgram,"vMatrix");
        mHTexture=GLES20.glGetUniformLocation(mProgram,"vTexture");
    }

    /**
     * Buffer初始化
     */
    protected void initBuffer(){
        ByteBuffer a=ByteBuffer.allocateDirect(32);
        a.order(ByteOrder.nativeOrder());
        mVerBuffer=a.asFloatBuffer();
        mVerBuffer.put(pos);
        mVerBuffer.position(0);
        ByteBuffer b=ByteBuffer.allocateDirect(32);
        b.order(ByteOrder.nativeOrder());
        mTexBuffer=b.asFloatBuffer();
        mTexBuffer.put(coord);
        mTexBuffer.position(0);
    }

    private int createTexture(Bitmap bitmap){
        int[] texture=new int[1];
        if(bitmap!=null&&!bitmap.isRecycled()){
            //生成纹理
            GLES20.glGenTextures(1,texture,0);
            //生成纹理
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,texture[0]);
            //设置缩小过滤为使用纹理中坐标最接近的一个像素的颜色作为需要绘制的像素颜色
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST);
            //设置放大过滤为使用纹理中坐标最接近的若干个颜色，通过加权平均算法得到需要绘制的像素颜色
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);
            //设置环绕方向S，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_CLAMP_TO_EDGE);
            //设置环绕方向T，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
            GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_CLAMP_TO_EDGE);
            //根据以上指定的参数，生成一个2D纹理
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
            return texture[0];
        }
        return 0;
    }

    protected void onUseProgram(){
        GLES20.glUseProgram(mProgram);
    }
    /**
     * 设置其他扩展数据
     */
    protected void onSetExpandData(VisibObj visibobj){
        //GLES20.glUniformMatrix4fv(mHMatrixHandler,1,false,matrix,0);
        GLES20.glUniformMatrix4fv(mHMatrixHandler,1,false,
                getFinalMatrix(visibobj.getMatrix()),0);
    }
    /**
     * 绑定默认纹理
     */
    protected void onBindTexture(VisibObj obj){
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0+textureType);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,obj.getTextureId());
        GLES20.glUniform1i(mHTexture,textureType);
    }


    public void drawObj(VisibObj visibobj){
        Obj3D obj = visibobj.getBindObj();
        onUseProgram();
        onSetExpandData(visibobj);
        onBindTexture(visibobj);
        onDraw(obj);
    }
    public float[] getFinalMatrix(float[] matrix){
        finalMatrix = new float[16];
        Matrix.multiplyMM(finalMatrix, 0, cameraMatrix,0 , matrix, 0);
        Matrix.multiplyMM(finalMatrix, 0, projMatrix,0 , finalMatrix, 0);

        return finalMatrix;
    }

    public void onDraw(Obj3D obj){
        GLES20.glEnableVertexAttribArray(mHPosition);
        GLES20.glVertexAttribPointer(mHPosition,3, GLES20.GL_FLOAT, false, 3*4,obj.vert);
        GLES20.glEnableVertexAttribArray(mHNormal);
        GLES20.glVertexAttribPointer(mHNormal,3, GLES20.GL_FLOAT, false, 3*4,obj.vertNorl);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0,obj.vertCount);
        GLES20.glDisableVertexAttribArray(mHPosition);
        GLES20.glDisableVertexAttribArray(mHNormal);
    }

    public void clearView(){
        GLES20.glClearColor(0f,0f,0f,0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
    }

    public void onSizeChanged(int width, int height) {
        GLES20.glViewport(0,0,width,height);

        Iterator<Map.Entry<String, VisibObj>> iter = getVisibobjs().entrySet().iterator();
        while(iter.hasNext()){
            float[] matrix = Gl2Utils.getOriginalMatrix();
            Matrix.scaleM(matrix, 0, 0.8f, 0.8f * width / height, 0.8f);
            iter.next().getValue().setMatrix(matrix);
        }

    }
    public void addVisibObj(String name,VisibObj obj){
        visibobjs.put(name, obj);
    }

    public void setVisibobjs(Map<String, VisibObj> visibobjs) {
        this.visibobjs = visibobjs;
    }

    public Map<String, VisibObj> getVisibobjs() {
        return visibobjs;
    }
}
