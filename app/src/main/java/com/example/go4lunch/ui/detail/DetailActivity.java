package com.example.go4lunch.ui.detail;

import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunch.R;
import com.example.go4lunch.base.BaseActivity;
import com.example.go4lunch.models.Workmate;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class DetailActivity extends BaseActivity {

    @BindView(R.id.activity_restaurant_picture)
    ImageView restaurant_picture;
    @BindView(R.id.activity_restaurant_name)
    TextView restaurant_name;
    @BindView(R.id.activity_restaurant_ratingBar)
    RatingBar restaurant_rating;
    @BindView(R.id.activity_restaurant_address)
    TextView restaurant_address;

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

    }




    private void configureRecyclerView(){
        this.mWorkmates = new ArrayList<>();
        this.mDetailAdapter = new DetailAdapter(this.mWorkmates);
        this.recyclerView.setAdapter(this.mDetailAdapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }




}
