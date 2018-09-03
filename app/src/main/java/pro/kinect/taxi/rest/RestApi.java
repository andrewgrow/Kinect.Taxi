package pro.kinect.taxi.rest;

import java.util.List;

import pro.kinect.taxi.db.EntityAuto;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RestApi {

    @POST("getauto.php")
    @FormUrlEncoded
    Call<List<EntityAuto>> getAllAutoCoordinates(
            @Field("func") String func
    );
}
