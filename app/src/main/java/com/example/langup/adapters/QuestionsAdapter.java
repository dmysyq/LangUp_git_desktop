package com.example.langup.adapters;

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
import com.example.langup.models.QuestionsData.Question;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.QuestionViewHolder> {
    private List<Question> questions;
    private Map<Integer, Integer> singleChoiceAnswers;
    private Map<Integer, List<Integer>> multipleChoiceAnswers;
    private boolean showResults = false;

    public QuestionsAdapter(List<Question> questions) {
        this.questions = questions;
        this.singleChoiceAnswers = new HashMap<>();
        this.multipleChoiceAnswers = new HashMap<>();
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_question, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        Question question = questions.get(position);
        holder.questionText.setText(question.getQuestion());
        
        if (question.isSingleChoice()) {
            setupSingleChoice(holder, question, position);
        } else if (question.isMultipleChoice()) {
            setupMultipleChoice(holder, question, position);
        }
    }

    private void setupSingleChoice(QuestionViewHolder holder, Question question, int position) {
        holder.radioGroup.setVisibility(View.VISIBLE);
        holder.checkboxContainer.setVisibility(View.GONE);
        
        holder.radioGroup.removeAllViews();
        for (int i = 0; i < question.getOptions().size(); i++) {
            RadioButton radioButton = new RadioButton(holder.itemView.getContext());
            radioButton.setText(question.getOptions().get(i));
            radioButton.setId(i);
            
            if (showResults) {
                if (i == question.getCorrectAnswer()) {
                    radioButton.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.correct_answer));
                } else if (singleChoiceAnswers.get(position) != null && 
                         singleChoiceAnswers.get(position) == i) {
                    radioButton.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.incorrect_answer));
                }
                radioButton.setEnabled(false);
            }
            
            holder.radioGroup.addView(radioButton);
        }

        Integer selectedAnswer = singleChoiceAnswers.get(position);
        if (selectedAnswer != null) {
            holder.radioGroup.check(selectedAnswer);
        }

        if (!showResults) {
            holder.radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                if (checkedId != -1) {
                    singleChoiceAnswers.put(position, checkedId);
                }
            });
        }
    }

    private void setupMultipleChoice(QuestionViewHolder holder, Question question, int position) {
        holder.radioGroup.setVisibility(View.GONE);
        holder.checkboxContainer.setVisibility(View.VISIBLE);
        
        holder.checkboxContainer.removeAllViews();
        for (int i = 0; i < question.getOptions().size(); i++) {
            CheckBox checkBox = new CheckBox(holder.itemView.getContext());
            checkBox.setText(question.getOptions().get(i));
            checkBox.setId(i);
            
            if (showResults) {
                if (question.getCorrectAnswers().contains(i)) {
                    checkBox.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.correct_answer));
                } else {
                    List<Integer> selectedAnswers = multipleChoiceAnswers.get(position);
                    if (selectedAnswers != null && selectedAnswers.contains(i)) {
                        checkBox.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.incorrect_answer));
                    }
                }
                checkBox.setEnabled(false);
            }
            
            List<Integer> selectedAnswers = multipleChoiceAnswers.get(position);
            if (selectedAnswers != null && selectedAnswers.contains(i)) {
                checkBox.setChecked(true);
            }
            
            if (!showResults) {
                final int optionIndex = i;
                checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    List<Integer> answers = multipleChoiceAnswers.getOrDefault(position, new ArrayList<>());
                    if (isChecked) {
                        if (!answers.contains(optionIndex)) {
                            answers.add(optionIndex);
                        }
                    } else {
                        answers.remove(Integer.valueOf(optionIndex));
                    }
                    multipleChoiceAnswers.put(position, answers);
                });
            }
            
            holder.checkboxContainer.addView(checkBox);
        }
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public Integer getSelectedAnswer(int position) {
        return singleChoiceAnswers.get(position);
    }

    public List<Integer> getSelectedAnswers(int position) {
        return multipleChoiceAnswers.getOrDefault(position, new ArrayList<>());
    }

    public void showResults() {
        showResults = true;
        notifyDataSetChanged();
    }

    static class QuestionViewHolder extends RecyclerView.ViewHolder {
        TextView questionText;
        RadioGroup radioGroup;
        LinearLayout checkboxContainer;

        QuestionViewHolder(View itemView) {
            super(itemView);
            questionText = itemView.findViewById(R.id.questionText);
            radioGroup = itemView.findViewById(R.id.radioGroup);
            checkboxContainer = itemView.findViewById(R.id.checkboxContainer);
        }
    }
} 