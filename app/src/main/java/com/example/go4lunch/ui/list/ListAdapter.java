package com.example.go4lunch.ui.list;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunch.R;
import com.example.go4lunch.googlemapsretrofit.pojo.Result;


import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListViewHolder> {

    // FOR DATA

    private List<Result> restaurants;



    //CONSTRUCTOR

    public ListAdapter(List<Result> restaurants){
    this.restaurants = restaurants;

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
    }

    @Override
    public int getItemCount() {
        return this.restaurants.size();
    }


}
