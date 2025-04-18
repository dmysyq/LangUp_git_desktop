package com.example.langup.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.langup.R;
import com.example.langup.models.GrammarSentence;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.util.List;

public class GrammarSentenceAdapter extends RecyclerView.Adapter<GrammarSentenceAdapter.ViewHolder> {
    private List<GrammarSentence> sentences;
    private OnAnswerChangedListener listener;
    private RecyclerView recyclerView;

    public interface OnAnswerChangedListener {
        void onAnswerChanged(int position, String answer);
    }

    public GrammarSentenceAdapter(List<GrammarSentence> sentences, OnAnswerChangedListener listener) {
        this.sentences = sentences;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sentence, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GrammarSentence sentence = sentences.get(position);
        StringBuilder displayText = new StringBuilder();
        
        for (int i = 0; i < sentence.getParts().size(); i++) {
            displayText.append(sentence.getParts().get(i));
            if (i < sentence.getParts().size() - 1) {
                displayText.append(" _____ ");
            }
        }
        
        holder.sentenceTextView.setText(displayText.toString());
        
        holder.answerEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                listener.onAnswerChanged(position, holder.answerEditText.getText().toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return sentences.size();
    }

    public void showFeedback(int position, boolean isCorrect) {
        if (recyclerView != null) {
            ViewHolder holder = (ViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
            if (holder != null) {
                holder.feedbackText.setVisibility(View.VISIBLE);
                holder.feedbackText.setText(isCorrect ? R.string.correct_answer : R.string.incorrect_answer);
                holder.feedbackText.setTextColor(holder.itemView.getContext().getResources()
                    .getColor(isCorrect ? R.color.correct_answer : R.color.incorrect_answer));
            }
        }
    }

    public void resetFeedback() {
        if (recyclerView != null) {
            for (int i = 0; i < sentences.size(); i++) {
                ViewHolder holder = (ViewHolder) recyclerView.findViewHolderForAdapterPosition(i);
                if (holder != null) {
                    holder.feedbackText.setVisibility(View.GONE);
                    holder.answerEditText.setText("");
                }
            }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView sentenceTextView;
        private TextInputLayout answerInputLayout;
        private TextInputEditText answerEditText;
        private TextView feedbackText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sentenceTextView = itemView.findViewById(R.id.sentenceTextView);
            answerInputLayout = itemView.findViewById(R.id.answerInputLayout);
            answerEditText = itemView.findViewById(R.id.answerEditText);
            feedbackText = itemView.findViewById(R.id.feedbackText);
        }
    }
} 