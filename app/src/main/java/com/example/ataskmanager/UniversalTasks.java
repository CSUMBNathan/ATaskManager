package com.example.ataskmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

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

        }

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

        SharedTask sharedTask = new SharedTask(event, date, description);

        mTaskDAO.insert(sharedTask);

        mUniversalEvent.getText().clear();
        mUniversalDate.getText().clear();
        mUniversalDescription.getText().clear();

        refreshSharedTaskList();
    }

    private void getUserInput() {
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
}
