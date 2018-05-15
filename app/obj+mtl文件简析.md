#以pikachu.obj和pikachu.mtl文件为例
.obj中：

+ mtllib pikachu.mtl　//表明使用对应的.mtl材质文件
+ usemtl pikagen      //使用纹理，表明使用pikagen对应的纹理


.mtl文件中：
```
    newmtl eye
        Ns 0
        d 1
        illum 2
        Kd 0.8 0.8 0.8
        Ks 0.0 0.0 0.0
        Ka 0.2 0.2 0.2
        map_Kd eye1.png
```
