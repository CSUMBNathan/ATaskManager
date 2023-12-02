package com.example.ataskmanager;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.ataskmanager.DB.AppDataBase;

@Entity(tableName = AppDataBase.USER_TABLE)
public class User {
    @PrimaryKey(autoGenerate = true)
    private int mUserId;

    private String mUserName;
    private String mPassword;

    private boolean mIsAdmin = false;
    //this is a branch test

    public User(String userName, String password, Boolean IsAdmin){
        this.mUserName = userName;
        this.mPassword = password;
        this.mIsAdmin = IsAdmin;
    }


    public User(String userName, String password) {
        this.mUserName = userName;
        this.mPassword = password;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int mUserId) {
        this.mUserId = mUserId;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String Password) {
        this.mPassword = Password;
    }

    public boolean getIsAdmin() {
        return mIsAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.mIsAdmin = isAdmin;
    }
}
