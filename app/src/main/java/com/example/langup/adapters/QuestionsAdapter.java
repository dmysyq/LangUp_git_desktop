package com.example.langup.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.langup.R;
import com.example.langup.models.Question;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.QuestionViewHolder> {
    private List<Question> questions;
    private List<Set<String>> selectedAnswers;

    public QuestionsAdapter() {
        this.questions = new ArrayList<>();
        this.selectedAnswers = new ArrayList<>();
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
        this.selectedAnswers.clear();
        for (int i = 0; i < questions.size(); i++) {
            selectedAnswers.add(new HashSet<>());
        }
        notifyDataSetChanged();
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
        holder.questionTextView.setText(question.getQuestion());
        
        if (question.isSingleChoice()) {
            holder.radioGroup.setVisibility(View.VISIBLE);
            holder.checkBoxGroup.setVisibility(View.GONE);
            
            holder.radioGroup.removeAllViews();
            for (String option : question.getOptions()) {
                RadioButton radioButton = new RadioButton(holder.itemView.getContext());
                radioButton.setText(option);
                radioButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        selectedAnswers.get(position).clear();
                        selectedAnswers.get(position).add(option);
                    }
                });
                holder.radioGroup.addView(radioButton);
            }
        } else {
            holder.radioGroup.setVisibility(View.GONE);
            holder.checkBoxGroup.setVisibility(View.VISIBLE);
            
            holder.checkBoxGroup.removeAllViews();
            for (String option : question.getOptions()) {
                CheckBox checkBox = new CheckBox(holder.itemView.getContext());
                checkBox.setText(option);
                checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        selectedAnswers.get(position).add(option);
                    } else {
                        selectedAnswers.get(position).remove(option);
                    }
                });
                holder.checkBoxGroup.addView(checkBox);
            }
        }
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public Question getQuestion(int position) {
        return questions.get(position);
    }

    public List<String> getSelectedAnswers(int position) {
        return new ArrayList<>(selectedAnswers.get(position));
    }

    static class QuestionViewHolder extends RecyclerView.ViewHolder {
        TextView questionTextView;
        RadioGroup radioGroup;
        ViewGroup checkBoxGroup;

        QuestionViewHolder(View itemView) {
            super(itemView);
            questionTextView = itemView.findViewById(R.id.questionTextView);
            radioGroup = itemView.findViewById(R.id.radioGroup);
            checkBoxGroup = itemView.findViewById(R.id.checkBoxGroup);
        }
    }
} 