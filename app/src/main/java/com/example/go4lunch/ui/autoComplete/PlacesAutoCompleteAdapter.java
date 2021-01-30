package com.example.go4lunch.ui.autoComplete;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.go4lunch.R;
import com.example.go4lunch.googlemapsretrofit.pojo.autocomplete.Prediction;


import java.util.List;

public class PlacesAutoCompleteAdapter extends ArrayAdapter<Prediction>  {

    private Context context;
    private List<Prediction> predictions;

    public PlacesAutoCompleteAdapter(Context context, List<Prediction> predictions) {
        super(context, R.layout.place_row_layout, predictions);
        this.context = context;
        this.predictions = predictions;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(context).inflate(R.layout.place_row_layout,parent,false);
        if (predictions != null && predictions.size() > 0) {
            Prediction prediction = predictions.get(position);
            TextView textViewName = listItem.findViewById(R.id.textViewName);
            textViewName.setText(prediction.getDescription());
        }
        return listItem;
    }

}







