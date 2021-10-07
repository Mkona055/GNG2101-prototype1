package uottawa.engineering.notifications;


import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

public class MainActivity extends AppCompatActivity {
    private NotificationManager notificationManager;
    public static final String CHANNEL_1_ID = "channel1";
    public static final String CHANNEL_2_ID = "channel2";
    public static final String CHANNEL_3_ID = "channel3";
    final long [] vibe = {1000, 1000, 1000, 1000, 1000};
    final Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CAMERA},123 );
        createNotificationChannels();
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Button buttonLightUp = findViewById(R.id.ButtonLightUp);
        Button buttonVibration = findViewById(R.id.ButtonVibration);
        Button buttonRingtone = findViewById(R.id.ButtonRingtone);


        buttonLightUp.setOnClickListener(v -> {
            try {
                toggleLightUpNotification(v);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        });
        buttonVibration.setOnClickListener(this::toggleVibrationNotification);
        buttonRingtone.setOnClickListener(this::toggleRingtoneNotification);
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    CHANNEL_1_ID,
                    "Channel 1",
                    NotificationManager.IMPORTANCE_HIGH
            );
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            channel1.setSound(notificationSound,audioAttributes);
            channel1.setDescription("This is Channel 1");

            NotificationChannel channel2 = new NotificationChannel(
                    CHANNEL_2_ID,
                    "Channel 2",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel2.enableLights(true);
            channel2.setLightColor(Color.WHITE);
            channel2.setDescription("This is Channel 2");

            NotificationChannel channel3 = new NotificationChannel(
                    CHANNEL_3_ID,
                    "Channel 3",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel3.setDescription("This is Channel 3");
            channel3.enableVibration(true);
            channel3.setVibrationPattern(vibe);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
            manager.createNotificationChannel(channel2);
            manager.createNotificationChannel(channel3);
        }
    }
    public void toggleRingtoneNotification(View v) {


        Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_one)
                .setContentTitle("Ringtone")
                .setContentText("All set ! Your trip was confirmed")
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setSound(notificationSound)
                .build();

        notificationManager.notify(1, notification);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void toggleLightUpNotification(View v) throws CameraAccessException {


        Notification notification = new NotificationCompat.Builder(this, CHANNEL_2_ID)
                .setSmallIcon(R.drawable.ic_two)
                .setContentTitle("LightUp")
                .setContentText("All set ! Your trip was confirmed")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        notificationManager.notify(2, notification);
        turnFlashLightOn();
    }



    public void toggleVibrationNotification(View v) {


        Notification notification = new NotificationCompat.Builder(this, CHANNEL_3_ID)
                .setSmallIcon(R.drawable.ic_three)
                .setVibrate(vibe)
                .setContentTitle("Vibration")
                .setContentText("All set ! Your trip was confirmed")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .build();

        notificationManager.notify(3, notification);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void turnFlashLightOn() throws CameraAccessException {
        if(getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)){
            String myString = "010101010101";
            CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE) ;
            String cameraId = cameraManager.getCameraIdList()[0];
            for (int i = 0; i < myString.length(); i++) {
                cameraManager.setTorchMode(cameraId, myString.charAt(i) == '0');
            }


        }
    }

}