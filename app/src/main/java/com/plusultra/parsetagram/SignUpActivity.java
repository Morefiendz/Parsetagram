package com.plusultra.parsetagram;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {
    private EditText username, password;
    private Button signUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        username = findViewById(R.id.etUser);
        password = findViewById(R.id.etPass);
        signUpBtn = findViewById(R.id.btnSI);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create the ParseUser
                ParseUser user = new ParseUser();

                // Set core properties
                user.setUsername(String.valueOf(username.getText()));
                user.setPassword(String.valueOf(password.getText()));

                // Invoke signUpInBackground
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(com.parse.ParseException e) {
                        if (e == null) {
                            final Intent intent = new Intent(SignUpActivity.this, TimelineActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // Sign up didn't succeed. Look at the ParseException
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}
