package com.example.mylocalmusic;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.mylocalmusic.data.MusicPlayerManager;
import com.example.mylocalmusic.databinding.ActivityMainBinding;
import com.example.mylocalmusic.ui.library.LibraryFragment;
import com.example.mylocalmusic.ui.player.PlayerFragment;
import com.example.mylocalmusic.ui.playlists.PlaylistsFragment;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private final Fragment libraryFragment = new LibraryFragment();
    private final Fragment playlistsFragment = new PlaylistsFragment();
    private final Fragment playerFragment = new PlayerFragment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 1. 뷰 바인딩 설정
        binding.miniPlayPause.setOnClickListener(v -> {
            MusicPlayerManager pm = MusicPlayerManager.getInstance(this);
            if (pm.isPlaying()) pm.pause();
            else pm.play(this, null, pm.getCurrentTitle()); // resume
            updateMiniPlayer();
        });
        setContentView(binding.getRoot());

        // 2. 초기 프래그먼트 설정
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, libraryFragment)
                .commit();

        // 3. 하단 네비게이션 리스너 설정
        binding.bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                int itemId = item.getItemId();
                if (itemId == R.id.nav_library) {
                    selectedFragment = libraryFragment;
                } else if (itemId == R.id.nav_playlists) {
                    selectedFragment = playlistsFragment;
                } else if (itemId == R.id.nav_player) {
                    selectedFragment = playerFragment;
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, selectedFragment)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .commit();
                    return true;
                }
                return false;
            }
        });
    }
    public void updateMiniPlayer() {
        MusicPlayerManager pm = MusicPlayerManager.getInstance(this);
        binding.miniTitle.setText(pm.getCurrentTitle().isEmpty()
                ? "재생 중인 곡 없음" : pm.getCurrentTitle());
        binding.miniPlayPause.setImageResource(
                pm.isPlaying() ? android.R.drawable.ic_media_pause
                        : android.R.drawable.ic_media_play);
    }
}
