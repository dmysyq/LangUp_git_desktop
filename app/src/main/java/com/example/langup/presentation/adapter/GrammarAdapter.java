package com.example.langup.presentation.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.langup.R;
import com.example.langup.presentation.ui.grammar.GrammarExercise;
import com.google.android.material.button.MaterialButton;
import java.util.List;

public class GrammarAdapter extends RecyclerView.Adapter<GrammarAdapter.GrammarViewHolder> {
    private static final String TAG = "GrammarAdapter";
    private final List<GrammarExercise> exercises;

    public GrammarAdapter(List<GrammarExercise> exercises) {
        this.exercises = exercises;
        Log.d(TAG, "Initialized with " + exercises.size() + " exercises");
    }

    @NonNull
    @Override
    public GrammarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_grammar, parent, false);
        Log.d(TAG, "Creating new ViewHolder");
        return new GrammarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GrammarViewHolder holder, int position) {
        GrammarExercise exercise = exercises.get(position);
        Log.d(TAG, "Binding exercise at position " + position + ": " + exercise.getSentence());
        
        holder.sentenceTextView.setText(exercise.getSentence());
        
        // Clear previous options
        holder.optionsRadioGroup.removeAllViews();
        
        // Add radio buttons for options
        for (String option : exercise.getOptions()) {
            RadioButton radioButton = new RadioButton(holder.itemView.getContext());
            radioButton.setText(option);
            radioButton.setEnabled(!exercise.isAnswered());
            holder.optionsRadioGroup.addView(radioButton);
        }

        // Setup check button
        holder.checkButton.setEnabled(!exercise.isAnswered());
        holder.checkButton.setOnClickListener(v -> {
            int selectedId = holder.optionsRadioGroup.getCheckedRadioButtonId();
            if (selectedId != -1) {
                RadioButton selectedButton = holder.optionsRadioGroup.findViewById(selectedId);
                String selectedAnswer = selectedButton.getText().toString();
                exercise.setSelectedAnswer(selectedAnswer);
                
                // Update UI
                updateAnswerColors(holder, exercise);
                holder.checkButton.setEnabled(false);
                
                // Disable radio buttons
                for (int i = 0; i < holder.optionsRadioGroup.getChildCount(); i++) {
                    holder.optionsRadioGroup.getChildAt(i).setEnabled(false);
                }
                
                Log.d(TAG, "Answer checked: " + selectedAnswer + ", Correct: " + exercise.isCorrect());
            }
        });

        // If already answered, show colors
        if (exercise.isAnswered()) {
            updateAnswerColors(holder, exercise);
        }
    }

    private void updateAnswerColors(GrammarViewHolder holder, GrammarExercise exercise) {
        for (int i = 0; i < holder.optionsRadioGroup.getChildCount(); i++) {
            RadioButton button = (RadioButton) holder.optionsRadioGroup.getChildAt(i);
            String option = button.getText().toString();
            
            if (option.equals(exercise.getSelectedAnswer())) {
                if (exercise.isCorrect()) {
                    button.setBackgroundColor(ContextCompat.getColor(button.getContext(), R.color.correct_answer));
                } else {
                    button.setBackgroundColor(ContextCompat.getColor(button.getContext(), R.color.wrong_answer));
                }
            } else if (option.equals(exercise.getCorrectAnswer())) {
                button.setBackgroundColor(ContextCompat.getColor(button.getContext(), R.color.correct_answer));
            }
        }
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    static class GrammarViewHolder extends RecyclerView.ViewHolder {
        TextView sentenceTextView;
        RadioGroup optionsRadioGroup;
        MaterialButton checkButton;

        GrammarViewHolder(View itemView) {
            super(itemView);
            sentenceTextView = itemView.findViewById(R.id.sentenceTextView);
            optionsRadioGroup = itemView.findViewById(R.id.optionsRadioGroup);
            checkButton = itemView.findViewById(R.id.checkButton);
        }
    }
} 