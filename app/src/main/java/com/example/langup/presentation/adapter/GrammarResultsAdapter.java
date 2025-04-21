package com.example.langup.presentation.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.langup.R;
import com.example.langup.domain.model.GrammarResult;

import java.util.ArrayList;
import java.util.List;

public class GrammarResultsAdapter extends RecyclerView.Adapter<GrammarResultsAdapter.ViewHolder> {
    private List<GrammarResult> results;

    public GrammarResultsAdapter() {
        this.results = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_grammar_result, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GrammarResult result = results.get(position);
        holder.questionText.setText(result.getQuestion());
        holder.userAnswerText.setText(result.getUserAnswer());
        holder.correctAnswerText.setText(result.getCorrectAnswer());
        
        String explanation = result.getExplanation();
        if (explanation != null && !explanation.isEmpty()) {
            holder.explanationText.setVisibility(View.VISIBLE);
            holder.explanationText.setText(explanation);
        } else {
            holder.explanationText.setVisibility(View.GONE);
        }

        // Set text colors based on correctness
        int textColor = result.isCorrect() ? 
                holder.itemView.getContext().getColor(R.color.correct_answer) :
                holder.itemView.getContext().getColor(R.color.incorrect_answer);
        holder.userAnswerText.setTextColor(textColor);
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public void updateResults(List<GrammarResult> newResults) {
        this.results = newResults;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView questionText;
        TextView userAnswerText;
        TextView correctAnswerText;
        TextView explanationText;

        ViewHolder(View itemView) {
            super(itemView);
            questionText = itemView.findViewById(R.id.questionText);
            userAnswerText = itemView.findViewById(R.id.userAnswerText);
            correctAnswerText = itemView.findViewById(R.id.correctAnswerText);
            explanationText = itemView.findViewById(R.id.explanationText);
        }
    }
} 