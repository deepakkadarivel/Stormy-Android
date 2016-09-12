package com.upbeat.stormy;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.upbeat.stormy.weather.Current;
import com.upbeat.stormy.ui.AlertDialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private Current mCurrent;

    @BindView(R.id.timeLabel) TextView mTimeLabel;
    @BindView(R.id.temperatureLabel) TextView mTemperatureLabel;
    @BindView(R.id.humidityValue) TextView mHumidityValue;
    @BindView(R.id.precipValue) TextView mPrecipValue;
    @BindView(R.id.summaryLabel) TextView mSummaryLabel;
    @BindView(R.id.locationLabel) TextView mLocationLabel;
    @BindView(R.id.iconImageView) ImageView mIconImageView;
    @BindView(R.id.refreshImageView) ImageView mRefreshImageView;
    @BindView(R.id.progressBar) ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mProgressBar.setVisibility(View.INVISIBLE);

        final double latitude = 37.8267;
        final double longitude = -122.423;

        mRefreshImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchWeatherData(latitude, longitude);
            }
        });

        fetchWeatherData(latitude, longitude);
    }

    private void fetchWeatherData(double latitude, double longitude) {
        String apiKey = "d7dc6aad54e77920b9bb1447c6f4e65a";
        String forecastUrl = "https://api.forecast.io/forecast/" + apiKey + "/" + latitude + "," + longitude;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(forecastUrl).build();
        Call call = client.newCall(request);

        if (networkIsAvailable()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    toggleRefresh();
                }
            });
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });

                    Log.e(TAG, "Exception Caught", e);
                    alertUserAboutError();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        String jsonData = response.body().string();
                        Log.v(TAG, jsonData);
                        if (response.isSuccessful()) {
                            mCurrent = gerCurrentDetails(jsonData);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateDispaly();
                                }
                            });
                        } else {
                            alertUserAboutError();
                        }
                    }
                    catch (IOException e) {
                        Log.e(TAG, "Exception Caught", e);
                    }
                    catch (JSONException e) {
                        Log.e(TAG, "Exception Caught", e);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });
                }
            });
        } else {
            Toast.makeText(MainActivity.this, R.string.network_unavailable_message, Toast.LENGTH_LONG).show();
        }
    }

    private void toggleRefresh() {
        if (mRefreshImageView.getVisibility() == View.INVISIBLE) {
            mRefreshImageView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.INVISIBLE);
        } else {
            mRefreshImageView.setVisibility(View.INVISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    private void updateDispaly() {
        mTemperatureLabel.setText(mCurrent.getTemperature() + "");
        mHumidityValue.setText(mCurrent.getHumidity() + "");
        mIconImageView.setImageDrawable(getResources().getDrawable(mCurrent.getIconId()));
        mPrecipValue.setText(mCurrent.getPrecipChance() + "%");
        mSummaryLabel.setText(mCurrent.getSummary());
        mTimeLabel.setText("At " + mCurrent.getFormattedTime() + " it will be.");
        mLocationLabel.setText(mCurrent.getTimeZone());
    }

    private Current gerCurrentDetails(String jsonData) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonData);
        String timezone = jsonObject.getString("timezone");
        Log.i(TAG, timezone);

        JSONObject currently = jsonObject.getJSONObject("currently");

        Current current = new Current();
        current.setHumidity(currently.getDouble("humidity"));
        current.setTemperature(currently.getDouble("temperature"));
        current.setPrecipChance(currently.getDouble("precipProbability"));
        current.setSummary(currently.getString("summary"));
        current.setTime(currently.getLong("time"));
        current.setIcon(currently.getString("icon"));
        current.setTimeZone(timezone);

        Log.i(TAG, current.getFormattedTime());

        return current;
    }

    private boolean networkIsAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }

        return isAvailable;
    }

    private void alertUserAboutError() {
        AlertDialogFragment dialogFragment = new AlertDialogFragment();
        dialogFragment.show(getFragmentManager(), "error_dialog");
    }
}
