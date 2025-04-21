package com.example.langup.data.repository;

import android.content.Context;
import android.util.Log;

import com.example.langup.domain.model.Episode;
import com.example.langup.domain.model.Series;
import com.example.langup.domain.model.SeriesMetadata;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class JsonLoader {
    private static final String TAG = "JsonLoader";
    private final Context context;

    public JsonLoader(Context context) {
        this.context = context;
    }

    public List<Series> loadSeriesFromFile(String filename) {
        List<Series> seriesList = new ArrayList<>();
        try {
            String jsonString = loadJSONFromAsset(filename);
            JSONArray seriesArray = new JSONArray(jsonString);
            
            for (int i = 0; i < seriesArray.length(); i++) {
                JSONObject seriesObject = seriesArray.getJSONObject(i);
                Series series = parseSeries(seriesObject);
                if (series != null) {
                    seriesList.add(series);
                }
            }
        } catch (JSONException | IOException e) {
            Log.e(TAG, "Error loading series from " + filename, e);
        }
        return seriesList;
    }

    private Series parseSeries(JSONObject seriesObject) {
        try {
            Series series = new Series();
            
            // Basic info
            series.setId(seriesObject.getString("id"));
            series.setTitle(seriesObject.getString("title"));
            series.setDescription(seriesObject.optString("description", ""));
            series.setLevel(seriesObject.optString("level", "beginner"));
            series.setAccent(seriesObject.optString("accent", ""));
            series.setImageUrl(seriesObject.optString("imageUrl", ""));
            series.setSource(seriesObject.optString("source", ""));
            series.setDifficulty(seriesObject.optInt("difficulty", 1));

            // Parse vocabulary items
            if (seriesObject.has("vocabulary")) {
                JSONArray vocabularyArray = seriesObject.getJSONArray("vocabulary");
                List<Series.VocabularyItem> vocabularyItems = new ArrayList<>();
                for (int i = 0; i < vocabularyArray.length(); i++) {
                    JSONObject vocabObject = vocabularyArray.getJSONObject(i);
                    Series.VocabularyItem item = new Series.VocabularyItem();
                    item.setWord(vocabObject.getString("word"));
                    item.setTranslation(vocabObject.getString("translation"));
                    item.setExample(vocabObject.getString("example"));
                    vocabularyItems.add(item);
                }
                series.setVocabulary(vocabularyItems);
            }

            // Parse episodes
            if (seriesObject.has("episodes")) {
                JSONArray episodesArray = seriesObject.getJSONArray("episodes");
                List<Episode> episodes = new ArrayList<>();
                for (int i = 0; i < episodesArray.length(); i++) {
                    JSONObject episodeObject = episodesArray.getJSONObject(i);
                    Episode episode = parseEpisode(episodeObject);
                    if (episode != null) {
                        episodes.add(episode);
                    }
                }
                series.setEpisodes(episodes);
            }

            // Parse questions
            if (seriesObject.has("questions")) {
                JSONArray questionsArray = seriesObject.getJSONArray("questions");
                List<String> questions = new ArrayList<>();
                for (int i = 0; i < questionsArray.length(); i++) {
                    questions.add(questionsArray.getString(i));
                }
                series.setQuestions(questions);
            }

            // Parse grammar
            if (seriesObject.has("grammar")) {
                JSONArray grammarArray = seriesObject.getJSONArray("grammar");
                List<String> grammar = new ArrayList<>();
                for (int i = 0; i < grammarArray.length(); i++) {
                    grammar.add(grammarArray.getString(i));
                }
                series.setGrammar(grammar);
            }

            // Parse script
            if (seriesObject.has("script")) {
                JSONArray scriptArray = seriesObject.getJSONArray("script");
                List<String> script = new ArrayList<>();
                for (int i = 0; i < scriptArray.length(); i++) {
                    script.add(scriptArray.getString(i));
                }
                series.setScript(script);
            }

            // Parse metadata
            if (seriesObject.has("metadata")) {
                JSONObject metadataObject = seriesObject.getJSONObject("metadata");
                SeriesMetadata metadata = parseMetadata(metadataObject);
                series.setMetadata(metadata);
            }

            return series;
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing series object", e);
            return null;
        }
    }

    private Episode parseEpisode(JSONObject episodeObject) {
        try {
            Episode episode = new Episode();
            episode.setId(episodeObject.getString("id"));
            episode.setTitle(episodeObject.getString("title"));
            episode.setDescription(episodeObject.optString("description", ""));
            episode.setVideoUrl(episodeObject.optString("videoUrl", ""));
            episode.setDuration(episodeObject.optInt("duration", 0));
            return episode;
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing episode object", e);
            return null;
        }
    }

    private SeriesMetadata parseMetadata(JSONObject metadataObject) {
        try {
            SeriesMetadata metadata = new SeriesMetadata();
            metadata.setGenres(parseStringArray(metadataObject, "genres"));
            metadata.setCountry(metadataObject.optString("country", ""));
            metadata.setFranchises(parseStringArray(metadataObject, "franchises"));
            return metadata;
        } catch (JSONException e) {
            Log.e(TAG, "Error parsing metadata object", e);
            return null;
        }
    }

    private List<String> parseStringArray(JSONObject object, String key) throws JSONException {
        List<String> result = new ArrayList<>();
        if (object.has(key)) {
            JSONArray array = object.getJSONArray(key);
            for (int i = 0; i < array.length(); i++) {
                result.add(array.getString(i));
            }
        }
        return result;
    }

    private String loadJSONFromAsset(String filename) throws IOException {
        String jsonString;
        try (InputStream is = context.getAssets().open(filename)) {
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            jsonString = new String(buffer, StandardCharsets.UTF_8);
        }
        return jsonString;
    }
} 