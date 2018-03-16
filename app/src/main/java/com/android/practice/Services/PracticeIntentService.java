package com.android.practice.Services;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
//adb shell dumpsys activity services ExampleBindService
//for knowing about currently running service from terminal
public class PracticeIntentService extends IntentService {
    public static final String URL_PATH = "url_path";
    public static final String FILE_NAME = "file_name";
    public static final String FILE_PATH = "file_path";
    public static final String RESULT = "result";
    public static final String NOTIFICATION = "com.android.practice.service.receiver";

    public PracticeIntentService() {
        super("PracticeIntentService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        String url_path = intent.getStringExtra(URL_PATH);
        String file_name = intent.getStringExtra(FILE_NAME);
        File output = new File(Environment.getExternalStorageDirectory(), file_name);

        if (output.exists()) {
            output.delete();
        }
        InputStream stream = null;
        FileOutputStream fos = null;
        int result = 0;
        try {
            URL url = new URL(url_path);
            stream = url.openConnection().getInputStream();
            InputStreamReader reader = new InputStreamReader(stream);
            fos = new FileOutputStream(output.getPath());
            int next = -1;
            while ((next = reader.read()) != -1) {
                fos.write(next);
            }
             result = Activity.RESULT_OK;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        publishResult(output.getAbsolutePath(),result);
    }

    private void publishResult(String absolutePath, int result) {
        Intent intent = new Intent(NOTIFICATION);
        intent.putExtra(FILE_PATH,absolutePath);
        intent.putExtra(RESULT,result);
        sendBroadcast(intent);
    }


}
