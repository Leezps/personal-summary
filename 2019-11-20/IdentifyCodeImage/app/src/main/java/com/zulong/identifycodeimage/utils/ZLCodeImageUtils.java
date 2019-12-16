package com.zulong.identifycodeimage.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ZLCodeImageUtils {
    private final String TAG = getClass().getSimpleName();
    private static final String ID_TAG = "zl_sdk_code_";
    private static final int DEFAULT_HEIGHT = 100;
    private static final int DEFAULT_TEXT_SIZE = 60;
    private static final int DEFAULT_BACKGROUND_COLOR = Color.rgb(0xee, 0xee, 0xee);

    private static ZLCodeImageUtils sZLCodeImageUtils;
    private int mPaddingLeft, mPaddingTop;
    private String mCode;

    public static ZLCodeImageUtils getInstance() {
        if (sZLCodeImageUtils == null) {
            synchronized (ZLCodeImageUtils.class) {
                if (sZLCodeImageUtils == null) {
                    sZLCodeImageUtils = new ZLCodeImageUtils();
                }
            }
        }
        return sZLCodeImageUtils;
    }

    public void setCode(String code) {
        mCode = code;
    }

    public Bitmap generateBitmap() {
        mPaddingLeft = 0;
        mPaddingTop = 70;
        Bitmap bitmap = Bitmap.createBitmap(50 * mCode.length(), DEFAULT_HEIGHT, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawColor(DEFAULT_BACKGROUND_COLOR);
        Paint paint = new Paint();
        paint.setColor(Color.rgb(0x00, 0x00, 0x00));
        paint.setFakeBoldText(true);
        paint.setTextSize(DEFAULT_TEXT_SIZE);
        for (int i = 0; i < mCode.length(); i++) {
            mPaddingLeft = 8 + 50 * i;
            canvas.drawText(String.valueOf(mCode.charAt(i)), mPaddingLeft, mPaddingTop, paint);
        }
        canvas.save();
        canvas.restore();
        return bitmap;
    }

    public boolean savePngImageToPictures(Context context, Bitmap bitmap) {
        File picturesFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (!picturesFile.exists() && !picturesFile.mkdirs()) {
            Log.e(TAG, "Pictures 文件夹不存在，并且未创建成功");
            return false;
        }
        File[] files = picturesFile.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                if (file.getName().startsWith(ID_TAG)) {
                    context.getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.Media.DATA + "=?",
                            new String[]{
                                    file.getAbsolutePath()
                            });
                }
            }
        }
        String fileName = ID_TAG + mCode + ".png";
        File file = new File(picturesFile, fileName);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            boolean isSuccess = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            Uri uri = Uri.fromFile(file);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            } else {
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, uri));
            }
            return isSuccess;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getIdentifyFromPictures() {
        File picturesFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (!picturesFile.exists()) {
            return null;
        }
        File[] files = picturesFile.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                String fileName = file.getName();
                if (fileName.startsWith(ID_TAG)) {
                    Log.e(TAG, "getIdentifyFromPictures: " + fileName);
                    Log.e(TAG, "getIdentifyFromPictures: " + fileName.substring(ID_TAG.length(), fileName.indexOf('.')));
                    return "Success";
                }
            }
            return null;
        } else {
            return null;
        }
    }
}
