package com.example.go4lunch.ui.detail;

import android.media.Image;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.R;
import com.example.go4lunch.base.BaseActivity;
import com.example.go4lunch.googlemapsretrofit.pojo.Result;
import com.example.go4lunch.models.Workmate;
import com.example.go4lunch.ui.list.ListAdapter;
import com.example.go4lunch.ui.list.ListFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.example.go4lunch.ui.list.ListViewHolder.MAX_RATING;
import static com.example.go4lunch.ui.list.ListViewHolder.MAX_STAR;

public class DetailActivity extends BaseActivity {

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


    @Override
    public int getLayout() {
        return R.layout.detail_restaurant;
    }

    @Override
    protected void onConfigureDesign() {


        updateUi();
        configureRecyclerView();
    }


    private void configureRecyclerView() {
        this.mWorkmates = new ArrayList<>();
        this.mDetailAdapter = new DetailAdapter(this.mWorkmates);
        this.recyclerView.setAdapter(this.mDetailAdapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    private void updateUi() {
        RequestManager glide = Glide.with(recyclerView);

        Gson gson = new Gson();
        String strObj = getIntent().getStringExtra("obj");
        Result obj = gson.fromJson(strObj, Result.class);

        restaurant_name.setText(obj.getName());
        restaurant_address.setText(obj.getVicinity());


        // Image

        if (!(obj.getPhotos() == null)) {
            if (!(obj.getPhotos().isEmpty())) {
                glide.load("https://maps.googleapis.com/maps/api/place/photo" +
                        "?maxwidth=80" +
                        "&maxheight=80" +
                        "&photoreference=" + obj.getPhotos().get(0).getPhotoReference() +
                        "&key=AIzaSyDZrTJrp5DeQR5mwPAoj14LWCVo7huGjzw").into(restaurant_picture);
            }
        } else {
            glide.load(R.drawable.ic_no_image_available).apply(RequestOptions.centerCropTransform()).into(restaurant_picture);
        }

        // Rating

        if (obj.getRating() != null){
            double googleRating = obj.getRating();
            double rating = googleRating / MAX_RATING * MAX_STAR;
            this.restaurant_rating.setRating((float)rating);
            this.restaurant_rating.setVisibility(View.VISIBLE);
        }



        // --------------
        // BUTTONS
        // --------------


    }

}

