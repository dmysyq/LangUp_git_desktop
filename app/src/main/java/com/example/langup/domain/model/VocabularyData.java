package com.example.langup.domain.model;

import java.util.List;

public class VocabularyData {
    private Metadata metadata;
    private List<VocabularyItem> vocabulary;

    public Metadata getMetadata() {
        return metadata;
    }

    public List<VocabularyItem> getVocabulary() {
        return vocabulary;
    }

    public static class Metadata {
        private String title;
        private String series;
        private int season;
        private int episode;
        private int level;
        private String duration;
        private String accent;
        private String description;

        public String getTitle() {
            return title;
        }

        public String getSeries() {
            return series;
        }

        public int getSeason() {
            return season;
        }

        public int getEpisode() {
            return episode;
        }

        public int getLevel() {
            return level;
        }

        public String getDuration() {
            return duration;
        }

        public String getAccent() {
            return accent;
        }

        public String getDescription() {
            return description;
        }
    }

    public static class VocabularyItem {
        private String word;
        private String definition;
        private String translation;

        public String getWord() {
            return word;
        }

        public String getDefinition() {
            return definition;
        }

        public String getTranslation() {
            return translation;
        }
    }
} 