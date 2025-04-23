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
    private List<Question> questions;

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
        
        // Clear previous options
        holder.singleChoiceGroup.removeAllViews();
        holder.multipleChoiceGroup.removeAllViews();
        
        // Initially disable check button
        holder.checkButton.setEnabled(false);
        
        // Add options based on question type
        if (question.isSingleChoice()) {
            Log.d(TAG, "onBindViewHolder: Setting up single choice question");
            holder.singleChoiceGroup.setVisibility(View.VISIBLE);
            holder.multipleChoiceGroup.setVisibility(View.GONE);
            
            for (int i = 0; i < question.getOptions().size(); i++) {
                RadioButton radioButton = new RadioButton(holder.itemView.getContext());
                radioButton.setText(question.getOptions().get(i));
                radioButton.setId(i);
                holder.singleChoiceGroup.addView(radioButton);
            }
            
            // Enable check button when an option is selected
            holder.singleChoiceGroup.setOnCheckedChangeListener((group, checkedId) -> {
                holder.checkButton.setEnabled(checkedId != -1);
            });
            
        } else if (question.isMultipleChoice()) {
            Log.d(TAG, "onBindViewHolder: Setting up multiple choice question");
            holder.singleChoiceGroup.setVisibility(View.GONE);
            holder.multipleChoiceGroup.setVisibility(View.VISIBLE);
            
            for (int i = 0; i < question.getOptions().size(); i++) {
                CheckBox checkBox = new CheckBox(holder.itemView.getContext());
                checkBox.setText(question.getOptions().get(i));
                checkBox.setId(i);
                checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    // Check if any checkbox is checked
                    boolean hasCheckedOption = false;
                    for (int j = 0; j < holder.multipleChoiceGroup.getChildCount(); j++) {
                        CheckBox cb = (CheckBox) holder.multipleChoiceGroup.getChildAt(j);
                        if (cb.isChecked()) {
                            hasCheckedOption = true;
                            break;
                        }
                    }
                    holder.checkButton.setEnabled(hasCheckedOption);
                });
                holder.multipleChoiceGroup.addView(checkBox);
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
                RadioButton selectedButton = holder.itemView.findViewById(selectedId);
                isCorrect = selectedId == question.getCorrectAnswer();
                Log.d(TAG, "checkAnswer: Single choice - isCorrect=" + isCorrect);
                updateOptionColor(selectedButton, isCorrect);
                
                if (!isCorrect) {
                    RadioButton correctButton = holder.itemView.findViewById(question.getCorrectAnswer());
                    updateOptionColor(correctButton, true);
                }
                
                // Disable radio buttons after checking
                for (int i = 0; i < holder.singleChoiceGroup.getChildCount(); i++) {
                    RadioButton radioButton = (RadioButton) holder.singleChoiceGroup.getChildAt(i);
                    radioButton.setEnabled(false);
                }
            } else {
                Log.w(TAG, "checkAnswer: Single choice - No answer selected or correctAnswer is null");
            }
        } else if (question.isMultipleChoice()) {
            List<Integer> selectedAnswers = new ArrayList<>();
            for (int i = 0; i < holder.multipleChoiceGroup.getChildCount(); i++) {
                CheckBox checkBox = (CheckBox) holder.multipleChoiceGroup.getChildAt(i);
                if (checkBox.isChecked()) {
                    selectedAnswers.add(checkBox.getId());
                }
            }
            
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
                    updateOptionColor(checkBox, true);
                } else if (isSelected && !isCorrectAnswer) {
                    updateOptionColor(checkBox, false);
                } else if (!isSelected && isCorrectAnswer) {
                    updateOptionColor(checkBox, true);
                }
                
                // Disable checkboxes after checking
                checkBox.setEnabled(false);
            }
        }
        
        // Disable check button after checking
        holder.checkButton.setEnabled(false);
    }

    private void updateOptionColor(View option, boolean isCorrect) {
        Log.d(TAG, "updateOptionColor: Updating color for option, isCorrect=" + isCorrect);
        int color = isCorrect ? 
            ContextCompat.getColor(option.getContext(), R.color.correct_answer) :
            ContextCompat.getColor(option.getContext(), R.color.wrong_answer);
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