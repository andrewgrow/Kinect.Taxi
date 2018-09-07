package pro.kinect.taxi.utils;

import android.content.Intent;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

import pro.kinect.taxi.App;

public class FileUtils {

    @Nullable
    public static File getDatabaseFileCopy() {
        try {
            String currentDBPath = App.getContext().getDatabasePath(App.dbName).getAbsolutePath();
            String copyDBPath = File.separator
                    + "copy_" + DateUtils.getCurrentDateAsString(DateUtils.FILE_DATE_FORMAT) + "_"
                    + App.dbName;

            File currentDB = new File(currentDBPath);
            File copyDB = new File(App.getContext().getCacheDir(), copyDBPath);

            FileChannel src = new FileInputStream(currentDB).getChannel();
            FileChannel dst = new FileOutputStream(copyDB).getChannel();
            dst.transferFrom(src, 0, src.size());
            src.close();
            dst.close();

            return copyDB;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("FileUtils", "error when copy database -> " + e.getMessage());
            return null;
        }
    }

    public static void changeDatabase(@NonNull Uri uri) {
        try {
            String currentDBPath = App.getContext().getDatabasePath(App.dbName).getAbsolutePath();
            File currentDB = new File(currentDBPath);

            InputStream inputStream = App.getContext().getContentResolver().openInputStream(uri);
            OutputStream outputStream = new FileOutputStream(currentDB);

            int BUFFER_SIZE = 2 * 1024 * 1024;
            try {
                byte[] buffer = new byte[BUFFER_SIZE];
                assert inputStream != null;
                int bytesRead = inputStream.read(buffer);
                while (bytesRead != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                    bytesRead = inputStream.read(buffer);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                outputStream.close();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getFileFromSystem(AppCompatActivity activity, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");
        activity.startActivityForResult(intent, requestCode);
    }
}
