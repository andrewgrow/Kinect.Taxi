package pro.kinect.taxi.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import io.reactivex.subjects.PublishSubject;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestManager {

    private static final String TAG = RestManager.class.getSimpleName();

    private static PublishSubject<List<RestAutoModel>> subjectAutoModel = PublishSubject.create();

    public static PublishSubject<List<RestAutoModel>> getSubjectAutoModel() {
        return subjectAutoModel;
    }

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

    public static void getAllAutoCoordinates() {
        Call<List<RestAutoModel>> call = getApi().getAllAutoCoordinates("GetCoordinatesAllAuto");
        call.enqueue(new Callback<List<RestAutoModel>>() {
            @Override
            public void onResponse(Call<List<RestAutoModel>> call, Response<List<RestAutoModel>> response) {
                if (response.isSuccessful()) {
                    List<RestAutoModel> list = response.body();
                    if (list != null) {
                        getSubjectAutoModel().onNext(list);
                    }
                } else {
                    // error response, no access to resource?
                }
            }

            @Override
            public void onFailure(Call<List<RestAutoModel>> call, Throwable t) {
                // something went completely south (like no internet connection)
//                Log.d("Error", t.getMessage());
            }
        });
    }
}
