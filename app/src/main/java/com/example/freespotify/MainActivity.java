package com.example.freespotify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private Button gLogIn, gSignUp;
    private TextView gResetPwd;
    private EditText gEmailUser,gPwd;
    private FirebaseAuth gAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gLogIn = findViewById(R.id.loginButton);

        gSignUp = findViewById(R.id.signUpButton);

        gResetPwd = findViewById(R.id.resetPwd);

        gEmailUser = findViewById(R.id.eUser);

        gPwd = findViewById(R.id.ePwd);

        gAuth = FirebaseAuth.getInstance();


        gLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gLogIn.setBackgroundColor(Color.parseColor("#F70000"));
                String email = gEmailUser.getText().toString();
                String password = gPwd.getText().toString();


               if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {

                   gAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
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

        gSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gSignUp.setBackgroundColor(Color.parseColor("#F70000"));
                Intent intent = new Intent(MainActivity.this,SignUpActivity.class);
                startActivityForResult(intent,1);

            }
        });

        gResetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gResetPwd.setTextColor(Color.parseColor("#F70000"));

                String emailAddress = gEmailUser.getText().toString();

                if(!TextUtils.isEmpty(emailAddress)) {

                    gAuth.sendPasswordResetEmail(emailAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
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
        FirebaseUser currentUser = gAuth.getCurrentUser();
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

                gAuth.signInWithEmailAndPassword(data.getStringExtra("email"),data.getStringExtra("password"))
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
