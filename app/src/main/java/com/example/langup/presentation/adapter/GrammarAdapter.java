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
import java.util.ArrayList;
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
        holder.sentenceTextView.setText(exercise.getSentence());
        holder.optionsRadioGroup.setOnCheckedChangeListener(null);
        holder.optionsRadioGroup.removeAllViews();
        holder.checkButton.setEnabled(!exercise.isAnswered());
        List<RadioButton> radioButtons = new ArrayList<>();
        for (int i = 0; i < exercise.getOptions().size(); i++) {
            RadioButton radioButton = new RadioButton(holder.itemView.getContext());
            radioButton.setText(exercise.getOptions().get(i));
            radioButton.setEnabled(!exercise.isAnswered());
            radioButtons.add(radioButton);
            holder.optionsRadioGroup.addView(radioButton);
        }
        // Восстановить выбранный ответ
        if (exercise.getSelectedAnswer() != null) {
            for (int i = 0; i < exercise.getOptions().size(); i++) {
                if (exercise.getOptions().get(i).equals(exercise.getSelectedAnswer())) {
                    RadioButton selected = (RadioButton) holder.optionsRadioGroup.getChildAt(i);
                    if (selected != null) selected.setChecked(true);
                }
            }
        }
        // Если уже отвечено, покрасить варианты
        if (exercise.isAnswered()) {
            for (int i = 0; i < holder.optionsRadioGroup.getChildCount(); i++) {
                RadioButton button = (RadioButton) holder.optionsRadioGroup.getChildAt(i);
                String option = button.getText().toString();
                if (option.equals(exercise.getSelectedAnswer())) {
                    if (exercise.isCorrect()) {
                        button.setBackgroundColor(ContextCompat.getColor(button.getContext(), R.color.correct_answer_light));
                    } else {
                        button.setBackgroundColor(ContextCompat.getColor(button.getContext(), R.color.wrong_answer_light));
                    }
                } else if (option.equals(exercise.getCorrectAnswer())) {
                    if (!option.equals(exercise.getSelectedAnswer())) {
                        button.setBackgroundColor(ContextCompat.getColor(button.getContext(), R.color.warning_light));
                    } else {
                        button.setBackgroundColor(ContextCompat.getColor(button.getContext(), R.color.correct_answer_light));
                    }
                } else {
                    button.setBackgroundColor(0x00000000); // transparent
                }
                button.setEnabled(false);
            }
            holder.checkButton.setEnabled(false);
        } else {
            holder.optionsRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                holder.checkButton.setEnabled(checkedId != -1);
            });
        }
        holder.checkButton.setOnClickListener(v -> {
            int selectedId = holder.optionsRadioGroup.getCheckedRadioButtonId();
            if (selectedId != -1) {
                RadioButton selectedButton = holder.optionsRadioGroup.findViewById(selectedId);
                String selectedAnswer = selectedButton.getText().toString();
                exercise.setSelectedAnswer(selectedAnswer);
                updateAnswerColors(holder, exercise);
                holder.checkButton.setEnabled(false);
                for (int i = 0; i < holder.optionsRadioGroup.getChildCount(); i++) {
                    holder.optionsRadioGroup.getChildAt(i).setEnabled(false);
                }
                if (holder.itemView.getContext() instanceof android.app.Activity) {
                    ((android.app.Activity) holder.itemView.getContext()).runOnUiThread(() -> {
                        if (holder.itemView.getContext() instanceof com.example.langup.presentation.ui.grammar.GrammarActivity) {
                            ((com.example.langup.presentation.ui.grammar.GrammarActivity) holder.itemView.getContext()).updateResultButtonState();
                        }
                    });
                }
            }
        });
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
                    button.setBackgroundColor(ContextCompat.getColor(button.getContext(), R.color.correct_answer_light));
                } else {
                    button.setBackgroundColor(ContextCompat.getColor(button.getContext(), R.color.wrong_answer_light));
                }
            } else if (option.equals(exercise.getCorrectAnswer())) {
                if (!option.equals(exercise.getSelectedAnswer())) {
                    button.setBackgroundColor(ContextCompat.getColor(button.getContext(), R.color.warning_light));
                } else {
                    button.setBackgroundColor(ContextCompat.getColor(button.getContext(), R.color.correct_answer_light));
                }
            } else {
                button.setBackgroundColor(0x00000000); // transparent
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