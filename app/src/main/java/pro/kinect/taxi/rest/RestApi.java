package pro.kinect.taxi.rest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RestApi {

    @POST("getauto.php")
    @FormUrlEncoded
    Call<List<RestAutoModel>> getAllAutoCoordinates(
            @Field("func") String func
    );
}
