package com.example.langup.presentation.ui.questions;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.langup.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class QuestionsResultsAdapter extends RecyclerView.Adapter<QuestionsResultsAdapter.ViewHolder> {
    private List<JSONObject> results = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_question_result, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            JSONObject result = results.get(position);
            holder.questionTextView.setText(result.getString("question"));
            holder.userAnswerTextView.setText(result.getString("userAnswer"));
            holder.correctAnswerTextView.setText(result.getString("correctAnswer"));
            
            boolean isCorrect = result.getBoolean("isCorrect");
            int textColor = holder.itemView.getContext().getColor(
                isCorrect ? R.color.correct_answer : R.color.incorrect_answer
            );
            holder.userAnswerTextView.setTextColor(textColor);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public void updateResults(String resultsJson) {
        results.clear();
        try {
            JSONArray resultsArray = new JSONArray(resultsJson);
            for (int i = 0; i < resultsArray.length(); i++) {
                results.add(resultsArray.getJSONObject(i));
            }
            notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView questionTextView;
        TextView userAnswerTextView;
        TextView correctAnswerTextView;

        ViewHolder(View itemView) {
            super(itemView);
            questionTextView = itemView.findViewById(R.id.questionTextView);
            userAnswerTextView = itemView.findViewById(R.id.userAnswerTextView);
            correctAnswerTextView = itemView.findViewById(R.id.correctAnswerTextView);
        }
    }
} 