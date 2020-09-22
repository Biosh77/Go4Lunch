package com.example.go4lunch.ui.list;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.example.go4lunch.R;
import com.example.go4lunch.googlemapsretrofit.pojo.Result;


import butterknife.BindView;
import butterknife.ButterKnife;

public class ListViewHolder extends RecyclerView.ViewHolder {


    @BindView(R.id.name)
    TextView textViewName;
    @BindView(R.id.address)
    TextView textViewAddress;
    @BindView(R.id.opening_hours)
    TextView textViewOpening;
    @BindView(R.id.distance)
    TextView textViewDistance;
    @BindView(R.id.number_users)
    TextView textViewNumber;
    @BindView(R.id.item_ratingBar)
    RatingBar ratingBar;
    @BindView(R.id.photo)
    ImageView imageViewPhoto;


    // FOR DATA

    ListViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void updateWithData(Result results) {
        RequestManager glide = Glide.with(itemView);


        // Nom
        this.textViewName.setText(results.getName());

        //Photo
        glide.load("https://maps.googleapis.com/maps/api/place/photo" +
                "?maxwidth=80" +
                "&maxheight=80" +
                "&photoreference=" + results.getPhotos().get(0).getPhotoReference() +
                "&key=AIzaSyDZrTJrp5DeQR5mwPAoj14LWCVo7huGjzw").into(imageViewPhoto);

        //Address
        this.textViewAddress.setText(results.getVicinity());

        //Opening Hours
        if (results.getOpeningHours() != null) {

        } else { results.getOpeningHours().getOpenNow();

        }

    }

}
