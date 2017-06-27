package com.wx.android.basement;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.UUID;


/**
 * Created by Administrator on 2017/6/27.
 */
@Table(database = AppDatabase.class)
public class User extends BaseModel {
    @PrimaryKey // at least one primary key required
    public UUID id;

    @Column
    public String name;

    @Column
    public int age;
}
