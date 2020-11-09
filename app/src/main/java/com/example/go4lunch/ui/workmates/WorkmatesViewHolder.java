package com.example.go4lunch.ui.workmates;

import android.graphics.Typeface;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
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
    ImageView picture;
    @BindView(R.id.user_text)
    TextView name;


    // FOR DATA
    WorkmatesViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void updateWithData(Workmate workmate){
        RequestManager glide = Glide.with(itemView);


        // Photo
        if (workmate.getUrlPicture()!=null){
            glide.load(workmate.getUrlPicture()).apply(RequestOptions.circleCropTransform()).into(picture);
        }else {
            glide.load(R.drawable.ic_no_image_available).apply(RequestOptions.circleCropTransform()).into(picture);
        }


        // Name

        if (workmate.getInterestedBy() != null) {
            name.setText(itemView.getContext().getString(R.string.is_eating_at, workmate.getUsername(), workmate.getInterestedBy()));
            name.setTypeface(name.getTypeface(), Typeface.NORMAL);
            name.setTextColor(itemView.getContext().getColor(R.color.colorBlack));
            name.setAlpha((float) 1);

        } else {
            name.setText(itemView.getContext().getString(R.string.has_not_decided_yet,workmate.getUsername()));
            name.setTypeface(name.getTypeface(), Typeface.ITALIC);
            name.setTextColor(itemView.getContext().getColor(R.color.colorGray));
            name.setAlpha((float) 0.5);
        }
    }
}
