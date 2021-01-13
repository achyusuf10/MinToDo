package com.example.minitodo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.minitodo.Adapter.AdapterData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    FloatingActionButton fabTambah;
    ListView Is;
    DBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fabTambah = findViewById(R.id.fab);
        fabTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddActivity.class));
            }
        });
        dbHelper = new DBHelper(this);
        Is = (ListView)findViewById(R.id.list_data);
        Is.setOnItemClickListener(this);

        setupListView();
    }

    private void setupListView() {
        Cursor cursor = dbHelper.getAll();
        AdapterData adapterData = new AdapterData(this,cursor,1);
        adapterData.notifyDataSetChanged();
        Is.setAdapter(adapterData);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        TextView getID = findViewById(R.id.tv_id);
        final long id = Long.parseLong(getID.getText().toString());
        Cursor cur = dbHelper.oneData(id);
        cur.moveToFirst();

        Intent GoInput = new Intent(MainActivity.this, AddActivity.class);
        GoInput.putExtra(DBHelper.ROW_ID,id);
        startActivity(GoInput);
    }
    @Override
    protected void onResume() {
        super.onResume();
        setupListView();
    }
}