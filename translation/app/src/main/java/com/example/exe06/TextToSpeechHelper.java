package com.example.exe06;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

public class TextToSpeechHelper {
    private TextToSpeech textToSpeech;
    private Locale currentLocale = Locale.ENGLISH; // Default to English

    public void initTextToSpeech(Context context) {
        if (textToSpeech == null) {
            textToSpeech = new TextToSpeech(context, status -> {
                if (status == TextToSpeech.SUCCESS) {
                    setLanguage(currentLocale); // Set default language
                } else {
                    Log.e("TTS", "Initialization failed!");
                }
            });
        }
    }

    public void setLanguage(Locale locale) {
        if (textToSpeech != null) {
            currentLocale = locale;
            int result = textToSpeech.setLanguage(currentLocale);
            if (result == TextToSpeech.LANG_MISSING_DATA) {
                Log.e("TTS", "Language data missing for: " + currentLocale.getDisplayLanguage());
            } else if (result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "Language not supported: " + currentLocale.getDisplayLanguage());
            } else {
                Log.i("TTS", "Language set to: " + currentLocale.getDisplayLanguage());
            }
        } else {
            Log.e("TTS", "TextToSpeech not initialized");
        }
    }

    public void speakText(String text) {
        if (textToSpeech != null) {
            if (text != null && !text.isEmpty()) {
                textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
            } else {
                Log.w("TTS", "Text is null or empty");
            }
        } else {
            Log.e("TTS", "TextToSpeech not initialized");
        }
    }

    public void shutdown() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
            textToSpeech = null; // Release reference
        }
    }
}
