
package com.example.go4lunch.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.googlemapsretrofit.RetrofitMaps;
import com.example.go4lunch.googlemapsretrofit.pojo.Example;
import com.example.go4lunch.googlemapsretrofit.pojo.Result;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class RestaurantDataRepository {

    private int PROXIMITY_RADIUS = 1500;

    public MutableLiveData<List<Result>> getRestaurant(android.location.Location location) {

        MutableLiveData<List<Result>> results = new MutableLiveData<>();

        // 2.2 - Get a Retrofit instance and the related endpoints
        RetrofitMaps mapsService = RetrofitMaps.retrofit.create(RetrofitMaps.class);

        // 2.3 - Create the call on the API
        Call<Example> call = mapsService.getNearbyPlaces("restaurant", location.getLatitude() + "," + location.getLongitude(), PROXIMITY_RADIUS);
        // 2.4 - Start the call
        call.enqueue(new Callback<Example>() {

            @Override
            public void onResponse(Response<Example> response, Retrofit retrofit) {
                results.setValue(response.body().getResults());


            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });
        return results;
    }

}