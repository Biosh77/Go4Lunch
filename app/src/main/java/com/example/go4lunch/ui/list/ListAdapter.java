package com.example.go4lunch.ui.list;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunch.R;
import com.example.go4lunch.googlemapsretrofit.pojo.Result;
import com.example.go4lunch.ui.detail.DetailActivity;


import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListViewHolder> {

    // FOR DATA

    private List<Result> restaurants;
    Context context;
    OnRestaurantClickListener listener;
    interface OnRestaurantClickListener{
        void onRestaurantClick(Result result);
    }


    //CONSTRUCTOR

    public ListAdapter(List<Result> restaurants,OnRestaurantClickListener listener ){
    this.restaurants = restaurants;
    this.listener = listener;

    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_list_item, parent, false);

        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {

        holder.updateWithData(this.restaurants.get(position));

        holder.itemView.setOnClickListener(v -> {
           Result result = restaurants.get(position);
           listener.onRestaurantClick(result);
       });
    }

    @Override
    public int getItemCount() {
        return this.restaurants.size();
    }


}
