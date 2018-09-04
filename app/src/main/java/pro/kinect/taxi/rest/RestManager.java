package pro.kinect.taxi.rest;

import android.annotation.SuppressLint;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

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

    public static void getAllAutoCoordinates(DisposableObserver<List<EntityAuto>> observer) {
        Call<List<EntityAuto>> call = getApi().getAllAutoCoordinates("GetCoordinatesAllAuto");
        call.enqueue(new Callback<List<EntityAuto>>() {
            @Override
            public void onResponse(Call<List<EntityAuto>> call, Response<List<EntityAuto>> response) {
                List<EntityAuto> list = response.body();
                if (!response.isSuccessful() || response.body() == null) {
                    Log.i(TAG, "response in not successful! code =" + response.code());
                    returnAllAutoFromDB(observer);
                    return;
                }
                EntityAuto.saveNewAuto(list, observer); // save
            }

            @Override
            public void onFailure(Call<List<EntityAuto>> call, Throwable t) {
                // something went completely south (like no internet connection)
                Log.i(TAG, t.getMessage());
                returnAllAutoFromDB(observer);
            }
        });
    }

    @SuppressLint("CheckResult")
    private static void returnAllAutoFromDB(DisposableObserver<List<EntityAuto>> observer) {
        Observable.fromCallable(() -> App.getDatabase().daoAuto().getAllAuto())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                    if (observer != null) {
                        observer.onNext(list);
                    }
                });
    }
}
