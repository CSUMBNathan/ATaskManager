package com.example.ataskmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.ataskmanager.DB.AppDataBase;
import com.example.ataskmanager.DB.TaskDAO;

import java.util.List;

public class EditTasks extends AppCompatActivity {

    private TaskDAO mTaskDAO;
    private List<Task> mTaskList;
    private ArrayAdapter<Task> mTaskAdapter;
    private int mUserId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tasks);

        Intent intent = getIntent();
        if (intent != null) {
            mUserId = intent.getIntExtra("USER_ID", -1);
            System.out.println(mUserId);
        }

        mTaskDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .TaskDAO();

        mTaskList = mTaskDAO.getTaskByUserId(mUserId);
        ListView listView = findViewById(R.id.listViewTasks);
        mTaskAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mTaskList);
        listView.setAdapter(mTaskAdapter);

        // Handle item click to edit task
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editTask(mTaskList.get(position));
            }
        });

        // Handle item long click to delete task
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showDeleteConfirmationDialog(mTaskList.get(position));
                return true;
            }
        });
    }

    private void editTask(Task task) {
        // Implement code to open the edit task screen with the selected task details
        // You can use an Intent to navigate to a new activity or fragment.
    }

    private void showDeleteConfirmationDialog(final Task task) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Task");
        builder.setMessage("Are you sure you want to delete this task?");

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteTask(task);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    private void deleteTask(Task task) {
        // Implement code to delete the task from the database
        mTaskDAO.delete(task);
        refreshTaskList();
    }

    private void refreshTaskList() {
        mTaskList.clear();
        mTaskList.addAll(mTaskDAO.getTaskByUserId(mUserId));
        mTaskAdapter.notifyDataSetChanged();
    }
}