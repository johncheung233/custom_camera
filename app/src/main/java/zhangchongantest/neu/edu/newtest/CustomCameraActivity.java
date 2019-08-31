package zhangchongantest.neu.edu.newtest;

import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CustomCameraActivity extends AppCompatActivity implements SurfaceHolder.Callback{
    @BindView(R.id.surfaceView)
    SurfaceView surfaceView;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.img_camera)
    ImageView imgCamera;
    @BindView(R.id.homecamera_bottom_relative)
    RelativeLayout homecameraBottomRelative;
    @BindView(R.id.camera_flash)
    ImageView cameraFlash;
    @BindView(R.id.camera_switch)
    ImageView cameraSwitch;
    @BindView(R.id.home_custom_top_relative)
    LinearLayout homeCustomTopRelative;

    private Camera mCamera;
    private SurfaceHolder mHolder;

    private CameraUtil cameraInstance;
    private int cameraId = 0;

    private int screenHeight;
    private int screenWidth;
    private int picWidth;
    private int picHeight;
    private boolean isView = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_camera);
        ButterKnife.bind(this);
        mHolder = surfaceView.getHolder();
        mHolder.addCallback(this);
        cameraInstance = CameraUtil.getInstance();
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        Log.d("custom","screenWidth:"+screenWidth+"   screenHeight:"+screenHeight);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCamera==null){
            mCamera = android.hardware.Camera.open(cameraId);
            if (mHolder !=null){
                startPreview(mCamera,mHolder);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }

    /**
     * 预览相机
     */
    private void startPreview(Camera camera, SurfaceHolder holder) {
        try {
            // 确认相机预览尺寸
            setupCamera(camera);
            camera.setPreviewDisplay(holder);
            cameraInstance.setCameraDisplayOrientation(this, cameraId, camera);
            camera.startPreview();
            isView = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置surfaceView的尺寸 因为camera默认是横屏，所以取得支持尺寸也都是横屏的尺寸
     * 我们在startPreview方法里面把它矫正了过来，但是这里我们设置设置surfaceView的尺寸的时候要注意 previewSize.height<previewSize.width
     * previewSize.width才是surfaceView的高度
     * 一般相机都是屏幕的宽度 这里设置为屏幕宽度 高度自适应 你也可以设置自己想要的大小
     */
    private void setupCamera(Camera camera) {
        //set up auto focus
        Camera.Parameters parameters = camera.getParameters();
        if (parameters.getSupportedFocusModes().contains(
                Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }
        //根据屏幕尺寸获取最佳 大小
        Camera.Size previewSize = cameraInstance.getPicPreviewSize(parameters.getSupportedPreviewSizes(),
                screenHeight, screenWidth);
        parameters.setPreviewSize(previewSize.width, previewSize.height);

        Camera.Size pictrueSize = cameraInstance.getPicPreviewSize(parameters.getSupportedPictureSizes(),
                screenHeight,screenWidth);
        parameters.setPictureSize(pictrueSize.width, pictrueSize.height);
        camera.setParameters(parameters);
        // picHeight = (screenWidth * pictrueSize.width) / pictrueSize.height;
        picWidth = pictrueSize.width;
        picHeight = pictrueSize.height;
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(screenWidth,
                (screenWidth * pictrueSize.width) / pictrueSize.height);
        surfaceView.setLayoutParams(params);
    }

    @OnClick({R.id.iv_back, R.id.img_camera, R.id.camera_flash, R.id.camera_switch})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                this.finish();
                break;
            case R.id.img_camera:
                takePhoto();
                break;
            case R.id.camera_flash:
                cameraInstance.turnLightOff(mCamera);
                cameraInstance.turnLightOn(mCamera);
                break;
            case R.id.camera_switch:
                switchCamera();
                break;
        }
    }

    /**
     * 切换前后摄像头
     */
    public void switchCamera() {
        releaseCamera();
        cameraId = (cameraId + 1) % Camera.getNumberOfCameras();
        mCamera = Camera.open(cameraId);
        if (mHolder != null) {
            startPreview(mCamera, mHolder);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        startPreview(mCamera,holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mCamera.stopPreview();
        startPreview(mCamera,holder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseCamera();
    }

    /**
     * 释放相机资源
     */
    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    /**
     * 拍照
     */
    private void takePhoto() {
        mCamera.takePicture(null, null, new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                isView = false;
                //将data 转换为位图 或者你也可以直接保存为文件使用 FileOutputStream
                //这里我相信大部分都有其他用处把 比如加个水印 后续再讲解
                Bitmap bitmap =ToolUtils.bytes2Bitmap(data);
                File mediaStorageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
                if (!mediaStorageDir.exists()) {
                    if (!mediaStorageDir.mkdirs()) {
                        return;
                    }
                }
                File mediaFile = new File(mediaStorageDir.getPath() + File.separator
                        + "Pictures/temp.jpg");
                try {
                    FileOutputStream fos = new FileOutputStream(mediaFile);
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);
                    fos.flush();
                    fos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });

    }
}
