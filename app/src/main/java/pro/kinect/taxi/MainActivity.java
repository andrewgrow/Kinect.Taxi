package pro.kinect.taxi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import pro.kinect.taxi.rest.RestAutoModel;
import pro.kinect.taxi.rest.RestManager;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private DisposableObserver<List<RestAutoModel>> listObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listObserver = new DisposableObserver<List<RestAutoModel>>() {
            @Override
            public void onNext(List<RestAutoModel> list) {
                Log.d(TAG, "всего: " + list.size() + " машин");
                for (RestAutoModel autoModel : list) {
                    Log.d(TAG, autoModel.toString());
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };

        Button btnGetData = findViewById(R.id.btnGetData);
        btnGetData.setOnClickListener(clickedView -> {
            RestManager.getAllAutoCoordinates();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        RestManager
                .getSubjectAutoModel()
                .onSubscribe(listObserver);
    }

    @Override
    protected void onPause() {
        listObserver.dispose();
        super.onPause();
    }
}
