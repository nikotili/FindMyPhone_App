package com.introtomobileappdev.introtomobileappdev;


import android.content.Context;

import com.introtomobileappdev.introtomobileappdev.utils.Constants;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

public class SocketTest
{
    public static void main(String[] args)
    {
        try {
            sendFile(new Socket(Constants.SRV_IP, Constants.SRV_PORT_FILES));
        }
        catch (Exception e)
        {

        }
    }

    public static void chat(Context a)
    {

        while (true)
        {
            try
            {
//                TimeUnit.SECONDS.sleep(5);
                Socket s = new Socket(Constants.SRV_IP, Constants.SRV_PORT);
                DataInputStream dataInputStream = new DataInputStream(s.getInputStream());
                DataOutputStream dataOutputStream = new DataOutputStream(s.getOutputStream());
                byte[] bytes = "Testing".getBytes(Charset.forName("UTF-8"));
                dataOutputStream.write(bytes);

                StringBuffer stringBuffer = new StringBuffer();

                stringBuffer.append(dataInputStream.readLine());
                if (!stringBuffer.toString().equals("null")) {
                    sendFile(new Socket(Constants.SRV_IP, Constants.SRV_PORT_FILES));
                    break;
                }


            }

            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

    }

    private static void start()
    {
        try
        {
            while (true)
            {
                Socket s = getNewSocket(Constants.SRV_IP, Constants.SRV_PORT);
                DataInputStream dataInputStream = new DataInputStream(s.getInputStream());
                DataOutputStream dataOutputStream = new DataOutputStream(s.getOutputStream());
                byte[] bytes = Constants.JAVA_SIGNATURE.getBytes(Charset.forName("UTF-8"));
                dataOutputStream.write(bytes);

                StringBuffer serverQuery = new StringBuffer();
                String tmp;
                serverQuery.append(dataInputStream.readLine());

                System.out.println("Fasd");
                System.out.println(serverQuery.toString());

                if (serverQuery.toString().contains("sendfile")) {
                    sendFile(getNewSocket(Constants.SRV_IP, Constants.SRV_PORT_FILES));
                }


                TimeUnit.SECONDS.sleep(5);
            }
        }

        catch (Exception e)
        {
//            System.out.println("cant");
            e.printStackTrace();
        }
    }


    private static void sendFile(Socket s)
    {
        File file = new File("files/file.jpg");

        try
        {
            FileInputStream fileInputStream = new FileInputStream(file);

            DataOutputStream dataOutputStream = new DataOutputStream(s.getOutputStream());
            int size = fileInputStream.available();
//            dataOutputStream.write(Constants.IMAGE.getBytes(Charset.forName("utf-8")));
            System.out.println(size);
            int sentBytes = 0;

            while(true)
            {
                sentBytes += Constants.DEFAULT_BUFFER_SIZE;
                if (sentBytes < size)
                {
                    byte[] bytes = new byte[Constants.DEFAULT_BUFFER_SIZE];
                    fileInputStream.read(bytes, 0, Constants.DEFAULT_BUFFER_SIZE);
                    dataOutputStream.write(bytes);
                    dataOutputStream.flush();
                }

                else
                {
                    int lastBytesSize = 1024 - (sentBytes - size);
                    System.out.println("sent bytes " + sentBytes + ", size " + size);
                    System.out.println(lastBytesSize);
                    byte[] lastBytes = new byte[lastBytesSize];
                    fileInputStream.read(lastBytes, 0, lastBytesSize);
                    dataOutputStream.write(lastBytes);
                    dataOutputStream.flush();
                    break;
                }
            }

            dataOutputStream.close();
            s.close();
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    private static Socket getNewSocket(String ip, int port)
    {
        try
        {
            return new Socket(ip, port);
        }

        catch (Exception e)
        {
            return null;
        }
    }

}
