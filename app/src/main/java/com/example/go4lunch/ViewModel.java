package com.example.go4lunch;


import androidx.lifecycle.LiveData;


import com.example.go4lunch.googlemapsretrofit.pojo.Result;
import com.example.go4lunch.repository.RestaurantDataRepository;
import com.example.go4lunch.repository.UserDataRepository;


import java.util.List;

public class ViewModel extends androidx.lifecycle.ViewModel {

    LiveData<List<Result>> restaurants;

    RestaurantDataRepository restaurantDataRepository;
    UserDataRepository userDataRepository;

    public ViewModel(RestaurantDataRepository restaurantDataSource, UserDataRepository userDataSource) {
        this.restaurantDataRepository = restaurantDataSource;
        this.userDataRepository = userDataSource;
    }

    public void init(android.location.Location location) {
        restaurants = restaurantDataRepository.getRestaurants(location);
    }

    public LiveData<List<Result>> getRestaurant() {
        return restaurants;
    }




}
