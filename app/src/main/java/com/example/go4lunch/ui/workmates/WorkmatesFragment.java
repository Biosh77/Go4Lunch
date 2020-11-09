package com.example.go4lunch.ui.workmates;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunch.R;
import com.example.go4lunch.ViewModel;
import com.example.go4lunch.injection.Injection;
import com.example.go4lunch.models.Workmate;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WorkmatesFragment extends Fragment {


    //FOR DESIGN

    @BindView(R.id.workmates_recycler)
    RecyclerView recyclerView;

    // FOR DATA

    private ViewModel workmatesViewModel;

    private WorkmatesAdapter adapter;




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        workmatesViewModel = new ViewModelProvider(this, Injection.provideViewModelFactory()).get(ViewModel.class);
        View root = inflater.inflate(R.layout.fragment_workmates, container, false);
        workmatesViewModel.init();
        workmatesViewModel.getWorkmate().observe(getViewLifecycleOwner(), new Observer<List<Workmate>>() {
            @Override
            public void onChanged(List<Workmate> workmates) {
                configureRecyclerView(workmates);
            }
        });

        ButterKnife.bind(this, root);
        return root;

    }

    private void configureRecyclerView(List<Workmate> workmates) {
        this.adapter = new WorkmatesAdapter(workmates);
        this.recyclerView.setAdapter(adapter);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }



}