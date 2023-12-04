package com.example.ataskmanager;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.ataskmanager.DB.AppDataBase;

@Entity(tableName = AppDataBase.UNIVERSAL_TASK_TABLE)
public class SharedTask {
    @PrimaryKey(autoGenerate = true)
    private int taskId;

    private String mEvent;
    private String mDate;
    private String mDescription;
    private boolean mCompleted = false;

    public SharedTask(String mEvent, String mDescription, String mDate) {
        this.mEvent = mEvent;
        this.mDate = mDate;
        this.mDescription = mDescription;
    }

    @Override
    public String toString() {
        return "Universal Task " + "\n" +
                "Event: " + mEvent + "\n" +
                "Description: " + mDescription + "\n" +
                "Date: " + mDate + "\n" +
                "Completed: " + mCompleted + "\n" +
                "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\n";
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getEvent() {
        return mEvent;
    }

    public void setEvent(String mEvent) {
        this.mEvent = mEvent;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String mDate) {
        this.mDate = mDate;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public boolean isCompleted() {
        return mCompleted;
    }

    public void setCompleted(boolean mCompleted) {
        this.mCompleted = mCompleted;
    }
}
