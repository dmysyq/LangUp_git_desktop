package com.example.langup.domain.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

public class SeriesWrapper implements Serializable {
    @SerializedName("series")
    private List<Series> series;

    public List<Series> getSeries() {
        return series;
    }

    public void setSeries(List<Series> series) {
        this.series = series;
    }
} 