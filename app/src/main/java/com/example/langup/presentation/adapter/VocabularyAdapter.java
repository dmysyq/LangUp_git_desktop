package com.example.langup.presentation.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.langup.R;
import com.example.langup.domain.model.VocabularyItem;
import java.util.List;

public class VocabularyAdapter extends RecyclerView.Adapter<VocabularyAdapter.VocabularyViewHolder> {
    private List<VocabularyItem> vocabularyItems;
    private boolean isTranslationVisible = false;

    public VocabularyAdapter(List<VocabularyItem> vocabularyItems) {
        this.vocabularyItems = vocabularyItems;
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
        holder.exampleTextView.setText(item.getExample());
        holder.wordTextView.setText(item.getWord());
        holder.translationTextView.setText(item.getTranslation());
        holder.translationContainer.setVisibility(isTranslationVisible ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return vocabularyItems != null ? vocabularyItems.size() : 0;
    }

    public void toggleTranslation() {
        isTranslationVisible = !isTranslationVisible;
        notifyDataSetChanged();
    }

    static class VocabularyViewHolder extends RecyclerView.ViewHolder {
        TextView exampleTextView;
        TextView wordTextView;
        TextView translationTextView;
        LinearLayout translationContainer;

        VocabularyViewHolder(View itemView) {
            super(itemView);
            exampleTextView = itemView.findViewById(R.id.exampleTextView);
            wordTextView = itemView.findViewById(R.id.wordTextView);
            translationTextView = itemView.findViewById(R.id.translationTextView);
            translationContainer = itemView.findViewById(R.id.translationContainer);
        }
    }
} 