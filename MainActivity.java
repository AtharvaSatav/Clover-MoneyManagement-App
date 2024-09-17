package com.example.transactionmanagerapp;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
//import com.github.PhilJay.MPAndroidChart;
//import com.github.mikephil.charting.data.PieData;
//import com.github.mikephil.charting.data.PieDataSet;
//import com.github.mikephil.charting.data.PieEntry;
//import com.github.mikephil.charting.utils.ColorTemplate;
import android.graphics.Color;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private ListView textViewSpokenText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the TextView
        textViewSpokenText = findViewById(R.id.textViewSpokenText);

        // Check and request microphone permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        REQUEST_RECORD_AUDIO_PERMISSION);
            } else {
                // Permissions are already granted
                // Load and display the last recognized spoken text
                displayLastSpokenText();
                //displayTransactions();
            }
        } else {
            // Permissions are automatically granted for devices below Android 6.0
            // Load and display the last recognized spoken text
            displayLastSpokenText();
            //displayTransactions();
        }
        //populatePieChart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Update the TextView with the latest data when the activity resumes
        displayLastSpokenText();
        //displayTransactions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                // Load and display the last recognized spoken text
                displayLastSpokenText();
                //displayTransactions();
            } else {
                // Permission denied
                // Handle the case where the user denied the permission.
                //textViewSpokenText.setText("Microphone permission denied. Cannot record voice.");
                TextView textView = findViewById(R.id.permission_denied_text);
                textView.setText("Microphone permission denied. Cannot record voice.");
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

//    private void displayTransactions() {
//        SharedPreferences prefs = getSharedPreferences("VoiceData", MODE_PRIVATE);
//        String transactions = prefs.getString("transactions", "No transactions recorded yet.");
//
//        textViewSpokenText.setText(transactions);
//    }

    private void displayLastSpokenText() {
        SharedPreferences prefs = getSharedPreferences("VoiceData", MODE_PRIVATE);
        String transactions = prefs.getString("transactions", "");

        // Split transactions by newline character
        String[] transactionArray = transactions.split("\n");

        // Create an ArrayAdapter to display the transactions in the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, transactionArray);

        ListView listView = findViewById(R.id.textViewSpokenText);
        listView.setAdapter(adapter);
    }

//    private void populatePieChart() {
//        PieChart pieChart = findViewById(R.id.pie_chart);
//
//        // Get the transactions from SharedPreferences
//        SharedPreferences prefs = getSharedPreferences("VoiceData", MODE_PRIVATE);
//        String transactions = prefs.getString("transactions", "");
//
//        // Split transactions by newline character
//        String[] transactionArray = transactions.split("\n");
//
//        // Create a hashmap to store category amounts
//        HashMap<String, Float> categoryAmounts = new HashMap<>();
//
//        // Iterate through transactions and update category amounts
//        for (String transaction : transactionArray) {
//            String[] parts = transaction.split(": ");
//            String category = parts[0];
//            float amount = Float.parseFloat(parts[1]);
//
//            if (categoryAmounts.containsKey(category)) {
//                categoryAmounts.put(category, categoryAmounts.get(category) + amount);
//            } else {
//                categoryAmounts.put(category, amount);
//            }
//        }
//
//        // Create a pie chart dataset
//        ArrayList<PieEntry> entries = new ArrayList<>();
//        for (Map.Entry<String, Float> entry : categoryAmounts.entrySet()) {
//            entries.add(new PieEntry(entry.getValue(), entry.getKey()));
//        }
//
//        PieDataSet dataSet = new PieDataSet(entries, "Categories");
//        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
//
//        PieData data = new PieData(dataSet);
//        pieChart.setData(data);
//        pieChart.animateY(1000);
//    }

//    private void displayLastSpokenText() {
//        SharedPreferences prefs = getSharedPreferences("VoiceData", MODE_PRIVATE);
//        String lastSpokenText = prefs.getString("lastSpokenText", "No recorded text yet");
//        String lastCategory = prefs.getString("lastCategory", "Unknown category");
//        String lastAmount = prefs.getString("lastAmount", "Unknown amount");
//
//        textViewSpokenText.setText("Last Spoken Text: " + lastSpokenText + "\n" +
//                "Category: " + lastCategory + "\n" +
//                "Amount: " + lastAmount);
//    }
}











//package com.example.transactionmanagerapp;
//
//import android.Manifest;
//import android.content.pm.PackageManager;
//import android.os.Build;
//import android.os.Bundle;
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//
//public class MainActivity extends AppCompatActivity {
//
//    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        // Check and request microphone permission
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
//                    != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.RECORD_AUDIO},
//                        REQUEST_RECORD_AUDIO_PERMISSION);
//            } else {
//                // Permissions are already granted
//                // You can choose to start recording here if needed
//            }
//        } else {
//            // Permissions are automatically granted for devices below Android 6.0
//            // You can choose to start recording here if needed
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Permission granted
//                // You can choose to start recording here if needed
//            } else {
//                // Permission denied
//                // Handle the case where the user denied the permission.
//            }
//        }
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }
//}
