package com.example.mylocalmusic.ui.player;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mylocalmusic.data.MusicPlayerManager;
import com.example.mylocalmusic.databinding.FragmentPlayerBinding;

public class PlayerFragment extends Fragment {
    private FragmentPlayerBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentPlayerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        MusicPlayerManager pm = MusicPlayerManager.getInstance(requireContext());

        updateUI();

        binding.btnPlayPause.setOnClickListener(v -> {
            if (pm.isPlaying()) pm.pause();
            else pm.play(requireContext(), null, pm.getCurrentTitle());
            updateUI();
        });
    }

    private void updateUI() {
        MusicPlayerManager pm = MusicPlayerManager.getInstance(requireContext());
        binding.tvNowTitle.setText(pm.getCurrentTitle().isEmpty()
                ? "재생 중인 곡 없음" : pm.getCurrentTitle());
        binding.btnPlayPause.setImageResource(
                pm.isPlaying() ? android.R.drawable.ic_media_pause
                        : android.R.drawable.ic_media_play);
    }
}
