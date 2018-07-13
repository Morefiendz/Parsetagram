package com.plusultra.parsetagram;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameIn, passwordIn;
    private Button loginBtn, signUpBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ParseUser currentUser = ParseUser.getCurrentUser();

        if (currentUser != null){
            final Intent intent = new Intent(LoginActivity.this, TimelineActivity.class);
            startActivity(intent);
            finish();
        }

        setContentView(R.layout.activity_login);

        usernameIn = findViewById(R.id.etUsername);
        passwordIn = findViewById(R.id.etPassword);
        loginBtn = findViewById(R.id.btnLogin);
        signUpBtn = findViewById(R.id.btnSignUp);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = usernameIn.getText().toString();
                final String password = passwordIn.getText().toString();

                logIn(username, password);
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void logIn(String username, String password) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null) {
                    Log.d("LoginActivity", "Login successful");

                    final Intent intent = new Intent(LoginActivity.this, TimelineActivity.class);
                    startActivity(intent);
                } else {
                    Log.e("LoginActivity", "Login failure");
                    e.printStackTrace();
                }

                finish();
            }
        });
    }
}