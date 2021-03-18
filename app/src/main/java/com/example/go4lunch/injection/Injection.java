package com.example.go4lunch.injection;




import com.example.go4lunch.repository.AutoCompleteRepository;
import com.example.go4lunch.repository.RestaurantDataRepository;
import com.example.go4lunch.repository.UserDataRepository;

public class Injection {


    public static RestaurantDataRepository provideRestaurantDataSource () {
        return new RestaurantDataRepository();
    }


    public static UserDataRepository provideUserRepository(){
        return new UserDataRepository();
    }

    public static AutoCompleteRepository provideAutocompleteRepository(){return new AutoCompleteRepository();}


    public static ViewModelFactory provideViewModelFactory(){
        RestaurantDataRepository dataSourceRestaurant=provideRestaurantDataSource();
        UserDataRepository dataSourceUser=provideUserRepository();
        AutoCompleteRepository dataSourceAuto=provideAutocompleteRepository();
        return new ViewModelFactory(dataSourceRestaurant,dataSourceUser,dataSourceAuto);
    }

}
