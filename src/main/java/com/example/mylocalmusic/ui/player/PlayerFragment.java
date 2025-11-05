package com.example.mylocalmusic.ui.player;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.mylocalmusic.databinding.FragmentPlayerBinding;
import com.google.android.material.snackbar.Snackbar;

public class PlayerFragment extends Fragment {
    private FragmentPlayerBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPlayerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.btnPlayPause.setOnClickListener(v ->
                Snackbar.make(v, "재생/일시정지 (추후 구현)", Snackbar.LENGTH_SHORT).show());
        binding.btnNext.setOnClickListener(v ->
                Snackbar.make(v, "다음 곡 (추후 구현)", Snackbar.LENGTH_SHORT).show());
        binding.btnPrev.setOnClickListener(v ->
                Snackbar.make(v, "이전 곡 (추후 구현)", Snackbar.LENGTH_SHORT).show());
    }
}
