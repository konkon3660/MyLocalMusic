package com.example.mylocalmusic.data;

import android.content.Context;
import android.net.Uri;

import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;

public class MusicPlayerManager {
    private static MusicPlayerManager instance;
    private final ExoPlayer player;
    private Uri currentUri;
    private String currentTitle = "";

    private MusicPlayerManager(Context context) {
        player = new ExoPlayer.Builder(context).build();
    }

    public static synchronized MusicPlayerManager getInstance(Context context) {
        if (instance == null) instance = new MusicPlayerManager(context.getApplicationContext());
        return instance;
    }

    public void play(Context context, Uri uri, String title) {
        if (currentUri == null || !currentUri.equals(uri)) {
            player.setMediaItem(MediaItem.fromUri(uri));
            player.prepare();
            currentUri = uri;
            currentTitle = title;
        }
        player.play();
    }

    public void pause() { player.pause(); }

    public boolean isPlaying() { return player.isPlaying(); }

    public String getCurrentTitle() { return currentTitle; }

    public ExoPlayer getPlayer() { return player; }

    public void release() { player.release(); }
}
