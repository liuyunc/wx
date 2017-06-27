package com.wx.android.basement;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Created by Administrator on 2017/6/27.
 */
@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
public class AppDatabase {
    public static final String NAME = "AppDatabase"; // we will add the .db extension

    public static final int VERSION = 1;
}
