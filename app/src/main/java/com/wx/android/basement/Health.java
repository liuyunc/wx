package com.wx.android.basement;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by Administrator on 2017/6/29.
 */
@Table(database = AppDatabase.class)
public class Health extends BaseModel {
    @PrimaryKey // at least one primary key required
    public int hid;

    @Column
    public int hr;  //心率


}
