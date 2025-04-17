package com.example.langup;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.material.imageview.ShapeableImageView;
import android.widget.TextView;
import com.example.langup.dialogs.AvatarPickerDialog;
import com.example.langup.utils.AvatarManager;
import java.util.HashMap;
import java.util.Map;
import com.example.langup.utils.PreferencesManager;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.chip.Chip;
import java.util.List;
import java.util.ArrayList;

public class UserProfileActivity extends AppCompatActivity {
    private static final String TAG = "UserProfileActivity";

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private AvatarManager avatarManager;
    private EditText nameEditText;
    private EditText usernameEditText;
    private EditText emailEditText;
    private ShapeableImageView avatarImageView;
    private TextView changeAvatarButton;
    private TextView saveButton;
    private TextView logoutButton;
    private ImageButton backButton;
    private String currentAvatarPath;
    private PreferencesManager preferencesManager;
    private ChipGroup genresChipGroup;
    private ChipGroup countriesChipGroup;
    private ChipGroup franchisesChipGroup;
    private TextView savePreferencesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Initialize Firebase and managers
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        avatarManager = new AvatarManager(this);
        preferencesManager = new PreferencesManager();

        // Initialize views
        initializeViews();
        
        // Load user data and preferences
        loadUserData();
        loadUserPreferences();

