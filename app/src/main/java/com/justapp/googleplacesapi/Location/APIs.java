package com.justapp.googleplacesapi.Location;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIs {

    String BASE_URL = "https://maps.googleapis.com/maps/api/";

    @GET("place/autocomplete/json")
    Call<ResponseBody> getPlaces(@Query("key") String key, @Query("type") String type, @Query("input") String input, @Query("components") String components);
}