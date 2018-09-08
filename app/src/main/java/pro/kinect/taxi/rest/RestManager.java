package pro.kinect.taxi.rest;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.Observer;

import io.reactivex.Emitter;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import pro.kinect.taxi.App;
import pro.kinect.taxi.db.EntityAuto;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestManager {

    private static final String TAG = RestManager.class.getSimpleName();

//    private static PublishSubject<List<EntityAuto>> subjectAutoModel = PublishSubject.create();
//
//    public static PublishSubject<List<EntityAuto>> getSubjectAutoModel() {
//        return subjectAutoModel;
//    }

    private static RestApi instance;

    public static RestApi getApi() {
        if (instance == null) {
            Gson gson = new GsonBuilder()
//                    .serializeNulls()
                    .create();

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            // set your desired log level
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);


            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(logging);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://webcab.404.kr.ua/mobile3/")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(httpClient.build())
                    .build();

            instance = retrofit.create(RestApi.class);
        }
        return instance;
    }

    public static void getAllAutoCoordinates(DisposableObserver<AutoListResponse> activityObserver) {
        AutoListResponse autoListResponse;

        Call<List<EntityAuto>> call = getApi().getAllAutoCoordinates("GetCoordinatesAllAuto");
        call.enqueue(new Callback<List<EntityAuto>>() {
            @Override
            public void onResponse(@NonNull Call<List<EntityAuto>> call,
                                   @NonNull Response<List<EntityAuto>> response) {
                List<EntityAuto> list = response.body();
                if (!response.isSuccessful() || response.body() == null) {
                    Log.i(TAG, "response in not successful! code =" + response.code());
                    returnAllAutoFromDB(BaseResponse.FAILURE, activityObserver);
                    return;
                }

                saveNewDataAndReturnList(list, activityObserver);
            }

            @Override
            public void onFailure(@NonNull Call<List<EntityAuto>> call, @NonNull Throwable t) {
                Log.i(TAG, t.getMessage() == null ?
                        "something went wrong (like no internet connection)" : t.getMessage());
                returnAllAutoFromDB(BaseResponse.FAILURE, activityObserver);
            }
        });
    }

    private static void saveNewDataAndReturnList(List<EntityAuto> autoList,
                                                 DisposableObserver<AutoListResponse> activityObserver) {
        // prepare emitter
        Emitter<Boolean> emitter = new Emitter<Boolean>() {
            @Override
            public void onNext(Boolean value) {
                // nothing for now
            }

            @Override
            public void onError(Throwable error) {
                returnAllAutoFromDB(BaseResponse.FAILURE, activityObserver);
            }

            @Override
            public void onComplete() {
                returnAllAutoFromDB(BaseResponse.SUCCESS, activityObserver);
            }
        };
        // save and wait...
        EntityAuto.saveNewAuto(autoList, emitter);
    }

    @SuppressLint("CheckResult")
    private static void returnAllAutoFromDB(@BaseResponse.Status int responseStatus,
                                            DisposableObserver<AutoListResponse> activityObserver) {
        Log.d(TAG, "returnAllAutoFromDB -> "
                + "status = " + (BaseResponse.isSuccess(responseStatus) ?
                "INTERNET SUCCESS" : "INTERNET FAILURE"));
        Observable.fromCallable(() -> App.getDatabase().daoAuto().getAllAuto())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                    if (activityObserver != null) {
                        AutoListResponse response = new AutoListResponse(responseStatus, list);
                        activityObserver.onNext(response);
                    }
                });
    }
}
