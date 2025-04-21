package com.example.langup.presentation.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.langup.R;
import com.example.langup.domain.model.Series.VocabularyItem;

import java.util.ArrayList;
import java.util.List;

public class VocabularyAdapter extends RecyclerView.Adapter<VocabularyAdapter.ViewHolder> {
    private List<VocabularyItem> vocabularyItems = new ArrayList<>();
    private boolean showTranslations = false;

    public void setVocabularyItems(List<VocabularyItem> items) {
        this.vocabularyItems = items;
        notifyDataSetChanged();
    }

    public void animateTranslations(boolean show) {
        this.showTranslations = show;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_vocabulary, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VocabularyItem item = vocabularyItems.get(position);
        holder.wordTextView.setText(item.getWord());
        holder.translationTextView.setText(item.getTranslation());
        holder.exampleTextView.setText(item.getExample());
        
        holder.translationTextView.setVisibility(showTranslations ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return vocabularyItems.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView wordTextView;
        TextView translationTextView;
        TextView exampleTextView;

        ViewHolder(View itemView) {
            super(itemView);
            wordTextView = itemView.findViewById(R.id.wordTextView);
            translationTextView = itemView.findViewById(R.id.translationTextView);
            exampleTextView = itemView.findViewById(R.id.exampleTextView);
        }
    }
} 