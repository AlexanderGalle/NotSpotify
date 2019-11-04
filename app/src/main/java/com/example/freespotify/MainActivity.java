package com.example.freespotify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private Button logIn, signUp;
    private TextView resetPwd;
    private EditText emailUser,pwd;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logIn = findViewById(R.id.loginButton);

        signUp = findViewById(R.id.signUpButton);

        resetPwd = findViewById(R.id.resetPwd);

        emailUser = findViewById(R.id.eUser);

        pwd = findViewById(R.id.ePwd);

        mAuth = FirebaseAuth.getInstance();


        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logIn.setBackgroundColor(Color.parseColor("#F70000"));
                String email = emailUser.getText().toString();
                String password = pwd.getText().toString();


               if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {

                   mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {
                           if (task.isSuccessful()) {
                               Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                               startActivity(intent);
                               finish();
                           } else {
                               Toast.makeText(MainActivity.this, "Email or Password is incorrect",
                                       Toast.LENGTH_LONG).show();
                           }

                       }
                   });
               }
               else {
                   Toast.makeText(MainActivity.this, "Fill in all fields", Toast.LENGTH_LONG).show();
               }

            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp.setBackgroundColor(Color.parseColor("#F70000"));
                Intent intent = new Intent(MainActivity.this,SignUpActivity.class);
                startActivityForResult(intent,1);

            }
        });

        resetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPwd.setTextColor(Color.parseColor("#F70000"));

                String emailAddress = emailUser.getText().toString();

                if(!TextUtils.isEmpty(emailAddress)) {

                    mAuth.sendPasswordResetEmail(emailAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(MainActivity.this, "Email Sent", Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(MainActivity.this, "Email Doesn't Exist", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(MainActivity.this, "Fill in email field", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public void onStart(){
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!= null)
        {
            Intent intent = new Intent(MainActivity.this,HomeActivity.class);
            startActivity(intent);
            finish();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {

                mAuth.signInWithEmailAndPassword(data.getStringExtra("email"),data.getStringExtra("password"))
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this, "Email or Password is incorrect",
                                    Toast.LENGTH_LONG).show();
                        }

                    }
                });
            }

            }
    }
}
