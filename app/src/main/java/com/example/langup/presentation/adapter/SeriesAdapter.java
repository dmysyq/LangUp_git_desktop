package com.example.langup.presentation.adapter;

import android.content.Context;
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
import java.util.List;

public class SeriesAdapter extends RecyclerView.Adapter<SeriesAdapter.SeriesViewHolder> {
    private List<Series> seriesList;
    private OnSeriesClickListener listener;

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
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_series, parent, false);
        return new SeriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SeriesViewHolder holder, int position) {
        Series series = seriesList.get(position);
        Context context = holder.itemView.getContext();
        
        holder.titleTextView.setText(series.getTitle());
        holder.accentTextView.setText(series.getAccent());
        holder.sourceTextView.setText(series.getSource());
        
        // Convert difficulty level to text
        String difficultyText;
        switch (series.getDifficulty()) {
            case 1:
                difficultyText = context.getString(R.string.beginner);
                break;
            case 2:
                difficultyText = context.getString(R.string.intermediate);
                break;
            case 3:
                difficultyText = context.getString(R.string.advanced);
                break;
            default:
                difficultyText = context.getString(R.string.unknown_level);
                break;
        }
        holder.levelTextView.setText(difficultyText);
        
        // Load image using Glide
        if (series.getImageUrl() != null && !series.getImageUrl().isEmpty()) {
            Glide.with(context)
                    .load(series.getImageUrl())
                    .placeholder(R.drawable.ic_image_placeholder)
                    .error(R.drawable.ic_image_error)
                    .into(holder.imageView);
        } else {
            holder.imageView.setImageResource(R.drawable.ic_image_placeholder);
        }
        
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onSeriesClick(series);
            }
        });
    }

    @Override
    public int getItemCount() {
        return seriesList.size();
    }

    public void updateSeries(List<Series> newSeriesList) {
        this.seriesList = newSeriesList;
        notifyDataSetChanged();
    }

    static class SeriesViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleTextView;
        TextView accentTextView;
        TextView sourceTextView;
        TextView levelTextView;

        SeriesViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.seriesImageView);
            titleTextView = itemView.findViewById(R.id.seriesTitleTextView);
            accentTextView = itemView.findViewById(R.id.seriesAccentTextView);
            sourceTextView = itemView.findViewById(R.id.seriesSourceTextView);
            levelTextView = itemView.findViewById(R.id.seriesLevelTextView);
        }
    }
} 