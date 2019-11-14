package com.example.freespotify;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsFragment extends Fragment {
    private Button gLogout, gConfirm;
    private EditText gEmail;
    private TextView gResetPwd;
    private FirebaseAuth gAuth;
    private ShareViewModel gViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        if (container == null) {
            return null;
        }
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        gAuth = FirebaseAuth.getInstance();
        gEmail = getView().findViewById(R.id.changeEmail);
        gResetPwd = getView().findViewById(R.id.PwdTitle);

        gEmail.setText(gAuth.getCurrentUser().getEmail());
        gConfirm = getView().findViewById(R.id.confrim);

        gLogout= getView().findViewById(R.id.logOut);

        gViewModel = ViewModelProviders.of(getActivity()).get(ShareViewModel.class);

        gResetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gResetPwd.setTextColor(Color.parseColor("#F70000"));

                String emailAddress = gAuth.getCurrentUser().getEmail();

                gAuth.sendPasswordResetEmail(emailAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getContext(), "Email Sent", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(getContext(), "Email Doesn't Exist", Toast.LENGTH_LONG).show();
                        }
                    }
                });


            }
        });

        gConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(gEmail.getText().toString()) ){
                    gAuth.getCurrentUser().updateEmail(gEmail.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getContext(),"Email Updated",Toast.LENGTH_SHORT).show();
                        }
                    });


                }
                else {
                    Toast.makeText(getContext(),"Make Sure all fields are filled",Toast.LENGTH_SHORT).show();
                }

            }
        });


        gLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReleasePlayer();
                gAuth.signOut();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                Toast.makeText(getActivity(), "You have logged out successfully", Toast.LENGTH_LONG).show();
                getActivity().finish();
            }
        });

    }

    private void ReleasePlayer(){
        if (gViewModel.getCurrentPlayer().getValue() != null)
        {
            gViewModel.getCurrentPlayer().getValue().release();
            gViewModel.setCurrentPlayer(null);
        }
    }

}
