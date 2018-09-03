package pro.kinect.taxi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import java.util.List;

import io.reactivex.observers.DisposableObserver;
import pro.kinect.taxi.db.EntityAuto;
import pro.kinect.taxi.rest.RestManager;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private DisposableObserver<List<EntityAuto>> listObserver;

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
    }

    @Override
    protected void onPause() {
        listObserver.dispose();
        super.onPause();
    }
}
