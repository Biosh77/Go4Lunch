package com.example.go4lunch.ui.workmates;

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

public class WorkmatesViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.user_photo)
    ImageView imageViewUserPhoto;
    @BindView(R.id.user_text)
    TextView textViewUser;


    // FOR DATA
    WorkmatesViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void updateWithData(Workmate workmate){
        RequestManager glide = Glide.with(itemView);
        // Photo
        glide.load(workmate.getUrlPicture()).apply(RequestOptions.circleCropTransform()).into(imageViewUserPhoto);

        // Name
        this.textViewUser.setText(workmate.getUsername());
    }
}
