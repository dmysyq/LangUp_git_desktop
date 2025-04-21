package com.example.langup.presentation.ui.main;

import com.example.langup.domain.model.SeriesMetadata;
import java.util.List;

public class CardState {
    public enum Status {
        LOADING,
        SUCCESS,
        ERROR
    }

    private Status status;
    private List<SeriesMetadata> seriesMetadataList;
    private String errorMessage;

    private CardState(Status status, List<SeriesMetadata> seriesMetadataList, String errorMessage) {
        this.status = status;
        this.seriesMetadataList = seriesMetadataList;
        this.errorMessage = errorMessage;
    }

    public static CardState loading() {
        return new CardState(Status.LOADING, null, null);
    }

    public static CardState success(List<SeriesMetadata> seriesMetadataList) {
        return new CardState(Status.SUCCESS, seriesMetadataList, null);
    }

    public static CardState error(String errorMessage) {
        return new CardState(Status.ERROR, null, errorMessage);
    }

    public Status getStatus() {
        return status;
    }

    public List<SeriesMetadata> getSeriesMetadataList() {
        return seriesMetadataList;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
} 