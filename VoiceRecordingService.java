//package com.example.transactionmanagerapp;
//
//import android.app.Service;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.IBinder;
//import java.util.ArrayList;
//import android.os.Bundle;
//import android.speech.SpeechRecognizer;
//import android.speech.RecognizerIntent;
//import android.speech.RecognitionListener;
//import android.util.Log;
//import androidx.annotation.Nullable;
//import androidx.core.app.NotificationCompat;
//import java.util.ArrayList;
//import java.util.Locale;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//public class VoiceRecordingService extends Service {
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        if (intent != null && "START_RECORDING".equals(intent.getAction())) {
//            startForeground(1, createNotification().build());
//            startVoiceRecording();
//        }
//        return START_STICKY;
//    }
//
//    private NotificationCompat.Builder createNotification() {
//        return new NotificationCompat.Builder(this, "CHANNEL_ID")
//                .setContentTitle("Recording Transaction")
//                .setContentText("Please speak your transaction details.")
//                .setSmallIcon(R.drawable.ic_mic);
//    }
//
//    private void startVoiceRecording() {
//        try {
//            SpeechRecognizer speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
//            Intent recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//            recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//            recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
//
//            speechRecognizer.setRecognitionListener(new RecognitionListener() {
//                @Override
//                public void onResults(Bundle results) {
//                    ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
//                    if (matches != null && !matches.isEmpty()) {
//                        String spokenText = matches.get(0);
//                        processVoiceInput(spokenText);
//                    }
//                }
//
//                @Override public void onReadyForSpeech(Bundle params) {}
//                @Override public void onBeginningOfSpeech() {}
//                @Override public void onRmsChanged(float rmsdB) {}
//                @Override public void onBufferReceived(byte[] buffer) {}
//                @Override public void onEndOfSpeech() {}
//                @Override public void onError(int error) {}
//                @Override public void onPartialResults(Bundle partialResults) {}
//                @Override public void onEvent(int eventType, Bundle params) {}
//            });
//
//            speechRecognizer.startListening(recognizerIntent);
//        } catch (SecurityException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void processVoiceInput(String spokenText) {
//        Pattern pattern = Pattern.compile("(\\w+)\\s(\\d+)");
//        Matcher matcher = pattern.matcher(spokenText);
//
//        if (matcher.find()) {
//            String category = matcher.group(1);
//            String amount = matcher.group(2);
//
//            Log.d("VoiceRecordingService", "Category: " + category + ", Amount: " + amount);
//
//            // Save the transaction in SharedPreferences
//            SharedPreferences prefs = getSharedPreferences("VoiceData", MODE_PRIVATE);
//            SharedPreferences.Editor editor = prefs.edit();
//
//            // Get the existing transactions list
//            String transactions = prefs.getString("transactions", "");
//
//            // Append the new transaction
//            transactions += category + ": " + amount + "\n";
//
//            // Save the updated list
//            editor.putString("transactions", transactions);
//            editor.apply();
//        }
//    }
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//}











package com.example.transactionmanagerapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.SpeechRecognizer;
import android.speech.RecognizerIntent;
import android.speech.RecognitionListener;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VoiceRecordingService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && "START_RECORDING".equals(intent.getAction())) {
            startForeground(1, createNotification().build());
            startVoiceRecording();
        }
        return START_STICKY;
    }

    private NotificationCompat.Builder createNotification() {
        return new NotificationCompat.Builder(this, "CHANNEL_ID")
                .setContentTitle("Recording Transaction")
                .setContentText("Please speak your transaction details.")
                .setSmallIcon(R.drawable.ic_mic);
    }

    private void startVoiceRecording() {
        try {
            SpeechRecognizer speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
            Intent recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

            speechRecognizer.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onResults(Bundle results) {
                    ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    if (matches != null && !matches.isEmpty()) {
                        String spokenText = matches.get(0);
                        processVoiceInput(spokenText);
                    }
                }

                @Override public void onReadyForSpeech(Bundle params) {}
                @Override public void onBeginningOfSpeech() {}
                @Override public void onRmsChanged(float rmsdB) {}
                @Override public void onBufferReceived(byte[] buffer) {}
                @Override public void onEndOfSpeech() {}
                @Override public void onError(int error) {}
                @Override public void onPartialResults(Bundle partialResults) {}
                @Override public void onEvent(int eventType, Bundle params) {}
            });

            speechRecognizer.startListening(recognizerIntent);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void processVoiceInput(String spokenText) {

        getSharedPreferences("VoiceData", MODE_PRIVATE)
                .edit()
                .putString("lastSpokenText", spokenText)
                .apply();


        Pattern pattern = Pattern.compile("(\\w+)\\s(\\d+)");
        Matcher matcher = pattern.matcher(spokenText);

        if (matcher.find()) {
            String category = matcher.group(1);
            String amount = matcher.group(2);

            getSharedPreferences("VoiceData", MODE_PRIVATE)
                    .edit()
                    .putString("lastCategory", category)
                    .putString("lastAmount", amount)
                    .apply();

            Log.d("VoiceRecordingService", "Category: " + category + ", Amount: " + amount);

            SharedPreferences prefs = getSharedPreferences("VoiceData", MODE_PRIVATE);
            String existingTransactions = prefs.getString("transactions", "");
            String newTransactions = existingTransactions + "\n" + category + ": " + amount;

            prefs.edit()
                    .putString("transactions", newTransactions)
                    .apply();
            // Save the transaction in your app's database or send a broadcast to update the UI or data
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}











//package com.example.transactionmanagerapp;
//
//import android.app.Service;
//import android.content.Intent;
//import android.os.IBinder;
//import android.os.Bundle;
//import android.speech.SpeechRecognizer;
//import android.speech.RecognizerIntent;
//import android.speech.RecognitionListener;
//import android.util.Log;
//import androidx.annotation.Nullable;
//import androidx.core.app.NotificationCompat;
//import java.util.ArrayList;
//import java.util.Locale;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//public class VoiceRecordingService extends Service {
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        if (intent != null && "START_RECORDING".equals(intent.getAction())) {
//            startForeground(1, createNotification().build());
//            startVoiceRecording();
//        }
//        return START_STICKY;
//    }
//
//    private NotificationCompat.Builder createNotification() {
//        return new NotificationCompat.Builder(this, "CHANNEL_ID")
//                .setContentTitle("Recording Transaction")
//                .setContentText("Please speak your transaction details.")
//                .setSmallIcon(R.drawable.ic_mic);
//    }
//
//    private void startVoiceRecording() {
//        try {
//            SpeechRecognizer speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
//            Intent recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//            recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//            recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
//
//            speechRecognizer.setRecognitionListener(new RecognitionListener() {
//                @Override
//                public void onResults(Bundle results) {
//                    ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
//                    if (matches != null && !matches.isEmpty()) {
//                        String spokenText = matches.get(0);
//                        processVoiceInput(spokenText);
//                    }
//                }
//
//                @Override public void onReadyForSpeech(Bundle params) {}
//                @Override public void onBeginningOfSpeech() {}
//                @Override public void onRmsChanged(float rmsdB) {}
//                @Override public void onBufferReceived(byte[] buffer) {}
//                @Override public void onEndOfSpeech() {}
//                @Override public void onError(int error) {}
//                @Override public void onPartialResults(Bundle partialResults) {}
//                @Override public void onEvent(int eventType, Bundle params) {}
//            });
//
//            speechRecognizer.startListening(recognizerIntent);
//        } catch (SecurityException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void processVoiceInput(String spokenText) {
//        Pattern pattern = Pattern.compile("(\\w+)\\s(\\d+)");
//        Matcher matcher = pattern.matcher(spokenText);
//
//        if (matcher.find()) {
//            String category = matcher.group(1);
//            String amount = matcher.group(2);
//
//            Log.d("VoiceRecordingService", "Category: " + category + ", Amount: " + amount);
//
//            // Save the transaction in your app's database or send a broadcast to update the UI or data
//        }
//    }
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//}
