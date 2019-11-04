package com.example.freespotify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SearchRecentSuggestionsProvider;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    Button submit;
    EditText email,userName,pwd;
    FirebaseAuth mAuth;
    //FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        email = findViewById(R.id.eEmail);

        userName = findViewById(R.id.eUser);

        pwd = findViewById(R.id.ePwd);

        submit = findViewById(R.id.submitButton);

        mAuth = FirebaseAuth.getInstance();

        //db = FirebaseFirestore.getInstance();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit.setBackgroundColor(Color.parseColor("#F70000"));
                final String newEmail = email.getText().toString();
                final String newUsername = userName.getText().toString();
                final String newPwd = pwd.getText().toString();
                if (!TextUtils.isEmpty(newEmail) && !TextUtils.isEmpty(newPwd) && !TextUtils.isEmpty(newUsername)) {

                    mAuth.createUserWithEmailAndPassword(newEmail, newPwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();

                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(newUsername).build();

                                user.updateProfile(profileUpdates);

                                /*Map<String, Object> username = new HashMap<>();
                                username.put("uid",user.getUid());
                                username.put("username", newUsername);

                                // Add a new document with a generated ID
                                db.collection("accounts")
                                        .add(username)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Toast.makeText(SignUpActivity.this, "Account Created",
                                                        Toast.LENGTH_LONG).show();
                                            }
                                        });*/

                                Intent returnIntent = new Intent();
                                returnIntent.putExtra("email", user.getEmail());
                                returnIntent.putExtra("password", newPwd);
                                setResult(RESULT_OK, returnIntent);
                                finish();

                            } else {
                                Toast.makeText(SignUpActivity.this, "Sign Up Failed, make sure you password contains at least 6 characters and your email exists",
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
