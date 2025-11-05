package com.example.mylocalmusic.ui.library;

import android.Manifest;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.mylocalmusic.MainActivity;
import com.example.mylocalmusic.R;
import com.example.mylocalmusic.data.MusicPlayerManager;
import com.example.mylocalmusic.databinding.FragmentLibraryBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class LibraryFragment extends Fragment {

    private FragmentLibraryBinding binding;
    private SongsAdapter adapter;

    private final ActivityResultLauncher<String[]> permissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                boolean granted = false;
                for (Boolean v : result.values()) { if (Boolean.TRUE.equals(v)) { granted = true; break; } }
                if (granted) {
                    loadSongs();
                } else {
                    showNeedPermission();
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLibraryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        adapter = new SongsAdapter(new ArrayList<>(), song -> {
            // ExoPlayer 재생
            MusicPlayerManager playerManager = MusicPlayerManager.getInstance(requireContext());
            playerManager.play(requireContext(), song.contentUri, song.title);

            // 미니플레이어 갱신
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).updateMiniPlayer();
                ((MainActivity) getActivity()).findViewById(R.id.mini_player).callOnClick();
            }
        });

        binding.rvSongs.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvSongs.addItemDecoration(
                new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        binding.rvSongs.setAdapter(adapter);

        // 권한 체크 후 로딩
        ensurePermissionThenLoad();
    }

    private void ensurePermissionThenLoad() {
        List<String> needs = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= 33) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_AUDIO)
                    != PackageManager.PERMISSION_GRANTED) {
                needs.add(Manifest.permission.READ_MEDIA_AUDIO);
            }
        } else {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                needs.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }

        if (needs.isEmpty()) {
            loadSongs();
        } else {
            permissionLauncher.launch(needs.toArray(new String[0]));
        }
    }

    private void showNeedPermission() {
        Snackbar.make(binding.getRoot(), "로컬 음악을 보려면 권한이 필요합니다.", Snackbar.LENGTH_LONG)
                .setAction("설정 열기", v -> {
                    // 간단 버전: 사용자가 직접 앱 설정으로 이동하도록 안내만
                }).show();
    }

    private void loadSongs() {
        new Thread(() -> {
            List<SongItem> list = queryMediaStore();
            requireActivity().runOnUiThread(() -> adapter.replaceAll(list));
        }).start();
    }

    private List<SongItem> queryMediaStore() {
        List<SongItem> list = new ArrayList<>();

        Uri collection = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                ? MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
                : MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        String[] projection = new String[] {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.IS_MUSIC
        };

        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
        String sortOrder = MediaStore.Audio.Media.DATE_ADDED + " DESC";

        try (Cursor cursor = requireContext().getContentResolver().query(
                collection, projection, selection, null, sortOrder)) {

            if (cursor == null) return list;

            int idCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID);
            int titleCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE);
            int artistCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST);
            int albumCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM);
            int durationCol = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);

            while (cursor.moveToNext()) {
                long id = cursor.getLong(idCol);
                String title = cursor.getString(titleCol);
                String artist = cursor.getString(artistCol);
                String album = cursor.getString(albumCol);
                long duration = cursor.getLong(durationCol);

                Uri contentUri = ContentUris.withAppendedId(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);

                list.add(new SongItem(title, artist, album, duration, contentUri));
            }
        }
        return list;
    }
}
