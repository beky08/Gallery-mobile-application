package com.example.gallery;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class AlbumPhotosActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<String> photoPaths;
    String albumName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // old recycle view layout use

        albumName = getIntent().getStringExtra("albumName");
        photoPaths = getIntent().getStringArrayListExtra("photoPaths");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(albumName);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3)); // photos in three grid

        PhotoGridAdapter adapter = new PhotoGridAdapter(this, photoPaths);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}