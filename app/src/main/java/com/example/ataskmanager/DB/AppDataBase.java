package com.example.ataskmanager.DB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.ataskmanager.Task;

@Database(entities = {Task.class},version = 1)
public abstract class AppDataBase extends RoomDatabase {
    public static final String DATABASE_NAME = "Task.db";
    public static final String TASK_TABLE = "task_table";

    private static volatile AppDataBase instance;
    private static final Object LOCK = new Object();

    public abstract Task TaskDAO();

    public static AppDataBase getInstance(Context context){
        if(instance == null){
            synchronized (LOCK){
                if(instance==null){
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDataBase.class,
                    DATABASE_NAME).build();
                }
            }
        }
        return instance;
    }

}
