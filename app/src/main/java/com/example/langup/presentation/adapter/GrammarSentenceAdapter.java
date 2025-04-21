package com.example.langup.presentation.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.langup.R;
import com.example.langup.domain.model.GrammarSentence;
import java.util.List;

public class GrammarSentenceAdapter extends RecyclerView.Adapter<GrammarSentenceAdapter.ViewHolder> {
    private List<GrammarSentence> sentences;
    private String selectedWord;
    private RecyclerView recyclerView;

    public GrammarSentenceAdapter(List<GrammarSentence> sentences) {
        this.sentences = sentences;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_grammar_sentence, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GrammarSentence sentence = sentences.get(position);
        holder.sentenceText.setText(sentence.getSentence());
        holder.answerEdit.setText(sentence.getUserAnswer());
        holder.feedbackText.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return sentences.size();
    }

    public void setSentences(List<GrammarSentence> sentences) {
        this.sentences = sentences;
        notifyDataSetChanged();
    }

    public void setSelectedWord(String word) {
        this.selectedWord = word;
        // Find the first empty answer field
        for (int i = 0; i < sentences.size(); i++) {
            GrammarSentence sentence = sentences.get(i);
            if (sentence.getUserAnswer() == null || sentence.getUserAnswer().isEmpty()) {
                sentence.setUserAnswer(word);
                notifyItemChanged(i);
                break;
            }
        }
    }

    public void resetFeedback() {
        for (GrammarSentence sentence : sentences) {
            sentence.setUserAnswer("");
        }
        notifyDataSetChanged();
    }

    public void showFeedback() {
        for (int i = 0; i < sentences.size(); i++) {
            GrammarSentence sentence = sentences.get(i);
            ViewHolder holder = (ViewHolder) recyclerView.findViewHolderForAdapterPosition(i);
            if (holder != null) {
                if (sentence.isCorrect()) {
                    holder.feedbackText.setText(R.string.correct);
                    holder.feedbackText.setTextColor(holder.itemView.getContext().getColor(R.color.correct_answer));
                } else {
                    holder.feedbackText.setText(sentence.getExplanation());
                    holder.feedbackText.setTextColor(holder.itemView.getContext().getColor(R.color.incorrect_answer));
                }
                holder.feedbackText.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView sentenceText;
        EditText answerEdit;
        TextView feedbackText;

        ViewHolder(View itemView) {
            super(itemView);
            sentenceText = itemView.findViewById(R.id.sentenceText);
            answerEdit = itemView.findViewById(R.id.answerEdit);
            feedbackText = itemView.findViewById(R.id.feedbackText);
        }
    }
} 