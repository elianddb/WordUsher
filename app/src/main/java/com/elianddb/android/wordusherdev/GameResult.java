package com.elianddb.android.wordusherdev;

import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameResult extends MediaManager.MediaActivity {
    public static final long SHOW_LETTER_DELAY = 100;
    public static final long NEXT_ACTIVITY_DELAY = 1000;

    public static final int MAX_NUM_OF_SCORES = 3;
    public static final int SCORE_HEADER = 2;
    public static final String[] headers = {
            "NICE TRY...",
            "NICE",
            "YOUR SCORE"
    };

    private int[] grades;
    private int gameStatus;
    private int gameIteration;
    private int gameResult;
    private int levelIndex;

    private Button menuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_result);
        menuButton = findViewById(R.id.button_to_menu);
        grades = getIntent().getIntArrayExtra("grades");
        gameResult = getIntent().getIntExtra("gameResult", PlayGame.FAIL);
        gameIteration = getIntent().getIntExtra("gameIteration", 0);
        levelIndex = getIntent().getIntExtra("levelIndex", PlayGame.CUSTOM_LEVEL);
        gameStatus = getIntent().getIntExtra("gameStatus", PlayGame.STOP);

        if (gameIteration >= MAX_NUM_OF_SCORES - 1) {
            gameStatus = PlayGame.STOP;
        }

        animateText(findViewById(R.id.result_header), getHeader());
        setGrades();

        if (gameStatus == PlayGame.CONTINUE) {
            continueToNextGame();
        } else {
            setNumberOfStars();
            makeMenuButtonAvailable();
        }
    }

    @Override
    public void onBackPressed() {
        MainActivity.showGoBackDialog(this, "Go back?",
                "Are you sure you want to go back to the title screen?");
    }

    public void continueToNextGame() {
        Intent intent = new Intent(this, PlayGame.class);
        intent.putExtra("gameIteration", ++gameIteration);
        intent.putExtra("gameStatus", gameStatus);
        intent.putExtra("levelIndex", levelIndex);
        intent.putExtra("grades", grades);
        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            startActivity(intent);
            overridePendingTransition(R.anim.slide_up_in, R.anim.slide_up_out);
            finish();
        }, NEXT_ACTIVITY_DELAY);
    }

    public void setNumberOfStars() {
        if (gameStatus == PlayGame.CONTINUE || levelIndex == PlayGame.CUSTOM_LEVEL) {
            return;
        }

        StringBuilder stars = new StringBuilder();
        int count = 0;
        for (long grade : grades) {
            if (grade > PlayGame.FAIL) {
                stars.append('★');
                ++count;
            }
        }
        TextView starsText = findViewById(R.id.result_stars);
        starsText.setText(stars);

        FileManager.modifyLineInFile(this, LevelSelect.STARS_GRADES_FILE,
                    levelIndex, String.valueOf(count));
    }

    public void makeMenuButtonAvailable() {
        menuButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });
        menuButton.setVisibility(View.VISIBLE);
    }

    public String getHeader() {
        if (gameStatus == PlayGame.STOP) {
            return headers[SCORE_HEADER];
        }

        return headers[gameResult];
    }


    public void setHeader() {
        TextView header = findViewById(R.id.result_header);
        header.setText(getHeader());
    }

    public void setGrades() {
        final TextView[] gradesTextViews = {
                findViewById(R.id.result_time0),
                findViewById(R.id.result_time1),
                findViewById(R.id.result_time2)
        };
        for (int index = 0; index < gradesTextViews.length; ++index) {
            final int grade = grades[index];
            if (grade <= PlayGame.NO_GRADE) {
                continue;
            } else if (grade == PlayGame.FAIL) {
                gradesTextViews[index].setTextColor(ContextCompat
                        .getColor(this, R.color.result_error));
            }

            gradesTextViews[index].setText(PlayGame.GRADE_STRINGS[grade]);
            Log.d("GameResult.setGradesFast()", "grades = " + grades[index]);
        }
    }

    public void animateText(final TextView textView, final String text) {
        final int[] index = {0};
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Log.d("GameResult.animateText()", "RUNNING" + index[0]);
                textView.setText(text.substring(0, index[0]));
                if (index[0]++ < text.length()) {
                    handler.postDelayed(this, SHOW_LETTER_DELAY);
                }
            }
        };

        handler.post(runnable);
    }
}