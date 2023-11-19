package com.example.ataskmanager.DB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import com.example.ataskmanager.Task;

@Dao
public interface TaskDAO {

    @Insert
    void insert(Task...tasks);

    @Update
    void update(Task...tasks);

    @Delete
    void delete(Task task);

}
