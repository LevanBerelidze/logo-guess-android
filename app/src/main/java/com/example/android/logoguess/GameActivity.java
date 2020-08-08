package com.example.android.logoguess;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.android.logoguess.data.LogoContract.Logos;
import com.example.android.logoguess.data.LogoReaderDbHelper;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        int currentLevel = getIntent().getIntExtra("CURRENT_LEVEL", 1);
        String cmd = "SELECT * FROM " + Logos.TABLE_LOGOS + " WHERE " + Logos.COLUMN_LEVEL  +
                " = " + currentLevel  + ";";
        SQLiteDatabase db = (new LogoReaderDbHelper(this)).getReadableDatabase();
        Cursor cursor = db.rawQuery(cmd, null);

        LogoAdapter adapter = new LogoAdapter(this, cursor);
        GridView logosView = (GridView) findViewById(R.id.logos_view);
        logosView.setAdapter(adapter);
        logosView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String currentLogo = view.getTag().toString(); // the tag will be the name of the file
                Intent guessIntent = new Intent(GameActivity.this, GuessActivity.class);
                guessIntent.putExtra("CURRENT_LOGO", currentLogo);
                startActivity(guessIntent);
            }
        });
    }

    @Override
    protected void onResume() {
        int currentLevel = getIntent().getIntExtra("CURRENT_LEVEL", 1);
        String cmd = "SELECT * FROM " + Logos.TABLE_LOGOS + " WHERE " + Logos.COLUMN_LEVEL  +
                " = " + currentLevel  + ";";
        SQLiteDatabase db = (new LogoReaderDbHelper(this)).getReadableDatabase();
        Cursor cursor = db.rawQuery(cmd, null);

        LogoAdapter adapter = new LogoAdapter(this, cursor);
        GridView logosView = (GridView) findViewById(R.id.logos_view);
        logosView.setAdapter(adapter);
        logosView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String currentLogo = view.getTag().toString(); // the tag will be the name of the file
                Intent guessIntent = new Intent(GameActivity.this, GuessActivity.class);
                guessIntent.putExtra("CURRENT_LOGO", currentLogo);
                startActivity(guessIntent);
            }
        });
        super.onResume();
    }
}
