package com.example.langup.presentation.base;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.langup.domain.utils.LocaleManager;

public abstract class BaseFragment extends Fragment {
    private LocaleManager localeManager;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        localeManager = LocaleManager.getInstance(context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        localeManager = LocaleManager.getInstance(requireContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null) {
            // Проверяем, не изменился ли язык
            String currentLanguage = localeManager.getCurrentLanguage();
            if (!currentLanguage.equals(getResources().getConfiguration().locale.getLanguage())) {
                localeManager.forceLocaleUpdate(getActivity());
            }
        }
    }

    protected LocaleManager getLocaleManager() {
        return localeManager;
    }
} 