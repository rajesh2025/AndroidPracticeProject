package com.android.practice.UI;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import androidx.annotation.NonNull;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.android.practice.Adapters.SongsDisplayAdapter;
import com.android.practice.R;
import com.android.practice.Services.ExampleForegroundService;
import com.android.practice.Utilities.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class Mp3SongsActivity extends AppCompatActivity implements  SongsDisplayAdapter.OnSongClickListener  {
    private String songs_fragment_tag = "SongsDisplayFragment";
    private static final String TAG = Mp3SongsActivity.class.getSimpleName();
    private  ExampleForegroundService service;

    @BindView(R.id.ll_player_control)
    LinearLayout layoutBottomSheet;
    BottomSheetBehavior sheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mp3_songs);
        ButterKnife.bind(this);

        SetUpUI();
    }

    private void SetUpUI() {
        attachFragmentUI();
        attachPlayerControl();
    }

    private void attachPlayerControl() {
        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

    }

    private void attachFragmentUI() {
        SongsDisplayFragment songsDisplayFragment = (SongsDisplayFragment) getSupportFragmentManager().findFragmentByTag(songs_fragment_tag);
        if (songsDisplayFragment == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            songsDisplayFragment = SongsDisplayFragment.getInstance();
            transaction.add(R.id.fl_songs_fragment, songsDisplayFragment, songs_fragment_tag);
            transaction.commit();
        }

    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            ExampleForegroundService.ServiceBinder serviceBinder = (ExampleForegroundService.ServiceBinder) binder;
            service  = serviceBinder.getServiceInstance();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
//            service.unbindService(serviceConnection);
        }
    };


    @Override
    protected void onPause() {
        super.onPause();
        Log.i("Mp3activty","onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Mp3activty","onResume");
        Intent serviceIntent = new Intent(this, ExampleForegroundService.class);
        bindService(serviceIntent,serviceConnection, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("Mp3activty","onStop");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("Mp3activty","onDestroy");

    }

    @Override
    public void onSongSelected(String track_id) {
        Log.i("song list clicked", track_id);

            Intent serviceIntent = new Intent(this, ExampleForegroundService.class);

//            if (!ExampleForegroundService.IS_SERVICE_RUNNING) {
                //serviceIntent.setAction(Constants.Action.START_FOREGROUND_ACTION);
//                ExampleForegroundService.IS_SERVICE_RUNNING = true;
//                bindService(serviceIntent,serviceConnection,Context.BIND_AUTO_CREATE);
              //  startService(serviceIntent);
                if (service != null) {
                    service.playSong(track_id,Constants.Action.START_FOREGROUND_ACTION);
                }
//            }



        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

    }


    @OnClick(R.id.ll_player_control)
    public void toggleBottomSheet() {
        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }
}
