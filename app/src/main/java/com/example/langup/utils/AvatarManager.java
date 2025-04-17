package com.example.langup.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AvatarManager {
    private static final String AVATARS_FOLDER = "avatars";
    private final Context context;
    private List<String> avatarPaths;

    public AvatarManager(Context context) {
        this.context = context;
        loadAvatarPaths();
    }

    private void loadAvatarPaths() {
        try {
            String[] files = context.getAssets().list(AVATARS_FOLDER);
            avatarPaths = new ArrayList<>(Arrays.asList(files));
        } catch (IOException e) {
            e.printStackTrace();
            avatarPaths = new ArrayList<>();
        }
    }

    public List<String> getAvatarPaths() {
        return avatarPaths;
    }

    public Drawable getAvatarDrawable(String avatarPath) {
        try {
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open(AVATARS_FOLDER + "/" + avatarPath);
            return Drawable.createFromStream(inputStream, null);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getDefaultAvatarPath() {
        return avatarPaths.isEmpty() ? null : avatarPaths.get(0);
    }
} 