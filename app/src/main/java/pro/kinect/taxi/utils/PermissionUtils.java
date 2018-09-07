package pro.kinect.taxi.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import pro.kinect.taxi.App;
import pro.kinect.taxi.R;

/** Helper to ask camera permission. */
public final class PermissionUtils {

    public static final String[] permissions = new String[] {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    public interface ICallback {
        void call();
    }


    public static boolean needPermissions() {
        Context context = App.getContext();

        for (String permissionName : permissions) {
            if (ContextCompat.checkSelfPermission(context, permissionName)
                    == PackageManager.PERMISSION_DENIED) {
                return true;
            }
        }

        return false;
    }

    public static void onPermissionDenied(final AppCompatActivity activity, String denyPermission,
                                          ICallback callback) {
        String humanReadableName = getHumanReadablePermissionName(denyPermission);
        String appName = activity.getString(R.string.app_name);
        String message = String.format(activity.getString(R.string.permission_not_granted_message),
                appName, humanReadableName, appName);

        DialogFragment dialogFragment = TaxiDialogs.showErrorAlert(activity, "onPermissionDenied",
                activity.getString(R.string.missing_permissions), message, new TaxiDialogs.OnOk() {
                    @Override
                    public void ok() {
                        Intent intent = new Intent()
                                .setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                .setData(Uri.fromParts("package",
                                        activity.getApplicationContext().getPackageName(),
                                        null));
                        activity.startActivity(intent);
                        if (callback != null) {
                            callback.call();
                        }
                    }
                });
        if (dialogFragment != null) {
            dialogFragment.setCancelable(false);
        }
    }

    public static String getHumanReadablePermissionName(@NonNull String permissionCode) {
        if (!permissionCode.contains("android.permission.")) {
            throw new IllegalArgumentException("Permission code must contains 'android.permission.'");
        }

        PackageManager pm = App.getContext().getPackageManager();
        PermissionInfo permissionInfo = null;
        try {
            permissionInfo = pm.getPermissionInfo(permissionCode, PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return permissionInfo == null ? permissionCode : permissionInfo.loadLabel(pm).toString();
    }

    public static PermissionObject with() {
        return new PermissionObject();
    }

    public static class SinglePermission {

        private String mPermissionName;
        private boolean mRationalNeeded = false;
        private String mReason;

        public SinglePermission(String permissionName) {
            mPermissionName = permissionName;
        }

        public SinglePermission(String permissionName, String reason) {
            mPermissionName = permissionName;
            mReason = reason;
        }

        public boolean isRationalNeeded() {
            return mRationalNeeded;
        }

        public void setRationalNeeded(boolean rationalNeeded) {
            mRationalNeeded = rationalNeeded;
        }

        public String getReason() {
            return mReason == null ? "" : mReason;
        }

        public void setReason(String reason) {
            mReason = reason;
        }

        public String getPermissionName() {
            return mPermissionName;
        }

        public void setPermissionName(String permissionName) {
            mPermissionName = permissionName;
        }
    }

    public static class PermissionObject {

        public PermissionRequestObject request(String permissionName) {
            return new PermissionRequestObject(new String[]{permissionName});
        }

        public PermissionRequestObject request(String... permissionNames) {
            return new PermissionRequestObject(permissionNames);
        }
    }

    public interface OnDenyPermission {
        void call(String denyPermission);
    }

    public interface OnGrantPermission {
        void call();
    }

    public interface OnResult {
        void call(int requestCode, String permissions[], int[] grantResults);
    }

    abstract public class OnRational {
        protected abstract void call(String permissionName);
    }

    static public class PermissionRequestObject {

        private static final String TAG = PermissionObject.class.getSimpleName();

        private ArrayList<SinglePermission> permissions;
        private int requestCode;
        private OnGrantPermission grantAllPermission;
        private OnDenyPermission denyPermission;
        private OnResult onResult;
        private OnRational onRational;
        private String[] permissionNames;

        public PermissionRequestObject(String[] permissionNames) {
            this.permissionNames = permissionNames;
        }

        public PermissionRequestObject ask(AppCompatActivity activity) {

            Random rnd = new Random(System.currentTimeMillis());
            requestCode = rnd.nextInt(Integer.MAX_VALUE) & 0x0000ffff;

            int length = this.permissionNames.length;
            permissions = new ArrayList<>(length);
            for (String mPermissionName : this.permissionNames) {
                permissions.add(new SinglePermission(mPermissionName));
            }

            if (needToAsk(activity)) {
                Log.i(TAG, "Asking for permission");
                ActivityCompat.requestPermissions(activity, this.permissionNames, requestCode);
            } else {
                Log.i(TAG, "No need to ask for permission");
                if (grantAllPermission != null) grantAllPermission.call();
            }
            return this;
        }

        private boolean needToAsk(AppCompatActivity activity) {
            ArrayList<SinglePermission> neededPermissions = new ArrayList<>(permissions);
            for (int i = 0; i < permissions.size(); i++) {
                SinglePermission perm = permissions.get(i);
                int checkRes = ContextCompat.checkSelfPermission(activity, perm.getPermissionName());
                if (checkRes == PackageManager.PERMISSION_GRANTED) {
                    neededPermissions.remove(perm);
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(activity, perm.getPermissionName())) {
                        perm.setRationalNeeded(true);
                    }
                }
            }
            permissions = neededPermissions;
            this.permissionNames = new String[permissions.size()];
            for (int i = 0; i < permissions.size(); i++) {
                this.permissionNames[i] = permissions.get(i).getPermissionName();
            }
            return permissions.size() != 0;
        }

        /**
         * Called for the first denied permission if there is need to show the rational
         */
        public PermissionRequestObject onRational(OnRational rationalFunc) {
            onRational = rationalFunc;
            return this;
        }

        /**
         * Called if all the permissions were granted
         */
        public PermissionRequestObject onAllGranted(OnGrantPermission grantOnPermission) {
            this.grantAllPermission = grantOnPermission;
            return this;
        }

        /**
         * Called if there is at least one denied permission
         */
        public PermissionRequestObject onAnyDenied(OnDenyPermission denyOnPermission) {
            this.denyPermission = denyOnPermission;
            return this;
        }

        /**
         * Called with the original operands from {@link AppCompatActivity#onRequestPermissionsResult(int, String[], int[])
         * onRequestPermissionsResult} for any result
         */
        public PermissionRequestObject onResult(OnResult resultFunc) {
            onResult = resultFunc;
            return this;
        }

        /**
         * This Method should be called from {@link AppCompatActivity#onRequestPermissionsResult(int, String[], int[])
         * onRequestPermissionsResult} with all the same incoming operands
         * <pre>
         * {@code
         *
         * public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
         *      if (mStoragePermissionRequest != null)
         *          mStoragePermissionRequest.onRequestPermissionsResult(requestCode, permissions,grantResults);
         * }
         * }
         * </pre>
         */
        public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

            if (this.requestCode == requestCode) {
                if (onResult != null) {
                    Log.i(TAG, "Calling Results function");
                    onResult.call(requestCode, permissions, grantResults);
                    return;
                }

                for (int i = 0; i < permissions.length; i++) {
                    Log.i(TAG, String.format("ReqCode: %d, ResCode: %d, PermissionName: %s", requestCode, grantResults[i], permissions[i]));

                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        if (this.permissions.get(i).isRationalNeeded()) {
                            if (onRational != null) {
                                Log.i(TAG, "Calling Rational function");
                                onRational.call(this.permissions.get(i).getPermissionName());
                            }
                        }
                        if (denyPermission != null) {
                            Log.i(TAG, "Calling Deny function");
                            denyPermission.call(this.permissions.get(i).getPermissionName());
                        } else Log.e(TAG, "NUll DENY FUNCTIONS");

                        // terminate if there is at least one deny
                        return;
                    }
                }

                // there has not been any deny
                if (grantAllPermission != null) {
                    Log.i(TAG, "Calling Grant onDenied");
                    grantAllPermission.call();
                } else Log.e(TAG, "NUll GRANT FUNCTIONS");
            }
        }
    }

    public static boolean checkPermissions(Context context, String... permissionNames) {
        for (String permissionName : permissionNames) {
            if (ContextCompat.checkSelfPermission(context, permissionName) == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }

        return true;

    }
}
