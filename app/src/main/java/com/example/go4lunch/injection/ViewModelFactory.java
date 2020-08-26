package com.example.go4lunch.injection;

        import androidx.annotation.NonNull;
        import androidx.lifecycle.ViewModel;
        import androidx.lifecycle.ViewModelProvider;

        import com.example.go4lunch.repository.RestaurantDataRepository;
        import com.example.go4lunch.repository.UserDataRepository;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final RestaurantDataRepository restaurantDataSource;
    private final UserDataRepository userDataSource;


    public ViewModelFactory(RestaurantDataRepository restaurantDataSource, UserDataRepository userDataSource) {
        this.restaurantDataSource = restaurantDataSource;
        this.userDataSource = userDataSource;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(com.example.go4lunch.ViewModel.class)) {
            return (T) new com.example.go4lunch.ViewModel(restaurantDataSource, userDataSource);
        }
        throw new

                IllegalArgumentException("unknow ViewModel class");
    }
}
