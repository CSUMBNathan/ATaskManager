package com.example.ataskmanager.DB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.ataskmanager.SharedTask;
import com.example.ataskmanager.Task;
import com.example.ataskmanager.User;

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
    List<Task> getAllTasks();

    @Query("SELECT * FROM " + AppDataBase.TASK_TABLE + " WHERE mTaskId = :taskId")
    List<Task> getTaskById(int taskId);

    @Query("SELECT * FROM " + AppDataBase.TASK_TABLE + " WHERE mUserId = :userId ORDER BY mDate desc")
    List<Task> getTaskByUserId(int userId);

    @Insert
    void insert(User...users);

    @Update
    void update(User...users);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM " + AppDataBase.USER_TABLE)
    List<User> getAllUsers();

    @Query("SELECT * FROM " + AppDataBase.USER_TABLE + " WHERE mUserName = :username")
    User getUserByUsername(String username);

    @Query("SELECT * FROM " + AppDataBase.USER_TABLE + " WHERE mUserId = :userId")
    User getUserByUserId(int userId);

    @Insert
    void insert(SharedTask...sharedTask);

    @Update
    void update(SharedTask...sharedTask);

    @Delete
    void delete(SharedTask...sharedTask);

    @Query("SELECT * FROM " + AppDataBase.UNIVERSAL_TASK_TABLE)
    List<SharedTask> getAllSharedTasks();

}
