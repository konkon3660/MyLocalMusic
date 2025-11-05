package com.example.mylocalmusic.ui.library;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mylocalmusic.R;
import java.util.List;

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.VH> {

    public interface OnSongClick {
        void onClick(SongItem item);
    }

    private final List<SongItem> data;
    private final OnSongClick onClick;

    public SongsAdapter(List<SongItem> data, OnSongClick onClick) {
        this.data = data;
        this.onClick = onClick;
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_song, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        SongItem item = data.get(pos);
        h.title.setText(item.title);
        h.sub.setText(item.subText());
        h.itemView.setOnClickListener(v -> onClick.onClick(item));
        h.more.setOnClickListener(v -> { /* 옵션메뉴 예정 */ });
    }

    @Override public int getItemCount() { return data.size(); }

    public void replaceAll(List<SongItem> newData) {
        data.clear();
        data.addAll(newData);
        notifyDataSetChanged();
    }

    static class VH extends RecyclerView.ViewHolder {
        ImageView cover;
        TextView title, sub;
        ImageButton more;
        VH(@NonNull View v) {
            super(v);
            cover = v.findViewById(R.id.img_cover);
            title = v.findViewById(R.id.tv_title);
            sub = v.findViewById(R.id.tv_sub);
            more = v.findViewById(R.id.btn_more);
        }
    }
}
