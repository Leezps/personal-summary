package com.zulong.identifycodeimage.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZLPermissionUtils {
    private static final String TAG = "ZLPermissionUtils";
    private static final int REQUEST_PERMISSION = 10001;
    private static final int REQUEST_PERMISSION_SETTINGS = 10002;
    public static final String PROMPT_TITLE = "prompt_title";
    public static final String PROMPT_CONTENT = "prompt_content";
    public static final String PROMPT_POSITIVE_TEXT = "prompt_positive_text";
    public static final String QUERY_TITLE = "query_title";
    public static final String QUERY_CONTENT = "query_content";
    public static final String QUERY_NEGATIVE_TEXT = "query_negative_text";
    public static final String QUERY_POSITIVE_TEXT = "query_positive_text";
    public static final String DENY_TITLE = "deny_title";
    public static final String DENY_CONTENT = "deny_content";
    public static final String DENY_NEGATIVE_TEXT = "deny_negative_text";
    public static final String DENY_POSITIVE_TEXT = "deny_positive_text";


    private static WeakReference<Activity> activityWeakReference = null;
    private static PermissionsListener permissionsListener = null;
    private static Map<String, String> permissionsParams = null;
    private static List<String> settingsPermissionList = new ArrayList<>();

    public static void setActivityWeakReference(Activity activity) {
        if (activity != null) {
            activityWeakReference = new WeakReference<>(activity);
        }
    }

    public static void setPermissionsListener(PermissionsListener listener) {
        if (listener != null) {
            permissionsListener = listener;
        }
    }

    public static void setPermissionsParams(HashMap<String, String> params) {
        if (params != null) {
            permissionsParams = params;
        }
    }

    public static void beginRequestPermission(final String[] permissions) {
        if (activityWeakReference == null) {
            Log.e(TAG, "Activity 弱引用为空，请传入正确的实例对象");
            return;
        }
        Activity activity = activityWeakReference.get();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(permissionsParams.get(PROMPT_TITLE));
        builder.setMessage(permissionsParams.get(PROMPT_CONTENT));
        builder.setCancelable(true);
        builder.setPositiveButton(permissionsParams.get(PROMPT_POSITIVE_TEXT), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    requestPermission(permissions);
                }
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private static void requestPermission(String[] permissions) {
        if (activityWeakReference == null || permissionsListener == null) {
            Log.e(TAG, "Activity 弱引用或权限监听事件为空，请传入正确的实例对象");
            return;
        }
        Activity activity = activityWeakReference.get();
        List<String> permissionLists = new ArrayList<>();
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionLists.add(permission);
            }
        }
        if (permissionLists.size() > 0) {
            ActivityCompat.requestPermissions(activity, permissionLists.toArray(new String[permissionLists.size()]), REQUEST_PERMISSION);
        } else {
            permissionsListener.allPermissionsGranted();
        }
    }

    public static void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (activityWeakReference == null || permissionsListener == null) {
            Log.e(TAG, "Activity 弱引用或权限监听事件为空，请传入正确的实例对象");
            return;
        }
        final Activity activity = activityWeakReference.get();
        if (activity == null) {
            return;
        }
        final List<String> deniedPermissions = new ArrayList<>();
        if (requestCode != REQUEST_PERMISSION) {
            return;
        }
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                deniedPermissions.add(permissions[i]);
            }
        }
        if (deniedPermissions.size() > 0) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, deniedPermissions.get(0))) {
                DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            requestPermission(deniedPermissions.toArray(new String[deniedPermissions.size()]));
                        } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                            permissionsListener.somePermissionsDenied(deniedPermissions);
                        }
                        dialog.dismiss();
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle(permissionsParams.get(DENY_TITLE));
                builder.setMessage(permissionsParams.get(DENY_CONTENT));
                builder.setCancelable(true);
                builder.setPositiveButton(permissionsParams.get(DENY_POSITIVE_TEXT), dialogListener);
                builder.setNegativeButton(permissionsParams.get(DENY_NEGATIVE_TEXT), dialogListener);
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            if (settingsPermissionList != null && settingsPermissionList.size() > 0) {
                                settingsPermissionList.clear();
                            } else if (settingsPermissionList == null) {
                                settingsPermissionList = new ArrayList<>();
                            }
                            settingsPermissionList.addAll(deniedPermissions);
                            //引导用户到设置中去进行设置
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.setData(Uri.fromParts("package", activity.getPackageName(), null));
                            activity.startActivityForResult(intent, REQUEST_PERMISSION_SETTINGS);
                        } else if (which == DialogInterface.BUTTON_NEGATIVE) {
                            permissionsListener.somePermissionsDenied(deniedPermissions);
                        }
                        dialog.dismiss();
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle(permissionsParams.get(QUERY_TITLE));
                builder.setMessage(permissionsParams.get(QUERY_CONTENT));
                builder.setCancelable(true);
                builder.setPositiveButton(permissionsParams.get(QUERY_POSITIVE_TEXT), dialogListener);
                builder.setNegativeButton(permissionsParams.get(QUERY_NEGATIVE_TEXT), dialogListener);
                AlertDialog queryDialog = builder.create();
                queryDialog.show();
            }
        } else {
            permissionsListener.allPermissionsGranted();
        }
    }

    public static void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != REQUEST_PERMISSION_SETTINGS) {
            return;
        }
        if (activityWeakReference == null || permissionsListener == null) {
            Log.e(TAG, "Activity 弱引用或权限监听事件为空，请传入正确的实例对象");
            return;
        }
        Activity activity = activityWeakReference.get();
        List<String> deniedPermissions = new ArrayList<>();
        for (String permission : settingsPermissionList) {
            if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                deniedPermissions.add(permission);
            }
        }
        if (deniedPermissions.size() > 0) {
            permissionsListener.somePermissionsDenied(deniedPermissions);
        } else {
            permissionsListener.allPermissionsGranted();
        }
    }

    public interface PermissionsListener {
        void allPermissionsGranted();

        void somePermissionsDenied(List<String> deniedPermissions);
    }
}
