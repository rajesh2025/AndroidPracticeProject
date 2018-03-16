package com.android.practice.Services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ExampleBindService extends Service {
    private List<String> results = new ArrayList<>();
    private int counter = 1;
    private MyBinder binder = new MyBinder();

    public ExampleBindService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        addStringToList();
        return binder;
    }

    public class MyBinder extends Binder {
        public ExampleBindService getService() {
           return ExampleBindService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        addStringToList();
        return START_STICKY;
    }

    public List<String> getStringList(){
        return results;
    }

    private  void addStringToList(){
        Random random = new Random();

        List<String> inputs = Arrays.asList("Linux","Android","iPhone","Windows7");

        results.add(inputs.get(random.nextInt(3))+" "+counter++);
        if(counter== Integer.MAX_VALUE){
            counter = 0;
        }

    }
}
