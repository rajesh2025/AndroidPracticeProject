package com.android.practice.UI;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.practice.R;
import com.android.practice.Services.PracticeIntentService;

public class IntentServiceActivity extends AppCompatActivity {

    private TextView tv_download_status;
    private  int READ_STORAGE;
    private  int WRITE_STORAGE;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                String file_path = bundle.getString(PracticeIntentService.FILE_PATH);
                int download_result = bundle.getInt(PracticeIntentService.RESULT);
                if (download_result == RESULT_OK) {
                    Toast.makeText(IntentServiceActivity.this, "Download complete" + file_path, Toast.LENGTH_LONG).show();
                    tv_download_status.setText("Download Done");
                } else {
                    Toast.makeText(IntentServiceActivity.this, "Download fail" + file_path, Toast.LENGTH_SHORT).show();
                    tv_download_status.setText("Download Fail");
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("IntentServiceAc","onCreate");

        setContentView(R.layout.activity_service_example);

        READ_STORAGE = ContextCompat.checkSelfPermission(IntentServiceActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        WRITE_STORAGE = ContextCompat.checkSelfPermission(IntentServiceActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        tv_download_status = findViewById(R.id.tv_download_status);
        findViewById(R.id.btn_download_file).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (READ_STORAGE != PackageManager.PERMISSION_GRANTED &&
                        WRITE_STORAGE != PackageManager.PERMISSION_GRANTED ) {
//                    if (ActivityCompat.shouldShowRequestPermissionRationale(IntentServiceActivity.this,
//                            Manifest.permission_group.STORAGE)) {
//                        ActivityCompat.
//                    }else {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);

//                    }
                }else{
                    startDownload();
                }

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("IntentServiceAc","onStart");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("IntentServiceAc","onResume");
        registerReceiver(receiver, new IntentFilter(PracticeIntentService.NOTIFICATION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode ==123){
            if(grantResults.length>0&& grantResults[0]==PackageManager.PERMISSION_GRANTED){
                startDownload();
            }
        }
    }

    private void startDownload() {

        Intent intent = new Intent(this, PracticeIntentService.class);
        intent.putExtra(PracticeIntentService.URL_PATH, "http://www.vogella.com/index.html");
        intent.putExtra(PracticeIntentService.FILE_NAME, "index.html");
        startService(intent);
        tv_download_status.setText("Service Started");

    }
}
