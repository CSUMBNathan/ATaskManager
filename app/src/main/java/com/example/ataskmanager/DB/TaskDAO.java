package com.example.ataskmanager.DB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.ataskmanager.Task;

import java.util.List;

@Dao
public interface TaskDAO {

    @Insert
    void insert(Task...tasks);

    @Update
    void update(Task...tasks);

    @Delete
    void delete(Task task);

    @Query("SELECT * FROM " + AppDataBase.TASK_TABLE + " ORDER BY mDate desc")
    List<Task> getTasks();

    @Query("SELECT * FROM " + AppDataBase.TASK_TABLE + " WHERE mTaskId = :taskId")
    List<Task> getLogById(int taskId);



}
