package com.github.subtitles.managers;

import android.util.Log;

import com.github.subtitles.ChatScreen;

import ru.yandex.speechkit.*;

/**
 * Created by Akmal on 20.11.2014.
 */
public class TalkRecognition {
    Recognizer recognizer;

    private ChatScreen chat;

    public TalkRecognition(final ChatScreen chat)
    {
        this.chat = chat;
    }

    public void start() {
        recognizer = Recognizer.create("ru-RU", "freeform", new RecognizerListener() {
            @Override
            public void onRecordingBegin(Recognizer recognizer) {
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
                        chat.rewriteLastMessage(message);
                        chat.addMessage("...");
                    } else {
                        chat.rewriteLastMessage(message + "...");
                    }
                }
            }

            @Override
            public void onRecognitionDone(Recognizer recognizer, Recognition recognition) {
                String message = recognition.getBestResultText();
                if (!message.isEmpty()) {
                }
            }

            @Override
            public void onError(Recognizer recognizer, ru.yandex.speechkit.Error error) {
//                chat.addMessage("ERROR");
            }
        }, true);

        recognizer.setVADEnabled(false);
        recognizer.start();
        chat.addMessage("...");
    }

    public void stop() {
        recognizer.finishRecording();
     //   recognizer.cancel();
    }
}
