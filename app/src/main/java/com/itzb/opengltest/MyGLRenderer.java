package com.itzb.opengltest;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.*;

public class MyGLRenderer implements GLSurfaceView.Renderer {


    /* private float[] tableVerticesWidthTriangles = {
             //三角形1
             0f, 0f,
             9f, 14f,
             0f, 14f,

             //三角形2
             0f, 0f,
             9f, 0f,
             9f, 14f,

             //直线
             0f, 7f,
             9f, 7f,

             //mallets
             4.5f, 2f,
             4.5f, 12f
     };*/
    private float[] tableVerticesWidthTriangles = {
            //三角形1
//            -0.5f, -0.5f,
//            0.5f, 0.5f,
//            -0.5f, 0.5f,
//
//            //三角形2
//            -0.5f, -0.5f,
//            0.5f, -0.5f,
//            0.5f, 0.5f,

//            三角形扇，带有z和w
//            0f, 0f, 0f, 1.5f, 1f, 1f, 1f,
//            -0.5f, -0.8f, 0f, 1f, 0.7f, 0.7f, 0.7f,
//            0.5f, -0.8f, 0f, 1f, 0.7f, 0.7f, 0.7f,
//            0.5f, 0.8f, 0f, 2f, 0.7f, 0.7f, 0.7f,
//            -0.5f, 0.8f, 0f, 2f, 0.7f, 0.7f, 0.7f,
//            -0.5f, -0.8f, 0f, 1f, 0.7f, 0.7f, 0.7f,
//
//            //直线
//            -0.5f, 0f, 0f, 1.5f,  1f, 0f, 0f,
//            0.5f, 0f, 0f,  1.5f, 1f, 0f, 0f,
//
//            //mallets
//            0f, -0.4f, 0f,  1.25f, 0f, 0f, 1f,
//            0f, 0.4f, 0f, 1.75f,  1f, 0f, 0f

            //三角形扇
            0f, 0f, 1f, 1f, 1f,
            -0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
            0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
            0.5f, 0.8f, 0.7f, 0.7f, 0.7f,
            -0.5f, 0.8f, 0.7f, 0.7f, 0.7f,
            -0.5f, -0.8f, 0.7f, 0.7f, 0.7f,

            //直线
            -0.5f, 0f, 1f, 0f, 0f,
            0.5f, 0f, 1f, 0f, 0f,

            //mallets
            0f, -0.4f, 0f, 0f, 1f,
            0f, 0.4f, 1f, 0f, 0f
    };

    private static final int BYTES_PER_FLOAT = 4;
    private FloatBuffer vertexData;
    private final Context mContext;
    private int program;
    //    private static final String U_COLOR = "u_Color";
//    private int uColorLocation;
    private static final String A_POSITION = "a_Position";
    private int aPositionLocation;
    private static final int POSITION_COMPONENT_COUNT = 2;
//    private static final int POSITION_COMPONENT_COUNT = 4;

    private static final String A_COLOR = "a_Color";
    private static final int COLOR_COMPONENT_COUNT = 3;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;
    private int aColorLocation;

    private static final String U_MATRIX = "u_Matrix";
    private final float[] projectionMatrix = new float[16];
    private int uMatrixLocation;

    private final float[] modelMatrix = new float[16];


    public MyGLRenderer(Context context) {
        mContext = context;
        vertexData = ByteBuffer
                .allocateDirect(tableVerticesWidthTriangles.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexData.put(tableVerticesWidthTriangles);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        String vertexShaderSource = TextResourceReader.readTextFileFromResource(mContext, R.raw.simple_vertex_shader);
        String fragmentShaderSource = TextResourceReader.readTextFileFromResource(mContext, R.raw.simple_fragment_shader);
        int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);
        int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);

        program = ShaderHelper.linkProgram(vertexShader, fragmentShader);

        ShaderHelper.validateProgram(program);//验证program

        glUseProgram(program);

//        uColorLocation = glGetUniformLocation(program, U_COLOR);//获取uniform位置
        aColorLocation = glGetAttribLocation(program, A_COLOR);//获取新属性位置
        aPositionLocation = glGetAttribLocation(program, A_POSITION);
        uMatrixLocation = glGetUniformLocation(program, U_MATRIX);

        //关联属性与顶点数据的数组
        vertexData.position(0);//从数据开头读取位置
//        glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT, false, 0, vertexData);//
        glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT, false, STRIDE, vertexData);//

        glEnableVertexAttribArray(aPositionLocation);// 使顶点数组可用

        vertexData.position(POSITION_COMPONENT_COUNT);//从第三位开始读取颜色
        glVertexAttribPointer(aColorLocation, COLOR_COMPONENT_COUNT, GL_FLOAT, false, STRIDE, vertexData);//
        glEnableVertexAttribArray(aColorLocation);// 使顶点数组可用

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);//设置视图尺寸
//        final float aspectRatio = width > height ?
//                (float) width / (float) height :
//                (float) height / (float) width;
//        if (width > height) {
//            Matrix.orthoM(projectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f);
//        } else {
//            Matrix.orthoM(projectionMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, -1f, 1f);
//        }

//        Matrix.perspectiveM(projectionMatrix, 0, 45, (float) width / (float) height, 1f, 10f);
        MatrixHelper.perspectiveM(projectionMatrix, 45f, (float) width / (float) height, 1f, 10f);

        //        利用模型矩阵平移物体
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.translateM(modelMatrix, 0, 0f, 0f, -2f);

        //绕X轴旋转桌子
        Matrix.translateM(modelMatrix, 0, 0f, 0f, -2.5f);
        Matrix.rotateM(modelMatrix, 0, -60f, 1f, 0f, 0f);

        final float[] temp = new float[16];//用来存矩阵相乘临时结果
        Matrix.multiplyMM(temp, 0, projectionMatrix, 0, modelMatrix, 0);
        System.arraycopy(temp, 0, projectionMatrix, 0, temp.length);//将计算结果拷贝回projectionMatrix



    }

    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GL_COLOR_BUFFER_BIT);
        glUniformMatrix4fv(uMatrixLocation, 1, false, projectionMatrix, 0);

//        //绘制桌子
//        glUniform4f(uColorLocation, 1.0f, 1.0f, 1.0f, 1.0f);
//        glDrawArrays(GL_TRIANGLES, 0, 6);

        //三角形扇的形式绘制桌子
//        glUniform4f(uColorLocation, 1.0f, 1.0f, 1.0f, 1.0f);
        glDrawArrays(GL_TRIANGLE_FAN, 0, 6);

        //绘制直线
//        glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
        glDrawArrays(GL_LINES, 6, 2);

        //绘制木槌1
//        glUniform4f(uColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);
        glDrawArrays(GL_POINTS, 8, 1);

        //绘制木槌2
//        glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
        glDrawArrays(GL_POINTS, 9, 1);
    }


}
