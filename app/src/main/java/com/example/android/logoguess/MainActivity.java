package com.example.android.logoguess;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.logoguess.data.LogoContract;
import com.example.android.logoguess.data.LogoContract.Logos;
import com.example.android.logoguess.data.LogoReaderDbHelper;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LogoReaderDbHelper dbHelper = new LogoReaderDbHelper(this);
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String cmd = "SELECT " + Logos._ID + " AS _id" + ", " + Logos.COLUMN_LEVEL + ", " +
                " COUNT(*) AS " + Logos.COLUMN_NUM_TOT + ", " +
                " COUNT(" + Logos.COLUMN_COMPLETED +") AS " + Logos.COLUMN_NUM_COMPL +
                " FROM " + Logos.TABLE_LOGOS + " GROUP BY " + Logos.COLUMN_LEVEL;
        Cursor cursor = database.rawQuery(cmd, null);
        LevelAdapter adapter = new LevelAdapter(this, cursor);
        ListView levelsView = (ListView) findViewById(R.id.levels_view);
        levelsView.setAdapter(adapter);
        levelsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int level =  Integer.parseInt(view.getTag().toString());
                Intent gameIntent = new Intent(MainActivity.this, GameActivity.class);
                gameIntent.putExtra("CURRENT_LEVEL", level);
                startActivity(gameIntent);
            }
        });
    }

    @Override
    protected void onResume() {
        LogoReaderDbHelper dbHelper = new LogoReaderDbHelper(this);
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String cmd = "SELECT " + Logos._ID + " AS _id" + ", " + Logos.COLUMN_LEVEL + ", " +
                " COUNT(*) AS " + Logos.COLUMN_NUM_TOT + ", " +
                " COUNT(" + Logos.COLUMN_COMPLETED +") AS " + Logos.COLUMN_NUM_COMPL +
                " FROM " + Logos.TABLE_LOGOS + " GROUP BY " + Logos.COLUMN_LEVEL;
        Cursor cursor = database.rawQuery(cmd, null);
        LevelAdapter adapter = new LevelAdapter(this, cursor);
        ListView levelsView = (ListView) findViewById(R.id.levels_view);
        levelsView.setAdapter(adapter);
        levelsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int level =  Integer.parseInt(view.getTag().toString());
                Intent gameIntent = new Intent(MainActivity.this, GameActivity.class);
                gameIntent.putExtra("CURRENT_LEVEL", level);
                startActivity(gameIntent);
            }
        });
        super.onResume();
    }

}
