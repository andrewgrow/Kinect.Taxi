package pro.kinect.taxi.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import java.util.List;

import io.reactivex.observers.DisposableObserver;
import pro.kinect.taxi.R;
import pro.kinect.taxi.db.EntityAuto;
import pro.kinect.taxi.rest.RestManager;
import pro.kinect.taxi.utils.PermissionUtils;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

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
}
