package com.introtomobileappdev.introtomobileappdev.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.introtomobileappdev.introtomobileappdev.R;
import com.introtomobileappdev.introtomobileappdev.tasks.RequestTask;
import com.introtomobileappdev.introtomobileappdev.utils.Constants;

import java.util.HashMap;

public class LogInActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button logInButton;
    private TextView signUpTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        initViews();
        setOnClickListeners();
//        activateTextChangedListeners();
    }

    private void activateTextChangedListeners()
    {
        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0 && !passwordEditText.getText().toString().isEmpty())
                    logInButton.setEnabled(true);

                else logInButton.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0 && !emailEditText.getText().toString().isEmpty())
                    logInButton.setEnabled(true);

                else logInButton.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    private void initViews()
    {
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        logInButton = findViewById(R.id.loginButton);
        signUpTextView = findViewById(R.id.signUpTextView);
    }

    private HashMap<String, String> getInputtedData()
    {
        HashMap<String, String> data = new HashMap<>();
        data.put(Constants.EMAIL_KEY, emailEditText.getText().toString());
        data.put(Constants.PASSWORD_KEY, passwordEditText.getText().toString());

        return data;
    }

    private void setOnClickListeners()
    {
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
//        logInButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                RequestTask requestTask = new RequestTask();
//                requestTask.execute(getInputtedData());
//
//                try
//                {
//                    String response = requestTask.get();
//                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
//                }
//
//                catch (Exception e)
//                {
//                    e.printStackTrace();
//                }
//            }
//        });
        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignUpActivity();
            }
        });
    }

    private void startSignUpActivity() {
        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivity(intent);
    }
}
