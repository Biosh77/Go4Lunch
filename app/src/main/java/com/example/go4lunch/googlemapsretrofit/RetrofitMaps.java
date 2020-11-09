package com.example.go4lunch.googlemapsretrofit;

import com.example.go4lunch.googlemapsretrofit.pojo.details.Details;
import com.example.go4lunch.googlemapsretrofit.pojo.nearbyplaces.Example;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.http.GET;
import retrofit.http.Query;


public interface RetrofitMaps {

    /*
     * Retrofit get annotation with our URL
     * And our method that will return.
     */
    @GET("nearbysearch/json?key=AIzaSyDZrTJrp5DeQR5mwPAoj14LWCVo7huGjzw")
    Call<Example> getNearbyPlaces(@Query("type") String type, @Query("location") String location, @Query("radius") int radius);

    @GET("details/json?key=AIzaSyDZrTJrp5DeQR5mwPAoj14LWCVo7huGjzw")
    Call<Details> getDetails(@Query("place_id")String placeId);

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/maps/api/place/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();


}
