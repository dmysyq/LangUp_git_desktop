package com.example.langup.presentation.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.langup.R;
import com.example.langup.presentation.ui.base.BaseActivity;
import com.example.langup.data.repository.AuthManager;
import com.example.langup.presentation.ui.main.MainActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends BaseActivity implements AuthManager.TokenRefreshListener {
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_sign_up;
    }
    private EditText nameEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    private ImageButton togglePasswordButton, toggleConfirmPasswordButton;
    private Button signUpButton, googleSignUpButton, facebookSignUpButton;
    private TextView loginTextView;
    private ImageButton backButton;
    private AuthManager authManager;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        authManager = new AuthManager(this);
        authManager.setTokenRefreshListener(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // Initialize Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("350062158843-5tkibsnqdetvb4fn8re0uj4607rb80sr.apps.googleusercontent.com")
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        initializeViews();
        setupClickListeners();
    }

    private void initializeViews() {
        nameEditText = findViewById(R.id.etName);
        emailEditText = findViewById(R.id.etEmail);
        passwordEditText = findViewById(R.id.etPassword);
        confirmPasswordEditText = findViewById(R.id.etConfirmPassword);
        togglePasswordButton = findViewById(R.id.togglePasswordButton);
        toggleConfirmPasswordButton = findViewById(R.id.toggleConfirmPasswordButton);
        signUpButton = findViewById(R.id.btnSignUp);
        googleSignUpButton = findViewById(R.id.btnGoogleSignUp);
        facebookSignUpButton = findViewById(R.id.btnFacebookSignUp);
        loginTextView = findViewById(R.id.tvLogin);
        backButton = findViewById(R.id.backButton);
    }

    private void setupClickListeners() {
        togglePasswordButton.setOnClickListener(v -> togglePasswordVisibility(passwordEditText, togglePasswordButton));
        toggleConfirmPasswordButton.setOnClickListener(v -> togglePasswordVisibility(confirmPasswordEditText, toggleConfirmPasswordButton));
        signUpButton.setOnClickListener(v -> attemptSignUp());
        googleSignUpButton.setOnClickListener(v -> signUpWithGoogle());
        facebookSignUpButton.setOnClickListener(v -> signUpWithFacebook());
        loginTextView.setOnClickListener(v -> finish());
        backButton.setOnClickListener(v -> finish());
    }

    private void togglePasswordVisibility(EditText passwordField, ImageButton toggleButton) {
        if (passwordField.getTransformationMethod() == android.text.method.HideReturnsTransformationMethod.getInstance()) {
            passwordField.setTransformationMethod(android.text.method.PasswordTransformationMethod.getInstance());
            toggleButton.setImageResource(R.drawable.ic_visibility_off);
        } else {
            passwordField.setTransformationMethod(android.text.method.HideReturnsTransformationMethod.getInstance());
            toggleButton.setImageResource(R.drawable.ic_visibility);
        }
    }

    private void attemptSignUp() {
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        if (!validateInput(name, email, password, confirmPassword)) {
            return;
        }

        signUpButton.setEnabled(false);
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        saveUserData(name, email);
                        authManager.setLoggedIn(true);
                        startMainActivity();
                    } else {
                        signUpButton.setEnabled(true);
                        String errorMessage = getErrorMessage(task.getException());
                        Toast.makeText(SignUpActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private boolean validateInput(String name, String email, String password, String confirmPassword) {
        if (TextUtils.isEmpty(name)) {
            nameEditText.setError(getString(R.string.error_field_required));
            return false;
        }
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError(getString(R.string.error_field_required));
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError(getString(R.string.error_invalid_email));
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError(getString(R.string.error_field_required));
            return false;
        }
        if (password.length() < 6) {
            passwordEditText.setError(getString(R.string.error_invalid_password));
            return false;
        }
        if (!password.equals(confirmPassword)) {
            confirmPasswordEditText.setError(getString(R.string.error_passwords_dont_match));
            return false;
        }
        return true;
    }

    private void saveUserData(String name, String email) {
        String userId = firebaseAuth.getCurrentUser().getUid();
        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("email", email);
        user.put("createdAt", System.currentTimeMillis());

        firestore.collection("users").document(userId)
                .set(user)
                .addOnFailureListener(e -> Toast.makeText(SignUpActivity.this,
                        "Error saving user data", Toast.LENGTH_SHORT).show());
    }

    private String getErrorMessage(Exception exception) {
        if (exception == null) return getString(R.string.error_unknown);
        
        String errorCode = ((com.google.firebase.auth.FirebaseAuthException) exception).getErrorCode();
        switch (errorCode) {
            case "ERROR_INVALID_EMAIL":
                return getString(R.string.error_invalid_email);
            case "ERROR_WEAK_PASSWORD":
                return getString(R.string.error_invalid_password);
            case "ERROR_EMAIL_ALREADY_IN_USE":
                return getString(R.string.error_email_already_in_use);
            default:
                return getString(R.string.error_unknown);
        }
    }

    private void signUpWithGoogle() {
        android.util.Log.d("SignUpActivity", "Starting Google Sign-In process");
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        android.util.Log.d("SignUpActivity", "onActivityResult called with requestCode: " + requestCode + ", resultCode: " + resultCode);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                android.util.Log.d("SignUpActivity", "Getting Google Sign-In account");
                GoogleSignInAccount account = task.getResult(ApiException.class);
                android.util.Log.d("SignUpActivity", "Google Sign-In successful, getting token");
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                android.util.Log.e("SignUpActivity", "Google Sign-In failed with error code: " + e.getStatusCode());
                android.util.Log.e("SignUpActivity", "Error message: " + e.getMessage());
                android.util.Log.e("SignUpActivity", "Status code: " + e.getStatus().getStatusCode());
                Toast.makeText(this, getString(R.string.google_sign_in_failed), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        android.util.Log.d("SignUpActivity", "Starting Firebase authentication with Google token");
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        android.util.Log.d("SignUpActivity", "Firebase authentication successful");
                        String name = task.getResult().getUser().getDisplayName();
                        String email = task.getResult().getUser().getEmail();
                        
                        // Check if this is a new user by checking if the user document exists
                        String userId = firebaseAuth.getCurrentUser().getUid();
                        firestore.collection("users").document(userId)
                                .get()
                                .addOnCompleteListener(documentTask -> {
                                    if (documentTask.isSuccessful() && !documentTask.getResult().exists()) {
                                        // This is a new user, save their data
                                        android.util.Log.d("SignUpActivity", "New user detected, saving user data");
                                        if (name != null && email != null) {
                                            saveUserData(name, email);
                                        }
                                    } else {
                                        android.util.Log.d("SignUpActivity", "Existing user, skipping data save");
                                    }
                                    authManager.setLoggedIn(true);
                                    startMainActivity();
                                });
                    } else {
                        android.util.Log.e("SignUpActivity", "Firebase authentication failed", task.getException());
                        if (task.getException() != null) {
                            android.util.Log.e("SignUpActivity", "Error code: " + ((FirebaseAuthException) task.getException()).getErrorCode());
                            android.util.Log.e("SignUpActivity", "Error message: " + task.getException().getMessage());
                        }
                        Toast.makeText(SignUpActivity.this, "Authentication failed: " + 
                            (task.getException() != null ? task.getException().getMessage() : "Unknown error"),
                            Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void signUpWithFacebook() {
        // Implement Facebook sign-up
        Toast.makeText(this, "Facebook sign-up not implemented yet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTokenRefreshed(boolean success) {
        if (!success) {
            Toast.makeText(this, getString(R.string.session_expired), Toast.LENGTH_SHORT).show();
            startMainActivity();
        }
    }
} 