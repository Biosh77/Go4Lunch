package com.example.go4lunch;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;


import com.example.go4lunch.googlemapsretrofit.pojo.autocomplete.Prediction;
import com.example.go4lunch.googlemapsretrofit.pojo.nearbyplaces.Result;
import com.example.go4lunch.models.Workmate;
import com.example.go4lunch.repository.AutoCompleteRepository;
import com.example.go4lunch.repository.RestaurantDataRepository;
import com.example.go4lunch.repository.UserDataRepository;


import java.util.List;

public class ViewModel extends androidx.lifecycle.ViewModel {

    LiveData<List<Result>> restaurants;
    LiveData<List<Workmate>> workmates;
    LiveData<com.example.go4lunch.googlemapsretrofit.pojo.details.Result> details;
    LiveData<List<Prediction>> predictions;

    RestaurantDataRepository restaurantDataRepository;
    UserDataRepository userDataRepository;
    AutoCompleteRepository autoCompleteRepository;

    public ViewModel(RestaurantDataRepository restaurantDataSource, UserDataRepository userDataSource, AutoCompleteRepository autoCompleteRepository) {
        this.restaurantDataRepository = restaurantDataSource;
        this.userDataRepository = userDataSource;
        this.autoCompleteRepository = autoCompleteRepository;
    }

    public void init(android.location.Location location) {
        restaurants = restaurantDataRepository.getRestaurants(location);
        workmates = userDataRepository.GetWorkmates();
        predictions = Transformations.switchMap(nomRestaurant, (address) -> autoCompleteRepository.getPlacesAutoComplete(address, location));
    }

    public void init(String placeId) {
        details = restaurantDataRepository.getDetailsRestaurant(placeId);
    }

    public void init() {
        workmates = userDataRepository.GetWorkmates();
    }

    private final MutableLiveData<String> nomRestaurant = new MutableLiveData<>();

    public void setInput(String address) {
        nomRestaurant.setValue(address);
    }


    public LiveData<List<Result>> getRestaurants() {
        return restaurants;
    }

    public LiveData<List<Workmate>> getWorkmates() {
        return workmates;
    }

    public LiveData<List<Prediction>> getPredictions() {
        return predictions;
    }

    public LiveData<com.example.go4lunch.googlemapsretrofit.pojo.details.Result> getDetails() {
        return details;
    }

    public void updateLikes(String uid, List<String> likes) {
        userDataRepository.updateLikes(uid, likes);
    }

    public void updateChoice(String uid, String interestedBy) {
        userDataRepository.updateChoice(uid, interestedBy);
    }

    public void updateVicinity(String uid, String vicinity){
        userDataRepository.updateVicinity(uid,vicinity);
    }

}
