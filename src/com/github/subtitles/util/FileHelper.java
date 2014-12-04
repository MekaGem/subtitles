package com.github.subtitles.util;

import android.content.Context;
import android.util.Log;
import com.github.subtitles.view.ChatMessageModel;
import com.github.subtitles.view.DialogModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FileHelper {
    private static final int BUFFER_SIZE = 10000;

    public static List<DialogModel> loadDialogs(Context context) {
        File dir = context.getDir("dialogs", Context.MODE_PRIVATE);
        File[] dialogs = dir.listFiles();

        List<DialogModel> dialogModels = new ArrayList<DialogModel>();
        byte[] buffer = new byte[BUFFER_SIZE];

        for (File file : dialogs) {

            try {
                BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file));

                int length = inputStream.read(buffer);
                String jsonText = new String(buffer, 0, length);
                JSONObject jsonObject = new JSONObject(jsonText);

                Log.i("LOADING DIALOG", jsonText);

                String title = jsonObject.getString("title");
                String date = jsonObject.getString("date");
                String lastMessage = jsonObject.getString("lastMessage");

                DialogModel dialogModel = new DialogModel();
                dialogModel.setTitle(title);
                dialogModel.setDate(date);
                dialogModel.setLastMessage(lastMessage);
                dialogModel.setFilename(file.getName());

                dialogModels.add(dialogModel);
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return dialogModels;
    }

    public static DialogModel loadDialog(Context context, String filename) {
        File dir = context.getDir("dialogs", Context.MODE_PRIVATE);
        File dialogFile = new File(dir, filename);
        byte[] buffer = new byte[BUFFER_SIZE];

        DialogModel dialogModel = new DialogModel();

        try {
            BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(dialogFile));

            int length = inputStream.read(buffer);
            String jsonText = new String(buffer, 0, length);
            JSONObject jsonObject = new JSONObject(jsonText);

            String title = jsonObject.getString("title");
            String date = jsonObject.getString("date");
            String lastMessage = jsonObject.getString("lastMessage");

            dialogModel.setTitle(title);
            dialogModel.setDate(date);
            dialogModel.setLastMessage(lastMessage);
            dialogModel.setFilename(dialogFile.getName());

            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dialogModel;
    }

    public static List<ChatMessageModel> loadChat(Context context, String filename) {
        File dir = context.getDir("dialogs", Context.MODE_PRIVATE);
        File dialogFile = new File(dir, filename);

        byte[] buffer = new byte[BUFFER_SIZE];
        List<ChatMessageModel> messageModels = new ArrayList<ChatMessageModel>();

        try {
            BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(dialogFile));

            int length = inputStream.read(buffer);
            String jsonText = new String(buffer, 0, length);
            JSONObject jsonObject = new JSONObject(jsonText);

            Iterator<String> iterator = (Iterator<String>) jsonObject.keys();
            while (iterator.hasNext()) {
                String key = iterator.next();
                Log.i("KEY", key);
                Log.i("VALUE", jsonObject.get(key).getClass().getSimpleName());
            }

            Log.i("JSON TEXT", jsonText);
            Log.i("JSON OBJECT", jsonObject.toString());
            JSONArray messages = jsonObject.getJSONArray("messages");

            for (int index = 0; index < messages.length(); ++index) {
                JSONObject message = messages.getJSONObject(index);
                ChatMessageModel messageModel = new ChatMessageModel();
                messageModel.setMessage(message.getString("message"));
                messageModel.setUserMessage(message.getBoolean("userMessage"));

                messageModels.add(messageModel);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return messageModels;
    }

    public static void saveDialog(Context context, String filename, DialogModel dialog, List<ChatMessageModel> messages) {
        File dir = context.getDir("dialogs", Context.MODE_PRIVATE);
        File dialogFile = new File(dir, filename);

        try {
            OutputStreamWriter outputStream = new OutputStreamWriter(new FileOutputStream(dialogFile));

            JSONObject jsonObject = new JSONObject();

            jsonObject.put("title", dialog.getTitle());
            jsonObject.put("date", dialog.getDate());

            if (messages.isEmpty()) {
                jsonObject.put("lastMessage", "EMPTY");
            } else {
                boolean userMessage = messages.get(messages.size() - 1).isUserMessage();
                String messageText = messages.get(messages.size() - 1).getMessage();
                if (userMessage) {
                    jsonObject.put("lastMessage", "Я: " + messageText);
                } else {
                    jsonObject.put("lastMessage", messageText);
                }
            }

            JSONArray messagesArray = new JSONArray();
            for (ChatMessageModel message : messages) {
                JSONObject messageJson = new JSONObject();
                messageJson.put("message", message.getMessage());
                messageJson.put("userMessage", message.isUserMessage());
                messagesArray.put(messageJson);
            }
            jsonObject.put("messages", (Object)messagesArray);
            outputStream.write(jsonObject.toString().toCharArray());

            Log.i("JSON", jsonObject.toString());
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
