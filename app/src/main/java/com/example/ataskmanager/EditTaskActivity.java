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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ataskmanager.DB.AppDataBase;
import com.example.ataskmanager.DB.TaskDAO;

import java.util.List;

public class EditTaskActivity extends AppCompatActivity {

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
        }

        mTaskDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .TaskDAO();
        Button mBackButton = findViewById(R.id.backButtonEditTask);
        mTaskList = mTaskDAO.getTaskByUserId(mUserId);
        ListView listView = findViewById(R.id.listViewTasks);
        mTaskAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mTaskList);
        listView.setAdapter(mTaskAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editTask(mTaskList.get(position));
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showDeleteConfirmationDialog(mTaskList.get(position));
                return true;
            }
        });

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = MainActivity.intentFactory(getApplicationContext(),mUserId);
                startActivity(intent);
            }
        });


    }//end of on create

    private void editTask(Task task) {
        // Create a custom dialog for editing task details
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Task");

        // Inflate the layout for the dialog
        View view = getLayoutInflater().inflate(R.layout.activity_edit_task_info, null);
        builder.setView(view);

        final TextView currentTask = view.findViewById(R.id.textViewTaskDetails);
        final EditText editEvent = view.findViewById(R.id.editTextEvent);
        final EditText editDate = view.findViewById(R.id.editTextDate);
        final EditText editDescription = view.findViewById(R.id.editTextDescription);
        Button buttonSave = view.findViewById(R.id.buttonSaveEditTask);

        // Set up UI elements with existing task details
        editEvent.setText(task.getEvent());
        editDate.setText(task.getDate());
        editDescription.setText(task.getDescription());
        currentTask.setText(task.toString());



        final AlertDialog dialog = builder.create();
        dialog.show();

        // Set up click listener for the "Save" button
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the modified values from UI elements
                String modifiedEvent = editEvent.getText().toString();
                String modifiedDate = editDate.getText().toString();
                String modifiedDescription = editDescription.getText().toString();

                // Update the task with modified values
                task.setEvent(modifiedEvent);
                task.setDate(modifiedDate);
                task.setDescription(modifiedDescription);

                // Update the task in the database
                mTaskDAO.update(task);

                // Dismiss the dialog
                dialog.dismiss();

                // Refresh the task list
                refreshTaskList();
            }
        });
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