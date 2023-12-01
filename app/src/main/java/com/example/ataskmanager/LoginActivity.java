package com.example.ataskmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ataskmanager.DB.AppDataBase;
import com.example.ataskmanager.DB.TaskDAO;

public class LoginActivity extends AppCompatActivity {

    private EditText mUsernameField;
    private EditText mPasswordField;

    private Button mButton;
    private Button mSignUpButton;

    private TaskDAO mTaskDAO;

    private String mUsername;
    private String mPassword;

    private User mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        wireUpDisplay();

        getDatabase();

    }

    private void wireUpDisplay() {

        findFieldsById();

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getValuesFromDisplay();
                if(checkForUserInDatabase()){
                    if(!validatePassword()){
                        Toast.makeText(LoginActivity.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                    }else{
                        Intent intent = MainActivity.intentFactory(getApplicationContext(),mUser.getUserId());
                        startActivity(intent);
                    }
                }
            }
        });

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignUpDialog();
            }
        });

    }   //end of wired display


    private void showSignUpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sign Up");

        // Inflate the layout for the dialog
        View view = getLayoutInflater().inflate(R.layout.activity_signup, null);
        builder.setView(view);

        final EditText usernameEditText = view.findViewById(R.id.editTextUsernameSignUp);
        final EditText passwordEditText = view.findViewById(R.id.editTextPasswordSignUp);
        final EditText confirmPasswordEditText = view.findViewById(R.id.editTextConfirmPasswordSignUp);

        builder.setPositiveButton("Sign Up", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String confirmPassword = confirmPasswordEditText.getText().toString();

                if (password.equals(confirmPassword)) {
                    signUp(username, password);
                } else {
                    Toast.makeText(LoginActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                }
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

    private void signUp(String username, String password) {
        // Perform sign-up logic here, for example, add the new user to the database
        User newUser = new User(username, password);
        mTaskDAO.insert(newUser);

        Toast.makeText(LoginActivity.this, "Sign Up successful", Toast.LENGTH_SHORT).show();
    }

    private boolean validatePassword(){
        return mUser.getPassword().equals(mPassword);
    }

    private boolean checkForUserInDatabase(){
        mUser = mTaskDAO.getUserByUsername(mUsername);
        if(mUser == null){
            Toast.makeText(this,"no user " + mUsername + " found", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void getValuesFromDisplay(){
        mUsername = mUsernameField.getText().toString();
        mPassword = mPasswordField.getText().toString();

    }

    private void getDatabase() {
        mTaskDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .TaskDAO();
    }

    public static Intent intentFactory(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        return intent;
    }

    private void findFieldsById(){
        mUsernameField = findViewById(R.id.editTextUsernameLoginActivity);
        mPasswordField = findViewById(R.id.editTextLoginPassword);
        mButton = findViewById(R.id.buttonLogin);
        mSignUpButton = findViewById(R.id.buttonSignUp);

    }

}