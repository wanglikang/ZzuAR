##相关矩阵
##1 相机矩阵 MatricCamera
###1.1 Matrix.setLookAtM函数可以设置相机矩阵
##2 投影方法
###2.1 正交投影
####使用正交投影，物体呈现出来的大小不会随着其距离视点的远近而发生变化。在Android OpenGLES程序中，我们可以使用以下方法来设置正交投影：
Matrix.orthoM (float[] m,           //接收正交投影的变换矩阵
                int mOffset,        //变换矩阵的起始位置（偏移量）
                float left,         //相对观察点近面的左边距
                float right,        //相对观察点近面的右边距
                float bottom,       //相对观察点近面的下边距
                float top,          //相对观察点近面的上边距
                float near,         //相对观察点近面距离
                float far)          //相对观察点远面距离

###2.2 透视投影
####使用透视投影，物体离视点越远，呈现出来的越小。离视点越近，呈现出来的越大。。在Android OpenGLES程序中，我们可以使用以下方法来设置透视投影：
Matrix.frustumM (float[] m,         //接收透视投影的变换矩阵
                int mOffset,        //变换矩阵的起始位置（偏移量）
                float left,         //相对观察点近面的左边距
                float right,        //相对观察点近面的右边距
                float bottom,       //相对观察点近面的下边距
                float top,          //相对观察点近面的上边距
                float near,         //相对观察点近面距离
                float far)          //相对观察点远面距离
##3 变换矩阵
Matrix.multiplyMM (float[] result, //接收相乘结果
                int resultOffset,  //接收矩阵的起始位置（偏移量）
                float[] lhs,       //左矩阵
                int lhsOffset,     //左矩阵的起始位置（偏移量）
                float[] rhs,       //右矩阵
                int rhsOffset)     //右矩阵的起始位置（偏移量）
