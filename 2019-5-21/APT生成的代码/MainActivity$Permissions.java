package com.leezp.note_permission_sample;

import com.leezp.library.listener.RequestPermission;
import com.leezp.library.listener.PermissionRequest;
import com.leezp.library.utils.PermissionUtils;

import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.ActivityCompat;
import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;

public class MainActivity$Permissions implements RequestPermission<com.leezp.note_permission_sample.MainActivity> {
    private static final int REQUEST_SHOWCAMERA = 666;
    private static String[] PERMISSION_SHOWCAMERA;

    public void requestPermission(com.leezp.note_permission_sample.MainActivity target, String[] permissions) {
        PERMISSION_SHOWCAMERA = permissions;
        if (PermissionUtils.hasSelfPermissions(target, PERMISSION_SHOWCAMERA)) {
            target.showCamera();
        } else if (PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA)) {
            target.showRationaleForCamera(new PermissionRequestImpl(target));
        } else {
            ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);
        }
    }

    public void onRequestPermissionsResult(com.leezp.note_permission_sample.MainActivity target, int requestCode, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_SHOWCAMERA:
                if (PermissionUtils.verifyPermissions(grantResults)) {
                    target.showCamera();
                } else if (!PermissionUtils.shouldShowRequestPermissionRationale(target, PERMISSION_SHOWCAMERA)) {
                    target.showNeverAskForCamera();
                } else {
                    target.showDeniedForCamera();
                }
                break;
            default:
                break;
        }
    }

    private static final class PermissionRequestImpl implements PermissionRequest {
        private final WeakReference<com.leezp.note_permission_sample.MainActivity> weakTarget;

        private PermissionRequestImpl(com.leezp.note_permission_sample.MainActivity target) {
            this.weakTarget = new WeakReference(target);
        }

        public void proceed() {
            com.leezp.note_permission_sample.MainActivity target = (com.leezp.note_permission_sample.MainActivity) this.weakTarget.get();
            if (target != null) {
                ActivityCompat.requestPermissions(target, PERMISSION_SHOWCAMERA, REQUEST_SHOWCAMERA);
            }
        }
    }

}