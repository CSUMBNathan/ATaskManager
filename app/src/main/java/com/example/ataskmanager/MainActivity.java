package com.example.ataskmanager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.ataskmanager.DB.AppDataBase;
import com.example.ataskmanager.DB.TaskDAO;
import com.example.ataskmanager.databinding.ActivityMainBinding;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final String USER_ID_KEY = "com.example.ataskmanager.userIdKey";
    private static final String PREFENCES_KEY = "com.example.ataskmanager.PREFENCES_KEY";
    ActivityMainBinding binding;

    TextView mTaskDetails;
    EditText mEvent;
    EditText mDate;
    EditText mDescription;

    Button mSubmit;
    Button mLogout;
    Button mEditTask;
    Button mAdminTools;

    TaskDAO mTaskDAO;

    List<Task> mTaskList;

    private int mUserId = -1;

    private SharedPreferences mPreferences = null;
    private User mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getDataBase();

        checkForUser();

        loginUser(mUserId);

        bindElements();

        checkAdminStatus();

        refreshDisplay();


        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitTask();
                refreshDisplay();
            }
        });

        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        mEditTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchEditTasksActivity();
            }
        });
    } //end of onCreate

    private void bindElements() {
        mTaskDetails = binding.textViewTaskDetails;

        mEvent = binding.editTextEvent;
        mDate = binding.editTextDate;
        mDescription = binding.editTextDescription;

        mSubmit = binding.buttonSubmit;
        mLogout = binding.buttonLogoutMain;
        mEditTask = binding.buttonEditTasks;
        mAdminTools = binding.buttonAdminTools;

        mTaskDetails.setMovementMethod(new ScrollingMovementMethod());
    }

    private void loginUser(int userId) {
        mUser = mTaskDAO.getUserByUserId(userId);
        addUserToPreference(userId);
        //invalidateOptionsMenu();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(mUser != null){
            MenuItem item = menu.findItem(R.id.logout_click);
            item.setTitle(mUser.getUserName());
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private void clearUserFromIntent() {
        getIntent().putExtra(USER_ID_KEY,-1);
    }

    private void clearUserFromPref() {
        addUserToPreference(-1);
    }

    private void addUserToPreference(int userId){
        if(mPreferences == null){
            getPrefs();
        }
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(USER_ID_KEY, userId);
        editor.apply();
    }

    private void getDataBase() {
        mTaskDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .TaskDAO();
    }

    private void checkForUser() {
        mUserId = getIntent().getIntExtra(USER_ID_KEY,-1);

        if(mUserId != -1){
            return;
        }

        if(mPreferences == null){
            getPrefs();
        }

        mUserId = mPreferences.getInt(USER_ID_KEY, -1);

        if(mUserId != -1){
            return;
        }

        List<User> users = mTaskDAO.getAllUsers();
        if(users.size() <= 0){
            User defaultUser = new User("daclink","daclink", true);
            mTaskDAO.insert(defaultUser);
        }

        Intent intent = LoginActivity.intentFactory(this);
        startActivity(intent);
    }

    private void getPrefs(){
        mPreferences = this.getSharedPreferences(PREFENCES_KEY, Context.MODE_PRIVATE);
    }

    private void logoutUser() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setMessage("Are you sure you want to logout?");
        alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clearUserFromIntent();
                        clearUserFromPref();
                        mUserId = -1;
                        checkForUser();
                    }
                });
        alertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertBuilder.create().show();
    }

    private void submitTask(){
        System.out.println("submit task running");
        String event = mEvent.getText().toString();
        String date = mDate.getText().toString();
        String details = mDescription.getText().toString();

        Task task = new Task(event, date, details, mUserId);

        mTaskDAO.insert(task);

        refreshDisplay();

        mEvent.getText().clear();
        mDate.getText().clear();
        mDescription.getText().clear();
    }

    private void refreshDisplay(){
        System.out.println("refresh display started");
        mTaskList = mTaskDAO.getTaskByUserId(mUserId);
//        mTaskList = mTaskDAO.getTaskById(mUserId);
        if(!mTaskList.isEmpty()){
            StringBuilder sb = new StringBuilder();
            for(Task task:mTaskList){
                sb.append(task.toString());
            }
            mTaskDetails.setText(sb.toString());
         }else{
            mTaskDetails.setText("Make some tasks lazybones.");
        }
    }

    public static Intent intentFactory(Context context, int userId) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(USER_ID_KEY, userId);

        return intent;
    }

    private void launchEditTasksActivity() {
        Intent intent = new Intent(MainActivity.this, EditTaskActivity.class);
        intent.putExtra("USER_ID", mUserId);
        System.out.println(mUserId);
        startActivity(intent);
    }

    private void checkAdminStatus() {
        if (mUser != null && mUser.getIsAdmin()) {
            // User is an admin, show the Admin Tools button
            mAdminTools.setVisibility(View.VISIBLE);

            // Set up click listener for the Admin Tools button
            mAdminTools.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Implement the logic for admin tools here
                    // For example, open a new activity or perform some admin-specific actions
                    Toast.makeText(MainActivity.this, "Admin Tools Clicked", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // User is not an admin, hide the Admin Tools button
            mAdminTools.setVisibility(View.GONE);
        }
    }

}