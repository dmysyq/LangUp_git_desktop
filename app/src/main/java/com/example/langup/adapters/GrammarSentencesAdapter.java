package com.example.langup.adapters;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.langup.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GrammarSentencesAdapter extends RecyclerView.Adapter<GrammarSentencesAdapter.SentenceViewHolder> {
    private List<GrammarSentence> sentences;
    private Map<Integer, String[]> userAnswers;
    private boolean showResults = false;

    public GrammarSentencesAdapter(List<GrammarSentence> sentences) {
        this.sentences = sentences;
        this.userAnswers = new HashMap<>();
    }

    @NonNull
    @Override
    public SentenceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_grammar_sentence, parent, false);
        return new SentenceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SentenceViewHolder holder, int position) {
        GrammarSentence sentence = sentences.get(position);
        
        // Собираем предложение для отображения
        StringBuilder displayText = new StringBuilder();
        List<String> parts = sentence.getParts();
        for (int i = 0; i < parts.size(); i++) {
            displayText.append(parts.get(i));
            if (i < parts.size() - 1) {
                displayText.append(" _____ ");
            }
        }
        holder.sentenceText.setText(displayText);

        // Устанавливаем сохраненные ответы пользователя
        String[] answers = userAnswers.get(position);
        if (answers != null) {
            holder.firstAnswerInput.setText(answers[0]);
            if (answers.length > 1) {
                holder.secondAnswerInput.setText(answers[1]);
            }
        }

        // Если показываем результаты
        if (showResults) {
            holder.firstAnswerInput.setEnabled(false);
            holder.secondAnswerInput.setEnabled(false);

            String[] correctAnswers = sentence.getAnswers();
            String[] userAns = userAnswers.get(position);

            if (userAns != null) {
                // Проверяем первый ответ
                if (!TextUtils.isEmpty(userAns[0])) {
                    boolean isFirstCorrect = userAns[0].equals(correctAnswers[0]);
                    int color = isFirstCorrect ? 
                            R.color.correct_answer : R.color.incorrect_answer;
                    holder.firstAnswerContainer.setBoxStrokeColor(
                            ContextCompat.getColor(holder.itemView.getContext(), color));
                }

                // Проверяем второй ответ, если он есть
                if (correctAnswers.length > 1 && userAns.length > 1 && 
                    !TextUtils.isEmpty(userAns[1])) {
                    boolean isSecondCorrect = userAns[1].equals(correctAnswers[1]);
                    int color = isSecondCorrect ? 
                            R.color.correct_answer : R.color.incorrect_answer;
                    holder.secondAnswerContainer.setBoxStrokeColor(
                            ContextCompat.getColor(holder.itemView.getContext(), color));
                }
            }
        } else {
            // Слушатели для ввода ответов
            setupAnswerListener(holder.firstAnswerInput, holder.secondAnswerInput, position);
        }

        // Скрываем второе поле ввода, если оно не нужно
        if (sentence.getAnswers().length == 1) {
            holder.secondAnswerContainer.setVisibility(View.GONE);
        } else {
            holder.secondAnswerContainer.setVisibility(View.VISIBLE);
        }
    }

    private void setupAnswerListener(TextInputEditText first, TextInputEditText second, int position) {
        first.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                saveAnswers(position, first.getText().toString(), second.getText().toString());
            }
        });

        second.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                saveAnswers(position, first.getText().toString(), second.getText().toString());
            }
        });
    }

    private void saveAnswers(int position, String first, String second) {
        userAnswers.put(position, new String[]{first, second});
    }

    @Override
    public int getItemCount() {
        return sentences.size();
    }

    public void showResults() {
        showResults = true;
        notifyDataSetChanged();
    }

    public int getCorrectAnswersCount() {
        int count = 0;
        for (int i = 0; i < sentences.size(); i++) {
            String[] correctAnswers = sentences.get(i).getAnswers();
            String[] userAns = userAnswers.get(i);
            
            if (userAns != null) {
                boolean isCorrect = true;
                for (int j = 0; j < correctAnswers.length; j++) {
                    if (j >= userAns.length || 
                        !correctAnswers[j].equals(userAns[j])) {
                        isCorrect = false;
                        break;
                    }
                }
                if (isCorrect) count++;
            }
        }
        return count;
    }

    static class SentenceViewHolder extends RecyclerView.ViewHolder {
        TextInputEditText sentenceText;
        TextInputEditText firstAnswerInput;
        TextInputEditText secondAnswerInput;
        TextInputLayout firstAnswerContainer;
        TextInputLayout secondAnswerContainer;

        SentenceViewHolder(View itemView) {
            super(itemView);
            sentenceText = itemView.findViewById(R.id.sentenceText);
            firstAnswerInput = itemView.findViewById(R.id.firstAnswerInput);
            secondAnswerInput = itemView.findViewById(R.id.secondAnswerInput);
            firstAnswerContainer = itemView.findViewById(R.id.firstAnswerContainer);
            secondAnswerContainer = itemView.findViewById(R.id.secondAnswerContainer);
        }
    }

    public static class GrammarSentence {
        private final List<String> parts;
        private final String[] answers;

        public GrammarSentence(List<String> parts, String[] answers) {
            this.parts = parts;
            this.answers = answers;
        }

        public List<String> getParts() { return parts; }
        public String[] getAnswers() { return answers; }
    }
} 