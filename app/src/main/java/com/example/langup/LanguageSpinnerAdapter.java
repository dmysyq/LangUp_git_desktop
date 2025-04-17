package com.example.langup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class LanguageSpinnerAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final List<String> languages;

    public LanguageSpinnerAdapter(@NonNull Context context, @NonNull List<String> languages) {
        super(context, 0, languages);
        this.context = context;
        this.languages = languages;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent, R.layout.spinner_item);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent, R.layout.spinner_dropdown_item);
    }

    private View createItemView(int position, View convertView, ViewGroup parent, int layoutId) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        }

        TextView textView = view.findViewById(android.R.id.text1);
        textView.setText(languages.get(position));

        return view;
    }
} 