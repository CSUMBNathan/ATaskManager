package com.example.ataskmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ataskmanager.DB.AppDataBase;
import com.example.ataskmanager.DB.TaskDAO;

import java.util.List;

public class AdminToolsActivity extends AppCompatActivity {

    private TaskDAO mTaskDAO;
    private List<User> mUserList;
    private ArrayAdapter<User> mUserAdapter;

    private Button mBack;

    private int mUserId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_tools);

        mBack = findViewById(R.id.adminToolsBackButton);

        Intent intent = getIntent();
        if (intent != null) {
            mUserId = intent.getIntExtra("USER_ID", -1);
        }

        mTaskDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .TaskDAO();

        mUserList = mTaskDAO.getAllUsers();
        ListView listView = findViewById(R.id.listViewUsers);
        mUserAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mUserList);
        listView.setAdapter(mUserAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User selectedUser = mUserList.get(position);

                System.out.println(mTaskDAO.getTaskById(1));
                System.out.println(selectedUser.getUserId());
                showUserTasks(selectedUser);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showDeleteUserConfirmationDialog(mUserList.get(position));
                return true;
            }
        });

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = MainActivity.intentFactory(getApplicationContext(),mUserId);
                startActivity(intent);

            }
        });
    }//end of on create


    private void showUserTasks(User user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Tasks for " + user.getUserName());

        View view = getLayoutInflater().inflate(R.layout.user_tasks_for_admin, null);
        builder.setView(view);

        final TextView userNameTextView = view.findViewById(R.id.textViewUserName);
        final TextView userTaskTextView = view.findViewById(R.id.textViewUserTasks);

        List<Task> selectedUserTask = mTaskDAO.getTaskByUserId(user.getUserId());

        if(!selectedUserTask.isEmpty()){
            StringBuilder sb = new StringBuilder();
            for(Task task:selectedUserTask){
                sb.append(task.toString());
            }
            userTaskTextView.setText(sb.toString());
        }else{
            userTaskTextView.setText("Wow such empty. :'(");
        }

        builder.show();

    }

    private void showDeleteUserConfirmationDialog(final User user) {
        if (user.getIsAdmin()) {
            // Display a message that the admin user cannot be deleted
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Cannot Delete Admin");
            builder.setMessage("The admin user cannot be deleted.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        } else {
            // Allow deletion for non-admin users
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Delete User");
            builder.setMessage("Are you sure you want to delete this user?");
            builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deleteUser(user);
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
    }

    private void deleteUser(User user) {
        // Implement code to delete the user from the database
        mTaskDAO.delete(user);

        // Refresh the user list
        refreshUserList();
    }

    private void refreshUserList() {
        mUserList.clear();
        mUserList.addAll(mTaskDAO.getAllUsers());
        mUserAdapter.notifyDataSetChanged();
    }

    public static Intent intentFactory(Context context, int userId) {
        Intent intent = new Intent(context, AdminToolsActivity.class);
        intent.putExtra("USER_ID", userId);
        return intent;
    }


}
