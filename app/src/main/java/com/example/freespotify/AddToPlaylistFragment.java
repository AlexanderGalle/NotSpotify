package com.example.freespotify;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class AddToPlaylistFragment extends Fragment {

    private ShareViewModel viewModel;
    private ImageButton close;
    private RecyclerView recyclerView;
    private String layout;

    public AddToPlaylistFragment(String Layout)
    {
        layout = Layout;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        if (container == null) {
            return null;
        }
        return inflater.inflate(R.layout.fragment_add_to_playlist, container, false);
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView = getView().findViewById(R.id.recyclerView);
        close = getView().findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ParentPlaylist parent = (ParentPlaylist)getParentFragment();

                PlaylistsFragment playlistsFragment = new PlaylistsFragment();

                parent.getFrameLayout(playlistsFragment);

            }
        });

        initRecyclerView();

    }

    private void initRecyclerView(){
        viewModel = ViewModelProviders.of(getActivity()).get(ShareViewModel.class);
        if(layout == "Add") {
            AddAdapter adapter = new AddAdapter(getContext(), getActivity(), viewModel, this);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
        else if(layout == "Remove") {
            RemoveAdapter adapter = new RemoveAdapter(getContext(),getActivity(),viewModel,this);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }

    }
}
