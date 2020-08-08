package com.example.android.logoguess.data;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class LogoReaderDbHelper extends SQLiteOpenHelper{

    private static final String LOG_TAG = LogoReaderDbHelper.class.getSimpleName();

    private static final int DATABASE_VERSION = 1;
    private static final String DB_NAME = "LogoReader.db";

    public LogoReaderDbHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String cmd = "CREATE TABLE logos (" + LogoContract.Logos._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "filename TEXT, level INTEGER, answer TEXT, completed INTEGER);\n";
        db.execSQL(cmd);

        cmd = "INSERT INTO logos VALUES(1,'adidas',1,'adidas',NULL),\n" +
                "(2,'american_eagle',1,'american eagle',NULL),\n" +
                "(3,'bbc',1,'bbc',NULL),\n" +
                "(4,'blockbuster',1,'blockbuster',NULL),\n" +
                "(5,'bp',1,'british petroleum',NULL),\n" +
                "(6,'cisco',1,'cisco',NULL),\n" +
                "(7,'comcast',1,'comcast',NULL),\n" +
                "(8,'disney',1,'disney',NULL),\n" +
                "(9,'ebay',1,'ebay',NULL),\n" +
                "(10,'expedia',1,'expedia',NULL),\n" +
                "(11,'firefox',1,'mozilla firefox',NULL),\n" +
                "(12,'garnier',2,'garnier',NULL),\n" +
                "(13,'harley_davidson',2,'harley davidson',NULL),\n" +
                "(14,'myspace',2,'myspace',NULL),\n" +
                "(15,'shell',2,'shell',NULL),\n" +
                "(16,'walmart',2,'walmart',NULL);";
        db.execSQL(cmd);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}



}