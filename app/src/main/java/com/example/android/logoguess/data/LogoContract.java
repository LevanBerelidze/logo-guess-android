package com.example.android.logoguess.data;

import android.net.Uri;
import android.provider.BaseColumns;

public final class LogoContract {

    public LogoContract(){}

    public static final String CONTENT_AUTHORITY = "com.example.android.logoguess";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_LOGOS = "logos";

    public static abstract class Logos implements BaseColumns {

        public static final Uri LOGOS_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_LOGOS);
        public static final String TABLE_LOGOS = "logos";
        public static final String COLUMN_FILENAME = "filename";
        public static final String COLUMN_LEVEL = "level";
        public static final String COLUMN_ANSWER = "answer";
        public static final String COLUMN_COMPLETED = "completed";

        // column names for temporary tables
        public static final String COLUMN_NUM_TOT = "num_tot";
        public static final String COLUMN_NUM_COMPL = "num_completed";

    }

}
