package com.example.mylocalmusic.ui.playlists;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.mylocalmusic.databinding.FragmentPlaylistsBinding;
import com.google.android.material.snackbar.Snackbar;

public class PlaylistsFragment extends Fragment {

    private FragmentPlaylistsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.fabAdd.setOnClickListener(v ->
                Snackbar.make(v, "플레이리스트 추가(추후 구현)", Snackbar.LENGTH_SHORT).show());
    }
}
