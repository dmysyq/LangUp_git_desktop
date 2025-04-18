package com.example.langup.adapters;

import android.animation.ValueAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.langup.R;
import com.example.langup.models.VocabularyItem;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VocabularyAdapter extends RecyclerView.Adapter<VocabularyAdapter.ViewHolder> {
    private List<VocabularyItem> vocabularyItems;
    private boolean showTranslations = false;
    private static final Pattern WORD_PATTERN = Pattern.compile("(\\w+)\\s*\\[(\\w+)\\]\\s*-\\s*(.+)");

    public VocabularyAdapter(List<VocabularyItem> vocabularyItems) {
        this.vocabularyItems = vocabularyItems;
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
        
        // Парсим слово, часть речи и определение
        Matcher matcher = WORD_PATTERN.matcher(item.getExample());
        if (matcher.find()) {
            holder.wordTextView.setText(matcher.group(1));
            holder.partOfSpeechTextView.setText("[" + matcher.group(2) + "]");
            holder.definitionTextView.setText("- " + matcher.group(3));
        } else {
            holder.wordTextView.setText(item.getWord());
            holder.definitionTextView.setText(item.getExample());
        }
        
        holder.translationTextView.setText(item.getTranslation());
        
        // Устанавливаем видимость перевода
        if (showTranslations) {
            holder.translationTextView.setAlpha(1f);
        } else {
            holder.translationTextView.setAlpha(0f);
        }
    }

    @Override
    public int getItemCount() {
        return vocabularyItems.size();
    }

    public void setShowTranslations(boolean show) {
        this.showTranslations = show;
        notifyDataSetChanged();
    }

    public void animateTranslations(boolean show) {
        this.showTranslations = show;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView wordTextView;
        TextView partOfSpeechTextView;
        TextView definitionTextView;
        TextView translationTextView;

        ViewHolder(View itemView) {
            super(itemView);
            wordTextView = itemView.findViewById(R.id.wordTextView);
            partOfSpeechTextView = itemView.findViewById(R.id.partOfSpeechTextView);
            definitionTextView = itemView.findViewById(R.id.definitionTextView);
            translationTextView = itemView.findViewById(R.id.translationTextView);
        }
    }
} 