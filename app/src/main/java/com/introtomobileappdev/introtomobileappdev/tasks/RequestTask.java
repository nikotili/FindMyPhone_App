package com.introtomobileappdev.introtomobileappdev.tasks;

import android.os.AsyncTask;

import com.introtomobileappdev.introtomobileappdev.utils.Constants;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.HashMap;

public class RequestTask extends AsyncTask<HashMap<String, String>, Void, String> {

    private String singIn(HashMap data)
    {
        return tryRequest(constructSignInRequest(data));
    }

    private String signUp(HashMap data)
    {
        return tryRequest(constructSignUpRequest(data));
    }

    private String tryRequest(String request)
    {
        String response;
        try
        {
            Socket s = new Socket(Constants.SRV_IP, Constants.SRV_PORT_DB);
            DataOutputStream dataOutputStream = new DataOutputStream(s.getOutputStream());
            DataInputStream dataInputStream = new DataInputStream(s.getInputStream());
            dataOutputStream.write(request.getBytes());
            dataOutputStream.flush();

            response = dataInputStream.readLine();

            dataOutputStream.close();
            dataInputStream.close();

            s.close();
            return response;
        }

        catch (Exception e)
        {
            e.printStackTrace();
            return Constants.CONNECTION_ERROR;
        }
    }

    private String constructSignUpRequest(HashMap<String, String> data)
    {
        String request = Constants.SIGN_UP_REQUEST;

        request += String.format("\n'%s', ", data.get(Constants.FIRST_NAME_KEY));
        request += String.format("'%s', ", data.get(Constants.LAST_NAME_KEY));
        request += String.format("'%s', ", data.get(Constants.EMAIL_KEY));
        request += String.format("'%s', ", data.get(Constants.PASSWORD_KEY));
        request += String.format("'%s', ", data.get(Constants.DATE_OF_BIRTH_KEY));
        request += String.format("'%s', ", data.get(Constants.ADDRESS_1_KEY));
        request += String.format("'%s', ", data.get(Constants.ADDRESS_2_KEY));
        request += String.format("'%s', ", data.get(Constants.POSTAL_CODE_KEY));
        request += String.format("'%s', ", data.get(Constants.COUNTRY_KEY));
        request += String.format("'%s', ", data.get(Constants.COUNTRY_KEY));
        request += String.format("'%s'", data.get(Constants.MOBILE_PHONE_KEY));

        return request;
    }

    private String constructSignInRequest(HashMap<String, String> data)
    {
        String request = Constants.SIGN_IN_REQUEST;
        request += String.format("\n'%s', ", data.get(Constants.EMAIL_KEY));
        request += String.format("'%s'", data.get(Constants.PASSWORD_KEY));
        return request;
    }

    @Override
    protected String doInBackground(HashMap... maps) {

        if (maps[0].size() != 2)
            return signUp(maps[0]);

        return singIn(maps[0]);
    }
}
