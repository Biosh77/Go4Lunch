package com.example.go4lunch.ui.detail;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.R;
import com.example.go4lunch.ViewModel;
import com.example.go4lunch.base.BaseActivity;
import com.example.go4lunch.googlemapsretrofit.pojo.nearbyplaces.Result;
import com.example.go4lunch.injection.Injection;
import com.example.go4lunch.models.Workmate;
import com.example.go4lunch.repository.UserDataRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.example.go4lunch.ui.list.ListViewHolder.MAX_RATING;
import static com.example.go4lunch.ui.list.ListViewHolder.MAX_STAR;

public class DetailActivity extends BaseActivity {

    private static final String TAG = "Blabla";
    @BindView(R.id.activity_restaurant_picture)
    ImageView restaurant_picture;
    @BindView(R.id.activity_restaurant_name)
    TextView restaurant_name;
    @BindView(R.id.activity_restaurant_ratingBar)
    RatingBar restaurant_rating;
    @BindView(R.id.activity_restaurant_address)
    TextView restaurant_address;
    @BindView(R.id.call)
    Button call_button;
    @BindView(R.id.like)
    Button like_button;
    @BindView(R.id.website)
    Button website_button;
    @BindView(R.id.choice_button)
    FloatingActionButton choice_button;


    @BindView(R.id.activity_restaurant_recycler)
    RecyclerView recyclerView;


    private DetailAdapter mDetailAdapter;
    private List<Workmate> mWorkmates;
    private ViewModel detailViewModel;


    private Result result;
    private Workmate workmate;

    private String website;
    private String phone;


    @Override
    public int getLayout() {
        return R.layout.detail_restaurant;
    }

    @Override
    protected void onConfigureDesign() {


        updateUi();
        getWorkmateChoice();
    }


    private void configureRecyclerView(List<Workmate> workmates) {
        this.mWorkmates = new ArrayList<>();
        this.mDetailAdapter = new DetailAdapter(workmates);
        this.recyclerView.setAdapter(this.mDetailAdapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    private void getWorkmateChoice() {
        UserDataRepository.getUserCollection().whereEqualTo("interestedBy", result.getName()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Workmate> workmates = new ArrayList<>();
                    for (DocumentSnapshot document : task.getResult()) {
                        Log.d("TAG", document.getId() + " => " + document.getData());
                        workmates.add(document.toObject(Workmate.class));
                    }
                    configureRecyclerView(workmates);
                } else {
                    Log.d("TAG", "Error getting documents: ", task.getException());
                }

            }

        });
    }



    //method

    public void updateChoiceUi(Workmate workmate, Result result){
        if (workmate.getInterestedBy() == null || !workmate.getInterestedBy().contains(result.getName())) {
            choice_button.setImageResource(R.drawable.before_validate);
        } else {
            choice_button.setImageResource(R.drawable.validate);
        }
    }

    private void updateUi() {


        RequestManager glide = Glide.with(recyclerView);


        Gson gson = new Gson();
        String strObj = getIntent().getStringExtra("obj");
        result = gson.fromJson(strObj, Result.class);

        workmate = (Workmate) getIntent().getSerializableExtra("workmate");


        detailViewModel = new ViewModelProvider(this, Injection.provideViewModelFactory()).get(ViewModel.class);
        detailViewModel.init(result.getPlaceId());
        detailViewModel.getDetails().observe(this, new Observer<com.example.go4lunch.googlemapsretrofit.pojo.details.Result>() {
            @Override
            public void onChanged(com.example.go4lunch.googlemapsretrofit.pojo.details.Result details) {

                website = details.getWebsite();
                phone = details.getFormattedPhoneNumber();

            }
        });

        updateChoiceUi(workmate,result);

        restaurant_name.setText(result.getName());
        restaurant_address.setText(result.getVicinity());


        // Image

        if (!(result.getPhotos() == null)) {
            if (!(result.getPhotos().isEmpty())) {
                glide.load("https://maps.googleapis.com/maps/api/place/photo" +
                        "?maxwidth=400" +
                        "&maxheight=400" +
                        "&photoreference=" + result.getPhotos().get(0).getPhotoReference() +
                        "&key=AIzaSyDZrTJrp5DeQR5mwPAoj14LWCVo7huGjzw").into(restaurant_picture);
            }
        } else {
            glide.load(R.drawable.ic_no_image_available).apply(RequestOptions.centerCropTransform()).into(restaurant_picture);
        }

        // Rating

        if (result.getRating() != null) {
            double googleRating = result.getRating();
            double rating = googleRating / MAX_RATING * MAX_STAR;
            this.restaurant_rating.setRating((float) rating);
            this.restaurant_rating.setVisibility(View.VISIBLE);
        }

    }

    // --------------
    // BUTTONS
    // --------------

    // Website

    @OnClick(R.id.website)
    public void onClickWeb() {
        if (website != null) {
            this.configureCustomTabs();
        } else {
            Toast.makeText(this, getResources().getString(R.string.website_unavailable), Toast.LENGTH_SHORT).show();
        }
    }

    private void configureCustomTabs() {
        String url = website;
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }

    // Phone

    @OnClick(R.id.call)
    public void onClickCall() {
        if (phone != null) {
            Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
            startActivity(callIntent);
        } else {
            Toast.makeText(this, getResources().getString(R.string.phone_unavailable), Toast.LENGTH_SHORT).show();
        }
    }

    // Like

    @OnClick(R.id.like)
    public void onClickLike(View v) {
        if (workmate.getLikes() == null || !workmate.getLikes().contains(result.getName())) {
            this.likeRestaurant();
        } else {
            this.dislikeRestaurant();
        }
    }

    private void likeRestaurant() {
        Toast.makeText(this, "Liked", Toast.LENGTH_SHORT).show();
        if (workmate.getLikes() == null)
            workmate.setLikes(new ArrayList<>());
        workmate.getLikes().add(result.getName());
        detailViewModel.updateLikes(workmate.getUid(), workmate.getLikes());
    }

    private void dislikeRestaurant() {
        Toast.makeText(this, "Unliked", Toast.LENGTH_SHORT).show();
        workmate.getLikes().remove(result.getName());
        detailViewModel.updateLikes(workmate.getUid(), workmate.getLikes());
    }

    // Choice

    @OnClick(R.id.choice_button)
    public void onClickChoice() {
        if (workmate.getInterestedBy() == null || !workmate.getInterestedBy().equals(result.getName())) {
            interestedBy();
        } else {
            notInterestedBy();
        }
        updateChoiceUi(workmate,result);
    }

    public void interestedBy() {
        if (workmate.getInterestedBy() == null)
            workmate.setInterestedBy(result.getName());
        detailViewModel.updateChoice(workmate.getUid(), result.getName());
    }

    public void notInterestedBy() {
        workmate.setInterestedBy(null);
        detailViewModel.updateChoice(workmate.getUid(), null);
    }
}



