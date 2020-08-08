package com.example.android.logoguess;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.android.logoguess.data.LogoContract;

public class LogoAdapter extends CursorAdapter {

    public LogoAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.logos_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView logoView = (ImageView) view.findViewById(R.id.logo_image_view);
        ImageView isCorrectView = (ImageView) view.findViewById(R.id.correct_image_view);
        RelativeLayout logosItemView = (RelativeLayout) view.findViewById(R.id.logos_item_view);

        int filenameColumnIndex = cursor.getColumnIndex(LogoContract.Logos.COLUMN_FILENAME);
        int completedColumnIndex = cursor.getColumnIndex(LogoContract.Logos.COLUMN_COMPLETED);

        String filename = cursor.getString(filenameColumnIndex);
        boolean completed = cursor.getInt(completedColumnIndex) == 1;

        int logoImgId = context.getResources().getIdentifier(filename,"drawable", context.getPackageName());
        logoView.setImageResource(logoImgId);
        logosItemView.setTag(filename);

        if(completed)
            isCorrectView.setVisibility(View.VISIBLE);
    }

}
