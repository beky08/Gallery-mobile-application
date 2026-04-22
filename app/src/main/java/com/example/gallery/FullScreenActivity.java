package com.example.gallery;

import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.Intent;
import android.content.IntentSender;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import java.util.ArrayList;

public class FullScreenActivity extends AppCompatActivity {
    ArrayList<String> imagePaths;
    ViewPager2 viewPager;
    ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);

        if (getSupportActionBar() != null) getSupportActionBar().hide();

        viewPager = findViewById(R.id.viewPager);
        imagePaths = getIntent().getStringArrayListExtra("paths");
        int position = getIntent().getIntExtra("position", 0);

        if (imagePaths != null) {
            adapter = new ViewPagerAdapter(imagePaths);
            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(position, false);
        }
    }

    private void showDeleteToggleDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Photo")
                .setMessage("Do you want to delete this photo?")
                .setPositiveButton("Delete", (dialog, which) -> executeDelete())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void executeDelete() {
        int currentPos = viewPager.getCurrentItem();
        String path = imagePaths.get(currentPos);
        Uri uri = getUriFromPath(path);

        if (uri == null) return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            ArrayList<Uri> uriList = new ArrayList<>();
            uriList.add(uri);
            PendingIntent pendingIntent = MediaStore.createDeleteRequest(getContentResolver(), uriList);
            try {
                startIntentSenderForResult(pendingIntent.getIntentSender(), 100, null, 0, 0, 0);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            int rows = getContentResolver().delete(uri, null, null);
            if (rows > 0) updateUIAfterDelete(currentPos);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            updateUIAfterDelete(viewPager.getCurrentItem());
        }
    }

    private void updateUIAfterDelete(int pos) {
        if (pos < imagePaths.size()) {
            imagePaths.remove(pos);
            adapter.notifyItemRemoved(pos);
            Toast.makeText(this, "Photo Deleted", Toast.LENGTH_SHORT).show();
            if (imagePaths.isEmpty()) finish();
        }
    }

    private Uri getUriFromPath(String path) {
        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA + "=? ",
                new String[]{path}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
            cursor.close();
            return ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
        }
        return null;
    }

    private class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolder> {
        private ArrayList<String> paths;
        ViewPagerAdapter(ArrayList<String> paths) { this.paths = paths; }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.full_image_item, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            // DiskCacheStrategy.ALL ፎቶዎችን በፍጥነት ለመጫን ይረዳል
            Glide.with(FullScreenActivity.this)
                    .load(paths.get(position))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.imageView);

            holder.imageView.setOnClickListener(v -> showDeleteToggleDialog());
        }

        @Override
        public int getItemCount() { return paths.size(); }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            ViewHolder(View v) { super(v); imageView = v.findViewById(R.id.fullImageViewItem); }
        }
    }
}