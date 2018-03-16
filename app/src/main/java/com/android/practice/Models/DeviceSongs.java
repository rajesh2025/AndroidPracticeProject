package com.android.practice.Models;

import android.provider.MediaStore;

/**
 * Created by user on 13-03-2018.
 */

public class DeviceSongs {
    private String _ID;
    private String ARTIST;
    private String TITLE;
    private String DATA;
    private  String DISPLAY_NAME;
    private String DURATION;

    public DeviceSongs(String _ID, String ARTIST, String TITLE,String DATA, String DISPLAY_NAME, String DURATION) {
        this._ID = _ID;
        this.ARTIST = ARTIST;
        this.TITLE = TITLE;
        this.DATA = DATA;
        this.DISPLAY_NAME = DISPLAY_NAME;
        this.DURATION = DURATION;
    }

    public String get_ID() {
        return _ID;
    }

    public void set_ID(String _ID) {
        this._ID = _ID;
    }

    public String getARTIST() {
        return ARTIST;
    }

    public void setARTIST(String ARTIST) {
        this.ARTIST = ARTIST;
    }

    public String getTITLE() {
        return TITLE;
    }

    public void setTITLE(String TITLE) {
        this.TITLE = TITLE;
    }

    public String getDISPLAY_NAME() {
        return DISPLAY_NAME;
    }

    public void setDISPLAY_NAME(String DISPLAY_NAME) {
        this.DISPLAY_NAME = DISPLAY_NAME;
    }

    public String getDURATION() {
        return DURATION;
    }

    public void setDURATION(String DURATION) {
        this.DURATION = DURATION;
    }

    public String getDATA() {
        return DATA;
    }

    public void setDATA(String DATA) {
        this.DATA = DATA;
    }

    @Override
    public int hashCode() {
        return _ID.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof  DeviceSongs){
            DeviceSongs song = (DeviceSongs) obj;
            if(song._ID.equals(this._ID)){
                return true;
            }
        }
        return false;
    }

}
