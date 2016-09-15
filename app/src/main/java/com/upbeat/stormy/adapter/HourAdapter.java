package com.upbeat.stormy.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.upbeat.stormy.R;
import com.upbeat.stormy.weather.Hour;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HourAdapter extends RecyclerView.Adapter<HourAdapter.HourViewHolder> {

    private Hour[] mHours;

    public HourAdapter(Hour[] hours) {
        mHours = hours;
    }

    @Override
    public HourViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hourly_list_item,parent,false);
        HourViewHolder viewHolder = new HourViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HourViewHolder holder, int position) {
        holder.bindHour(mHours[position]);
    }

    @Override
    public int getItemCount() {
        return mHours.length;
    }

    public class HourViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.timeLabel) TextView timeLabel;
        @BindView(R.id.iconImageView) ImageView iconImageView;
        @BindView(R.id.summaryLabel) TextView summaryLabel;
        @BindView(R.id.temperatureLabel) TextView temperatureLabel;

        public HourViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindHour(Hour hour) {
            timeLabel.setText(hour.getTimeValue());
            iconImageView.setImageResource(hour.getIconId());
            summaryLabel.setText(hour.getSummary());
            temperatureLabel.setText(hour.getTemperature() + "");
        }
    }


}
