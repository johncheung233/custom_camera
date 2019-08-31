package zhangchongantest.neu.edu.newtest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ToolUtils {
    public static Bitmap getBitmapFromUri(Context context, Uri uri) {
        InputStream ips;
        try {
            ips = context.getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.RGB_565;
        BitmapFactory.decodeStream(ips, null, onlyBoundsOptions);
        try {
            ips.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int originalWidth = onlyBoundsOptions.outWidth;
        int originHight = onlyBoundsOptions.outHeight;
        if (originalWidth == -1 || originHight == -1) {
            return null;
        }
        float hh = 800f;
        float ww = 400f;
        int be = 1;
        if (originalWidth > originHight && originalWidth > ww) {
            be = (int) (originalWidth / ww);
        } else if (originalWidth < originHight && originHight > hh) {
            be = (int) (originHight / hh);
        }
        if (be <= 0) {
            be = 1;
        }
        BitmapFactory.Options bitmapOption = new BitmapFactory.Options();
        bitmapOption.inSampleSize = be;
        bitmapOption.inDither = true;
        bitmapOption.inPreferredConfig = Bitmap.Config.RGB_565;
        try {
            ips = context.getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        Bitmap bitmap = BitmapFactory.decodeStream(ips, null, bitmapOption);
        try {
            ips.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return compressImage(bitmap);
    }

    public static Bitmap compressImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int option = 100;
        while (baos.toByteArray().length / 1024 > 100) {
            baos.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, option, baos);
            option -= 10;
            if (option<=0){
                break;
            }
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        Bitmap resultBitmap = BitmapFactory.decodeStream(bais,null,null);
        return resultBitmap;
    }

    public static Uri getOutPutMediaFileUri(Context context) {
        File mediaFile = null;
        String cameraPath;
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "Pictures/temp.jpg");
        cameraPath = mediaFile.getPath();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri contentUri = FileProvider.getUriForFile(context,
                    context.getApplicationContext().getPackageName() + ".provider",
                    mediaFile);
            return contentUri;
        } else {
            return Uri.fromFile(mediaFile);
        }
    }

    public static byte[] bitmap2Bytes(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] datas = baos.toByteArray();
        return datas;
    }

    public static Bitmap bytes2Bitmap(byte[] bytes){
        if (bytes.length==0){
            return null;
        }
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return bitmap;
    }
}
