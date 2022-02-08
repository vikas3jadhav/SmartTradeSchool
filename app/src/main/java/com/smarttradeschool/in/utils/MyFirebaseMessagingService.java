package com.smarttradeschool.in.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;


import com.google.firebase.installations.Utils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import com.smarttradeschool.in.R;
import com.smarttradeschool.in.activity.HomeActivity;
import com.smarttradeschool.in.activity.NotificationActivity;
import com.squareup.picasso.Picasso;


import org.json.JSONArray;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String CHANNEL_ID = "Smart Trade School";
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private NotificationUtils notificationUtils;

    Bitmap bitmap;
    Bitmap userpic;



    NotificationManager mNotificationManager;

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);

        storeRegIdInPref(token);


    }


    private void storeRegIdInPref(String token) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("fcmtoken", token);
        editor.commit();
    }




    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // There are two types of messages data messages and notification messages. Data messages
        // are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data
        // messages are the type
        // traditionally used with GCM. Notification messages are only received here in
        // onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated
        // notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages
        // containing both notification
        // and data payloads are treated as notification messages. The Firebase console always
        // sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        //
        /*Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }*/

        Log.e(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage == null)
            return;

        String action =remoteMessage.getData().get("action");



        if ( action.equals("oneloginreturn")) {


            // Check if message contains a notification payload.
            if (remoteMessage.getNotification() != null) {
                Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
                //handleNotification(remoteMessage.getNotification().getBody());
                handleNotification(remoteMessage);
            }

            // Check if message contains a data payload.
            if (remoteMessage.getData().size() > 0) {
                Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
                //Toast.makeText(this, "Push Received", Toast.LENGTH_SHORT).show();

            /*try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }*/
                handleDataMessage(remoteMessage);
            }
        } else {

        }
    }

    private void handleNotification(RemoteMessage remoteMessage) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", remoteMessage.getMessageType());
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);


//            Toast.makeText(this, "testt", Toast.LENGTH_SHORT).show();
//            Log.e("pushh", "fsdf");

            // play notification sound
//            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
//            notificationUtils.playNotificationSound();
            handleDataMessage(remoteMessage);
        } else {
            // If the app is in background, firebase itself handles the notification
            handleDataMessage(remoteMessage);
        }
    }

    private void handleDataMessage(RemoteMessage remoteMessage) {

        //message will contain the Push Message
        Log.e(TAG, remoteMessage + "");
        String title = remoteMessage.getData().get("title");
        String message = remoteMessage.getData().get("message");
        String notificationid = remoteMessage.getData().get("id");

        sendNotification(title, message, notificationid);



    }

    private void sendNotification(String title, String messageBody, String notificationid) {

        boolean isLoadWithPicasso;

        Log.d("push", "inside push message");

        //generating unique id for every notification
        long time = new Date().getTime();
        String tmpStr = String.valueOf(time);
        String last4Str = tmpStr.substring(tmpStr.length() - 5);
        int notificationId = Integer.valueOf(last4Str);

        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        // white icon problem solution
        int icon;
        Bitmap largeIcon;

            icon = R.drawable.ic_noti;
            largeIcon = userpic;
            isLoadWithPicasso = true;



        //notification sound
//        Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
//                + "://" + this.getPackageName() + "/" + R.raw.notification);

   //     Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Uri soundUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"+ getApplicationContext().getPackageName() + "/" + R.raw.notification);


        Intent parentIntent;
        parentIntent = new Intent(this, HomeActivity.class);


        Intent intent;
        intent = new Intent(this, HomeActivity.class);


        Bundle bundle = new Bundle();

        bundle.putString("notificationid", notificationid);
        bundle.putString("message", messageBody);
        bundle.putString("title", title);
        intent.putExtras(bundle);

        // The stack builder object will contain an artificial back stack for
        // the started Activity. This ensures that navigating backward from the Activity
        // leads out of your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addNextIntentWithParentStack(parentIntent);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(intent);

        PendingIntent contentIntent = stackBuilder.getPendingIntent(notificationId,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);

//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        /*NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setLargeIcon(largeIcon)
                .setSmallIcon(icon)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setStyle(new NotificationCompat.InboxStyle())
                .setGroupSummary(true)
                .setSound(defaultSoundUri);*/

        NotificationChannel mChannel;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            String description = getString(R.string.app_name);
            mChannel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_HIGH);
            mChannel.setLightColor(Color.GRAY);
            mChannel.enableLights(true);
            mChannel.setDescription(description);
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            mChannel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), audioAttributes);

            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel( mChannel );
            }
        }


        NotificationCompat.Builder notificationBuilder;
//        notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder = new NotificationCompat.Builder(this, "12")
                    .setLargeIcon(largeIcon)
                    //.setSmallIcon(icon)
                    .setTicker(title).setWhen(0)
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setContentText(messageBody)
                    .setSound(soundUri)
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                   // .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    //.setStyle(new NotificationCompat.InboxStyle())/*Notification with Image*/
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                    .setGroupSummary(true);


        notificationBuilder.setSmallIcon(icon);

        int color = 0x960000;
        notificationBuilder.setColor(color);
        notificationBuilder.setContentIntent(contentIntent);

        createNotificationChannel(notificationBuilder, mNotificationManager);

        mNotificationManager.notify(notificationId, notificationBuilder.build());
    }

/*    private void sendNotificationFinal(String title, String messageBody, String TrueOrFalse,
                                       String classname) {

        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("classname", classname);
        *//*PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);*//*
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(HomeActivity.class);
        stackBuilder.addNextIntent(intent);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.sts)
                .setContentTitle(title)
                .setContentText(messageBody)
                //.setAutoCancel(true)
                .setGroupSummary(true)
                .setSound(defaultSoundUri)
                .setNumber(1)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }*/



    private void createNotificationChannel(NotificationCompat.Builder notificationBuilder,
                                           NotificationManager mNotificationManager) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //builder
            notificationBuilder
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setChannelId(CHANNEL_ID);

            //manager
            CharSequence name = "Smart Trade School";
            String description = "Smart Trade School";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            mNotificationManager.createNotificationChannel(channel);
        }
    }



    static JSONArray response;


    public interface SidebarApiInterface {

        @FormUrlEncoded
        @POST("./")
        Call<ResponseBody> getSidebar(@FieldMap Map<String,String> param );
    }



}
