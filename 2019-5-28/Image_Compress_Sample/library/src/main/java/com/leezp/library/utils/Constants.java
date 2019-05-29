package com.leezp.library.utils;

import android.os.Environment;

public class Constants {
    // 相机
    public static final int CAMERA_CODE = 1001;
    // 相册
    public static final int ALBUM_CODE = 1002;
    // 缓存根目录
    public static final String BASE_CACHE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/";
    // 压缩后缓存路径
    public static final String COMPRESS_CACHE = "compress_cache";
}
