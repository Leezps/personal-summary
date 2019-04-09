package com.leezp.sophix;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Enumeration;

import dalvik.system.DexFile;

public class PatchManager {
    private File file;
    private Context context;

    public PatchManager(File file, Context context) {
        this.file = file;
        this.context = context;
    }

    public void loadPatch() {
        File optFile = new File(context.getFilesDir(), this.file.getName());
        if (optFile.exists()) {
            optFile.delete();
        }
        try {
            final DexFile dexFile = DexFile.loadDex(file.getAbsolutePath(), optFile.getAbsolutePath(), Context.MODE_PRIVATE);
            ClassLoader classLoader = new ClassLoader() {
                @Override
                protected Class<?> findClass(String name) throws ClassNotFoundException {
                    Class clazz = dexFile.loadClass(name, this);
                    if (clazz == null) {
                        clazz = Class.forName(name);
                    }
                    return clazz;
                }
            };
            Enumeration<String> entry = dexFile.entries();
            while (entry.hasMoreElements()) {
                String key = entry.nextElement();
                Log.i("Leezp", "---------找到类" + key);
                Class realClazz = dexFile.loadClass(key, classLoader);
                if (realClazz != null) {
                    findMethod(realClazz);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void findMethod(Class realClazz) {
        Method[] methods = realClazz.getMethods();
        for (Method rightMethod : methods) {
            Replace methodReplace = rightMethod.getAnnotation(Replace.class);
            if (methodReplace == null) {
                continue;
            }
            //rightMethod --> ArtMethod对象
            //wrongMethod --> ArtMethod对象
            Log.i("Leezp", "找到替换的方法 " + methodReplace.toString() + " clazz对象 " + realClazz.toString());
            String wrongClazz = methodReplace.clazz();
            String methodName = methodReplace.method();
            try {
                Class<?> wrongClass = Class.forName(wrongClazz);
                Method wrongMethod = wrongClass.getDeclaredMethod(methodName, rightMethod.getParameterTypes());
                replace(wrongMethod, rightMethod);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static native void replace(Method wrongMethod, Method rightMethod);
}
