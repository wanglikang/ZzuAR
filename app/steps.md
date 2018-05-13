#绘制opengl es 的基本步骤
####1、加载顶点着色器
####2、加载片元着色器
####3、准备的顶点数据，颜色数据，索引数据
####4、在继承了Render接口的类中的OnSurfaceCreated()中初始化opengl
####5、在继承了Render接口的类中的OnSurfaceChanged()中设置视图窗口
####6、在继承了Render接口的类中的OnDrawFrame()中进行一下操作
#####6.1

+ GLES20.glUseProgram(mProgram);//获取顶点着色器的vPosition成员句柄
+ mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");//启用三角形顶点的句柄
+ GLES20.glEnableVertexAttribArray(mPositionHandle);//准备三角形的坐标数据
+ GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,GLES20.GL_FLOAT, false,vertexStride, vertexBuffer);//获取片元着色器的vColor成员的句柄
+ mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");//设置绘制三角形的颜色
+ GLES20.glUniform4fv(mColorHandle, 1, color, 0);//绘制三角形
+ GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);//禁止顶点数组的句柄
+ GLES20.glDisableVertexAttribArray(mPositionHandle);
