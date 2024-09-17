package com.example.transactionmanagerapp;

import android.Manifest;
import android.app.PendingIntent;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.SmsMessage;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import android.provider.Telephony;

public class SmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            if (pdus != null) {
                for (Object pdu : pdus) {
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
                    String sender = smsMessage.getDisplayOriginatingAddress();
                    String messageBody = smsMessage.getMessageBody();

                    // Log the SMS details for debugging
                    Log.d("SmsReceiver", "SMS received from: " + sender);
                    Log.d("SmsReceiver", "Message: " + messageBody);

                    // Check if the app has the necessary notification permission before attempting to send a notification
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        // Android 13 and above require POST_NOTIFICATIONS permission
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                                != PackageManager.PERMISSION_GRANTED) {
                            // Permission is not granted, handle appropriately
                            Log.w("SmsReceiver", "POST_NOTIFICATIONS permission not granted. Cannot send notification.");
                            return; // Exit without sending a notification
                        }
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                        if (Telephony.Sms.getMessageType(intent) != Telephony.Sms.MESSAGE_TYPE_TRANSACTIONAL) {
                            // Ignore low-priority SMS messages
                            return;
                    }

                    // If permission is granted, or if below Android 13, proceed with sending the notification
                    sendNotification(context);
                }
            }
        }
    }

    private void sendNotification(Context context) {
        Intent recordIntent = new Intent(context, VoiceRecordingService.class);
        recordIntent.setAction("START_RECORDING");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "CHANNEL_ID")
                .setSmallIcon(R.drawable.ic_mic)
                .setContentTitle("New Transaction Detected")
                .setContentText("Record your transaction by voice")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .addAction(R.drawable.ic_mic, "Record", PendingIntent.getService(context, 0, recordIntent, PendingIntent.FLAG_IMMUTABLE));

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        try {
            notificationManager.notify(1, builder.build());
        } catch (SecurityException e) {
            Log.e("SmsReceiver", "Notification permission denied or not available.", e);
        }
    }
}
