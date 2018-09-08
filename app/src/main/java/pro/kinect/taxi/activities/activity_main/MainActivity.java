package pro.kinect.taxi.activities.activity_main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.List;

import io.reactivex.observers.DisposableObserver;
import pro.kinect.taxi.R;
import pro.kinect.taxi.activities.activity_permission_request.PermissionRequestActivity;
import pro.kinect.taxi.db.EntityAuto;
import pro.kinect.taxi.interfaces.ActivityCallback;
import pro.kinect.taxi.utils.PermissionUtils;

public class MainActivity extends AppCompatActivity implements ActivityCallback {

    private static final String TAG = MainActivity.class.getSimpleName();
    protected static final int GET_FILE_REQUEST_CODE = 1;

    private MainActivityPresenter presenter;
    private TextView tvTopInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        presenter = MainActivityPresenter.getInstance(MainActivity.this);
        getLifecycle().addObserver(presenter);

        initUI();
    }

    private void initUI() {
        tvTopInfo = findViewById(R.id.tvTopInfo);
    }

    @Override
    public AppCompatActivity getActivity() {
        return this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (PermissionUtils.needPermissions()) {
            PermissionRequestActivity.start(MainActivity.this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent resultData) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case GET_FILE_REQUEST_CODE : {
                if (resultData != null && resultData.getData() != null) {
                    presenter.gotUriNewDbFile(resultData.getData());
                }
                break;
            }
            default: break;
        }
    }



    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (menu instanceof MenuBuilder) {
            ((MenuBuilder) menu).setOptionalIconsVisible(true);
        }
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (R.id.menu_update == id) {
            presenter.updateDb();
            return true;
        } else if (R.id.menu_export == id) {
            presenter.exportDB(MainActivity.this);
            return true;
        } else if (R.id.menu_import == id) {
            presenter.importDB(MainActivity.this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setTopInfoText(String text) {
        tvTopInfo.setText(text);
    }
}
