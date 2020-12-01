package com.example.go4lunch.ui.detail;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.R;
import com.example.go4lunch.models.Workmate;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailViewHolder extends RecyclerView.ViewHolder {



    @BindView(R.id.user_detail_photo)
    ImageView user_restaurant_photo;
    @BindView(R.id.user_detail_text)
    TextView user_restaurant_text;



    public DetailViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public void updateWithData(Workmate workmate){
        RequestManager glide = Glide.with(itemView);



        // Photo
        glide.load(workmate.getUrlPicture()).apply(RequestOptions.circleCropTransform()).into(user_restaurant_photo);

        // Name
        this.user_restaurant_text.setText(workmate.getUsername());

    }
}
