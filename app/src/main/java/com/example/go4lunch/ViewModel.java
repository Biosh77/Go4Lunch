package com.example.go4lunch;


import androidx.lifecycle.LiveData;


import com.example.go4lunch.googlemapsretrofit.pojo.nearbyplaces.Result;
import com.example.go4lunch.models.Workmate;
import com.example.go4lunch.repository.RestaurantDataRepository;
import com.example.go4lunch.repository.UserDataRepository;


import java.util.List;

public class ViewModel extends androidx.lifecycle.ViewModel {

    LiveData<List<Result>> restaurants;
    LiveData<List<Workmate>> workmates;
    LiveData<com.example.go4lunch.googlemapsretrofit.pojo.details.Result> details;

    RestaurantDataRepository restaurantDataRepository;
    UserDataRepository userDataRepository;

    public ViewModel(RestaurantDataRepository restaurantDataSource, UserDataRepository userDataSource) {
        this.restaurantDataRepository = restaurantDataSource;
        this.userDataRepository = userDataSource;
    }

    public void init(android.location.Location location) {
        restaurants = restaurantDataRepository.getRestaurants(location);
        workmates = userDataRepository.GetWorkmates();
    }

    public void init(String placeId){
        details = restaurantDataRepository.getDetailsRestaurant(placeId);
    }

    public void init(){
        workmates = userDataRepository.GetWorkmates();
    }

    public LiveData<List<Result>> getRestaurant() {
        return restaurants;
    }

    public LiveData<List<Workmate>> getWorkmate() {
        return workmates;
    }

    public LiveData<com.example.go4lunch.googlemapsretrofit.pojo.details.Result> getDetails(){return details;}


}
