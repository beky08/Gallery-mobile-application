package com.example.gallery;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;

public class PhotoGridAdapter extends RecyclerView.Adapter<PhotoGridAdapter.ViewHolder> {
    private Context context;
    private ArrayList<String> paths;

    public PhotoGridAdapter(Context context, ArrayList<String> paths) {
        this.context = context;
        this.paths = paths;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.image_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String path = paths.get(position);
        Glide.with(context).load(path).centerCrop().into(holder.img);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, FullScreenActivity.class);
            intent.putStringArrayListExtra("paths", paths);
            intent.putExtra("position", position);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() { return paths.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        ViewHolder(View v) { super(v); img = v.findViewById(R.id.imageView); }
    }
}