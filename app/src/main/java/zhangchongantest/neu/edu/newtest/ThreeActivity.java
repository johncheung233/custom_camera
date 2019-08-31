package zhangchongantest.neu.edu.newtest;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Author: Cheung SzeOn
 * Date: 2019/8/3 21:29
 * Description: test
 * History:
 */
public class ThreeActivity extends AppCompatActivity {
    @BindView(R.id.button_customCamera)
    Button buttonCustomCamera;
    private String TAG = "test";
    int i = 0;
    private int TAKE_PICTURE = 10086;
    private int REQUEST_PERMISSION = 10010;
    private Boolean isGranted = false;
    private CustomPopupWindow customPopupWindow;
    private Uri imageUri;
    private ImageView ivPicture;
    private Bitmap decodeBitmap;
    private Bitmap reslutBitmap;

    private int CODE_TAKE_CUSTOM_PHOT0 = 10011;

    private int CODE_DISPLAY_PHOT0 = 10012;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three);
        ButterKnife.bind(this);
        CustomProgressBar progressBar = (CustomProgressBar) findViewById(R.id.customProgressBar);
        Button buttonAdd = (Button) findViewById(R.id.button_add);
        ivPicture = (ImageView) findViewById(R.id.imageView_picture);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (i<=100){
//                    i = i+10;
//                    progressBar.updateValue(i);
//                }
                //showPopUpwindow();
                Log.d("TAG", "isGranted:" + isGranted);
                if (!isGranted) {
                    return;
                }
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                imageUri = ToolUtils.getOutPutMediaFileUri(ThreeActivity.this);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    cameraIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }
                startActivityForResult(cameraIntent, TAKE_PICTURE);
            }
        });
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
            ||checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(ThreeActivity.this
                    , Manifest.permission.CAMERA)) {
                ActivityCompat.requestPermissions(ThreeActivity.this,
                        new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
            } else {
                ActivityCompat.requestPermissions(ThreeActivity.this,
                        new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
            }
        } else {
            isGranted = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION) {
            if (permissions.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                isGranted = true;
            }
        }

    }

    private void showPopUpwindow() {
        if (customPopupWindow == null) {
            customPopupWindow = new CustomPopupWindow(this);
            customPopupWindow.setLisnter(new CustomPopupWindow.ImageViewClickLisnter() {
                @Override
                public void onClick() {

                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PICTURE) {
            Log.d("TAG:", "resultCode:" + resultCode);
            if (resultCode == RESULT_OK) {
                handler.post(runnable);
            }
        }
        if (requestCode == CODE_TAKE_CUSTOM_PHOT0){
            if (resultCode == RESULT_OK){
//                handler.post(getPhoto);
                startActivityForResult(new Intent(ThreeActivity.this,
                        PictureDisplayActivity.class),CODE_DISPLAY_PHOT0);
            }
        }
        if (requestCode == CODE_DISPLAY_PHOT0){
            if (resultCode == RESULT_OK){

            }else if (resultCode ==RESULT_CANCELED){
                Toast.makeText(this,"文件打开失败",Toast.LENGTH_LONG);
            }
        }
    }

    Runnable getPhoto = new Runnable(){
        @Override
        public void run() {
            File mediaStorageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    return;
                }
            }
            File mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "Pictures/temp.jpg");
            try {
                FileInputStream fos = new FileInputStream(mediaFile);
                reslutBitmap = BitmapFactory.decodeStream(fos);
                handler.sendEmptyMessage(2);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

        /**
     * bitmap decode and encode
     */
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Bitmap resultBitmap = ToolUtils.getBitmapFromUri(ThreeActivity.this, imageUri);
            if (resultBitmap == null) {
                Log.d("bitmap", "resultBitmap==null");
                return;
            }
            byte[] imgByte = ToolUtils.bitmap2Bytes(resultBitmap);
            if (imgByte.length == 0) {
                Log.d("bitmap", "bitmap2Bytes==null");
                return;
            }
            String encodeSte = Base64.encodeToString(imgByte, Base64.DEFAULT);
            if (TextUtils.isDigitsOnly(encodeSte)) {
                Log.d("bitmap", "encodeSte==null");
                return;
            }
            Log.d("bitmap", "encodeSte:" + encodeSte);
            byte[] decodeBytes = Base64.decode(encodeSte, Base64.DEFAULT);
            if (decodeBytes.length == 0) {
                Log.d("bitmap", "decodeBytes==null");
                return;
            }
            decodeBitmap = ToolUtils.bytes2Bitmap(decodeBytes);
            handler.sendEmptyMessage(1);
        }
    };

    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    ivPicture.setImageBitmap(decodeBitmap);
                    break;
                case 2:
                    ivPicture.setImageBitmap(reslutBitmap);
                    break;
            }
        }
    };

    @OnClick(R.id.button_customCamera)
    public void onViewClicked() {
        startActivityForResult(new Intent(this,CustomCameraActivity.class),CODE_TAKE_CUSTOM_PHOT0);
    }


}
