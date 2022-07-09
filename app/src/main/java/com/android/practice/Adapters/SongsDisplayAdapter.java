package com.android.practice.Adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.practice.Models.DeviceSongs;
import com.android.practice.R;

import java.util.List;

/**
 * Created by user on 13-03-2018.
 */

public class SongsDisplayAdapter extends RecyclerView.Adapter<SongsDisplayAdapter.SongViewHolder> {
    private Context mContext;
    private List<DeviceSongs> mSongsList;
    private OnSongClickListener mSongClickListener;

    public SongsDisplayAdapter(Context context, List<DeviceSongs> songsList, OnSongClickListener songClickListener) {
        mContext = context;
        mSongsList = songsList;
        mSongClickListener = songClickListener;
    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_songs_list_item, parent, false);
       final SongViewHolder viewHolder = new SongViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSongClickListener.onSongSelected(getItem(viewHolder.getAdapterPosition()).get_ID());
            }
        });


        return viewHolder;
    }

    private DeviceSongs getItem(int position) {
        return mSongsList.get(position);
    }

    @Override
    public void onBindViewHolder(SongViewHolder holder, int position) {
        holder.tv_song_title.setText(getItem(position).getDISPLAY_NAME());
        holder.tv_song_artist.setText(getItem(position).getARTIST());
        holder.tv_song_duration.setText(getItem(position).getDURATION());
    }

    @Override
    public int getItemCount() {
        return mSongsList.size();
    }

    public class SongViewHolder extends RecyclerView.ViewHolder {
        TextView tv_song_title, tv_song_artist, tv_song_duration;

        private SongViewHolder(View itemView) {
            super(itemView);
            tv_song_title = itemView.findViewById(R.id.tv_song_title);
            tv_song_artist = itemView.findViewById(R.id.tv_song_artist);
            tv_song_duration = itemView.findViewById(R.id.tv_song_duration);
        }
    }

    public interface OnSongClickListener {
        void onSongSelected(String track_id);
    }
}
