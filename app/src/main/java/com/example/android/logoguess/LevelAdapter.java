package com.example.android.logoguess;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.android.logoguess.data.LogoContract;

public class LevelAdapter extends CursorAdapter {

    public LevelAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.levels_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView numLevelView = (TextView) view.findViewById(R.id.level_num_view);
        TextView progressView = (TextView) view.findViewById(R.id.progress_num_view);
        LinearLayout levelsItem = (LinearLayout) view.findViewById(R.id.levels_item_view);

        int levelColumnIndex = cursor.getColumnIndex(LogoContract.Logos.COLUMN_LEVEL);
        int totalColumnIndex = cursor.getColumnIndex(LogoContract.Logos.COLUMN_NUM_TOT);
        int complColumnIndex = cursor.getColumnIndex(LogoContract.Logos.COLUMN_NUM_COMPL);

        int currentLevel = cursor.getInt(levelColumnIndex);
        int numTot = cursor.getInt(totalColumnIndex);
        int numCompl = cursor.getInt(complColumnIndex);

        int progressPercentage = (int) ( ( (double) numCompl) / numTot * 100);

        numLevelView.setText("" + currentLevel);
        progressView.setText(progressPercentage + "%");
        levelsItem.setTag(currentLevel);
    }

}
