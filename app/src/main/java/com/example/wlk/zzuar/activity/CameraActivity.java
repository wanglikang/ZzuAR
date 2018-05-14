/*
 * Copyright 2016 The TensorFlow Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.wlk.zzuar.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.Image.Plane;
import android.media.ImageReader;
import android.media.ImageReader.OnImageAvailableListener;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Trace;
import android.util.Log;
import android.util.Size;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.wlk.zzuar.R;
import com.example.wlk.zzuar.obj.GLGod;
import com.example.wlk.zzuar.obj.ObjFilter;
import com.example.wlk.zzuar.obj.VisibObj;
import com.example.wlk.zzuar.tf.CameraConnectionFragment;
import com.example.wlk.zzuar.tf.LegacyCameraConnectionFragment;
import com.example.wlk.zzuar.tf.OverlayView;
import com.example.wlk.zzuar.tf.util.ImageUtils;
import com.example.wlk.zzuar.tf.util.Logger;
import com.example.wlk.zzuar.tracking.mGLSurfaceView;
import com.example.wlk.zzuar.utils.Gl2Utils;


import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Map;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public abstract class CameraActivity extends Activity
        implements OnImageAvailableListener, Camera.PreviewCallback {
    private static final Logger LOGGER = new Logger();

    private static final int PERMISSIONS_REQUEST = 1;

    private static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    private static final String PERMISSION_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;

    private boolean debug = false;

    private Handler handler;
    private HandlerThread handlerThread;
    private boolean useCamera2API;
    private boolean isProcessingFrame = false;
    private byte[][] yuvBytes = new byte[3][];
    private int[] rgbBytes = null;
    private int yRowStride;

    protected int previewWidth = 0;
    protected int previewHeight = 0;

    private Runnable postInferenceCallback;
    private Runnable imageConverter;
    public mGLSurfaceView mGLView;

//    private ObjViewDistributor distributor;
    private GLGod god;
    private VisibObj vobj1;
    private VisibObj vobj2;

//    private Obj3D obj;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        LOGGER.d("onCreate " + this);
        super.onCreate(null);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_camera);
        mGLView = (mGLSurfaceView) findViewById(R.id.glsurface);

        mGLView.setEGLContextClientVersion(2);
        mGLView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        mGLView.getHolder().setFormat(PixelFormat.TRANSLUCENT);

        mGLView.setZOrderOnTop(true);

        god = GLGod.getTHEGod(this);

//        distributor = new ObjViewDistributor(this);
//
//        ObjViewDistributor distributor  = new ObjViewDistributor(this);
//        mGLView.setDistributor(distributor);
//
//        obj = new Obj3D();
//        try {
//            ObjReader.read(getAssets().open("3dres/hat.obj"), obj);
//            mFilter.setObj3D(obj);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        mGLView.setRenderer(new GLSurfaceView.Renderer() {
            public float height;
            public float width;
            public float ratio;
            public float[] lookAtMatrix = {
                    0f, 10f, 0f,
                    0f, 0f, 0f,
                    0f, 0f, 1f
            };
            public float[] objCameraMatrix=new float[16];
            public float[] objProjMatrix=new float[16];
            public float[] objMatrix=new float[16];


            @Override
            public void onSurfaceCreated(GL10 gl, EGLConfig config) {
                god.initGod();
                GLES20.glClearColor(0f, 0f, 0f, 0f);

                vobj1 = new VisibObj("hat").bindGod(god);
                vobj2 = new VisibObj("hat2").bindGod(god);
                vobj1.setObjNameAndReadObj("hat");
                vobj2.setObjNameAndReadObj("hat");
                Log.i("learning", "mFilter.create()");
                // GLES20.glEnable(GLES20.GL_DEPTH_TEST);
                //gl.glClearColor(0, 0, 0, 0);

                    Matrix.setLookAtM(god.getCameraMatrix(), 0,
                            lookAtMatrix[0], lookAtMatrix[1], lookAtMatrix[2],
                            lookAtMatrix[3], lookAtMatrix[4], lookAtMatrix[5],
                            lookAtMatrix[6], lookAtMatrix[7], lookAtMatrix[8]);

/////////////////////////////////////////////////////////////////////////////////////////////
//                Matrix.setLookAtM(mFilter.getCameraMatrix(), 0,
//                            lookAtMatrix[0],lookAtMatrix[1],lookAtMatrix[2],
//                            lookAtMatrix[3],lookAtMatrix[4],lookAtMatrix[5],
//                            lookAtMatrix[6],lookAtMatrix[7],lookAtMatrix[8]);
////
//                        0, 10, 0,
//                        0f, 0f, 0f,
//                        0, 0, 1);
            }

            @Override
            public void onSurfaceChanged(GL10 gl, int width, int height) {
                this.width = (float) (width * 1.0);
                this.height = (float) (height * 1.0);
                Log.i("glview", "onSurfaceChanged:" + this.width + ":" + this.height);
                this.ratio = this.width / this.height;


                Matrix.frustumM(god.getProjMatrix(),
                            0,
                            -this.ratio , this.ratio,
                            -1 , 1 ,
                            1, 1000);
///////////////////////////////////////////////////////////////////////
//              Matrix.orthoM(mFilter.getProjMatrix(),0 ,
//                      -this.ratio, this.ratio,
//                      -1f, 1f,
//                      1f, 1000f);

                Log.i("info", "this.width:"+this.width+";this.height:"+this.height+";this.ratio:"+this.ratio);
                reSurfaceChanged(width, height);
            }

            @Override
            public void onDrawFrame(GL10 gl) {
                java.util.Queue<mGLSurfaceView.ObjInfo> objlists = mGLView.getObjList();
                Log.i("learning", "objlists.size():" + objlists.size());

//                mFilter.pushMatrix();
//                mFilter.draw();
//                mFilter.popMatrix();
                god.clearView();
//                VisibObj obj = god.getVisibobjs().get("hat");
//                obj.pushMatrix();
//                //Matrix.translateM(obj.getMatrix(), 0, translateX, 0f, translateZ);
//                Matrix.scaleM(obj.getMatrix(), 0, 0.4f, 0.4f, 0.4f);
//                obj.requestDraw();
//                obj.popMatrix();

                if (objlists != null && objlists.size() > 0) {
                    float midx;
                    float midz;
                    float boxwidth;
                    float boxheight;
                    float dx;
                    float dz;
//                    ObjFilter currFilter;
                    VisibObj obj;
                    for (mGLSurfaceView.ObjInfo p : objlists) {
                        RectF f = p.location;
                        int timestmp = p.lifetime;
                        if (timestmp > 1) {
                            Log.i("learning", f.toString() + ":" + timestmp);
                            p.lifetime--;

                            mGLView.addObjList(p);
                        } else continue;


                        Log.i("glview", f.toString());
                        midx = f.left + (f.right - f.left) / 2;
                        midz = f.top + (f.bottom - f.top) / 2;

                        boxwidth = f.right - f.left;
                        boxheight = f.bottom - f.top;

                        Log.i("info", "mid:" + midx + ":" + midz);
                        Log.i("info", "box:" + boxwidth + ":" + boxheight);

                        dx = midx - this.width / 2;
                        dz = midz - this.height / 2;
                        Log.i("info", "dd:" + dx + ":" + dz);
                        float y = lookAtMatrix[1];

                        float translateX = dx / this.width * y / 1.0f*2 ;
                        float translateZ = dz / this.height * y / 1.0f*2;
                        if (dx > 0) {
                            translateX = 0 - translateX;
                        }
                        if (dz < 0) {
                            translateZ = 0 - translateZ;
                        }
                        Log.i("info", "translateX:" + translateX + ";" + "translateZ:" + translateZ);

                        obj = god.getVisibobjs().get("hat");
                        obj.pushMatrix();
                        Matrix.translateM(obj.getMatrix(), 0, translateX, 0f, translateZ);
                        Matrix.scaleM(obj.getMatrix(), 0, 0.4f, 0.4f * ratio, 0.4f);
                        obj.requestDraw();
                        obj.popMatrix();
//                        currFilter = mGLView.getDistributor().getMatchClassName("hat");
//                        currFilter.pushMatrix();
//
////                        Matrix.translateM(mFilter.getMatrix(), 0, translateX, 0f, translateZ);
//                        Matrix.translateM(currFilter.getMatrix(), 0, translateX, 0f, translateZ);
//                        Matrix.scaleM(currFilter.getMatrix(), 0, 0.4f, 0.4f * ratio, 0.4f);
//                        currFilter.draw();
//                        currFilter.popMatrix();
                    }
                }
            }
        });

        if (hasPermission()) {
            setFragment();
        } else {
            requestPermission();
        }
        mGLView.bringToFront();

    }

    public void reSurfaceChanged(int width, int height) {
        god.onSizeChanged(width,height);
//
//        Map<String,ObjFilter> filter = mGLView.getDistributor().getFilterMap();
//        for (Map.Entry<String,ObjFilter> i :filter.entrySet()) {
//            i.getValue().onSizeChanged(width, height);
//            float[] matrix = Gl2Utils.getOriginalMatrix();
//            Matrix.scaleM(matrix, 0, 0.8f, 0.8f * width / height, 0.8f);
//            i.getValue().setMatrix(matrix);
//        }

    }

    private byte[] lastPreviewFrame;

    protected int[] getRgbBytes() {
        imageConverter.run();
        return rgbBytes;
    }

    protected int getLuminanceStride() {
        return yRowStride;
    }

    protected byte[] getLuminance() {
        return yuvBytes[0];
    }

    /**
     * Callback for android.hardware.Camera API
     *
     * @param bytes
     * @param camera
     */
    @Override
    public void onPreviewFrame(final byte[] bytes, final Camera camera) {
        Log.i("learning", "in OnPreviewFrame()");
        if (isProcessingFrame) {
            LOGGER.w("Dropping frame!");
            return;
        }

        try {
            // Initialize the storage bitmaps once when the resolution is known.
            if (rgbBytes == null) {
                Camera.Size previewSize = camera.getParameters().getPreviewSize();
                Log.i("learing", previewSize.height + ":" + previewSize.width);
                previewHeight = previewSize.height;
                previewWidth = previewSize.width;
                rgbBytes = new int[previewWidth * previewHeight];
                onPreviewSizeChosen(new Size(previewSize.width, previewSize.height), 90);
            }
        } catch (final Exception e) {
            LOGGER.e(e, "Exception!");
            return;
        }

        isProcessingFrame = true;
        lastPreviewFrame = bytes;
        yuvBytes[0] = bytes;
        yRowStride = previewWidth;

        imageConverter =
                new Runnable() {
                    @Override
                    public void run() {
                        ImageUtils.convertYUV420SPToARGB8888(bytes, previewWidth, previewHeight, rgbBytes);
                    }
                };

        postInferenceCallback =
                new Runnable() {
                    @Override
                    public void run() {
                        camera.addCallbackBuffer(bytes);
                        isProcessingFrame = false;
                    }
                };
        processImage();
    }

    /**
     * Callback for Camera2 API
     *
     * @param reader 渲染器
     */
    @Override
    public void onImageAvailable(final ImageReader reader) {
        Log.i("learning", "in onImageAvailable()");
        //We need wait until we have some size from onPreviewSizeChosen
        if (previewWidth == 0 || previewHeight == 0) {
            return;
        }
        if (rgbBytes == null) {
            rgbBytes = new int[previewWidth * previewHeight];
        }
        try {
            final Image image = reader.acquireLatestImage();

            if (image == null) {
                return;
            }

            if (isProcessingFrame) {
                image.close();
                return;
            }
            isProcessingFrame = true;
            Trace.beginSection("imageAvailable");
            final Plane[] planes = image.getPlanes();
            fillBytes(planes, yuvBytes);
            yRowStride = planes[0].getRowStride();
            final int uvRowStride = planes[1].getRowStride();
            final int uvPixelStride = planes[1].getPixelStride();

            imageConverter =
                    new Runnable() {
                        @Override
                        public void run() {
                            ImageUtils.convertYUV420ToARGB8888(
                                    yuvBytes[0],
                                    yuvBytes[1],
                                    yuvBytes[2],
                                    previewWidth,
                                    previewHeight,
                                    yRowStride,
                                    uvRowStride,
                                    uvPixelStride,
                                    rgbBytes);
                        }
                    };

            postInferenceCallback =
                    new Runnable() {
                        @Override
                        public void run() {
                            image.close();
                            isProcessingFrame = false;
                        }
                    };

            processImage();
        } catch (final Exception e) {
            LOGGER.e(e, "Exception!");
            Trace.endSection();
            return;
        }
        Trace.endSection();
    }

    @Override
    public synchronized void onStart() {
        LOGGER.d("onStart " + this);
        super.onStart();
    }

    @Override
    public synchronized void onResume() {
        LOGGER.d("onResume " + this);
        super.onResume();

        handlerThread = new HandlerThread("inference");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
    }

    @Override
    public synchronized void onPause() {
        LOGGER.d("onPause " + this);

        if (!isFinishing()) {
            LOGGER.d("Requesting finish");
            finish();
        }

        handlerThread.quitSafely();
        try {
            handlerThread.join();
            handlerThread = null;
            handler = null;
        } catch (final InterruptedException e) {
            LOGGER.e(e, "Exception!");
        }

        super.onPause();
    }

    @Override
    public synchronized void onStop() {
        LOGGER.d("onStop " + this);
        super.onStop();
    }

    @Override
    public synchronized void onDestroy() {
        LOGGER.d("onDestroy " + this);
        super.onDestroy();
    }

    protected synchronized void runInBackground(final Runnable r) {
        if (handler != null) {
            handler.post(r);
        }
    }

    @Override
    public void onRequestPermissionsResult(
            final int requestCode, final String[] permissions, final int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                setFragment();
            } else {
                requestPermission();
            }
        }
    }

    private boolean hasPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(PERMISSION_CAMERA) == PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(PERMISSION_STORAGE) == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (shouldShowRequestPermissionRationale(PERMISSION_CAMERA) ||
                    shouldShowRequestPermissionRationale(PERMISSION_STORAGE)) {
                Toast.makeText(CameraActivity.this,
                        "Camera AND storage permission are required for this demo", Toast.LENGTH_LONG).show();
            }
            requestPermissions(new String[]{PERMISSION_CAMERA, PERMISSION_STORAGE}, PERMISSIONS_REQUEST);
        }
    }

    // Returns true if the device supports the required hardware level, or better.
    private boolean isHardwareLevelSupported(
            CameraCharacteristics characteristics, int requiredLevel) {
        int deviceLevel = characteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL);
        if (deviceLevel == CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY) {
            return requiredLevel == deviceLevel;
        }
        // deviceLevel is not LEGACY, can use numerical sort
        return requiredLevel <= deviceLevel;
    }

    private String chooseCamera() {
        final CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            for (final String cameraId : manager.getCameraIdList()) {
                final CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);

                // We don't use a front facing camera in this sample.
                final Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (facing != null && facing == CameraCharacteristics.LENS_FACING_FRONT) {
                    continue;
                }

                final StreamConfigurationMap map =
                        characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

                if (map == null) {
                    continue;
                }

                // Fallback to camera1 API for internal cameras that don't have full support.
                // This should help with legacy situations where using the camera2 API causes
                // distorted or otherwise broken previews.
                useCamera2API = (facing == CameraCharacteristics.LENS_FACING_EXTERNAL)
                        || isHardwareLevelSupported(characteristics,
                        CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_FULL);
                LOGGER.i("Camera API lv2?: %s", useCamera2API);
                Log.i("learning", "isuse camera2api:" + useCamera2API);

                return cameraId;
            }
        } catch (CameraAccessException e) {
            LOGGER.e(e, "Not allowed to access camera");
        }

        return null;
    }

    protected void setFragment() {
        String cameraId = chooseCamera();
        if (cameraId == null) {
            Toast.makeText(this, "No Camera Detected", Toast.LENGTH_SHORT).show();
            finish();
        }

        Fragment fragment;
        if (useCamera2API) {
            CameraConnectionFragment camera2Fragment =
                    CameraConnectionFragment.newInstance(
                            new CameraConnectionFragment.ConnectionCallback() {
                                @Override
                                public void onPreviewSizeChosen(final Size size, final int rotation) {
                                    previewHeight = size.getHeight();
                                    previewWidth = size.getWidth();
                                    CameraActivity.this.onPreviewSizeChosen(size, rotation);
                                }
                            },
                            this,
                            getLayoutId(),
                            getDesiredPreviewFrameSize());

            camera2Fragment.setCamera(cameraId);
            fragment = camera2Fragment;
        } else {
            fragment =
                    new LegacyCameraConnectionFragment(this, getLayoutId(), getDesiredPreviewFrameSize());
        }

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    protected void fillBytes(final Plane[] planes, final byte[][] yuvBytes) {
        // Because of the variable row stride it's not possible to know in
        // advance the actual necessary dimensions of the yuv planes.
        for (int i = 0; i < planes.length; ++i) {
            final ByteBuffer buffer = planes[i].getBuffer();
            if (yuvBytes[i] == null) {
                LOGGER.d("Initializing buffer %d at size %d", i, buffer.capacity());
                yuvBytes[i] = new byte[buffer.capacity()];
            }
            buffer.get(yuvBytes[i]);
        }
    }

    public boolean isDebug() {
        return debug;
    }

    public void requestRender() {
        final OverlayView overlay = (OverlayView) findViewById(R.id.debug_overlay);
        if (overlay != null) {
            overlay.postInvalidate();
        }
    }

    public void addCallback(final OverlayView.DrawCallback callback) {
        final OverlayView overlay = (OverlayView) findViewById(R.id.debug_overlay);
        if (overlay != null) {
            overlay.addCallback(callback);
        }
    }

    public void onSetDebug(final boolean debug) {
    }

    @Override
    public boolean onKeyDown(final int keyCode, final KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_VOLUME_UP
                || keyCode == KeyEvent.KEYCODE_BUTTON_L1 || keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
            debug = !debug;
            requestRender();
            onSetDebug(debug);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void readyForNextImage() {
        if (postInferenceCallback != null) {
            postInferenceCallback.run();
        }
    }

    protected int getScreenOrientation() {
        switch (getWindowManager().getDefaultDisplay().getRotation()) {
            case Surface.ROTATION_270:
                return 270;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_90:
                return 90;
            default:
                return 0;
        }
    }

    /**
     * 抽象方法，用于处理Image图像
     */
    protected abstract void processImage();

    /**
     * 用于初始化设置预览窗口大小
     *
     * @param size
     * @param rotation
     */
    protected abstract void onPreviewSizeChosen(final Size size, final int rotation);

    /**
     * 抽象方法，用于获取布局资源ID
     *
     * @return
     */
    protected abstract int getLayoutId();

    protected abstract Size getDesiredPreviewFrameSize();
}
