package com.itzb.opengltest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private GLSurfaceView glSurfaceView;
    private ActivityManager am;
    private boolean rendererSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        glSurfaceView = new GLSurfaceView(this);

        am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        boolean supportES2 = am.getDeviceConfigurationInfo().reqGlEsVersion >= 2000;
        if (!supportES2) {
            Toast.makeText(this, "This device does not support ES 2.0"
                    , Toast.LENGTH_SHORT).show();
            return;

        }
        glSurfaceView.setEGLContextClientVersion(2);
        rendererSet = true;
        setContentView(glSurfaceView);

        glSurfaceView.setRenderer(new MyGLRenderer(this));
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (rendererSet) {
            glSurfaceView.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (rendererSet) {
            glSurfaceView.onResume();
        }

    }
}
