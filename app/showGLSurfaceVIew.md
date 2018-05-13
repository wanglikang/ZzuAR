原博客参见：[设置GLSurfaceView透明](https://blog.csdn.net/AndroidTalk/article/details/51440207)
##第一步：修改GLSurfaceView颜色模式
修改GLSurfaceView颜色模式为有透明度选项的模式，然后把该GLSurfaceView放在其他View之上
```
mGLSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
mGLSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
mGLSurfaceView.setZOrderOnTop(true);
```
##第二步：在Renderer里把背景设置为透明
在GLSurfaceView.Renderer的onSurfaceCreated方法里，把背景的透明度设为0：
```
gl.glClearColor(0,0,0,0);
```

##第三步：根据实际需求，设置图形的透明度值（比如1）
这一步看似不重要，其实相比前两步也确实不重要，但是只有把这一步写出来，整个逻辑才能说清楚。
```
float colors[] = new float[]{
                1,1,1,1,//最后一位是透明度
                0,0,1,1,
                0,1,0,1
        };
mColorBuffer = ByteBuffer.allocateDirect(colors.length*4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
mColorBuffer.put(colors);
mColorBuffer.position(0);
```