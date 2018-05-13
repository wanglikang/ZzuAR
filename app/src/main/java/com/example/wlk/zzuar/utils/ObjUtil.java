package com.example.wlk.zzuar.utils;

import android.content.Context;
import android.util.Log;

import com.example.wlk.zzuar.obj.Obj3D;
import com.example.wlk.zzuar.obj.ObjReader;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class ObjUtil {
    private ArrayList<Obj3D> objs = new ArrayList<>();

    private Context context;
    public ObjUtil(Context context){
        this.context = context;

    }
    public void LoadObjFromAssets(String objpath){
        Obj3D obj = new Obj3D();
        try {
            ObjReader.read(this.context.getAssets().open(objpath), obj);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void LoadAllObjFromAssets(){
        String assetsObjDir = "3dres";
        while(!objs.isEmpty())
            objs.remove(0);

        try {
            String[] objsname = this.context.getAssets().list(assetsObjDir);
            Log.i("3dobj", "start load all 3d obj from assress");
            for(String f:objsname){
                if(f.endsWith(".obj")) {//加载.obj文件
                    Obj3D newobj = new Obj3D();
                    ObjReader.read(
                            this.context.getAssets().open(assetsObjDir + "/" + f), newobj);
                    Log.i("3dobj", "load " + f);
                    Log.i("3dobj", "done load all 3d obj from assress");
                }
            }



        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
