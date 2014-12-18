package com.github.subtitles.managers;

import android.text.Html;
import android.util.Log;

import com.github.subtitles.ChatScreen;

import com.parse.ParseObject;
import ru.yandex.speechkit.*;

/**
 * Created by Akmal on 20.11.2014.
 */
public class TalkRecognition {
    Recognizer recognizer;

    private boolean isActive = false;

    private ChatScreen chat;

    public TalkRecognition(final ChatScreen chat)
    {
        this.chat = chat;
    }

    public void start() {
        recognizer = Recognizer.create("ru-RU", "freeform", new RecognizerListener() {
            @Override
            public void onRecordingBegin(Recognizer recognizer) {
                chat.addMessage("...");
            }

            @Override
            public void onSpeechDetected(Recognizer recognizer) {
            }

            @Override
            public void onRecordingDone(Recognizer recognizer) {
            }

            @Override
            public void onSoundDataRecorded(Recognizer recognizer, byte[] bytes) {
            }

            @Override
            public void onPowerUpdated(Recognizer recognizer, float v) {
            }

            @Override
            public void onPartialResults(Recognizer recognizer, Recognition recognition, boolean isEndOfUtterance) {
                String message = recognition.getBestResultText();
                if (!message.isEmpty()) {
                    if (isEndOfUtterance) {
                        ParseObject recognizedPhrase = new ParseObject("RecognizedPhrase");
                        recognizedPhrase.put("Text", chat.getLastMessage());
                        recognizedPhrase.saveInBackground();
                        chat.rewriteLastMessage(message);
                        if (isActive) {
                            chat.addMessage("...");
                        }
                    } else {
                        if (isActive) {
                            chat.rewriteLastMessage(message + "...");
                        } else {
                            chat.rewriteLastMessage(message);
                        }
                    }
                }
            }

            @Override
            public void onRecognitionDone(Recognizer recognizer, Recognition recognition) {
            }

            @Override
            public void onError(Recognizer recognizer, ru.yandex.speechkit.Error error) {
            }
        }, true);

        recognizer.setVADEnabled(false);
        recognizer.start();
        isActive = true;
    }

    public void stop() {
        recognizer.finishRecording();
        isActive = false;
        if (chat.getMessagesCount() > 0) {
            String message = chat.getLastMessage();
            if (message == "...") {
                chat.deleteLastMessage();
            } else if (message.endsWith("...")) {
                chat.rewriteLastMessage(message.substring(0, message.length() - 3));
            }
        }
    }
}
