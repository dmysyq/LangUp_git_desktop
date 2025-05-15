package com.example.langup.presentation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import com.example.langup.R;
import com.example.langup.presentation.model.LanguageItem;
import java.util.List;

public class LanguageSelectorAdapter extends ArrayAdapter<LanguageItem> {

    public LanguageSelectorAdapter(Context context, List<LanguageItem> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_language_selector, parent, false);
        }
        LanguageItem item = getItem(position);
        ImageView flagImageView = convertView.findViewById(R.id.flagImageView);
        flagImageView.setImageResource(item.getDrawableResId());
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }
} 