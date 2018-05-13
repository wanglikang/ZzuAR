package com.example.wlk.zzuar.utils;

import android.content.Context;
import android.util.Log;

import com.example.wlk.zzuar.obj.Obj3D;
import com.example.wlk.zzuar.obj.ObjFilter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ObjViewDistributor {
    private Context context;
    private Map<String,ObjFilter> filterMap ;
//    public ObjViewDistributor(Context context){
//        ObjFilter mFilter = new ObjFilter(context.getResources());
////        ObjUtil objUtil = new ObjUtil(context);
////        objUtil.LoadAllObjFromAssets();
//        filterMap = new HashMap<>();
//        for(Map.Entry<String, Obj3D> i :objUtil.getObj3DMap().entrySet()){
//            ObjFilter objf = new ObjFilter(context.getResources());
//            objf.setObj3D(i.getValue());
//            objf.create();
//            filterMap.put(i.getKey(), objf);
//        }
//    }

    public ObjFilter getMatchClassName(String classname){
        if(filterMap!=null){
            if(filterMap.size()>0){
                return filterMap.get(classname);
            }
        }
        Log.i("3dobj", "no one was match with:"+classname);
        return  null;
    }

    public Map<String, ObjFilter> getFilterMap() {
        return filterMap;
    }
}
