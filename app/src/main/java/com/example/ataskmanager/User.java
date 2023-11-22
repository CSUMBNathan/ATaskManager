package com.example.ataskmanager;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.ataskmanager.DB.AppDataBase;

@Entity(tableName = AppDataBase.USER_TABLE)
public class User {
    @PrimaryKey(autoGenerate = true)
    private int mUserId;

    private String mUserName;
    private String mUsername;


}
