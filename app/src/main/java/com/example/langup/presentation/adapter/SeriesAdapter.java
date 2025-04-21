package com.example.langup.presentation.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.langup.R;
import com.example.langup.domain.model.Series;
import com.example.langup.domain.model.SeriesMetadata;

import java.util.List;

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
        
        holder.titleTextView.setText(metadata.getTitle());
        holder.descriptionTextView.setText(metadata.getDescription());
        
        // Здесь можно добавить загрузку изображения с помощью Glide или Picasso
        // Glide.with(holder.itemView.getContext()).load(metadata.getImageUrl()).into(holder.imageView);
        
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
        notifyDataSetChanged();
    }

    static class SeriesViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleTextView;
        TextView descriptionTextView;

        SeriesViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.series_image);
            titleTextView = itemView.findViewById(R.id.series_title);
            descriptionTextView = itemView.findViewById(R.id.series_description);
        }
    }
} 