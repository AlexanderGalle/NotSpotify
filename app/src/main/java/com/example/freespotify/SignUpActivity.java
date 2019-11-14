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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignUpActivity extends AppCompatActivity {

    private Button gSubmit;
    private EditText gEmail, gUserName, gPwd;
    private FirebaseAuth gAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        gEmail = findViewById(R.id.eEmail);

        gUserName = findViewById(R.id.eUser);

        gPwd = findViewById(R.id.ePwd);

        gSubmit = findViewById(R.id.submitButton);

        gAuth = FirebaseAuth.getInstance();

        gSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gSubmit.setBackgroundColor(Color.parseColor("#F70000"));
                final String newEmail = gEmail.getText().toString();
                final String newUsername = gUserName.getText().toString();
                final String newPwd = gPwd.getText().toString();
                if (!TextUtils.isEmpty(newEmail) && !TextUtils.isEmpty(newPwd) && !TextUtils.isEmpty(newUsername)) {

                    gAuth.createUserWithEmailAndPassword(newEmail, newPwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = gAuth.getCurrentUser();

                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(newUsername).build();

                                user.updateProfile(profileUpdates);

                                Intent returnIntent = new Intent();
                                returnIntent.putExtra("email", user.getEmail());
                                returnIntent.putExtra("password", newPwd);
                                setResult(RESULT_OK, returnIntent);
                                finish();

                            } else {
                                Toast.makeText(SignUpActivity.this, "Sign Up Failed, make sure you password contains at least 6 characters and your gEmail exists",
                                        Toast.LENGTH_LONG).show();
                            }

                        }
                    });

                }
                else {
                    Toast.makeText(SignUpActivity.this, "Fill in all fields",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
