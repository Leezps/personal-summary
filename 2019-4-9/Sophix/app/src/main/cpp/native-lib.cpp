#include <jni.h>
#include <string>
#include "art_method.h"

extern "C" JNIEXPORT jstring

JNICALL
Java_com_leezp_sophix_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C" JNIEXPORT void

JNICALL
Java_com_leezp_sophix_PatchManager_replace(
        JNIEnv *env,
        jclass type,
        jobject wrongMethod,
        jobject rightMethod) {
    art::mirror::ArtMethod *wrong = reinterpret_cast<art::mirror::ArtMethod *>(env->FromReflectedMethod(wrongMethod));
    art::mirror::ArtMethod *right = reinterpret_cast<art::mirror::ArtMethod *>(env->FromReflectedMethod(rightMethod));

    wrong->declaring_class_ = right->declaring_class_;
    wrong->access_flags_ = right->access_flags_ | 0x0001;
    wrong->dex_cache_resolved_types_ = right->dex_cache_resolved_types_;
    wrong->dex_cache_resolved_methods_ = right->dex_cache_resolved_methods_;
    wrong->dex_cache_strings_ = right->dex_cache_strings_;
    wrong->dex_code_item_offset_ = right->dex_code_item_offset_;
    wrong->dex_method_index_ = right->dex_method_index_;
    wrong->gc_map_ = right->gc_map_;
    wrong->entry_point_from_jni_ = right->entry_point_from_jni_;
    wrong->entry_point_from_quick_compiled_code_ = right->entry_point_from_quick_compiled_code_;
    wrong->entry_point_from_interpreter_ = right->entry_point_from_interpreter_;
    wrong->method_index_ = right->method_index_;
}
