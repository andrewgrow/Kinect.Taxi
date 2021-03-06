package pro.kinect.taxi.activities.activity_main;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.List;

import io.reactivex.observers.DisposableObserver;
import pro.kinect.taxi.App;
import pro.kinect.taxi.BuildConfig;
import pro.kinect.taxi.db.EntityAuto;
import pro.kinect.taxi.interfaces.ActivityCallback;
import pro.kinect.taxi.rest.AutoListResponse;
import pro.kinect.taxi.rest.BaseResponse;
import pro.kinect.taxi.rest.RestManager;
import pro.kinect.taxi.utils.DateUtils;
import pro.kinect.taxi.utils.FileUtils;

import static pro.kinect.taxi.App.getContext;

public class MainActivityPresenter implements LifecycleObserver {

    private static final String TAG = MainActivityPresenter.class.getSimpleName();

    private static MainActivityPresenter instance;
    private DisposableObserver<AutoListResponse> listObserver;
    private DisposableObserver<List<EntityAuto>> searchAutoObserver;
    private WeakReference<ActivityCallback> weakCallbackReference;
    private TextWatcher mTextWatcher;

    private MainActivityPresenter() {
        // private constructor
    }

    @NonNull
    public static MainActivityPresenter getInstance(ActivityCallback callback) {
        if (instance == null) {
            instance = new MainActivityPresenter();
            instance.makeNewObservers();
        }
        if (callback != null) {
            instance.weakCallbackReference = new WeakReference<>(callback);
        }
        return instance;
    }

    @Nullable
    private static ActivityCallback getCallback() {
        if (getInstance(null).weakCallbackReference == null) {
            return null;
        }
        return getInstance(null).weakCallbackReference.get();
    }

    @Nullable
    private static MainActivity getActivity() {
        if (getCallback() == null) {
            return null;
        }
        return (MainActivity) getCallback().getActivity();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private void onResume() {
        EntityAuto.getCachedAutoResponse(BaseResponse.LOCAL_RESPONSE, listObserver);
    }

    private void makeNewObservers() {
        listObserver = new DisposableObserver<AutoListResponse>() {
            @Override
            public void onNext(AutoListResponse response) {
                if (response == null) {
                    return;
                }

                MainActivity activity = getActivity();
                if (activity == null) {
                    return;
                }

                String lastUpdate =
                        "Последнее удачное обновление было: " + DateUtils.getDateAsString(
                        App.getInstance().getAppPrefs().getLastTimeGettingAuto(),
                        DateUtils.DATE_FORMAT_STANDART) + " ";

                String success = response.isSuccess() ?
                        "Обновление БД выполнено успешно. " : response.getStatus() == BaseResponse.FAILURE ?
                        "Связаться с сервером не удалось. " + lastUpdate :
                        " Локальный список из БД. " + lastUpdate;

                String listData = "";
                if (response.getAutoList() != null) {
                    listData = "Сейчас в БД " + response.getAutoList().size() + " машин";
                }

                activity.setTopInfoText(success + listData);

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };

        searchAutoObserver = new DisposableObserver<List<EntityAuto>>() {
            @Override
            public void onNext(List<EntityAuto> autoList) {
                MainActivity activity = getActivity();
                if (activity == null || autoList == null) {
                    return;
                }
                activity.searchResult(autoList);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
    }

    protected void updateDb() {
        if (getActivity() != null) {
            getActivity().setTopInfoText("Обновляем данные...");
        }
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
        FileUtils.getFileFromSystem(activity, MainActivity.GET_FILE_REQUEST_CODE);
    }

    public void gotUriNewDbFile(Uri uri) {
        if (uri.toString().contains(".db")) {
            FileUtils.changeDatabase(uri);
        }
    }

    public TextWatcher getSearchTextWatcher() {
        if (mTextWatcher == null) {
            mTextWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String result = editable.toString().toLowerCase();
                    Log.d(TAG, "editText = " + result);
                    EntityAuto.searchAuto(result, searchAutoObserver);
                }
            };
        }
        return mTextWatcher;
    }
}
