package com.example.cad;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cad.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup extends AppCompatActivity {

    private ImageView profileImageView;
    private TextView addImageTextView;
    private TextInputEditText usernameEditText;
    private TextInputEditText emailEditText;
    private TextInputEditText passwordEditText;
    private Button signupButton;
    private TextView alreadyHaveAccountTextView;
    private TextView signInTextView;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.cad.R.layout.activity_signup);

        profileImageView = findViewById(R.id.imageButton2);
        addImageTextView = findViewById(R.id.textView);
        usernameEditText = findViewById(R.id.textInputEditText);
        emailEditText = findViewById(R.id.editText3);
        passwordEditText = findViewById(R.id.editText4);
        signupButton = findViewById(R.id.button);
        alreadyHaveAccountTextView = findViewById(R.id.textView5);
        signInTextView = findViewById(R.id.textView4);

        // Initialize Firebase Realtime Database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                // Perform input validation
                if (username.isEmpty()) {
                    usernameEditText.setError("Username is required");
                    return;
                }

                if (email.isEmpty()) {
                    emailEditText.setError("Email is required");
                    return;
                }

                if (password.isEmpty()) {
                    passwordEditText.setError("Password is required");
                    return;
                }

                // Save user data to Firebase Realtime Database
                saveUserToFirebase(username, email, password);
            }
        });

        signInTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement your sign-in navigation here
                Toast.makeText(Signup.this, "Sign In clicked", Toast.LENGTH_SHORT).show();
                // Example:
                // Intent intent = new Intent(Signup.this, SignInActivity.class);
                // startActivity(intent);
                // finish();
            }
        });
    }

    private void saveUserToFirebase(String username, String email, String password) {
        // Create a unique key for the user in the database
        String userId = mDatabase.child("users").push().getKey();

        if (userId != null) {
            // Create a User object to store the data
            User user = new User(username, email, password);

            // Write the user data to the "users" node in the database
            mDatabase.child("users").child(userId).setValue(user)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(Signup.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                            // Optionally, navigate to the next screen or clear the input fields
                            usernameEditText.setText("");
                            emailEditText.setText("");
                            passwordEditText.setText("");
                        } else {
                            Log.e("SignupActivity", "Failed to create account: " + task.getException().getMessage()); //Important
                            Toast.makeText(Signup.this, "Failed to create account: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(Signup.this, "Failed to generate user ID", Toast.LENGTH_SHORT).show();
        }
    }

    // Data model class for User
    public static class User {
        public String username;
        public String email;
        public String password;

        public User() {
            // Default constructor required for Firebase
        }

        public User(String username, String email, String password) {
            this.username = username;
            this.email = email;
            this.password = password;
        }
    }
}
