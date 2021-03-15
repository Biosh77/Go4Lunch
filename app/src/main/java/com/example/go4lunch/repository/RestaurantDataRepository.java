package com.example.go4lunch.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.googlemapsretrofit.RetrofitMaps;
import com.example.go4lunch.googlemapsretrofit.pojo.details.Details;
import com.example.go4lunch.googlemapsretrofit.pojo.nearbyplaces.NearbySearch;
import com.example.go4lunch.googlemapsretrofit.pojo.nearbyplaces.Result;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class RestaurantDataRepository {

    private int PROXIMITY_RADIUS = 5000;

    public MutableLiveData<List<Result>> getRestaurants(android.location.Location location) {

        MutableLiveData<List<Result>> results = new MutableLiveData<>();

        // 2.2 - Get a Retrofit instance and the related endpoints
        RetrofitMaps mapsService = RetrofitMaps.retrofit.create(RetrofitMaps.class);

        // 2.3 - Create the call on the API
        Call<NearbySearch> call = mapsService.getNearbyPlaces("restaurant", location.getLatitude() + "," + location.getLongitude(), PROXIMITY_RADIUS);
        // 2.4 - Start the call
        call.enqueue(new Callback<NearbySearch>() {

            @Override
            public void onResponse(Response<NearbySearch> response, Retrofit retrofit) {
                results.setValue(response.body().getResults());


            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });
        return results;
    }




    public MutableLiveData<com.example.go4lunch.googlemapsretrofit.pojo.details.Result> getDetailsRestaurant(String placeId) {

        MutableLiveData<com.example.go4lunch.googlemapsretrofit.pojo.details.Result> results = new MutableLiveData<>();

        RetrofitMaps details = RetrofitMaps.retrofit.create(RetrofitMaps.class);

        Call<Details> call = details.getDetails(placeId);

        call.enqueue(new Callback<Details>() {
            @Override
            public void onResponse(Response<Details> response, Retrofit retrofit) {
                results.setValue(response.body().getResult());
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });

        return results;
    }
}