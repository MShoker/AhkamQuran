package com.shoker.ahkamquran;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shoker.ahkamquran.dummy.DummyContent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ItemListActivity extends AppCompatActivity {


    private boolean mTwoPane;
    List<String> titlesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String[] titlesArray = getResources().getStringArray(R.array.titles_array);
        titlesList = Arrays.asList(titlesArray);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());


        if (findViewById(R.id.item_detail_container) != null) {
            mTwoPane = true;
        }

        View recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setAdapter(new TitlesAdapter(this, titlesList, mTwoPane));
    }

}