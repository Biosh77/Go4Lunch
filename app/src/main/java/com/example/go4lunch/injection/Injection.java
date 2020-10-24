package com.example.go4lunch.injection;




import com.example.go4lunch.repository.RestaurantDataRepository;
import com.example.go4lunch.repository.UserDataRepository;


import java.util.List;

public class Injection {


    public static RestaurantDataRepository provideRestaurantDataSource () {
        return new RestaurantDataRepository();
    }


    public static UserDataRepository provideUserRepository(){
        return new UserDataRepository();
    }


    public static ViewModelFactory provideViewModelFactory(){
        RestaurantDataRepository dataSourceRestaurant=provideRestaurantDataSource();
        UserDataRepository dataSourceUser=provideUserRepository();
        return new ViewModelFactory(dataSourceRestaurant,dataSourceUser);
    }

}
