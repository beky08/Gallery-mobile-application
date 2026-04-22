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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import java.util.ArrayList;
import java.util.Map;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {
    private Context context;
    private ArrayList<String> albumNames;
    private Map<String, ArrayList<ImageModel>> groupedData;

    public AlbumAdapter(Context context, Map<String, ArrayList<ImageModel>> data) {
        this.context = context;
        this.groupedData = data;
        this.albumNames = new ArrayList<>(data.keySet());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.album_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String name = albumNames.get(position);
        ArrayList<ImageModel> photos = groupedData.get(name);

        holder.title.setText(name);
        holder.count.setText(photos.size() + " Photos");

        Glide.with(context)
                .load(photos.get(0).getPath())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(holder.img);

        holder.itemView.setOnClickListener(v -> {
            ArrayList<String> paths = new ArrayList<>();
            for (ImageModel im : photos) paths.add(im.getPath());

            Intent intent = new Intent(context, AlbumPhotosActivity.class);
            intent.putExtra("albumName", name);
            intent.putStringArrayListExtra("photoPaths", paths);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() { return albumNames.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView title, count;
        ViewHolder(View v) {
            super(v);
            img = v.findViewById(R.id.albumThumbnail);
            title = v.findViewById(R.id.albumNameTxt);
            count = v.findViewById(R.id.albumCountTxt);
        }
    }
}