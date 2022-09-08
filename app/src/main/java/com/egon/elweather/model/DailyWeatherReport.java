package com.egon.elweather.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DailyWeatherReport {

    public static final String WEATHER_TYPE_CLOUDS = "Clouds";
    public static final String WEATHER_TYPE_CLEAR = "Clear";
    public static final String WEATHER_TYPE_RAIN = "Rain";
    public static final String WEATHER_TYPE_WIND = "Wind";
    public static final String WEATHER_TYPE_SNOW = "Snow";

    private String cityName;
    private String cityCountry;
    private int currentTemp;
    private int maxTemp;
    private int minTemp;
    private String weatherType;
    private String weatherDesc;
    private String formattedDate;

    public DailyWeatherReport() {}

    public DailyWeatherReport(String cityName, String cityCountry, int currentTemp, int maxTemp, int minTemp, String weatherType, String weatherDesc, String rawDate) {
        this.cityName = cityName;
        this.cityCountry = cityCountry;
        this.currentTemp = currentTemp;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.weatherType = weatherType;
        this.weatherDesc = weatherDesc;
        this.formattedDate = rawDateToPretty(rawDate);
    }

    public String rawDateToPretty (String rawDate) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "MMM d, yyyy hh:mmaa";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = (Date) inputFormat.parse(rawDate);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityCountry() {
        return cityCountry;
    }

    public void setCityCountry(String cityCountry) {
        this.cityCountry = cityCountry;
    }

    public int getCurrentTemp() {
        return currentTemp;
    }

    public void setCurrentTemp(int currentTemp) {
        this.currentTemp = currentTemp;
    }

    public int getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(int maxTemp) {
        this.maxTemp = maxTemp;
    }

    public int getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(int minTemp) {
        this.minTemp = minTemp;
    }

    public String getWeatherType() {
        return weatherType;
    }

    public void setWeatherType(String weatherType) {
        this.weatherType = weatherType;
    }

    public String getWeatherDesc() {
        return weatherDesc;
    }

    public void setWeatherDesc(String weatherDesc) {
        this.weatherDesc = weatherDesc;
    }

    public String getFormattedDate() {
        return formattedDate;
    }

    public void setFormattedDate(String formattedDate) {
        this.formattedDate = formattedDate;
    }
}
