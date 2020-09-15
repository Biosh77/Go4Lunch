package com.example.go4lunch.ui.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunch.R;
import com.example.go4lunch.ViewModel;
import com.example.go4lunch.googlemapsretrofit.pojo.Result;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListFragment extends Fragment {



    // FOR DESIGN

    @BindView(R.id.list_recycler) RecyclerView recyclerView;

    // FOR DATA

    private ListAdapter adapter;
    private List<Result> mRestaurants;


    private ViewModel mViewModel;




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list_item, container, false);
        ButterKnife.bind(this, view);

        this.configureRecyclerView();

        return view;
    }


    private void configureRecyclerView(){
        this.mRestaurants = new ArrayList<>();
        this.adapter = new ListAdapter(this.mRestaurants);
        this.recyclerView.setAdapter(adapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


    // RETROFIT CALL ?  NON MAIS JE SAIS PAS ETC



    // UI

}