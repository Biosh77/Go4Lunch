package com.example.go4lunch.ui.detail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunch.R;
import com.example.go4lunch.models.Workmate;
import com.example.go4lunch.ui.workmates.WorkmatesViewHolder;

import java.util.List;

public class DetailAdapter extends RecyclerView.Adapter<DetailViewHolder> {

    //DATA

    List<Workmate> workmates;

    //CONSTRUCTOR

    public DetailAdapter (List<Workmate> workmates){
        this.workmates=workmates;
    }


    @NonNull
    @Override
    public DetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.detail_recycler,parent,false);
        return new DetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailViewHolder holder, int position) {
        holder.updateWithData(this.workmates.get(position));
    }

    @Override
    public int getItemCount() {
        return workmates.size();
    }
}
