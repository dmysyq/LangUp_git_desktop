package com.example.langup.presentation.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.langup.R;
import com.example.langup.domain.model.Series;
import com.example.langup.domain.model.SeriesMetadata;

import java.util.List;
import android.util.Log;

public class SeriesAdapter extends RecyclerView.Adapter<SeriesAdapter.SeriesViewHolder> {
    private List<Series> seriesList;
    private final OnSeriesClickListener listener;

    public interface OnSeriesClickListener {
        void onSeriesClick(Series series);
    }

    public SeriesAdapter(List<Series> seriesList, OnSeriesClickListener listener) {
        this.seriesList = seriesList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SeriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_series, parent, false);
        return new SeriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SeriesViewHolder holder, int position) {
        Series series = seriesList.get(position);
        SeriesMetadata metadata = series.getMetadata();
        
        Log.d("SeriesAdapter", "Binding series: " + metadata.getTitle());
        
        // Set title
        holder.titleTextView.setText(metadata.getTitle());
        
        // Set accent and difficulty
        holder.accentTextView.setText(metadata.getAccent());
        holder.difficultyTextView.setText(String.format("Level %d", metadata.getDifficulty()));
        
        // Set country and source
        holder.countryTextView.setText(metadata.getCountry());
        holder.sourceTextView.setText(metadata.getSource());
        
        // Load image using Glide
        String imageUrl = metadata.getImageUrl();
        Log.d("SeriesAdapter", "Loading image from URL: " + imageUrl);
        
        if (imageUrl != null) {
            // Always load from assets/banner folder and append .png extension
            String assetPath = "file:///android_asset/banner/" + imageUrl + ".png";
            Glide.with(holder.itemView.getContext())
                .load(assetPath)
                .placeholder(R.drawable.ic_image_placeholder)
                .error(R.drawable.ic_image_placeholder)
                .centerCrop()
                .into(holder.imageView);
        } else {
            holder.imageView.setImageResource(R.drawable.ic_image_placeholder);
        }
        
        // Premium icon
        ImageView premiumIcon = holder.itemView.findViewById(R.id.premium_icon);
        if (metadata.isPremium()) {
            premiumIcon.setVisibility(View.VISIBLE);
        } else {
            premiumIcon.setVisibility(View.GONE);
        }
        
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSeriesClick(series);
            }
        });
    }

    @Override
    public int getItemCount() {
        return seriesList != null ? seriesList.size() : 0;
    }

    public void updateSeries(List<Series> newSeriesList) {
        this.seriesList = newSeriesList;
        Log.d("SeriesAdapter", "Updated series count: " + (newSeriesList != null ? newSeriesList.size() : 0));
        notifyDataSetChanged();
    }

    static class SeriesViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleTextView;
        TextView accentTextView;
        TextView difficultyTextView;
        TextView countryTextView;
        TextView sourceTextView;

        SeriesViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.series_image);
            titleTextView = itemView.findViewById(R.id.series_title);
            accentTextView = itemView.findViewById(R.id.series_accent);
            difficultyTextView = itemView.findViewById(R.id.series_difficulty);
            countryTextView = itemView.findViewById(R.id.series_country);
            sourceTextView = itemView.findViewById(R.id.series_source);
        }
    }
} 