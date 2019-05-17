package com.introtomobileappdev.introtomobileappdev.tasks;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.util.Log;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.introtomobileappdev.introtomobileappdev.activities.MainActivity;
import com.introtomobileappdev.introtomobileappdev.utils.Constants;
import com.introtomobileappdev.introtomobileappdev.R;
import com.introtomobileappdev.introtomobileappdev.utils.Utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static android.content.Context.LOCATION_SERVICE;

public class ConnectionTask extends AsyncTask<Context, Void, Void>
{

    private MediaPlayer mediaPlayer;

    private void playSongAction(Context context)
    {
        mediaPlayer = MediaPlayer.create(context, R.raw.alert);
        mediaPlayer.start();
    }

    private void stopSongAction()
    {
        if (mediaPlayer != null)
            if (mediaPlayer.isPlaying())
                mediaPlayer.stop();
    }


    private Location getLastKnownLocation(final Context context) {
        LocationManager mLocationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        context.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, 1,1);
        for (String provider : providers) {
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    private Location getLocation(final Context context)
    {
        try
        {
            LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (location == null)
            {
                throw new NullPointerException();
            }

            else
            {
                return location;
            }

        }

        catch (SecurityException e)
        {
            e.printStackTrace();
        }

        return null;
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

                dataOutputStream.write(Utils.buildDefaultResponse(getLastKnownLocation(context)).getBytes());
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
                        try {
                            takePhoto(context, Constants.REAR_CAMERA);
                            TimeUnit.SECONDS.sleep(2);
                            sendTakenPhoto(context, Constants.SRV_IP, Constants.SRV_PORT_FILES);
                            deleteTakenPhoto(context);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        break;

                    case Constants.PLAY_SONG_ACTION:
                        playSongAction(context);
                        break;

                    case Constants.STOP_SONG_ACTION:
                        stopSongAction();
                        break;

                    case Constants.ERASE_EVERYTHING_ACTION:
                        //TO BE
                        break;

                    case Constants.CHECK_IF_ONLINE_ACTION:
                        // TO BE

                        break;

                    default: //NULL

                        break;
                }

            }

            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private void takePictureAction(Context context, int camID) {
        try {
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
