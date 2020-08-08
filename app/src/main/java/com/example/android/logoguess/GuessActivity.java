package com.example.android.logoguess;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.flexbox.*;
import com.example.android.logoguess.data.LogoContract.Logos;
import com.example.android.logoguess.data.LogoReaderDbHelper;
import java.util.ArrayList;
import java.util.Random;

public class GuessActivity extends AppCompatActivity {

    private static final int NUM_POSSIBLE_LETTERS = 18;
    private static final int NUM_ROWS_IN_KEYBOARD = 3;
    private static SQLiteDatabase dbWritable;

    private ImageView logoView;
    private String correctAnswer;
    private ArrayList<TextView> answerBarPieces;
    private ArrayList<View> invisibleKeys;
    private int numUserChars;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess);

        logoView = findViewById(R.id.logo_image_view_guess_activity);

        dbWritable = (new LogoReaderDbHelper(this)).getWritableDatabase();

        Cursor cursor = getCurrentLogoData();
        cursor.moveToNext();
        setLogoImage(cursor);

        correctAnswer = cursor.getString(cursor.getColumnIndex(Logos.COLUMN_ANSWER));
        char[] possibleAnswerChars = getPossibleAnswerChars(correctAnswer);
        numUserChars = 0;
        drawAnswerBar(correctAnswer);

        if(checkCompleted(cursor)) {
            ArrayList<Character> correctChars = new ArrayList<>();
            for(int i = 0; i < correctAnswer.length(); i++) {
                if(Character.isAlphabetic(correctAnswer.charAt(i))) {
                    correctChars.add(correctAnswer.charAt(i));
                }
            }
            for(int i = 0; i < correctChars.size(); i++) {
                answerBarPieces.get(i).setText("" + correctChars.get(i));
            }
            return;
        }

        generateKeyboard(possibleAnswerChars);
        invisibleKeys = new ArrayList<>();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void drawAnswerBar(String correctAnswer) {
        answerBarPieces = new ArrayList<>();
        LinearLayout row = new LinearLayout(this);

        for(int i = 0; i < correctAnswer.length(); i++) {
            TextView answerBarPiece = new TextView(this);
            if(!Character.isAlphabetic(correctAnswer.charAt(i))) {
                ( (FlexboxLayout) findViewById(R.id.answer_bar)).addView(row);
                LinearLayout tempView =  new LinearLayout(this);
                setCustomProperties(answerBarPiece, null, 28, 6, 2);
                tempView.addView(answerBarPiece);
                ( (FlexboxLayout) findViewById(R.id.answer_bar)).addView(tempView);
                row = new LinearLayout(this);
                continue;
            }
            setCustomProperties(answerBarPiece, getResources().getDrawable(R.drawable.rounded_rect),
                    28, 6, 2);
            row.addView(answerBarPiece);
            answerBarPieces.add(answerBarPiece);
            if(i == correctAnswer.length() - 1) {
                ( (FlexboxLayout) findViewById(R.id.answer_bar)).addView(row);
            }
        }
    }

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void generateKeyboard(final char[] possibleAnswerChars) {
        final LinearLayout keyboardView = (LinearLayout) findViewById(R.id.keyboard_view);
        int numColumns = possibleAnswerChars.length / NUM_ROWS_IN_KEYBOARD;
        for(int i = 0; i < NUM_ROWS_IN_KEYBOARD; i++) {
            LinearLayout row = new LinearLayout(this);
            row.setGravity(Gravity.CENTER);
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f));
            for(int j = 0; j < numColumns; j++) {
                final TextView letterView = new TextView(this);
                letterView.setText("" + possibleAnswerChars[i * numColumns  + j]);
                setCustomProperties(letterView, getResources().getDrawable(R.drawable.rounded_rect),
                        42, 10, 6);
                row.addView(letterView);
                letterView.setTag("" + possibleAnswerChars[i * numColumns + j]);
                letterView.setOnClickListener(new View.OnClickListener() {

                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onClick(View v) {
                        answerBarPieces.get(numUserChars).setText(v.getTag().toString());
                        answerBarPieces.get(numUserChars).setTag("" + v.getTag().toString());
                        numUserChars++;
                        if(numUserChars >= answerBarPieces.size()) {
                            if(checkAnswer(answerBarPieces, correctAnswer)) {
                                ContentValues values = new ContentValues();
                                values.put(Logos.COLUMN_COMPLETED, 1);
                                dbWritable.update(Logos.TABLE_LOGOS, values,
                                        Logos._ID + " = " + getCurrentLogoId(), null);
                                keyboardView.setVisibility(View.INVISIBLE);
                                ( (LinearLayout) findViewById(R.id.guess_activity_layout) ).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        finish();
                                    }
                                });
                            } else {
                                shakeView(logoView);
                                for(int i = 0; i < answerBarPieces.size(); i++) {
                                    numUserChars = 0;
                                    answerBarPieces.get(i).setText("");
                                    for(View invisible : invisibleKeys) {
                                        invisible.setVisibility(View.VISIBLE);
                                    }
                                    invisibleKeys.clear();
                                }
                            }
                            return;
                        }
                        v.setVisibility(View.INVISIBLE);
                        invisibleKeys.add(v);
                    }
                });
            }
            keyboardView.addView(row);
        }
    }

    private void shakeView(View logoView) {
        Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
        shake.setRepeatCount(5);
        logoView.startAnimation(shake);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean checkAnswer(ArrayList<TextView> answerBarPieces, String correctAnswer) {
        String userAnswer = "";
        String correctAnswerAlpha = onlyAlpha(correctAnswer);
        for(int i = 0; i < answerBarPieces.size(); i++) {
            userAnswer += answerBarPieces.get(i).getTag().toString();
        }
        return userAnswer.equals(correctAnswerAlpha);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private String onlyAlpha(String correctAnswer) {
        String result = "";
        for (int i = 0; i < correctAnswer.length(); i++)
            if(Character.isAlphabetic(correctAnswer.charAt(i)))
                result += correctAnswer.charAt(i);
        return result;
    }

    /**
     * The method sets custom properties for a TextView object.
     * The sizes are specified in dp.
     * @param letterView
     * @param size
     * @param textSize
     * @param margin
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void setCustomProperties(TextView letterView, Drawable background, int size, int textSize, int margin) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dpToPx(size), dpToPx(size));
        params.setMargins(dpToPx(margin), dpToPx(margin), dpToPx(margin), dpToPx(margin));
        params.gravity = Gravity.CENTER;
        letterView.setAllCaps(true);
        letterView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        if(background != null)
            letterView.setBackgroundDrawable(background);
        letterView.setTextSize(dpToPx(textSize));
        letterView.setLayoutParams(params);
        letterView.setTextColor(Color.BLACK);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private char[] getPossibleAnswerChars(String correctAnswer) {
        char[] result = new char[NUM_POSSIBLE_LETTERS];

        for(int i = 0; i < correctAnswer.length(); i++) {
            if (!Character.isAlphabetic(correctAnswer.charAt(i))) {
                result[i] = getRandomLowerCaseAlphabetic();
                continue;
            }
            result[i] = correctAnswer.charAt(i);
        }

        for(int i = correctAnswer.length(); i < result.length; i++) {
            result[i] = getRandomLowerCaseAlphabetic();
        }

        shuffleArray(result);

        return result;
    }

    private void shuffleArray(char[] result) {
        for(int i = 0; i < result.length; i++) {
            int randIndex = (new Random()).nextInt(result.length);
            char temp = result[randIndex];
            result[randIndex] = result[i];
            result[i] = temp;
        }
    }

    private void setLogoImage(Cursor cursor) {
        int filenameColumnIndex = cursor.getColumnIndex(Logos.COLUMN_FILENAME);
        String filename = cursor.getString(filenameColumnIndex);
        int logoImgId = getResources().getIdentifier(filename,"drawable", getPackageName());
        logoView.setImageResource(logoImgId);
    }

    private boolean checkCompleted(Cursor cursor) {
        int completedColumnIndex = cursor.getColumnIndex(Logos.COLUMN_COMPLETED);
        return cursor.getInt(completedColumnIndex) == 1;
    }

    private Cursor getCurrentLogoData() {
        SQLiteDatabase dbReadable = (new LogoReaderDbHelper(this)).getReadableDatabase();
        String currentLogoFileName = getIntent().getStringExtra("CURRENT_LOGO");

        String cmd = "SELECT " + Logos._ID + ", " + Logos.COLUMN_FILENAME + ", " + Logos.COLUMN_ANSWER + ", " +
                Logos.COLUMN_COMPLETED + " FROM " + Logos.TABLE_LOGOS +
                " WHERE " + Logos.COLUMN_FILENAME + " =  ?";
        return dbReadable.rawQuery(cmd, new String[] {currentLogoFileName});
    }

    private int getCurrentLogoId() {
        Cursor cursor = getCurrentLogoData();
        cursor.moveToNext();
        return cursor.getInt(cursor.getColumnIndex(Logos._ID));
    }

    public char getRandomLowerCaseAlphabetic() {
        return(char) ((new Random()).nextInt((int) 'z' - 'a' + 1) + (int) 'a');
    }
}
