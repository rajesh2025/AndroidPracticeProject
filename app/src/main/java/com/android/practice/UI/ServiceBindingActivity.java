package com.android.practice.UI;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.practice.R;
import com.android.practice.Services.ExampleBindService;

import java.util.ArrayList;
import java.util.List;

public class ServiceBindingActivity extends AppCompatActivity implements View.OnClickListener,
        AdapterView.OnItemClickListener, ServiceConnection {
    private List<String> wordList;
    private ExampleBindService service;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_binding);
        wordList = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, wordList);
        ListView lv_word_list = findViewById(R.id.lv_word_list);
        lv_word_list.setAdapter(adapter);
        findViewById(R.id.btn_update_list).setOnClickListener(this);
        findViewById(R.id.btn_start_service).setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindService(new Intent(this, ExampleBindService.class), this, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_update_list:
                if (service != null) {
                    wordList.clear();
                    wordList.addAll(service.getStringList());
                    adapter.notifyDataSetChanged();
                }
            case R.id.btn_start_service:
                Intent newIntent = new Intent(this, ExampleBindService.class);
                startService(newIntent);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        ExampleBindService.MyBinder binder = (ExampleBindService.MyBinder) service;
        this.service = binder.getService();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        service = null;

    }
}
