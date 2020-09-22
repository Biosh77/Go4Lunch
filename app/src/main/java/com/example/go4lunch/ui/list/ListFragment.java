package com.example.go4lunch.ui.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunch.R;
import com.example.go4lunch.ViewModel;
import com.example.go4lunch.googlemapsretrofit.pojo.Location;
import com.example.go4lunch.googlemapsretrofit.pojo.Result;
import com.example.go4lunch.injection.Injection;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListFragment extends Fragment {



    // FOR DESIGN

    @BindView(R.id.list_recycler) RecyclerView recyclerView;

    // FOR DATA

    private ListAdapter adapter;


    private ViewModel mViewModel;




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        mViewModel =
                ViewModelProviders.of(this, Injection.provideViewModelFactory()).get(ViewModel.class);
        View root = inflater.inflate(R.layout.fragment_list_item, container, false);
        //mViewModel.init(Location);
        mViewModel.getRestaurant().observe(this, new Observer<List<Result>>()  {
            @Override
            public void onChanged(List<Result> results) {
                configureRecyclerView(results);
            }
        });
        return root;
    }



    private void configureRecyclerView(List<Result> results){
        this.adapter = new ListAdapter(results);
        this.recyclerView.setAdapter(adapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}