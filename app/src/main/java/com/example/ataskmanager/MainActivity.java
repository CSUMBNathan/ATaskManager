package com.example.ataskmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
                submitGymLog();
                refreshDisplay();
            }
        });

    } //end of onCreate

    private void submitGymLog(){
        String event = mEvent.getText().toString();
        String date = mDate.getText().toString();
        String details = mTaskDetails.getText().toString();

        Task task = new Task(event, date, details);

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
        }else{
            mTaskDetails.setText("Make some tasks lazybones.");
        }
    }

}