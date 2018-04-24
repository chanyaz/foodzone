package com.maya.wadmin.notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.maya.wadmin.R;
import com.maya.wadmin.activities.SplashActivity;
import com.maya.wadmin.utilities.Logger;
import com.maya.wadmin.utilities.Utility;


/**
 * Created by Maya on 11/30/2016.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService
{
    private static final String TAG = "NOTIFICATION DATA";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Logger.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0)
        {
            Logger.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null)
        {
            Logger.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
        try
        {
            String content = remoteMessage.getData().get("message");
            String bitmap_url=remoteMessage.getData().get("url");
            String title=remoteMessage.getData().get("title");
            String bigText=remoteMessage.getData().get("big_text");
            String summary=remoteMessage.getData().get("summary");
            String color=remoteMessage.getData().get("color");
            String allowBig=remoteMessage.getData().get("big_allow_icon");
            String notification_id=remoteMessage.getData().get("nofitication_id");
            String optionType=remoteMessage.getData().get("type");
            sendNotification(content,bitmap_url,title,bigText,summary,color,allowBig,notification_id,optionType);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private void sendNotification(final String messageBody, final String bitmap_url, final String title, final String bigText, String summary, String color, String allowBig, String notication_id, String optionType)
    {


    if(messageBody!=null)
    {
    if (optionType != null)
    {

                if(optionType.equals("1"))
                {

                    Intent intent = new Intent(this, SplashActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

                    Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();

                    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                            .setTicker(title).setWhen(0)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentText(messageBody)
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri)
                            .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                            .setContentIntent(pendingIntent);
                    NotificationManager notificationManager =
                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    if (title != null) {
                        notificationBuilder.setContentTitle(title);
                    } else {
                        notificationBuilder.setContentTitle("Memories");
                    }
                    if (color != null) {
                        notificationBuilder.setColor(Color.parseColor(color));
                    } else {
                        notificationBuilder.setColor(Color.parseColor("#FF5656"));
                    }
                    if (summary != null)
                        bigPictureStyle.setSummaryText(messageBody);
                    if (summary != null)
                        bigPictureStyle.setSummaryText(messageBody);
                    if (bigText != null) {
                        bigPictureStyle.setBigContentTitle(bigText);
                    } else {

                    }
                    if (bitmap_url != null) {
                        Bitmap bitmap1 = Utility.urlToBitMap(bitmap_url);
                        if (bitmap1 != null) {
                            bigPictureStyle.bigPicture(bitmap1);
                            notificationBuilder.setStyle(bigPictureStyle);

                        }
                    } else {
                        //notificationBuilder.setStyle(bigPictureStyle);
                    }
                    if (allowBig != null) {
                        if (allowBig.equals("1")) {
                            notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
                        } else {

                        }
                    } else {

                    }
                    if (notication_id != null) {
                        try {
                            int notification_number = Integer.parseInt(notication_id);
                            if (notification_number > 0) {
                                notificationManager.notify(notification_number, notificationBuilder.build());
                            }
                        } catch (Exception e) {
                            notificationManager.notify(0, notificationBuilder.build());
                        }


                    } else {
                        notificationManager.notify(0, notificationBuilder.build());
                    }
                }
                else
                {

                }
    }
    }

    }


}
