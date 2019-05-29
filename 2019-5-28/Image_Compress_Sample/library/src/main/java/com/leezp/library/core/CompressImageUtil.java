package com.leezp.library.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;

import com.leezp.library.config.CompressConfig;
import com.leezp.library.listener.CompressResultListener;
import com.leezp.library.utils.Constants;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 压缩图片
 */
public class CompressImageUtil {
    private CompressConfig config;
    private Context context;

    public CompressImageUtil(CompressConfig config, Context context) {
        this.config = config == null ? CompressConfig.getDefaultConfig() : config;
        this.context = context;
    }

    public void compress(String originalPath, CompressResultListener listener) {
        if (config.isEnablePixelCompress()) {
            try {
                compressImageByPixel(originalPath, listener);
            } catch (FileNotFoundException e) {
                listener.onCompressFailed(originalPath, String.format("图片压缩失败，%s", e.toString()));
                e.printStackTrace();
            }
        } else {
            compressImageByQuality(BitmapFactory.decodeFile(originalPath), originalPath, listener);
        }
    }

    // 采样率压缩
    private void compressImageByPixel(String originalPath, CompressResultListener listener) throws FileNotFoundException {
        File file = new File(originalPath);
        if (!file.exists()) throw new FileNotFoundException("原始文件不存在");
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(originalPath, options);
        options.inJustDecodeBounds = false;
        int longest = options.outWidth > options.outHeight ? options.outWidth : options.outHeight;
        if (longest > config.getMaxPixel()) {
            int be = 1;
            while (true) {
                ++be;
                if (longest / be <= config.getMaxPixel()) {
                    options.inSampleSize = be;
                    break;
                }
            }
        } else {
            options.inSampleSize = 1;
        }
        Bitmap bitmap = BitmapFactory.decodeFile(originalPath, options);
        if (config.isEnableQualityCompress()) {
            compressImageByQuality(bitmap, originalPath, listener);
        } else {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            int result = saveBitmapFile(baos.toByteArray(), originalPath, listener);
            if (result == 0) listener.onCompressSuccess(originalPath);
        }
    }

    /**
     * 多线程执行质量压缩
     *
     * @param bitmap   图片/照片对象
     * @param listener 回调接口
     */
    private void compressImageByQuality(final Bitmap bitmap, final String filePath, final CompressResultListener listener) {
        if (config.isEnableQualityCompress()) {
            new Thread() {
                @Override
                public void run() {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
                    int options = 100;
                    while (baos.toByteArray().length > config.getMaxSize() && options >= 0) {// 循环判断如果压缩后图片是否大于100kb,大于继续压缩
                        baos.reset();// 重置baos即清空baos
                        bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options,把压缩后的数据存放到baos中
                        options -= 10;// 每次都减少10
                    }
                    int result = saveBitmapFile(baos.toByteArray(), filePath, listener);
                    if (result == 0) listener.onCompressSuccess(filePath);
                }
            }.start();
        }
    }

    /**
     * Bitmap 对象保存成图片文件
     *
     * @param bitmapBytes 位图的字节流
     */
    public int saveBitmapFile(byte[] bitmapBytes, String filePath, CompressResultListener listener) {
        String savePath = Constants.BASE_CACHE_PATH + context.getPackageName() + "/cache/" + Constants.COMPRESS_CACHE;
        File folder = new File(savePath); // 将要保存图片的路径
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File file = new File(savePath + filePath.substring(filePath.lastIndexOf("/")));
        if (file.exists()) {
            file.delete();
        }
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bos.write(bitmapBytes);
            bos.flush();
            bos.close();
        } catch (FileNotFoundException e) {
            listener.onCompressFailed(filePath, "无法创建保存的压缩文件");
            e.printStackTrace();
            return -1;
        } catch (IOException e) {
            listener.onCompressFailed(filePath, "无法向压缩文件里输入数据流");
            e.printStackTrace();
            return -2;
        }
        return 0;
    }
}
