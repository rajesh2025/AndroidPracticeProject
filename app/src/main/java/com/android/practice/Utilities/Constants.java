package com.android.practice.Utilities;

/**
 * Created by user on 09-03-2018.
 */

public class Constants {

    public interface Action {
        String MAIN_ACTION = "com.android.practice.Utilities.action.main";
        String INIT_ACTION = "com.android.practice.Utilities.action.init";
        String PREV_ACTION = "com.android.practice.Utilities.action.prev";
        String PLAY_ACTION = "com.android.practice.Utilities.action.play";
        String NEXT_ACTION = "com.android.practice.Utilities.action.next";
        String START_FOREGROUND_ACTION = "com.android.practice.Utilities.action.start_foreground";
        String END_FOREGROUND_ACTION = "com.android.practice.Utilities.action.end_foreground";
    }

    public interface NOTIFICATION_ID {
        int FOREGROUND_SERVICE = 101;
    }
}