        // Set up click listeners
        setupClickListeners();
    }

    private void initializeViews() {
        nameEditText = findViewById(R.id.nameEditText);
        usernameEditText = findViewById(R.id.usernameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        avatarImageView = findViewById(R.id.avatarImageView);
        changeAvatarButton = findViewById(R.id.changeAvatarButton);
        saveButton = findViewById(R.id.saveButton);
        logoutButton = findViewById(R.id.logoutButton);
        backButton = findViewById(R.id.backButton);
        genresChipGroup = findViewById(R.id.genresChipGroup);
        countriesChipGroup = findViewById(R.id.countriesChipGroup);
        franchisesChipGroup = findViewById(R.id.franchisesChipGroup);
        savePreferencesButton = findViewById(R.id.savePreferencesButton);

        // Set email field as non-editable
        emailEditText.setEnabled(false);

        // Initialize predefined values
        initializePredefinedValues();
    }

    private void initializePredefinedValues() {
        // Initialize genres
        String[] genres = getResources().getStringArray(R.array.genres);
        for (String genre : genres) {
            addChip(genresChipGroup, genre);
        }

        // Initialize countries
        String[] countries = getResources().getStringArray(R.array.countries);
        for (String country : countries) {
            addChip(countriesChipGroup, country);
        }

        // Initialize franchises
        String[] franchises = getResources().getStringArray(R.array.franchises);
        for (String franchise : franchises) {
            addChip(franchisesChipGroup, franchise);
        }
    }

    private void addChip(ChipGroup chipGroup, String text) {
        Chip chip = new Chip(this);
        chip.setText(text);
        chip.setCheckable(true);
        chip.setChecked(false);
        chip.setChipBackgroundColorResource(R.color.chip_background);
        chip.setTextColor(getResources().getColor(R.color.chip_text));
        chip.setChipStrokeColorResource(R.color.chip_stroke);
        chip.setChipStrokeWidth(getResources().getDimensionPixelSize(R.dimen.chip_stroke_width));
        chip.setTextSize(14);
        chip.setPadding(
            getResources().getDimensionPixelSize(R.dimen.chip_padding_horizontal),
            0,
            getResources().getDimensionPixelSize(R.dimen.chip_padding_horizontal),
            0
        );
        chip.setMinHeight(getResources().getDimensionPixelSize(R.dimen.chip_min_height));
        chip.setClickable(true);
        chip.setFocusable(true);
        chipGroup.addView(chip);
    }

    private void updateChipState(Chip chip, boolean isSelected) {
        if (isSelected) {
            chip.setChipBackgroundColorResource(R.color.chip_background_selected);
            chip.setTextColor(getResources().getColor(R.color.chip_text_selected));
            chip.setChipStrokeColorResource(R.color.chip_stroke_selected);
        } else {
            chip.setChipBackgroundColorResource(R.color.chip_background);
            chip.setTextColor(getResources().getColor(R.color.chip_text));
            chip.setChipStrokeColorResource(R.color.chip_stroke);
        }
    }

    private void setupClickListeners() {
        saveButton.setOnClickListener(v -> saveUserData());
        backButton.setOnClickListener(v -> finish());
        logoutButton.setOnClickListener(v -> logout());
        changeAvatarButton.setOnClickListener(v -> showAvatarPicker());
        savePreferencesButton.setOnClickListener(v -> saveUserPreferences());
    }

    private void showAvatarPicker() {
        AvatarPickerDialog dialog = new AvatarPickerDialog(this, avatarPath -> {
            currentAvatarPath = avatarPath;
            avatarImageView.setImageDrawable(avatarManager.getAvatarDrawable(avatarPath));
        });
        dialog.show();
    }

    private void loadUserData() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Log.e(TAG, "No user signed in");
            Toast.makeText(this, "No user signed in", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        Log.d(TAG, "Loading data for user: " + user.getUid());
        
        DocumentReference userRef = db.collection("users").document(user.getUid());
        userRef.get()
            .addOnSuccessListener(document -> {
                if (document.exists()) {
                    Log.d(TAG, "Document data: " + document.getData());
                    
                    // Get user data with null checks
                    String name = document.getString("name");
                    String username = document.getString("username");
                    String email = document.getString("email");
                    currentAvatarPath = document.getString("avatarPath");

                    // Update UI
                    if (name != null) nameEditText.setText(name);
                    if (username != null) usernameEditText.setText(username);
                    if (email != null) {
                        emailEditText.setText(email);
                    } else {
                        emailEditText.setText(user.getEmail());
                    }

                    // Load avatar
                    if (currentAvatarPath != null) {
                        avatarImageView.setImageDrawable(avatarManager.getAvatarDrawable(currentAvatarPath));
                    } else {
                        // Set default avatar
                        currentAvatarPath = avatarManager.getDefaultAvatarPath();
                        if (currentAvatarPath != null) {
                            avatarImageView.setImageDrawable(avatarManager.getAvatarDrawable(currentAvatarPath));
                        }
                    }
                } else {
                    Log.d(TAG, "No such document, creating new user profile");
                    createNewUserProfile(user);
                }
            })
            .addOnFailureListener(e -> {
                Log.e(TAG, "Error loading profile", e);
                Toast.makeText(this, "Error loading profile: " + e.getMessage(), 
                             Toast.LENGTH_SHORT).show();
            });
    }

    private void createNewUserProfile(FirebaseUser user) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("uid", user.getUid());
        userData.put("email", user.getEmail());
        userData.put("name", "");
        userData.put("username", "");
        userData.put("avatarPath", avatarManager.getDefaultAvatarPath());

        db.collection("users").document(user.getUid())
            .set(userData)
            .addOnSuccessListener(aVoid -> {
                Log.d(TAG, "User profile created successfully");
                loadUserData(); // Reload data after creation
            })
            .addOnFailureListener(e -> {
                Log.e(TAG, "Error creating user profile", e);
                Toast.makeText(this, "Error creating profile: " + e.getMessage(),
                             Toast.LENGTH_SHORT).show();
            });
    }

    private void saveUserData() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Log.e(TAG, "No user signed in during save");
            return;
        }

        String name = nameEditText.getText().toString().trim();
        String username = usernameEditText.getText().toString().trim();

        Map<String, Object> updates = new HashMap<>();
        updates.put("name", name);
        updates.put("username", username);
        updates.put("avatarPath", currentAvatarPath);

        Log.d(TAG, "Saving user data: " + updates);

        DocumentReference userRef = db.collection("users").document(user.getUid());
        userRef.update(updates)
            .addOnSuccessListener(aVoid -> {
                Log.d(TAG, "Profile updated successfully");
                Toast.makeText(this, "Profile saved successfully", Toast.LENGTH_SHORT).show();
            })
            .addOnFailureListener(e -> {
                Log.e(TAG, "Error updating profile", e);
                Toast.makeText(this, "Error saving profile: " + e.getMessage(),
                             Toast.LENGTH_SHORT).show();
            });
    }

    private void logout() {
        mAuth.signOut();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private void loadUserPreferences() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) return;

        preferencesManager.loadPreferences(user.getUid(), new PreferencesManager.PreferencesCallback() {
            @Override
            public void onSuccess() {}

            @Override
            public void onError(String error) {
                Toast.makeText(UserProfileActivity.this, error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPreferencesLoaded(Map<String, List<String>> preferences) {
                updatePreferencesUI(preferences);
            }
        });
    }

    private void updatePreferencesUI(Map<String, List<String>> preferences) {
        // Обновляем UI для жанров
        List<String> genres = preferences.get("genres");
        if (genres != null) {
            for (String genre : genres) {
                addChip(genresChipGroup, genre);
            }
        }

        // Обновляем UI для стран
        List<String> countries = preferences.get("countries");
        if (countries != null) {
            for (String country : countries) {
                addChip(countriesChipGroup, country);
            }
        }

        // Обновляем UI для франшиз
        List<String> franchises = preferences.get("franchises");
        if (franchises != null) {
            for (String franchise : franchises) {
                addChip(franchisesChipGroup, franchise);
            }
        }
    }

    private void saveUserPreferences() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) return;

        Map<String, List<String>> preferences = new HashMap<>();
        preferences.put("genres", getSelectedChips(genresChipGroup));
        preferences.put("countries", getSelectedChips(countriesChipGroup));
        preferences.put("franchises", getSelectedChips(franchisesChipGroup));

        preferencesManager.savePreferences(user.getUid(), preferences, new PreferencesManager.PreferencesCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(UserProfileActivity.this, R.string.preferences_saved, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(UserProfileActivity.this, R.string.preferences_error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPreferencesLoaded(Map<String, List<String>> preferences) {}
        });
    }

    private List<String> getSelectedChips(ChipGroup chipGroup) {
        List<String> selected = new ArrayList<>();
        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            Chip chip = (Chip) chipGroup.getChildAt(i);
            if (chip.isChecked()) {
                selected.add(chip.getText().toString());
            }
        }
        return selected;
    }
}
