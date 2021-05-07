package com.ethanbovard.tapnews.ui.weather;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ethanbovard.tapnews.R;

import java.util.ArrayList;
import java.util.List;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ViewHolder> {
    private final Narrative[] narrativeSet;
    private final List<View> views;
    @NonNull
    @Override
    public ForecastAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.weather_forecast_node, parent, false);
        views.add(view);
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        view.setPadding(0, 10, 0, 10);
        return new ViewHolder(view);
    }

    public ForecastAdapter (Narrative[] narrativeSet) {
        this.narrativeSet = narrativeSet;
        views = new ArrayList<View>();
    }
    @Override
    public void onBindViewHolder(@NonNull ForecastAdapter.ViewHolder holder, int position) {
        Narrative narrative = narrativeSet[position];
        TextView narrativeField = holder.itemView.findViewById(R.id.narrativeTextField);
        TextView daypartNameField = holder.itemView.findViewById(R.id.dayLabel);
        narrativeField.setText(narrative.narrative);
        daypartNameField.setText(narrative.daypartName);
    }

    @Override
    public int getItemCount() {
        return narrativeSet != null ? narrativeSet.length : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
