package com.example.gallery;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        checkPermissions();
    }

    // አዲስ ፎቶ በካሜራ ሲነሳ ወይም አፑ ሲከፈት ወዲያው እንዲያድስ (Refresh)
    @Override
    protected void onResume() {
        super.onResume();
        if (checkPermissionStatus()) {
            loadAlbums();
        }
    }

    private boolean checkPermissionStatus() {
        String permission = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) ?
                Manifest.permission.READ_MEDIA_IMAGES : Manifest.permission.READ_EXTERNAL_STORAGE;
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    private void checkPermissions() {
        String permission = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) ?
                Manifest.permission.READ_MEDIA_IMAGES : Manifest.permission.READ_EXTERNAL_STORAGE;

        if (!checkPermissionStatus()) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, 1);
        } else {
            loadAlbums();
        }
    }

    private void loadAlbums() {
        ArrayList<ImageModel> allImages = getAllImagesFromStorage();
        Map<String, ArrayList<ImageModel>> grouped = new LinkedHashMap<>();

        for (ImageModel img : allImages) {
            String album = img.getAlbumName();
            if (album == null || album.isEmpty()) album = "Other";

            if (!grouped.containsKey(album)) {
                grouped.put(album, new ArrayList<>());
            }
            grouped.get(album).add(img);
        }

        AlbumAdapter adapter = new AlbumAdapter(this, grouped);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);
    }

    private ArrayList<ImageModel> getAllImagesFromStorage() {
        ArrayList<ImageModel> tempImages = new ArrayList<>();
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        // DATE_MODIFIED ን በመጠቀም አዳዲስ ፎቶዎች መጀመሪያ እንዲመጡ እናደርጋለን
        String[] proj = {
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME
        };

        Cursor cursor = getContentResolver().query(uri, proj, null, null, MediaStore.Images.Media.DATE_MODIFIED + " DESC");

        if (cursor != null) {
            int dataCol = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

            while (cursor.moveToNext()) {
                String path = cursor.getString(dataCol);

                // --- ትክክለኛውን የአልበም ስም የማግኛ ዘዴ ---
                String albumName = "Unknown";
                try {
                    File file = new File(path);
                    if (file.getParentFile() != null) {
                        // የፎልደሩን ስም በቀጥታ ከፋይሉ መንገድ ላይ ይወስዳል
                        albumName = file.getParentFile().getName();
                    }
                } catch (Exception e) {
                    albumName = "Other";
                }

                tempImages.add(new ImageModel(path, 0, albumName));
            }
            cursor.close();
        }
        return tempImages;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadAlbums();
        }
    }
}