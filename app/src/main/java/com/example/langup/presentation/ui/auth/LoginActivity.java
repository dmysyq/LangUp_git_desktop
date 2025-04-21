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
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class LoginActivity extends BaseActivity implements AuthManager.TokenRefreshListener {
    private static final int RC_SIGN_IN = 9001;
    private EditText emailEditText, passwordEditText;
    private ImageButton togglePasswordButton;
    private Button loginButton, googleSignInButton, facebookLoginButton;
    private TextView forgotPasswordTextView, signUpTextView;
    private ImageButton backButton;
    private AuthManager authManager;
    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient googleSignInClient;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        authManager = new AuthManager(this);
        authManager.setTokenRefreshListener(this);
        firebaseAuth = FirebaseAuth.getInstance();

        // Initialize Google Sign In with default configuration
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("350062158843-5tkibsnqdetvb4fn8re0uj4607rb80sr.apps.googleusercontent.com")
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        initializeViews();
        setupClickListeners();
    }

    private void initializeViews() {
        emailEditText = findViewById(R.id.etEmail);
        passwordEditText = findViewById(R.id.etPassword);
        togglePasswordButton = findViewById(R.id.togglePasswordButton);
        loginButton = findViewById(R.id.btnLogin);
        googleSignInButton = findViewById(R.id.btnGoogleSignIn);
        facebookLoginButton = findViewById(R.id.btnFacebookLogin);
        forgotPasswordTextView = findViewById(R.id.tvForgotPassword);
        signUpTextView = findViewById(R.id.tvSignUp);
        backButton = findViewById(R.id.backButton);
    }

    private void setupClickListeners() {
        togglePasswordButton.setOnClickListener(v -> togglePasswordVisibility());
        loginButton.setOnClickListener(v -> attemptLogin());
        googleSignInButton.setOnClickListener(v -> signInWithGoogle());
        facebookLoginButton.setOnClickListener(v -> signInWithFacebook());
        forgotPasswordTextView.setOnClickListener(v -> startPasswordReset());
        signUpTextView.setOnClickListener(v -> startSignUp());
        backButton.setOnClickListener(v -> finish());
    }

    private void togglePasswordVisibility() {
        if (passwordEditText.getTransformationMethod() == android.text.method.HideReturnsTransformationMethod.getInstance()) {
            passwordEditText.setTransformationMethod(android.text.method.PasswordTransformationMethod.getInstance());
            togglePasswordButton.setImageResource(R.drawable.ic_visibility_off);
        } else {
            passwordEditText.setTransformationMethod(android.text.method.HideReturnsTransformationMethod.getInstance());
            togglePasswordButton.setImageResource(R.drawable.ic_visibility);
        }
    }

    private void attemptLogin() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (!validateInput(email, password)) {
            return;
        }

        if (authManager.isAccountLocked()) {
            long remainingTime = authManager.getRemainingLockoutTime() / 1000 / 60;
            Toast.makeText(this, getString(R.string.account_locked, remainingTime), Toast.LENGTH_LONG).show();
            return;
        }

        loginButton.setEnabled(false);
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    loginButton.setEnabled(true);
                    if (task.isSuccessful()) {
                        authManager.setLoggedIn(true);
                        startMainActivity();
                    } else {
                        authManager.incrementLoginAttempts();
                        String errorMessage = getErrorMessage(task.getException());
                        Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private boolean validateInput(String email, String password) {
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
        return true;
    }

    private String getErrorMessage(Exception exception) {
        if (exception == null) return getString(R.string.error_unknown);
        
        String errorCode = ((com.google.firebase.auth.FirebaseAuthException) exception).getErrorCode();
        switch (errorCode) {
            case "ERROR_INVALID_EMAIL":
                return getString(R.string.error_invalid_email);
            case "ERROR_WRONG_PASSWORD":
                return getString(R.string.error_incorrect_password);
            case "ERROR_USER_NOT_FOUND":
                return getString(R.string.error_user_not_found);
            case "ERROR_USER_DISABLED":
                return getString(R.string.error_user_disabled);
            case "ERROR_TOO_MANY_REQUESTS":
                return getString(R.string.error_too_many_requests);
            default:
                return getString(R.string.error_unknown);
        }
    }

    private void signInWithGoogle() {
        android.util.Log.d("LoginActivity", "Starting Google Sign-In process");
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            android.util.Log.d("LoginActivity", "Received Google Sign-In result");
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                android.util.Log.d("LoginActivity", "Getting Google Sign-In account");
                GoogleSignInAccount account = task.getResult(ApiException.class);
                android.util.Log.d("LoginActivity", "Google Sign-In successful, account email: " + account.getEmail());
                android.util.Log.d("LoginActivity", "Getting ID token");
                String idToken = account.getIdToken();
                if (idToken != null) {
                    android.util.Log.d("LoginActivity", "ID token received, length: " + idToken.length());
                    firebaseAuthWithGoogle(idToken);
                } else {
                    android.util.Log.e("LoginActivity", "ID token is null");
                    Toast.makeText(this, "Authentication failed: ID token is null", Toast.LENGTH_SHORT).show();
                }
            } catch (ApiException e) {
                android.util.Log.e("LoginActivity", "Google Sign-In failed with error code: " + e.getStatusCode());
                android.util.Log.e("LoginActivity", "Error message: " + e.getMessage());
                android.util.Log.e("LoginActivity", "Error status: " + e.getStatus());
                Toast.makeText(this, "Google Sign-In failed: " + e.getStatus(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        android.util.Log.d("LoginActivity", "Starting Firebase authentication with Google token");
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        
        // First try to sign in
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        android.util.Log.d("LoginActivity", "Firebase authentication successful - User exists");
                        android.util.Log.d("LoginActivity", "User UID: " + firebaseAuth.getCurrentUser().getUid());
                        android.util.Log.d("LoginActivity", "User email: " + firebaseAuth.getCurrentUser().getEmail());
                        authManager.setLoggedIn(true);
                        startMainActivity();
                    }
                });
    }

    private void startPasswordReset() {
        String email = emailEditText.getText().toString().trim();
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError(getString(R.string.error_invalid_email));
            return;
        }

        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, getString(R.string.password_reset_email_sent),
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(LoginActivity.this, getString(R.string.password_reset_failed),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void startSignUp() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void signInWithFacebook() {
        // Implement Facebook sign-in
        Toast.makeText(this, "Facebook sign-in not implemented yet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTokenRefreshed(boolean success) {
        if (!success) {
            Toast.makeText(this, getString(R.string.session_expired), Toast.LENGTH_SHORT).show();
            startMainActivity();
        }
    }
}
