#include <jni.h>
#include <string>

extern "C" {
int b_main(int argc, const char *argv[]);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_leezp_incrementupdate_1bsdiff_1demo_MainActivity_bsPatch(JNIEnv *env, jobject instance,
                                                                  jstring oldApk_, jstring patch_,
                                                                  jstring output_) {
    // 将 java 的字符串转为 C/C++ 的字符串，转换为 UTF-8 格式的 char 指针
    const char *oldApk = env->GetStringUTFChars(oldApk_, 0);
    const char *patch = env->GetStringUTFChars(patch_, 0);
    const char *output = env->GetStringUTFChars(output_, 0);

    // 1行代码
    // if(argc != 4) error
    //bspatch oldfile newfile patchfile
    const char *argv[] = {"", oldApk, output, patch};
    b_main(4, argv);

    // 释放指针（java 将变量/常量释放）
    // release 之前已经调用了 close
    env->ReleaseStringUTFChars(oldApk_, oldApk);
    env->ReleaseStringUTFChars(patch_, patch);
    env->ReleaseStringUTFChars(output_, output);
}