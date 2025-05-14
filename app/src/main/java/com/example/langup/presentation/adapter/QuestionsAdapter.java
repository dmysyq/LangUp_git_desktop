package com.example.langup.presentation.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.langup.R;
import com.example.langup.domain.model.SeriesContent.Question;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.QuestionViewHolder> {
    private static final String TAG = "QuestionsAdapter";
    private final List<Question> questions;

    public QuestionsAdapter(List<Question> questions) {
        this.questions = questions;
        Log.d(TAG, "Constructor: Initialized with " + questions.size() + " questions");
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: Creating new view holder");
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_question, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        Question question = questions.get(position);
        Log.d(TAG, String.format("onBindViewHolder: Binding question at position %d: id=%s, type=%s",
            position, question.getId(), question.getType()));
        
        holder.questionTextView.setText(question.getQuestion());
        holder.singleChoiceGroup.setOnCheckedChangeListener(null);
        holder.singleChoiceGroup.removeAllViews();
        holder.multipleChoiceGroup.removeAllViews();
        holder.checkButton.setEnabled(false);

        if (question.isSingleChoice()) {
            holder.singleChoiceGroup.setVisibility(View.VISIBLE);
            holder.multipleChoiceGroup.setVisibility(View.GONE);
            List<RadioButton> radioButtons = new ArrayList<>();
            for (int i = 0; i < question.getOptions().size(); i++) {
                RadioButton radioButton = new RadioButton(holder.itemView.getContext());
                radioButton.setText(question.getOptions().get(i));
                radioButton.setId(i);
                radioButton.setEnabled(!question.isAnswered());
                radioButtons.add(radioButton);
                holder.singleChoiceGroup.addView(radioButton);
            }
            // Восстановить выбранный ответ
            if (question.getSelectedAnswer() != null) {
                RadioButton selected = holder.singleChoiceGroup.findViewById(question.getSelectedAnswer());
                if (selected != null) selected.setChecked(true);
            }
            // Если уже отвечено, покрасить варианты
            if (question.isAnswered()) {
                for (int i = 0; i < holder.singleChoiceGroup.getChildCount(); i++) {
                    RadioButton rb = (RadioButton) holder.singleChoiceGroup.getChildAt(i);
                    if (question.getSelectedAnswer() != null && i == question.getSelectedAnswer()) {
                        updateOptionColor(rb, question.isCorrect(), false);
                    } else if (question.getCorrectAnswer() != null && i == question.getCorrectAnswer() && !question.isCorrect()) {
                        updateOptionColor(rb, true, false);
                    }
                    rb.setEnabled(false);
                }
                holder.checkButton.setEnabled(false);
            } else {
                holder.singleChoiceGroup.setOnCheckedChangeListener((group, checkedId) -> {
                    holder.checkButton.setEnabled(checkedId != -1);
                });
            }
        } else if (question.isMultipleChoice()) {
            holder.singleChoiceGroup.setVisibility(View.GONE);
            holder.multipleChoiceGroup.setVisibility(View.VISIBLE);
            final int maxSelectable = question.getCorrectAnswers() != null ? question.getCorrectAnswers().size() : 1;
            List<CheckBox> checkBoxes = new ArrayList<>();
            for (int i = 0; i < question.getOptions().size(); i++) {
                CheckBox checkBox = new CheckBox(holder.itemView.getContext());
                checkBox.setText(question.getOptions().get(i));
                checkBox.setId(i);
                checkBox.setEnabled(!question.isAnswered());
                checkBoxes.add(checkBox);
                // Восстановить выбранные
                if (question.getSelectedAnswers() != null && question.getSelectedAnswers().contains(i)) {
                    checkBox.setChecked(true);
                }
                checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    int checkedCount = 0;
                    for (CheckBox cb : checkBoxes) {
                        if (cb.isChecked()) checkedCount++;
                    }
                    if (checkedCount > maxSelectable) {
                        buttonView.setChecked(false);
                        return;
                    }
                    boolean hasCheckedOption = checkedCount > 0;
                    holder.checkButton.setEnabled(hasCheckedOption);
                });
                holder.multipleChoiceGroup.addView(checkBox);
            }
            // Если уже отвечено, покрасить варианты
            if (question.isAnswered()) {
                for (int i = 0; i < holder.multipleChoiceGroup.getChildCount(); i++) {
                    CheckBox cb = (CheckBox) holder.multipleChoiceGroup.getChildAt(i);
                    boolean isSelected = question.getSelectedAnswers() != null && question.getSelectedAnswers().contains(i);
                    boolean isCorrectAnswer = question.getCorrectAnswers() != null && question.getCorrectAnswers().contains(i);
                    if (isSelected && isCorrectAnswer) {
                        updateOptionColor(cb, true, false);
                    } else if (isSelected) {
                        updateOptionColor(cb, false, false);
                    } else if (isCorrectAnswer) {
                        updateOptionColor(cb, false, true);
                    }
                    cb.setEnabled(false);
                }
                holder.checkButton.setEnabled(false);
            }
        }
        holder.checkButton.setOnClickListener(v -> checkAnswer(holder, question));
    }

    private void checkAnswer(QuestionViewHolder holder, Question question) {
        Log.d(TAG, String.format("checkAnswer: Checking answer for question id=%s, type=%s",
            question.getId(), question.getType()));
        
        boolean isCorrect = false;
        
        if (question.isSingleChoice()) {
            int selectedId = holder.singleChoiceGroup.getCheckedRadioButtonId();
            Log.d(TAG, String.format("checkAnswer: Single choice - selectedId=%d, correctAnswer=%s",
                selectedId, question.getCorrectAnswer()));
            
            if (selectedId != -1 && question.getCorrectAnswer() != null) {
                question.setSelectedAnswer(selectedId);
                RadioButton selectedButton = holder.itemView.findViewById(selectedId);
                isCorrect = selectedId == question.getCorrectAnswer();
                Log.d(TAG, "checkAnswer: Single choice - isCorrect=" + isCorrect);
                updateOptionColor(selectedButton, isCorrect, false);
                
                if (!isCorrect) {
                    RadioButton correctButton = holder.itemView.findViewById(question.getCorrectAnswer());
                    updateOptionColor(correctButton, true, false);
                }
                
                // Disable radio buttons after checking
                for (int i = 0; i < holder.singleChoiceGroup.getChildCount(); i++) {
                    RadioButton radioButton = (RadioButton) holder.singleChoiceGroup.getChildAt(i);
                    radioButton.setEnabled(false);
                }
            } else {
                Log.w(TAG, "checkAnswer: Single choice - No answer selected or correctAnswer is null");
            }
        } else {
            List<Integer> selectedAnswers = new ArrayList<>();
            for (int i = 0; i < holder.multipleChoiceGroup.getChildCount(); i++) {
                CheckBox checkBox = (CheckBox) holder.multipleChoiceGroup.getChildAt(i);
                if (checkBox.isChecked()) {
                    selectedAnswers.add(checkBox.getId());
                }
            }
            question.setSelectedAnswers(selectedAnswers);
            Log.d(TAG, String.format("checkAnswer: Multiple choice - selectedAnswers=%s, correctAnswers=%s",
                selectedAnswers, question.getCorrectAnswers()));
            
            isCorrect = selectedAnswers.equals(question.getCorrectAnswers());
            Log.d(TAG, "checkAnswer: Multiple choice - isCorrect=" + isCorrect);
            
            // Update colors for all options
            for (int i = 0; i < holder.multipleChoiceGroup.getChildCount(); i++) {
                CheckBox checkBox = (CheckBox) holder.multipleChoiceGroup.getChildAt(i);
                boolean isSelected = selectedAnswers.contains(i);
                boolean isCorrectAnswer = question.getCorrectAnswers() != null && 
                    question.getCorrectAnswers().contains(i);
                
                if (isSelected && isCorrectAnswer) {
                    updateOptionColor(checkBox, true, false); // green
                } else if (isSelected) {
                    updateOptionColor(checkBox, false, false); // red
                } else if (isCorrectAnswer) {
                    updateOptionColor(checkBox, false, true); // yellow
                }
                // Disable checkboxes after checking
                checkBox.setEnabled(false);
            }
        }
        // Disable check button after checking
        holder.checkButton.setEnabled(false);
        // Оповестим активность, что был дан ответ (чтобы обновить счетчик)
        if (holder.itemView.getContext() instanceof android.app.Activity) {
            ((android.app.Activity) holder.itemView.getContext()).runOnUiThread(() -> {
                if (holder.itemView.getContext() instanceof com.example.langup.presentation.ui.questions.QuestionsActivity) {
                    ((com.example.langup.presentation.ui.questions.QuestionsActivity) holder.itemView.getContext()).updateResultButtonState();
                }
            });
        }
    }

    private void updateOptionColor(View option, boolean isCorrect, boolean isWarning) {
        int color;
        if (isWarning) {
            color = ContextCompat.getColor(option.getContext(), R.color.warning_light);
        } else if (isCorrect) {
            color = ContextCompat.getColor(option.getContext(), R.color.correct_answer_light);
        } else {
            color = ContextCompat.getColor(option.getContext(), R.color.wrong_answer_light);
        }
        option.setBackgroundColor(color);
    }

    @Override
    public int getItemCount() {
        return questions != null ? questions.size() : 0;
    }

    static class QuestionViewHolder extends RecyclerView.ViewHolder {
        TextView questionTextView;
        RadioGroup singleChoiceGroup;
        LinearLayout multipleChoiceGroup;
        MaterialButton checkButton;

        QuestionViewHolder(View itemView) {
            super(itemView);
            questionTextView = itemView.findViewById(R.id.questionTextView);
            singleChoiceGroup = itemView.findViewById(R.id.singleChoiceGroup);
            multipleChoiceGroup = itemView.findViewById(R.id.multipleChoiceGroup);
            checkButton = itemView.findViewById(R.id.checkButton);
        }
    }
} 