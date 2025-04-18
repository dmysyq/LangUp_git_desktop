package com.example.langup.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.langup.R;
import com.example.langup.models.VocabularyWord;

import java.util.List;

public class VocabularyAdapter extends RecyclerView.Adapter<VocabularyAdapter.ViewHolder> {
    private final List<VocabularyWord> words;
    private boolean showTranslations;

    public VocabularyAdapter(List<VocabularyWord> words, boolean showTranslations) {
        this.words = words;
        this.showTranslations = showTranslations;
    }

    public void setShowTranslations(boolean showTranslations) {
        this.showTranslations = showTranslations;
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
        VocabularyWord word = words.get(position);
        holder.wordTextView.setText(word.getWord());
        holder.definitionTextView.setText(word.getDefinition());
        holder.translationTextView.setText(word.getTranslation());
        holder.translationTextView.setVisibility(showTranslations ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return words.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView wordTextView;
        final TextView definitionTextView;
        final TextView translationTextView;

        ViewHolder(View view) {
            super(view);
            wordTextView = view.findViewById(R.id.wordTextView);
            definitionTextView = view.findViewById(R.id.definitionTextView);
            translationTextView = view.findViewById(R.id.translationTextView);
        }
    }
} 