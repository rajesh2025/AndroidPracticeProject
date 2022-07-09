package com.android.practice.UI;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.practice.Adapters.SongsDisplayAdapter;
import com.android.practice.Models.DeviceSongs;
import com.android.practice.R;

import java.util.ArrayList;
import java.util.List;

public class SongsDisplayFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 204;
    private static final int REQUEST_PERMISSION_SETTING = 203;
    private int songs_loader_id = 345;
    private Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    private String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
    private String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
    private List<DeviceSongs> songs = new ArrayList<>();
    private SongsDisplayAdapter displayAdapter;
    LayoutInflater inflater;
    public static SongsDisplayFragment songsFragmentInstance;
    private String[] projection = {
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DURATION
    };
    private SharedPreferences permissionStatus;

    public static SongsDisplayFragment getInstance() {
        if (songsFragmentInstance == null) {
            songsFragmentInstance =  new SongsDisplayFragment();
        }
        return songsFragmentInstance;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        permissionStatus = context.getSharedPreferences("permissionStatus", Context.MODE_PRIVATE);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadSongsFromDevice();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_songs_display, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpView(view);
    }

    private void setUpView(View view) {
        RecyclerView rv_songs_list = view.findViewById(R.id.rv_songs_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rv_songs_list.setLayoutManager(layoutManager);
        rv_songs_list.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        displayAdapter = new SongsDisplayAdapter(getActivity(), songs, (Mp3SongsActivity)(getActivity()));
        rv_songs_list.setAdapter(displayAdapter);
    }


    @Override
    public void onResume() {
        super.onResume();
        Loader songs_loader = getActivity().getSupportLoaderManager().getLoader(songs_loader_id);
        if (songs_loader != null) {
            getActivity().getSupportLoaderManager().restartLoader(songs_loader_id, null, this);
        }
    }


    private void loadSongsFromDevice() {
        int READ_STORAGE = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE);
        int WRITE_STORAGE = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (READ_STORAGE != PackageManager.PERMISSION_GRANTED &&
                WRITE_STORAGE != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                alertDialogOnRequestType(0);
            } else if (permissionStatus.getBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE, false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                alertDialogOnRequestType(1);
            } else {
                requestAllKindPermissions(new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE}
                        , EXTERNAL_STORAGE_PERMISSION_CONSTANT);

            }

            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE, true);
            editor.apply();
        } else {
            getActivity().getSupportLoaderManager().initLoader(songs_loader_id, null, this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Loader songs_loader = getActivity().getSupportLoaderManager().getLoader(songs_loader_id);
        if (songs_loader != null) {
            getActivity().getSupportLoaderManager().destroyLoader(songs_loader_id);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == EXTERNAL_STORAGE_PERMISSION_CONSTANT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadSongsFromDevice();
            }
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                alertDialogOnRequestType(0);
            } else {
                Toast.makeText(getActivity(), "Unable to get Permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                loadSongsFromDevice();
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), uri, projection, selection, null, sortOrder);

    }

    @Override
    public void onLoadFinished(Loader loader, Cursor cursor) {
        //   songs.clear();
        while (cursor.moveToNext()) {
            DeviceSongs song = new DeviceSongs(cursor.getString(0)
                    , cursor.getString(1)
                    , cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5));
            if (!songs.contains(song)) {
                songs.add(song);
            }
        }
        displayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }




    private void alertDialogOnRequestType(final int requestType) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Need Storage Permission");
        builder.setMessage("This app needs storage permission.");

        builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (requestType == 0) {
                    dialog.cancel();
                    requestAllKindPermissions(new String[]{
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE}
                            , EXTERNAL_STORAGE_PERMISSION_CONSTANT);
                } else if (requestType == 1) {
                    dialog.cancel();
//                    sentToSettings = true;
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                    intent.setData(uri);
                    startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                    Toast.makeText(getActivity(), "Go to Permissions to Grant Storage", Toast.LENGTH_LONG).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    private void requestAllKindPermissions(String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(getActivity(), permissions, requestCode);
    }


}
