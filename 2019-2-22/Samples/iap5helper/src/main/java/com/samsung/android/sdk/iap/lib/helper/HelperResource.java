package com.samsung.android.sdk.iap.lib.helper;

import android.content.Context;

public class HelperResource {
    /**
     * 根据资源的名字获取其ID值
     *
     * @param context   上下文
     *
     * @param className 资源类型，如id/layout/drawable等
     *
     * @param name  资源名称
     *
     * @return  资源的ID值
     *
     */
    public static int getIdByName(Context context, String className, String name) {
        String packageName = context.getPackageName();
        int id = 0;
        try {
            Class r = Class.forName(packageName+".R");
            Class[] classes = r.getClasses();
            Class desireClass = null;

            for (int i = 0; i < classes.length; i++) {
                if (classes[i].getName().split("\\$")[1].equals(className)) {
                    desireClass = classes[i];
                    break;
                }
            }
            if (desireClass != null)
                id = desireClass.getField(name).getInt(desireClass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return id;
    }
}
