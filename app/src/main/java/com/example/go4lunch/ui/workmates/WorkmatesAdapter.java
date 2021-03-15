package com.example.go4lunch.ui.workmates;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunch.R;
import com.example.go4lunch.googlemapsretrofit.pojo.nearbyplaces.Result;
import com.example.go4lunch.models.Workmate;

import java.util.List;

public class WorkmatesAdapter extends RecyclerView.Adapter<WorkmatesViewHolder> {

    // FOR DATA

    List<Workmate> workmates;
    OnItemClickListener listener;



    public interface OnItemClickListener {
        void onItemClick(Workmate workmate);
    }



    // CONSTRUCTOR

    public WorkmatesAdapter(List<Workmate> workmates, OnItemClickListener listener) {
        this.workmates = workmates;
        this.listener = listener;
    }

    @Override
    public WorkmatesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_workmates_item,parent,false);
        return new WorkmatesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkmatesViewHolder holder, int position) {
        holder.updateWithData(this.workmates.get(position));

        holder.itemView.setOnClickListener(v -> {
            Workmate workmate = workmates.get(position);
            listener.onItemClick(workmate);
        });
    }


    @Override
    public int getItemCount() {
        return workmates.size();
    }
}
