package com.example.ataskmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.ataskmanager.DB.AppDataBase;
import com.example.ataskmanager.DB.TaskDAO;

import java.util.List;

public class UniversalTasks extends AppCompatActivity {
    private TaskDAO mTaskDAO;
    private List<SharedTask> mSharedTaskList;
    private ArrayAdapter<SharedTask> mSharedTaskAdapter;
    private int mUserId = -1;

    private  ListView mListViewUni;

    private EditText mUniversalEvent;
    private EditText mUniversalDate;
    private EditText mUniversalDescription;

    private Button mUniversalBackButton;
    private Button mUniversalSubmitButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_universal_tasks);

        getUserIdFromIntent();
        findViewForFields();

        mTaskDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .TaskDAO();

        mSharedTaskList = mTaskDAO.getAllSharedTasks();

        mSharedTaskAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mSharedTaskList);
        mListViewUni.setAdapter(mSharedTaskAdapter);

        //I did not know you could do this, it looks so sleek, I'm weak.
        mUniversalSubmitButton.setOnClickListener(view -> insertSharedTask());

        mUniversalBackButton.setOnClickListener((view -> backToAdminToolsActivity()));

        mListViewUni.setOnItemLongClickListener((parent, view, position, id) -> {
            SharedTask selectedSharedTask = mSharedTaskList.get(position);

            showDeleteSharedTaskConfirmationDialog(selectedSharedTask);

            return true;
        });

        mListViewUni.setOnItemClickListener((parent, view, position, id) -> {
            SharedTask selectedSharedTask = mSharedTaskList.get(position);

            showMarkAsCompletedAlert(selectedSharedTask);
        });

        } //end of on create

    private void findViewForFields() {
        mUniversalBackButton = findViewById(R.id.buttonBackUni);
        mUniversalSubmitButton = findViewById(R.id.buttonSubmitUni);
        mUniversalEvent = findViewById(R.id.editTextEventUni);
        mUniversalDate = findViewById(R.id.editTextDateUni);
        mUniversalDescription = findViewById(R.id.editTextDescriptionUni);
        mListViewUni = findViewById(R.id.listViewUniTasks);

    }

    private void insertSharedTask() {
        String event = mUniversalEvent.getText().toString();
        String date = mUniversalDate.getText().toString();
        String description = mUniversalDescription.getText().toString();

        if (event.isEmpty() || date.isEmpty() || description.isEmpty()) {
            // Show a toast or some feedback indicating that fields cannot be empty
            return;
        }

        SharedTask sharedTask = new SharedTask(event, description, date);

        mTaskDAO.insert(sharedTask);

        mUniversalEvent.getText().clear();
        mUniversalDate.getText().clear();
        mUniversalDescription.getText().clear();

        refreshSharedTaskList();
    }

    private void backToAdminToolsActivity(){
        Intent intent = AdminToolsActivity.intentFactory(getApplicationContext(),mUserId);
        startActivity(intent);
    }

    private void getUserIdFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            mUserId = intent.getIntExtra("USER_ID", -1);
        }
    }

    private void refreshSharedTaskList() {
        mSharedTaskList.clear();
        mSharedTaskList.addAll(mTaskDAO.getAllSharedTasks());
        mSharedTaskAdapter.notifyDataSetChanged();
    }

    public static Intent intentFactory(Context context, int userId) {
        Intent intent = new Intent(context, UniversalTasks.class);
        intent.putExtra("USER_ID", userId);
        return intent;
    }

    private void showDeleteSharedTaskConfirmationDialog(final SharedTask sharedTask) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Shared Task");
        builder.setMessage("Are you sure you want to delete this shared task?");
        builder.setPositiveButton("Delete", (dialog, which) -> {
            deleteSharedTask(sharedTask);
            dialog.dismiss();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void deleteSharedTask(SharedTask sharedTask) {
        // Implement code to delete the shared task from the database
        mTaskDAO.delete(sharedTask);

        // Refresh the shared task list
        refreshSharedTaskList();
    }

    private void showMarkAsCompletedAlert(final SharedTask sharedTask) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Mark as (in)Completed");
        builder.setMessage("Do you want to change the completion of this?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            markSharedTaskAsCompleted(sharedTask);
            dialog.dismiss();
        });
        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void markSharedTaskAsCompleted(SharedTask sharedTask) {
        // Implement code to mark the shared task as completed in the database
        sharedTask.setCompleted(!sharedTask.isCompleted());
        mTaskDAO.update(sharedTask);

        // Refresh the shared task list
        refreshSharedTaskList();
    }

}
/*Use Case 01: Predefined Users

Force quit the application
Login as testuser1
Display the username 'testuser1'
Logout
Login as admin2
Display the username 'admin2'
Display something specific to the admin user.
Something like an admin button or a link to edit items.


This use case passes if all of these conditions are met. It fails otherwise.


Use Case 02: Persistence

Add an item to the database
Force quit the application
Show the item added in step 1 is still in the database
Change an item in the database
Force quit the application
Show the item modifications from step 4 have been saved



Use Case 03: Add a user

User opens the app.
User clicks on the 'Sign Up' button.
App displays the registration screen.
User enters the required information.
Registration requires the user to reenter password, if they do not match, a warning toast is displayed, and the user is not created.
User clicks 'Create Account.'
App validates the input.
User account is created.
User is redirected to the login screen.
Toast message indicates successful account creation.
Use case ends.


Use Case 04: Delete Task

User is logged in and on the main dashboard.
User clicks on the ‘Edit’ button
App switches activity to EditTaskActivity
User long clicks on desired task
Alert pops up asking user for confirmation of deletion
If “no” is selected, the alert is dismissed and the use case ends.
Iser clicks “Yes” and the task is removed from the database.
Alert dismissed.
Use case ends.




Use Case 05: Add Task

User is logged in and on the main dashboard.
User clicks on the 'Add Task' button.
App displays the task creation screen.
User enters task details.
User clicks 'Save.'
App validates and stores the task data.
User is redirected to the updated task list on the main dashboard.
Use case ends.

Use Case 06: Edit Task

User is logged in and on the main dashboard.
User clicks on the ‘Edit’ button
App switches activity to EditTaskActivity
User clicks on desired task
Shows clicked task information via alert.
User fills fields with new information
User clicks 'Save.'
App validates and stores the task data.
User is redirected back toEditTaskActivity
Use case ends.




Use Case 07: View User Task (admin only)

Login as AdminUser
Navigate towards the admin specific button in the landing page.
User is shown list of all users with relevant information
Click on user in list
View all of that users task
Use case ends




Use Case 08:delete user (admin only)

Use Case description
Login as AdminUser
Navigate towards the admin specific button in the landing page.
User is shown list of all users with relevant information
Long click on user admin wished to delete
If admin longclicks on any user who is an admin, an alert appears declaring can’t delete admin, and use case ends.’
Alert pops up with confirmation message of deletion.
User clicks ‘no’ and exits alert, use case ends.
User clicks yes and user is deleted from database
Use case ends.




Use Case 09: Add universal tasks (admin only)

Login as AdminUser
Navigate towards the admin specific button in the landing page.
User is shown list of all users with relevant information
Click on button labeled “Add Task”
User is redirected to to new activity
User is greeted with a list of all universal tasks.
User enters fields to make new task
If any fields are empty toast warning occurs
New task is added to the database, and is visible to all users on the landing page.
Use case ends.




Use Case 10: Delete Universal task (admin only)
Login as AdminUser
Navigate towards the admin specific button in the landing page.
User is shown list of all users with relevant information
Click on button labeled “Add Task”
User is redirected to to new activity
User is greeted with a list of all universal tasks.
Long Click on task from list
Alert Pops up asking user for confirmation of deletion
If user clicks no, alert disappears and use case ends with no changes
If the user clicks yes, then the task is removed from the database, and as a result removed from the task list in the landing page.

Use Case 11 Mark Universal Task As Complete
Login as AdminUser
Navigate towards the admin specific button in the landing page.
User is shown list of all users with relevant information
Click on button labeled “Add Task”
User is redirected to to new activity
User is greeted with a list of all universal tasks.
User clicks on task
Alert message appears asking for confirmation of status change
    If user clicks no alert stops and use case ends
If user clicks yes, the boolean is set to its opposite (true to false, false to true)
Completed Tasks are ordered at the button of the list found in the landing page
Use case ends.

Use Case 12 Deleter Own account (non-admin only)

User logs into account and is taken to the landing page.
User long clicks on user name
An alert pops up asking user for confirmation of deletions of their account
    If the user picks no, the alert goes away and use case ends
If the user clicks yes, the user is deleted from the database, and logged out, redirected to the login page.
*/