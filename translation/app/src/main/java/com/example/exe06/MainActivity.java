package com.example.exe06;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private boolean permissionToRecordAudio = false;
    private TextToSpeechHelper textToSpeechHelper;
    private SpeechToTextHelper speechToTextHelper;
    private EditText editText;
    private TextView recognizedTextView;
    private Spinner languageSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Request audio recording permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION);
        } else {
            permissionToRecordAudio = true;
        }

        editText = findViewById(R.id.editText);
        recognizedTextView = findViewById(R.id.recognizedTextView);
        Button buttonSpeak = findViewById(R.id.buttonSpeak);
        Button buttonListen = findViewById(R.id.buttonListen);
        Button buttonClear = findViewById(R.id.buttonClear);
        languageSpinner = findViewById(R.id.languageSpinner);

        // Set up Spinner for language selection
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.language_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(adapter);

        textToSpeechHelper = new TextToSpeechHelper();
        textToSpeechHelper.initTextToSpeech(this);

        speechToTextHelper = new SpeechToTextHelper();
        speechToTextHelper.initSpeechToText(this);

        // Set default language
        updateLanguage(Locale.ENGLISH);

        // Listener for language selection
        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedLanguage = (String) parent.getItemAtPosition(position);
                if (selectedLanguage.equals("Tamil")) {
                    updateLanguage(new Locale("ta", "IN"));
                } else {
                    updateLanguage(Locale.ENGLISH);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Optional: handle if no item is selected
            }
        });

        speechToTextHelper.setOnSpeechRecognitionListener(new SpeechToTextHelper.OnSpeechRecognitionListener() {
            @Override
            public void onSpeechRecognized(String recognizedText) {
                editText.setText(recognizedText);
                recognizedTextView.setText(recognizedText);
                textToSpeechHelper.speakText("You said: " + recognizedText);
            }

            @Override
            public void onSpeechError(int errorCode) {
                String errorMessage = getErrorMessage(errorCode);
                Toast.makeText(MainActivity.this, "Speech recognition error: " + errorMessage, Toast.LENGTH_LONG).show();
            }

            private String getErrorMessage(int errorCode) {
                switch (errorCode) {
                    case SpeechRecognizer.ERROR_AUDIO:
                        return "Audio recording error.";
                    case SpeechRecognizer.ERROR_CLIENT:
                        return "Client-side error.";
                    case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                        return "Insufficient permissions.";
                    case SpeechRecognizer.ERROR_NETWORK:
                        return "Network error.";
                    case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                        return "Network timeout.";
                    case SpeechRecognizer.ERROR_NO_MATCH:
                        return "No speech match.";
                    case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                        return "Recognizer is busy.";
                    case SpeechRecognizer.ERROR_SERVER:
                        return "Server error.";
                    case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                        return "Speech timeout.";
                    default:
                        return "Unknown error.";
                }
            }
        });

        buttonSpeak.setOnClickListener(v -> {
            String text = editText.getText().toString();
            if (!text.isEmpty()) {
                textToSpeechHelper.speakText(text);
            } else {
                Toast.makeText(MainActivity.this, "Please enter text to speak", Toast.LENGTH_SHORT).show();
            }
        });

        buttonListen.setOnClickListener(v -> {
            if (permissionToRecordAudio) {
                speechToTextHelper.startSpeechRecognition();
            } else {
                Toast.makeText(MainActivity.this, "Permission to record audio is required", Toast.LENGTH_SHORT).show();
            }
        });

        buttonClear.setOnClickListener(v -> {
            editText.setText(""); // Clear the EditText
            recognizedTextView.setText(""); // Clear the recognized text display
            Toast.makeText(MainActivity.this, "Text cleared", Toast.LENGTH_SHORT).show(); // Optional feedback
        });
    }

    private void updateLanguage(Locale locale) {
        speechToTextHelper.setLanguage(locale);
        textToSpeechHelper.setLanguage(locale);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (textToSpeechHelper != null) {
            textToSpeechHelper.shutdown();
        }
        if (speechToTextHelper != null) {
            speechToTextHelper.shutdown();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            permissionToRecordAudio = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
            if (!permissionToRecordAudio) {
                Toast.makeText(this, "Permission to record audio is required for this app to function", Toast.LENGTH_LONG).show();
            }
        }
    }
}
