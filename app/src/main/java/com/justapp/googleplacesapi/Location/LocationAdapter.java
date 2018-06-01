package com.justapp.googleplacesapi.Location;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.justapp.googleplacesapi.R;

import java.util.HashMap;
import java.util.List;

public class LocationAdapter extends ArrayAdapter<String> {

    private HashMap<String, String> hashMap = new HashMap<>();
    private int selected_button_position;

    public LocationAdapter(@NonNull Context context, @NonNull List<String> objects, HashMap<String, String> hashMap, int selected_button_position) {
        super(context, R.layout.row, objects);
        this.hashMap = hashMap;
        this.selected_button_position = selected_button_position;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater myInflator = LayoutInflater.from(getContext());
        View resultRowView = myInflator.inflate(R.layout.row, parent, false);

        String main_text = getItem(position);
        String secondary_text = hashMap.get(main_text);

        TextView mainTV = (TextView) resultRowView.findViewById(R.id.main);
        TextView secondaryTV = (TextView) resultRowView.findViewById(R.id.submain);
        final RadioButton radioButton = (RadioButton) resultRowView.findViewById(R.id.radioButton);

        mainTV.setText(main_text);
        secondaryTV.setText(secondary_text);

        Log.i(LocationSearch.TAG, "Selected Button: " + selected_button_position);

        if (position == selected_button_position) {
            radioButton.setChecked(true);
        }
        else {
            radioButton.setChecked(false);
        }

//        radioButton.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (radioButton.isChecked()) {
//                            radioButton.setChecked(false);
//                            selected_button_position = -1;
//                            Log.i(LocationSearch.TAG, "Already Checked, now setting selected button to : " + -1);
//                        }
//                        else {
//                            radioButton.setChecked(true);
//                            selected_button_position = position;
//                            Log.i(LocationSearch.TAG, "Not Checked, now setting selected button to : " + position);
//                        }
//
//                    }
//                }
//        );

        return resultRowView;

    }
}
