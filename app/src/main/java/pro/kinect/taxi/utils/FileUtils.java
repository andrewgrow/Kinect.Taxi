package pro.kinect.taxi.utils;

import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import pro.kinect.taxi.App;

public class FileUtils {

    @Nullable
    public static File getDatabaseFileCopy() {
        try {
            String currentDBPath = App.getContext().getDatabasePath(App.dbName).getAbsolutePath();
            String copyDBPath = File.separator + "copy_" + App.dbName;
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
}
