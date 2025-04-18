package com.example.langup;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class VocabularyAdapter extends RecyclerView.Adapter<VocabularyAdapter.VocabularyViewHolder> {
    private List<VocabularyItem> vocabularyItems;
    private boolean showTranslations;

    public VocabularyAdapter(List<VocabularyItem> vocabularyItems, boolean showTranslations) {
        this.vocabularyItems = vocabularyItems;
        this.showTranslations = showTranslations;
    }

    @NonNull
    @Override
    public VocabularyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_vocabulary, parent, false);
        return new VocabularyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VocabularyViewHolder holder, int position) {
        VocabularyItem item = vocabularyItems.get(position);
        holder.wordTextView.setText(item.getWord());
        holder.descriptionTextView.setText(item.getDescription());
        holder.translationTextView.setText(item.getTranslation());
        holder.translationTextView.setVisibility(showTranslations ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return vocabularyItems.size();
    }

    public void setShowTranslations(boolean showTranslations) {
        this.showTranslations = showTranslations;
    }

    public void updateVocabularyItems(List<VocabularyItem> items) {
        this.vocabularyItems = items;
        notifyDataSetChanged();
    }

    static class VocabularyViewHolder extends RecyclerView.ViewHolder {
        TextView wordTextView;
        TextView descriptionTextView;
        TextView translationTextView;

        VocabularyViewHolder(View itemView) {
            super(itemView);
            wordTextView = itemView.findViewById(R.id.wordTextView);
            translationTextView = itemView.findViewById(R.id.translationTextView);
        }
    }
} 