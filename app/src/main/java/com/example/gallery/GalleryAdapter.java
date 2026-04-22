package com.example.gallery;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;

public class GalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private ArrayList<SectionModel> sectionList;

    public GalleryAdapter(Context context, ArrayList<SectionModel> sectionList) {
        this.context = context;
        this.sectionList = sectionList;
    }

    @Override
    public int getItemViewType(int position) { return sectionList.get(position).type; }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == SectionModel.TYPE_HEADER) {
            View v = LayoutInflater.from(context).inflate(R.layout.header_item, parent, false);
            return new HeaderViewHolder(v);
        } else {
            View v = LayoutInflater.from(context).inflate(R.layout.image_item, parent, false);
            return new ItemViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SectionModel section = sectionList.get(position);
        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).title.setText(section.albumName);
        } else {
            ItemViewHolder itemHolder = (ItemViewHolder) holder;
            Glide.with(context).load(section.image.getPath()).centerCrop().into(itemHolder.img);

            itemHolder.itemView.setOnClickListener(v -> {
                ArrayList<String> albumPhotos = new ArrayList<>();
                String currentAlbum = section.image.getAlbumName();
                int idx = 0, targetIdx = 0;

                for (SectionModel sm : sectionList) {
                    if (sm.type == SectionModel.TYPE_ITEM && sm.image.getAlbumName().equals(currentAlbum)) {
                        albumPhotos.add(sm.image.getPath());
                        if (sm.image.getPath().equals(section.image.getPath())) targetIdx = idx;
                        idx++;
                    }
                }

                Intent intent = new Intent(context, FullScreenActivity.class);
                intent.putStringArrayListExtra("paths", albumPhotos);
                intent.putExtra("position", targetIdx);
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() { return sectionList.size(); }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        HeaderViewHolder(View v) { super(v); title = v.findViewById(R.id.headerTitle); }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        ItemViewHolder(View v) { super(v); img = v.findViewById(R.id.imageView); }
    }
}