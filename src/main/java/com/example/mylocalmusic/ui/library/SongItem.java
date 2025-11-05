package com.example.mylocalmusic.ui.library;

import android.net.Uri;

public class SongItem {
    public final String title;
    public final String artist;
    public final String album;
    public final long durationMs;
    public final Uri contentUri;

    public SongItem(String title, String artist, String album, long durationMs, Uri contentUri) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.durationMs = durationMs;
        this.contentUri = contentUri;
    }

    public String subText() {
        // "아티스트 • 앨범 • mm:ss"
        return String.format("%s • %s • %s",
                artist == null ? "Unknown" : artist,
                album == null ? "Unknown" : album,
                formatDuration(durationMs));
    }

    private static String formatDuration(long ms) {
        long totalSec = ms / 1000;
        long min = totalSec / 60;
        long sec = totalSec % 60;
        return String.format("%d:%02d", min, sec);
    }
}
