package pro.kinect.taxi.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.util.List;

import io.reactivex.observers.DisposableObserver;
import pro.kinect.taxi.BuildConfig;
import pro.kinect.taxi.R;
import pro.kinect.taxi.db.EntityAuto;
import pro.kinect.taxi.rest.RestManager;
import pro.kinect.taxi.utils.FileUtils;
import pro.kinect.taxi.utils.PermissionUtils;

import static pro.kinect.taxi.App.getContext;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int GET_FILE_REQUEST_CODE = 1;

    private DisposableObserver<List<EntityAuto>> listObserver;
    private int attemptPermissionRequest = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnGetData = findViewById(R.id.btnGetData);
        btnGetData.setOnClickListener(clickedView -> {
            RestManager.getAllAutoCoordinates(listObserver);
        });

        Button btnExportDb = findViewById(R.id.btnExportDb);
        btnExportDb.setOnClickListener(exportDbListener());

        Button btnImportDb = findViewById(R.id.btnImportDb);
        btnImportDb.setOnClickListener(importDbListener());
    }

    @Override
    protected void onResume() {
        super.onResume();

        listObserver = new DisposableObserver<List<EntityAuto>>() {
            @Override
            public void onNext(List<EntityAuto> list) {
                Log.d(TAG, "всего получено: " + list.size() + " машин");
//                for (EntityAuto entityAuto : list) {
//                    Log.d(TAG, entityAuto.toString());
//                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };

        if (PermissionUtils.needPermissions()) {
            PermissionRequestActivity.start(MainActivity.this);
            return;
        }
    }

    @Override
    protected void onPause() {
        listObserver.dispose();
        listObserver = null;
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent resultData) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case GET_FILE_REQUEST_CODE : {
                Uri uri = null;
                if (resultData != null) {
                    uri = resultData.getData();
                    Log.i(TAG, "Uri: " + uri.toString());
                    if (uri.toString().contains(".db")) {
                        FileUtils.changeDatabase(uri);
                    }
                }
                break;
            }
            default: break;
        }
    }

    private View.OnClickListener exportDbListener() {
        return view -> {
            File db = FileUtils.getDatabaseFileCopy();
            if (db != null && db.exists()) {
                String authority = BuildConfig.APPLICATION_ID;
                Uri pdfURI = FileProvider.getUriForFile(getContext(), authority, db);
                Intent share = new Intent();
                share.setAction(Intent.ACTION_SEND);
                share.setType("application/octet-stream");
                share.putExtra(Intent.EXTRA_STREAM, pdfURI);
                startActivity(share);
            }
        };
    }

    private View.OnClickListener importDbListener() {
        return view -> {
            FileUtils.getFileFromSystem(MainActivity.this, GET_FILE_REQUEST_CODE);
        };
    }
}
