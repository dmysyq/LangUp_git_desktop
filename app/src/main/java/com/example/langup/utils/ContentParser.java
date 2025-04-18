package com.example.langup.utils;

import android.util.Log;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContentParser {
    private static final String TAG = "ContentParser";
    private static final String BASE_URL = "https://listeninenglish.com";

    public static class ParsedContent {
        public String title;
        public String description;
        public List<String> questions;
        public List<String> grammarPoints;
        public List<String> vocabulary;
        public String audioUrl;
        public String transcript;
        public int level;
        public String category;
    }

    public static ParsedContent parseContent(String url) {
        try {
            Document doc = Jsoup.connect(url).get();
            ParsedContent content = new ParsedContent();

            // Parse title
            Element titleElement = doc.selectFirst("h1");
            if (titleElement != null) {
                content.title = titleElement.text();
            }

            // Parse description
            Element descriptionElement = doc.selectFirst(".description");
            if (descriptionElement != null) {
                content.description = descriptionElement.text();
            }

            // Parse questions
            Elements questionElements = doc.select(".questions li");
            content.questions = new ArrayList<>();
            for (Element question : questionElements) {
                content.questions.add(question.text());
            }

            // Parse grammar points
            Elements grammarElements = doc.select(".grammar-points li");
            content.grammarPoints = new ArrayList<>();
            for (Element grammar : grammarElements) {
                content.grammarPoints.add(grammar.text());
            }

            // Parse vocabulary
            Elements vocabularyElements = doc.select(".vocabulary li");
            content.vocabulary = new ArrayList<>();
            for (Element vocab : vocabularyElements) {
                content.vocabulary.add(vocab.text());
            }

            // Parse audio URL
            Element audioElement = doc.selectFirst("audio source");
            if (audioElement != null) {
                content.audioUrl = audioElement.attr("src");
            }

            // Parse transcript
            Element transcriptElement = doc.selectFirst(".transcript");
            if (transcriptElement != null) {
                content.transcript = transcriptElement.text();
            }

            // Parse level
            Element levelElement = doc.selectFirst(".level");
            if (levelElement != null) {
                content.level = Integer.parseInt(levelElement.text().replaceAll("[^0-9]", ""));
            }

            // Parse category
            Element categoryElement = doc.selectFirst(".category");
            if (categoryElement != null) {
                content.category = categoryElement.text();
            }

            return content;
        } catch (Exception e) {
            Log.e(TAG, "Error parsing content from URL: " + url, e);
            return null;
        }
    }

    public static Map<String, Object> convertToJsonFormat(ParsedContent content) {
        Map<String, Object> jsonData = new HashMap<>();
        
        // Metadata
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("id", generateId(content.title));
        metadata.put("title", content.title);
        metadata.put("description", content.description);
        metadata.put("level", content.level);
        metadata.put("category", content.category);
        jsonData.put("metadata", metadata);

        // Content
        Map<String, Object> contentData = new HashMap<>();
        contentData.put("audioUrl", content.audioUrl);
        contentData.put("transcript", content.transcript);
        contentData.put("questions", content.questions);
        contentData.put("grammarPoints", content.grammarPoints);
        contentData.put("vocabulary", content.vocabulary);
        jsonData.put("content", contentData);

        return jsonData;
    }

    private static String generateId(String title) {
        return title.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "_");
    }
} 