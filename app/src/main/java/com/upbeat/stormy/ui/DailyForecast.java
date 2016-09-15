package com.upbeat.stormy.ui;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.upbeat.stormy.MainActivity;
import com.upbeat.stormy.R;
import com.upbeat.stormy.adapter.DayAdapter;
import com.upbeat.stormy.weather.Day;

import java.util.Arrays;

public class DailyForecast extends ListActivity {

    private Day[] mDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_forecast);
        Intent intent = getIntent();
        Parcelable[] parcelable = intent.getParcelableArrayExtra(MainActivity.DAILY_FORECAST);
        mDays = Arrays.copyOf(parcelable, parcelable.length, Day[].class);
        DayAdapter adapter = new DayAdapter(this, mDays);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        String dayOfTheWeek = mDays[position].getDayOfTheWeek();
        String maxTemperature = mDays[position].getTemperatureMax() + "";
        String summary = mDays[position].getSummary();
        String message = String.format("On %s the maximum temperature is %s and it will be %s", dayOfTheWeek, maxTemperature, summary);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

    }
}
