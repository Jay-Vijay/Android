package com.example.exe06;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

import java.util.ArrayList;
import java.util.Locale;

public class SpeechToTextHelper implements RecognitionListener {
    private SpeechRecognizer speechRecognizer;
    private OnSpeechRecognitionListener listener;
    private Locale currentLocale = Locale.getDefault(); // Default locale

    public void initSpeechToText(Context context) {
        if (speechRecognizer == null) {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
            speechRecognizer.setRecognitionListener(this);
        }
    }

    public void startSpeechRecognition() {
        if (speechRecognizer != null) {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, currentLocale.toString());
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now");

            try {
                speechRecognizer.startListening(intent);
            } catch (IllegalStateException e) {
                Log.e("STT", "Failed to start listening", e);
                if (listener != null) {
                    listener.onSpeechError(SpeechRecognizer.ERROR_CLIENT);
                }
            }
        } else {
            Log.e("STT", "SpeechRecognizer is not initialized");
            if (listener != null) {
                listener.onSpeechError(SpeechRecognizer.ERROR_CLIENT);
            }
        }
    }

    public void setOnSpeechRecognitionListener(OnSpeechRecognitionListener listener) {
        this.listener = listener;
    }

    public void setLanguage(Locale locale) {
        this.currentLocale = locale;
    }

    @Override
    public void onResults(Bundle results) {
        ArrayList<String> recognizedText = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        if (listener != null && recognizedText != null && !recognizedText.isEmpty()) {
            listener.onSpeechRecognized(recognizedText.get(0));
        } else if (listener != null) {
            listener.onSpeechError(SpeechRecognizer.ERROR_NO_MATCH);
        }
    }

    @Override
    public void onPartialResults(Bundle bundle) {
        // Handle partial results if needed
    }

    @Override
    public void onEvent(int eventType, Bundle params) {
        // Handle other events if needed
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
        // Handle ready for speech event if needed
    }

    @Override
    public void onBeginningOfSpeech() {
        // Handle beginning of speech event if needed
    }

    @Override
    public void onRmsChanged(float rmsdB) {
        // Handle RMS changes if needed
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        // Handle buffer received event if needed
    }

    @Override
    public void onEndOfSpeech() {
        // Handle end of speech event if needed
    }

    @Override
    public void onError(int error) {
        String errorMessage;
        switch (error) {
            case SpeechRecognizer.ERROR_AUDIO:
                errorMessage = "Audio recording error.";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                errorMessage = "Client-side error.";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                errorMessage = "Insufficient permissions.";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                errorMessage = "Network error.";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                errorMessage = "Network timeout.";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                errorMessage = "No speech match.";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                errorMessage = "Recognizer is busy.";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                errorMessage = "Server error.";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                errorMessage = "Speech timeout.";
                break;
            default:
                errorMessage = "Unknown error.";
                break;
        }
        Log.e("STT", "Error: " + errorMessage);
        if (listener != null) {
            listener.onSpeechError(error);
        }
    }

    public void shutdown() {
        if (speechRecognizer != null) {
            speechRecognizer.cancel();
            speechRecognizer.destroy();
            speechRecognizer = null;
        }
    }

    public interface OnSpeechRecognitionListener {
        void onSpeechRecognized(String recognizedText);
        void onSpeechError(int errorCode);
    }
}
