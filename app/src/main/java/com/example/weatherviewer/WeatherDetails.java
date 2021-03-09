package com.example.weatherviewer;

import android.os.Bundle;

import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class WeatherDetails extends Fragment {


    public WeatherDetails() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {

        WeatherData data = getArguments().getParcelable(MainActivity.WEATHER_KEY);

        View view = inflater.inflate(R.layout.fragment_weather_details, container, false);

        TextView cityTextView = view.findViewById(R.id.cityTextView);
        TextView currentTemperatureTextView = view.findViewById(R.id.currentTemperatureTextView);
        TextView highTemperatureTextView = view.findViewById(R.id.highTemperatureTextView);
        TextView lowTemperatureTextView = view.findViewById(R.id.lowTemperatureTextView);
        TextView descriptionTextView = view.findViewById(R.id.weatherDescriptionTextView);

        if (data != null) {
            cityTextView.setText(data.getCity());

            currentTemperatureTextView.setText(
                    String.format(
                            getResources().getString(R.string.temperature_celsius),
                            data.getCurrentTemperature()
                    )
            );

            highTemperatureTextView.setText(
                    String.format(
                            getResources().getString(R.string.temperature_celsius),
                            data.getHighTemperature()
                    )
            );

            lowTemperatureTextView.setText(
                    String.format(
                            getResources().getString(R.string.temperature_celsius),
                            data.getLowTemperature()
                    )
            );

            descriptionTextView.setText(data.getDescription());
        }

        return view;
    }

}
