package pro.kinect.taxi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import pro.kinect.taxi.R;
import pro.kinect.taxi.utils.PermissionUtils;

public class PermissionRequestActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION_CODE = 0;

    public static void start(@NonNull AppCompatActivity activity) {
        Intent intent = new Intent(activity, PermissionRequestActivity.class);
        activity.startActivityForResult(intent, REQUEST_PERMISSION_CODE);
    }

    private String denyPermission;
    private boolean isAlreadyStarted;
    private PermissionUtils.PermissionRequestObject permissionRequest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isAlreadyStarted) {
            startRequestPermission();
        } else if (denyPermission != null) {
            PermissionUtils.onPermissionDenied(PermissionRequestActivity.this, denyPermission,
                    new PermissionUtils.ICallback() {
                @Override
                public void call() {
                    isAlreadyStarted = false;
                }
            });
        } else {
            if (PermissionUtils.checkPermissions(PermissionRequestActivity.this, PermissionUtils.permissions)) {
                onPermissionGranted();
            } else {
                PermissionRequestActivity.start(PermissionRequestActivity.this);
            }
        }
    }


    private void startRequestPermission() {
        isAlreadyStarted = true;
        permissionRequest = PermissionUtils.with()
                .request(PermissionUtils.permissions)
                .onAnyDenied(this::onPermissionDenied)
                .onAllGranted(this::onPermissionGranted)
                .ask(this);
    }

    protected void onPermissionGranted() {
        setResult(AppCompatActivity.RESULT_OK);
        finish();
    }

    protected void onPermissionDenied(String denyPermission) {
        PermissionRequestActivity.this.denyPermission = denyPermission;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (permissionRequest != null) {
            permissionRequest.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


}
