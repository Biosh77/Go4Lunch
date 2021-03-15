package com.example.go4lunch.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.googlemapsretrofit.RetrofitMaps;
import com.example.go4lunch.googlemapsretrofit.pojo.autocomplete.AutoComplete;
import com.example.go4lunch.googlemapsretrofit.pojo.autocomplete.Prediction;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class AutoCompleteRepository {

    private int PROXIMITY_RADIUS = 5000;

    public MutableLiveData<List<Prediction>> getPlacesAutoComplete(String input,android.location.Location location){

        MutableLiveData<List<Prediction>> predictions = new MutableLiveData<>();

        RetrofitMaps autoComplete = RetrofitMaps.retrofit.create(RetrofitMaps.class);

        Call<AutoComplete> call = autoComplete.getPlacesAutoComplete(input,location.getLatitude() + "," + location.getLongitude(), PROXIMITY_RADIUS);

        call.enqueue(new Callback<AutoComplete>() {
            @Override
            public void onResponse(Response<AutoComplete> response, Retrofit retrofit) {
                predictions.setValue(response.body().getPredictions());
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage()); }
        });

        return predictions;
    }
}
