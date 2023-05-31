package com.shoker.ahkamquran;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shoker.ahkamquran.dummy.DummyContent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ItemListActivity extends AppCompatActivity {


    private boolean mTwoPane;
    List<String> titlesList;
    public static InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String[] titlesArray = getResources().getStringArray(R.array.titles_array);
        titlesList = Arrays.asList(titlesArray);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        MobileAds.initialize(this,new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getResources().getString(R.string.ad_unit_id));
        interstitialAd.loadAd(new AdRequest.Builder().build());


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());


        if (findViewById(R.id.item_detail_container) != null) {
            mTwoPane = true;
            Bundle arguments = new Bundle();
            arguments.putInt(ItemDetailFragment.ARG_ITEM_ID,0);
            ItemDetailFragment fragment = new ItemDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.item_detail_container, fragment)
                    .commit();
        }

        View recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setAdapter(new TitlesAdapter(this, titlesList, mTwoPane));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayout.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mi_about:
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
}