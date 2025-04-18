package com.example.langup.utils;

import android.util.Log;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ContentCollector {
    private static final String TAG = "ContentCollector";
    private static final String BASE_URL = "https://listeninenglish.com";
    private static final String OUTPUT_DIR = "content";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void collectContent() {
        try {
            // Create output directory if it doesn't exist
            File outputDir = new File(OUTPUT_DIR);
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }

            // Get all content links from the main page
            Document mainPage = Jsoup.connect(BASE_URL).get();
            Elements contentLinks = mainPage.select("a[href*=/lesson/]");

            List<String> urls = new ArrayList<>();
            for (Element link : contentLinks) {
                urls.add(BASE_URL + link.attr("href"));
            }

            // Process each content URL
            for (String url : urls) {
                try {
                    ContentParser.ParsedContent content = ContentParser.parseContent(url);
                    if (content != null) {
                        Map<String, Object> jsonData = ContentParser.convertToJsonFormat(content);
                        saveToFile(jsonData, content.title);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error processing URL: " + url, e);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error collecting content", e);
        }
    }

    private static void saveToFile(Map<String, Object> jsonData, String title) {
        try {
            String fileName = title.toLowerCase()
                    .replaceAll("[^a-z0-9\\s-]", "")
                    .replaceAll("\\s+", "_") + ".json";
            
            File file = new File(OUTPUT_DIR, fileName);
            FileWriter writer = new FileWriter(file);
            gson.toJson(jsonData, writer);
            writer.close();
            
            Log.d(TAG, "Saved content to file: " + fileName);
        } catch (IOException e) {
            Log.e(TAG, "Error saving content to file", e);
        }
    }

    public static void main(String[] args) {
        collectContent();
    }
} 