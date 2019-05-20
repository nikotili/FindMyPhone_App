package com.introtomobileappdev.introtomobileappdev.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.introtomobileappdev.introtomobileappdev.R;
import com.introtomobileappdev.introtomobileappdev.tasks.RequestTask;
import com.introtomobileappdev.introtomobileappdev.utils.Constants;
import com.introtomobileappdev.introtomobileappdev.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView signUpHeaderTextView;

    private TextInputLayout firstNameInputLayout;
    private EditText firstNameEditText;

    private TextInputLayout lastNameInputLayout;
    private EditText lastNameEditText;

    private TextInputLayout emailInputLayout;
    private EditText emailEditText;

    private TextInputLayout passwordInputLayout;
    private EditText passwordEditText;

    private TextInputLayout repeatPasswordInputLayout;
    private EditText repeatPasswordEditText;

    private TextInputLayout dateOfBirthInputLayout;
    private EditText dateOfBirthEditText;

    private TextInputLayout addressLine1InputLayout;
    private EditText addressLine1EditText;

    private TextInputLayout addressLine2InputLayout;
    private EditText addressLine2EditText;

    private TextInputLayout postalCodeInputLayout;
    private EditText postalCodeEditText;

    private TextInputLayout countryInputLayout;
    private EditText countryEditText;

    private TextInputLayout cityInputLayout;
    private EditText cityEditText;

    private TextInputLayout mobilePhoneInputLayout;
    private EditText mobilePhoneEditText;

    private Button createAccountButton;
    private ArrayList<View> views = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);
        initViews();
        fillEmail();
        setOnClickListeners();
        addTextChangedListeners();
    }


    private void setOnClickListeners()
    {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLogInActivity();
            }
        });

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    if (isDataValid())
                    {
                        RequestTask requestTask = new RequestTask();
                        String response = requestTask.execute(getInputtedData()).get();

                        if (response.equals(Constants.SIGN_UP_SUCCESS_MESSAGE)) {
                            Toast.makeText(getApplicationContext(), "Sign up success!", Toast.LENGTH_LONG).show();
                            startLogInActivityWithSignUpSuccess();
                        } else {
                            Toast.makeText(getApplicationContext(), "This user exists!", Toast.LENGTH_LONG).show();
                        }
                    }

                    else
                        Toast.makeText(getApplicationContext(), "Data not valid!", Toast.LENGTH_LONG).show();
                }

                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        dateOfBirthEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        dateOfBirthEditText.setText(simpleDateFormat.format(calendar.getTime()));
                    }

                };


                new DatePickerDialog(SignUpActivity.this, onDateSetListener, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
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

    private void addTextChangedListeners()
    {
        firstNameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String text = ((EditText) v).getText().toString();
                text = text.trim();

                if (!text.isEmpty())
                {
                    String s = "Hello " + text;
                    signUpHeaderTextView.setText(s);
                    firstNameInputLayout.setErrorEnabled(false);
                }

                else
                {
                    signUpHeaderTextView.setText(R.string.sign_up_header);
                    firstNameInputLayout.setError(getString(R.string.first_name_error));
                }

                if(hasFocus) firstNameInputLayout.setErrorEnabled(false);

            }
        });

        lastNameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String text = ((EditText) v).getText().toString();
                text = text.trim();

                if (!text.isEmpty())
                {
                    lastNameInputLayout.setErrorEnabled(false);
                }

                else
                {
                    lastNameInputLayout.setError(getString(R.string.last_name_error));
                }

                if(hasFocus) lastNameInputLayout.setErrorEnabled(false);

            }
        });

        emailEditText.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String text = ((EditText) v).getText().toString();
                text = text.trim();

                if (!text.isEmpty())
                {
                    emailInputLayout.setErrorEnabled(false);
                }

                else
                {
                    emailInputLayout.setError(getString(R.string.email_error));
                }

                if(hasFocus) emailInputLayout.setErrorEnabled(false);
            }
        });

        passwordEditText.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String text = ((EditText) v).getText().toString();

                if (text.length() >= 8 && Utils.isValidPassword(text))
                {
                    passwordInputLayout.setErrorEnabled(false);
                }

                else
                {
                    passwordInputLayout.setError(getString(R.string.password_error));
                }

                if(hasFocus) passwordInputLayout.setErrorEnabled(false);
            }
        });

        repeatPasswordEditText.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String text = ((EditText) v).getText().toString();
                String password = passwordEditText.getText().toString();

                if (text.equals(password))
                {
                    repeatPasswordInputLayout.setErrorEnabled(false);
                }

                else
                {
                    repeatPasswordInputLayout.setError(getString(R.string.repeat_password_error));
                }

                if(hasFocus) repeatPasswordInputLayout.setErrorEnabled(false);
            }
        });



        dateOfBirthEditText.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String text = ((EditText) v).getText().toString();
                text = text.trim();

                if (!text.isEmpty())
                {
                    dateOfBirthInputLayout.setErrorEnabled(false);
                }

                else
                {
                    dateOfBirthInputLayout.setError(getString(R.string.date_of_birth_error));
                }

                if(hasFocus) dateOfBirthInputLayout.setErrorEnabled(false);

            }
        });

        addressLine1EditText.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String text = ((EditText) v).getText().toString();
                text = text.trim();

                if (!text.isEmpty())
                {
                    addressLine1InputLayout.setErrorEnabled(false);
                }

                else
                {
                    addressLine1InputLayout.setError(getString(R.string.address_line1_error));
                }

                if(hasFocus) addressLine1InputLayout.setErrorEnabled(false);
            }
        });

        postalCodeEditText.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String text = ((EditText) v).getText().toString();
                text = text.trim();

                if (!text.isEmpty())
                {
                    postalCodeInputLayout.setErrorEnabled(false);
                }

                else
                {
                    postalCodeInputLayout.setError(getString(R.string.postal_code_error));
                }

                if(hasFocus) postalCodeInputLayout.setErrorEnabled(false);
            }
        });

        countryEditText.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String text = ((EditText) v).getText().toString();
                text = text.trim();

                if (!text.isEmpty())
                {
                    countryInputLayout.setErrorEnabled(false);
                }

                else
                {
                    countryInputLayout.setError(getString(R.string.country_error));
                }

                if(hasFocus) countryInputLayout.setErrorEnabled(false);
            }
        });

        cityEditText.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String text = ((EditText) v).getText().toString();
                text = text.trim();

                if (!text.isEmpty())
                {
                    cityInputLayout.setErrorEnabled(false);
                }

                else
                {
                    cityInputLayout.setError(getString(R.string.city_error));
                }

                if(hasFocus) cityInputLayout.setErrorEnabled(false);
            }
        });

        mobilePhoneEditText.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String text = ((EditText) v).getText().toString();
                text = text.trim();

                if (!text.isEmpty())
                {
                    mobilePhoneInputLayout.setErrorEnabled(false);
                }

                else
                {
                    mobilePhoneInputLayout.setError(getString(R.string.mobile_phone_error));
                }

                if(hasFocus) mobilePhoneInputLayout.setErrorEnabled(false);
            }
        });




    }

    private boolean isDataValid()
    {
        for (View view : views)
        {
            if (view instanceof TextInputLayout && ((TextInputLayout) view).isErrorEnabled())
                return false;
            else if (view instanceof EditText && ((EditText) view).getText().toString().isEmpty())
                return false;
        }

        return true;
    }


    private HashMap<String, String> getInputtedData()
    {
        HashMap<String, String> data = new HashMap<>();

        data.put(Constants.FIRST_NAME_KEY, firstNameEditText.getText().toString());
        data.put(Constants.LAST_NAME_KEY, lastNameEditText.getText().toString());
        data.put(Constants.EMAIL_KEY, emailEditText.getText().toString());
        data.put(Constants.PASSWORD_KEY, passwordEditText.getText().toString());
        data.put(Constants.DATE_OF_BIRTH_KEY, dateOfBirthEditText.getText().toString());
        data.put(Constants.ADDRESS_1_KEY, addressLine1EditText.getText().toString());
        data.put(Constants.ADDRESS_2_KEY, addressLine2EditText.getText().toString());
        data.put(Constants.POSTAL_CODE_KEY, postalCodeEditText.getText().toString());
        data.put(Constants.COUNTRY_KEY, countryEditText.getText().toString());
        data.put(Constants.CITY_KEY, cityEditText.getText().toString());
        data.put(Constants.MOBILE_PHONE_KEY, mobilePhoneEditText.getText().toString());

        return data;
    }

    private void startLogInActivity()
    {
        Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
        startActivity(intent);
    }

    private void startLogInActivityWithSignUpSuccess()
    {
        Intent intent = new Intent(getApplicationContext(), LogInActivity.class);
        intent.putExtra(Constants.EMAIL_KEY, emailEditText.getText().toString());
        startActivity(intent);
        finish();
    }

    private void initViews()
    {
        toolbar = findViewById(R.id.toolbar);

        signUpHeaderTextView = findViewById(R.id.signUpHeaderTextView);

        firstNameInputLayout = findViewById(R.id.firstNameInputLayout);
        views.add(firstNameInputLayout);
        firstNameEditText = findViewById(R.id.firstNameEditText);
        views.add(firstNameEditText);

        lastNameInputLayout = findViewById(R.id.lastNameInputLayout);
        views.add(lastNameInputLayout);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        views.add(lastNameEditText);

        emailInputLayout = findViewById(R.id.emailInputLayout);
        views.add(emailInputLayout);
        emailEditText = findViewById(R.id.emailEditText);
        views.add(emailEditText);

        passwordInputLayout = findViewById(R.id.passwordInputLayout);
        views.add(passwordInputLayout);
        passwordEditText = findViewById(R.id.passwordEditText);
        views.add(passwordEditText);

        repeatPasswordInputLayout = findViewById(R.id.repeatPasswordInputLayout);
        views.add(repeatPasswordInputLayout);
        repeatPasswordEditText = findViewById(R.id.repeatPasswordEditText);
        views.add(repeatPasswordEditText);

        dateOfBirthInputLayout = findViewById(R.id.dateOfBirthInputLayout);
        views.add(dateOfBirthInputLayout);
        dateOfBirthEditText = findViewById(R.id.dateOfBirthEditText);
        views.add(dateOfBirthEditText);

        addressLine1InputLayout = findViewById(R.id.address1InputLayout);
        views.add(addressLine1InputLayout);
        addressLine1EditText = findViewById(R.id.address1EditText);
        views.add(addressLine1EditText);

        addressLine2InputLayout = findViewById(R.id.address2InputLayout);
        addressLine2EditText = findViewById(R.id.address2EditText);

        postalCodeInputLayout = findViewById(R.id.postalCodeInputLayout);
        views.add(postalCodeInputLayout);
        postalCodeEditText = findViewById(R.id.postalCodeEditText);
        views.add(postalCodeEditText);

        countryInputLayout = findViewById(R.id.countryInputLayout);
        views.add(countryInputLayout);
        countryEditText = findViewById(R.id.countryEditText);
        views.add(countryEditText);

        cityInputLayout = findViewById(R.id.cityInputLayout);
        views.add(cityInputLayout);
        cityEditText = findViewById(R.id.cityEditText);
        views.add(cityEditText);

        mobilePhoneInputLayout = findViewById(R.id.mobilePhoneInputLayout);
        views.add(mobilePhoneInputLayout);
        mobilePhoneEditText = findViewById(R.id.mobilePhoneEditText);
        views.add(mobilePhoneEditText);

        createAccountButton = findViewById(R.id.createAccountButton);

    }
}
