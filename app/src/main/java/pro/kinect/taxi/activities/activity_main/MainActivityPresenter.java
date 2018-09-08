package pro.kinect.taxi.activities.activity_main;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.util.List;

import io.reactivex.observers.DisposableObserver;
import pro.kinect.taxi.BuildConfig;
import pro.kinect.taxi.db.EntityAuto;
import pro.kinect.taxi.rest.RestManager;
import pro.kinect.taxi.utils.FileUtils;

import static pro.kinect.taxi.App.getContext;

public class MainActivityPresenter implements LifecycleObserver {

    private static MainActivityPresenter instance;

    private MainActivityPresenter() {
        // private constructor
    }

    public static MainActivityPresenter getInstance() {
        if (instance == null) {
            instance = new MainActivityPresenter();
        }
        return instance;
    }

    protected void updateDb(DisposableObserver<List<EntityAuto>> listObserver) {
        RestManager.getAllAutoCoordinates(listObserver);
    }

    protected void exportDB(@NonNull MainActivity activity) {
        File db = FileUtils.getDatabaseFileCopy();
        if (db != null && db.exists()) {
            String authority = BuildConfig.APPLICATION_ID;
            Uri pdfURI = FileProvider.getUriForFile(getContext(), authority, db);
            Intent share = new Intent();
            share.setAction(Intent.ACTION_SEND);
            share.setType("application/octet-stream");
            share.putExtra(Intent.EXTRA_STREAM, pdfURI);
            activity.startActivity(share);
        }
    }

    protected void importDB(@NonNull MainActivity activity) {
        FileUtils.getFileFromSystem(activity, activity.GET_FILE_REQUEST_CODE);
    }

    public void gotUriNewDbFile(Uri uri) {
        if (uri.toString().contains(".db")) {
            FileUtils.changeDatabase(uri);
        }
    }
}
