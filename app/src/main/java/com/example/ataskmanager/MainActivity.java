package com.example.ataskmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

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

import com.example.ataskmanager.DB.AppDataBase;
import com.example.ataskmanager.DB.TaskDAO;
import com.example.ataskmanager.databinding.ActivityMainBinding;

import java.util.List;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    TextView mTaskDetails;
    EditText mEvent;
    EditText mDate;
    EditText mDescription;

    Button mSubmit;

    TaskDAO mTaskDAO;

    List<Task> mTaskList;

    private int mUserId = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mTaskDetails = binding.textViewTaskDetails;
        mSubmit = binding.buttonSubmit;
        mEvent = binding.editTextEvent;
        mDate = binding.editTextDate;
        mDescription = binding.editTextDescription;
        //mReturn = binding.

        mTaskDetails.setMovementMethod(new ScrollingMovementMethod());

        mTaskDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .TaskDAO();

        refreshDisplay();

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitTask();
                refreshDisplay();

            }
        });

    } //end of onCreate



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout_click) {
            Toast.makeText(this, "Item 1 selected", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void submitTask(){
        String event = mEvent.getText().toString();
        String date = mDate.getText().toString();
        String details = mDescription.getText().toString();

        Task task = new Task(event, date, details, mUserId);

        mTaskDAO.insert(task);
    }


    private void refreshDisplay(){
        mTaskList = mTaskDAO.getTasks();
        if(!mTaskList.isEmpty()){
            StringBuilder sb = new StringBuilder();
            for(Task task:mTaskList){
                sb.append(task.toString());
            }
            mTaskDetails.setText(sb.toString());
            System.out.println(sb.toString());
        }else{
            mTaskDetails.setText("Make some tasks lazybones.");
        }
    }

}