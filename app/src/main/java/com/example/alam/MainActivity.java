package com.example.alam;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import ai.picovoice.porcupine.*;

import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

// Display Contacts using java
public class MainActivity extends AppCompatActivity {


    private ListView listView;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Check if the request code matches the READ_CONTACTS permission request
        if (requestCode == 101) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // If permission is granted, retrieve contacts
                retriveContacts(new ArrayList<>(),new ArrayList<>());
            } else {
                // Permission denied, handle accordingly (optional: show a message)
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.list); // Initialize the ListView
        // Check if READ_CONTACTS permission is not granted
        Log.d("prawin","prawin");
        if (checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            // Request READ_CONTACTS permission
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 101);
        } else {
            retriveContacts(new ArrayList<>(),new ArrayList<>());
        }
    }



    // Method to retrieve contacts and populate the provided lists with names and numbers
    private void retriveContacts(List<String> name, List<String> number) {
        // Get the ContentResolver instance from the current context
        ContentResolver resolver = getContentResolver();

        // Perform a query to retrieve contact information from the device's contacts
        Cursor cursor = resolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, // URI for accessing contact data
                null, // Projection: retrieve all columns
                null, // Selection: no filtering
                null, // SelectionArgs: no arguments
                ContactsContract.Contacts.DISPLAY_NAME + " ASC" // Sort order: by display name in ascending order
        );
        // Retrieve the column indices for the display name and phone number
        int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

// Iterate over the cursor's result set
        while (cursor.moveToNext()) {
            // Retrieve the contact name and phone number from the cursor and add them to the lists
            name.add(cursor.getString(nameIndex));
            number.add(cursor.getString(numberIndex));
        }
        cursor.close();
        setAdapter(name,number);
    }
    private void setAdapter(List<String> name, List<String> number) {

        // Creating a new instance of contact_adapter locally within the method
        contact_adapter contact_adapter = new contact_adapter(this, name, number);
        // Setting the newly created adapter to the listView
        listView.setAdapter(contact_adapter);
    }


}



