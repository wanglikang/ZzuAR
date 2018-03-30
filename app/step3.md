## 绘制模式--顶点法
####GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, shapePos.length/3);
####参数如下：
int GL_POINTS       //将传入的顶点坐标作为单独的点绘制
int GL_LINES        //将传入的坐标作为单独线条绘制，ABCDEFG六个顶点，绘制AB、CD、EF三条线
int GL_LINE_STRIP   //将传入的顶点作为折线绘制，ABCD四个顶点，绘制AB、BC、CD三条线
int GL_LINE_LOOP    //将传入的顶点作为闭合折线绘制，ABCD四个顶点，绘制AB、BC、CD、DA四条线。
int GL_TRIANGLES    //将传入的顶点作为单独的三角形绘制，ABCDEF绘制ABC,DEF两个三角形
int GL_TRIANGLE_FAN    //将传入的顶点作为扇面绘制，ABCDEF绘制ABC、ACD、ADE、AEF四个三角形
int GL_TRIANGLE_STRIP   //将传入的顶点作为三角条带绘制，ABCDEF绘制ABC,BCD,CDE,DEF四个三角形

##绘制模式--索引法