package highonstudy.com.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import highonstudy.com.R;
import highonstudy.com.activity.MainActivity;
import highonstudy.com.activity.WebViewActivity;
import highonstudy.com.data.constant.AppConstant;
import highonstudy.com.data.sqlite.NotificationDbController;

import java.util.Map;
import java.util.concurrent.ExecutionException;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getData().size() > 0) {
            Map<String, String> params = remoteMessage.getData();
            sendNotification(params.get("title"), params.get("message"), params.get("url"),params.get("image"));
            broadcastNewNotification();
        }
    }

    private void sendNotification(String title, String messageBody, String posturl, String image) {

        // insert data into database
        NotificationDbController notificationDbController = new NotificationDbController(MyFirebaseMessagingService.this);
        notificationDbController.insertNotifyData(title, messageBody, posturl+"?utm_source=app");

        Bitmap bitmap = null;
        try {
            bitmap = Glide
                    .with(getApplicationContext())
                    .asBitmap()
                    .load(image)
                    .submit()
                    .get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String CHANNEL_ID = "noti01";

        Intent intent;
        if (posturl != null) {
            intent = new Intent(this, WebViewActivity.class);
            intent.putExtra(AppConstant.JOBTITLE, title);
            intent.putExtra(AppConstant.JOBURL, posturl+"?utm_source=app");
        } else {
            intent = new Intent(this, MainActivity.class);
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setDefaults(4)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000, 1000})
                .setSound(defaultSoundUri)
                .setChannelId(CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_bookmark))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody));
        if (bitmap != null) {
            notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap).setSummaryText(messageBody));
        }

        if (Build.VERSION.SDK_INT >= 16) {
            notificationBuilder.setPriority(1);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
        }

        notificationManager.notify(0, notificationBuilder.build());
    }

    private void broadcastNewNotification() {
        Intent intent = new Intent(AppConstant.NEW_NOTI);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

}
