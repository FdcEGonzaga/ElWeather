package com.egon.elweather.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.egon.elweather.R;
import com.egon.elweather.activity.MainActivity;
import com.egon.elweather.model.DailyWeatherReport;

import java.util.ArrayList;

public class MainWeatherAdapter extends RecyclerView.Adapter<MainWeatherAdapter.MainWeatherViewHolder> {
    private ArrayList<DailyWeatherReport> arrReports;

    public MainWeatherAdapter(ArrayList<DailyWeatherReport> arrReports) {
        this.arrReports = arrReports;
    }

    @NonNull
    @Override
    public MainWeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_weather, parent, false);
        return new MainWeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainWeatherViewHolder holder, int position) {
        DailyWeatherReport reports = arrReports.get(position);
        holder.updateUi(reports);
    }

    @Override
    public int getItemCount() {
        return arrReports.size();
    }

    public static class MainWeatherViewHolder extends RecyclerView.ViewHolder {
        private ImageView lwIcon;
        private TextView lwTitle, lwDesc, lwHighTemp, lwLowTemp;

        public MainWeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            lwIcon = itemView.findViewById(R.id.lw_icon);
            lwTitle = itemView.findViewById(R.id.lw_title);
            lwDesc = itemView.findViewById(R.id.lw_desc);
            lwHighTemp = itemView.findViewById(R.id.lw_highTemp);
            lwLowTemp = itemView.findViewById(R.id.lw_lowTemp);
        }

        public void updateUi(DailyWeatherReport report) {
            switch (report.getWeatherType()) {
                case DailyWeatherReport.WEATHER_TYPE_CLOUDS:
                    lwIcon.setImageDrawable(lwIcon.getResources().getDrawable(R.drawable.cloudy));
                    break;
                case DailyWeatherReport.WEATHER_TYPE_RAIN:
                    lwIcon.setImageDrawable(lwIcon.getResources().getDrawable(R.drawable.rainy));
                    break;
                case DailyWeatherReport.WEATHER_TYPE_WIND:
                    lwIcon.setImageDrawable(lwIcon.getResources().getDrawable(R.drawable.windy));
                    break;
                case DailyWeatherReport.WEATHER_TYPE_SNOW:
                    lwIcon.setImageDrawable(lwIcon.getResources().getDrawable(R.drawable.snowy));
                    break;
                case DailyWeatherReport.WEATHER_TYPE_CLEAR:
                    lwIcon.setImageDrawable(lwIcon.getResources().getDrawable(R.drawable.sunny));
                    break;
            }

            lwTitle.setText(report.getFormattedDate());
            lwDesc.setText(report.getWeatherDesc());
            lwHighTemp.setText(convertToCelcius(report.getMaxTemp()) + "°");
            lwLowTemp.setText(convertToCelcius(report.getMinTemp()) + "°");
        }
    }

    private static int convertToCelcius(int Fahrenheit) {
        return (Fahrenheit - 32) * 5 / 9;
    }
}
