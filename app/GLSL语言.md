#着色器语言基础
##GLSL虽然是基于C/C++的语言，但是它和C/C++还是有很大的不同的，比如在GLSL中没有double、long等类型，没有union、enum、unsigned以及位运算等特性。
##GLSL中的数据类型主要分为
##七种类型：
###标量 只有bool、int和float三种
###向量 列向量
####共有vec2、vec3、vec4，ivec2、ivec3、ivec4、bvec2、bvec3和bvec4九种类型
####i表示int类型、b表示bool类型 
###矩阵
####mat2、mat3、mat4；2*2/3*3/4*4维矩阵
###采样器
####采样器是专门用来对纹理进行采样工作的，在GLSL中一般来说，一个采样器变量表示一副或者一套纹理贴图。所谓的纹理贴图可以理解为我们看到的物体上的皮肤。
###结构体
#### 用struct来定义结构体，与C语言相同
###数组
####与C语言相同
###空  void
##类型不可以自动提升
##低精度的int不能转换为低精度的float
##不可以强制转换
#限定符
##attritude：一般用于各个顶点各不相同的量。如顶点颜色、坐标等。
##uniform：一般用于对于3D物体中所有顶点都相同的量。比如光源位置，统一变换矩阵等。
##varying：表示易变量，一般用于顶点着色器传递到片元着色器的量。
##const：常量。
#函数
in：输入参数，无修饰符时默认为此修饰符。
out：输出参数。
inout：既可以作为输入参数，又可以作为输出参数。
#浮点精度
##必须指定浮点类型的精度，否则编译会报错
###
    lowp：低精度。8位。
    mediump：中精度。10位。
    highp：高精度。16位。
#内建变量
##输入变量
        gl_FragCoord：当前片元相对窗口位置所处的坐标。
        gl_FragFacing：bool型，表示是否为属于光栅化生成此片元的对应图元的正面。

##输出变量
        gl_FragColor：当前片元颜色
        gl_FragData：vec4类型的数组。向其写入的信息，供渲染管线的后继过程使用。
#内置函数
##常见数学函数
    radians(角度)<->degrees(弧度)
    sin,cos,tan,pow,exp
    log,sqrt,abs,sign....
##几何函数

    length(x)：计算向量x的长度
    distance(x,y)：返回向量xy之间的距离
    dot(x,y)：返回向量xy的点积
    cross(x,y)：返回向量xy的差积
    normalize(x)：返回与x向量方向相同，长度为1的向量
##矩阵函数

    matrixCompMult(x,y)：将矩阵相乘
    lessThan(x,y)：返回向量xy的各个分量执行x< y的结果，类似的有greaterThan,equal,notEqual
    lessThanEqual(x,y)：返回向量xy的各个分量执行x<= y的结果，类似的有类似的有greaterThanEqual
    any(bvec x)：x有一个元素为true，则为true
    all(bvec x)：x所有元素为true，则返回true，否则返回false
    not(bvec x)：x所有分量执行逻辑非运算
##纹理采样函数
    texture2D、texture2DProj、texture2DLod、texture2DProjLod、textureCube、textureCubeLod
    texture3D、texture3DProj、texture3DLod、texture3DProjLod等
    
    texture表示纹理采样，2D表示对2D纹理采样，3D表示对3D纹理采样
    Lod后缀，只适用于顶点着色器采样
    Proj表示纹理坐标st会除以q

