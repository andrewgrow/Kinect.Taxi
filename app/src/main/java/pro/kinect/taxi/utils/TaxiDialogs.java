package pro.kinect.taxi.utils;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import pro.kinect.taxi.R;

public class TaxiDialogs {

    public interface OnOk {
        void ok();
    }

    /**
     * Simple alert dialog with one button "OK"
     */
    public static class OkDialog extends DialogFragment {
        protected AppCompatActivity activity;
        protected String tag;
        protected String title;
        protected String message;
        protected OnOk onOk;

        static OkDialog create(AppCompatActivity activity, String tag, String title, String message, OnOk onOk) {
            OkDialog alertError = new OkDialog();

            alertError.activity = activity;
            alertError.tag = tag;
            alertError.title = title;
            alertError.message = message;
            alertError.onOk = onOk;

            return alertError;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setCancelable(false);
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (onOk != null) {
                        onOk.ok();
                    }
                }
            });

            builder.setTitle(title);
            builder.setMessage(message);

            AlertDialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);

            return dialog;
        }

        @Override
        public void onPause() {
            super.onPause();
            dismiss();
        }
    }

    public static DialogFragment showErrorAlert(AppCompatActivity activity, String tag, String title,
                                                String message, OnOk onOk) {
        if (tag == null || activity == null) {
            return null;
        }
        String messageText = TextUtils.isEmpty(message) ? "Unhandled message: showErrorAlert" : message;
        String titleText = TextUtils.isEmpty(title) ? activity.getString(R.string.alert) : title;
        OkDialog dialog = OkDialog.create(activity, tag, titleText, messageText, onOk);
        dialog.show(activity.getSupportFragmentManager(), tag);
        return dialog;
    }

}
