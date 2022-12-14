package com.elianddb.android.wordusherdev;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class PlayGame extends MediaManager.MediaActivity {
    public static final int STOP = 0;
    public static final int CONTINUE = 1;

    public static final int NO_GRADE = -1;
    public static final int FAIL = 0;
    public static final int PASS = 1;
    public static final String[] GRADE_STRINGS = {
        "FAIL",
        "PASS"
    };

    public static final int CUSTOM_LEVEL = -1;

    public static final int NUM_OF_ROWS = 8;
    public static final int NUM_OF_COLS = 8;

    private boolean isHoldAttempt;
    private int gameStatus;
    private int levelIndex;
    private int gameIteration;
    private int[] grades;

    private TextView debug;
    private ImageButton grab;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);
        gameStatus = getIntent().getIntExtra("gameStatus", STOP);
        gameIteration = getIntent().getIntExtra("gameIteration", 0);
        grades = getIntent().getIntArrayExtra("grades");

        String word = getIntent().getStringExtra("customWord");
        levelIndex = getIntent().getIntExtra("levelIndex", CUSTOM_LEVEL);
        if (word == null) {
            if (gameIteration >= LevelSelect.WORDS_PER_LEVEL - 1) {
                gameStatus = STOP;
            }

            String fileName = LevelSelect.LEVEL_NAMES[levelIndex];
            word = FileManager.getAssetLine(this, fileName, gameIteration);
            Log.d("PlayGame.onCreate()", "Got word from file: " + fileName);
        }

        Display display = new Display(this, NUM_OF_ROWS, NUM_OF_COLS);
        Scene game = new Scene(display, R.drawable.background_socket);
        Player player = new Player(0, 0, R.drawable.robot_calm, R.drawable.robot_hold, game);
        new WordBuilder(word, game);

        debug = findViewById(R.id.debug_text);
        ImageButton up = findViewById(R.id.dpad_up);
        up.setOnTouchListener(new DPadTranslation(player, Player.Direction.UP));

        ImageButton down = findViewById(R.id.dpad_down);
        down.setOnTouchListener(new DPadTranslation(player, Player.Direction.DOWN));

        ImageButton right = findViewById(R.id.dpad_right);
        right.setOnTouchListener(new DPadTranslation(player, Player.Direction.RIGHT));

        ImageButton left = findViewById(R.id.dpad_left);
        left.setOnTouchListener(new DPadTranslation(player, Player.Direction.LEFT));

        grab = findViewById(R.id.button_attach);
        grab.setOnClickListener(view -> {
            if (player.isHolding()) {
                MainActivity.setForegroundTint(grab, R.color.transparent);
                player.letGo();
                return;
            }

            MainActivity.setForegroundTint(grab, R.color.button_press_red);
            debug.setText(getViewName(grab));
            isHoldAttempt = true;
        });

        Button skip = findViewById(R.id.button_skip);
        skip.setOnClickListener(view -> goToResults(FAIL, gameStatus));
    }

    @Override
    public void onBackPressed() {
        MainActivity.showGoBackDialog(this,
                "Go back?",
                "Are you sure you want to go back to the title screen?");
    }

    public void goToResults(int result, int status) {
        Intent intent = new Intent(PlayGame.this, GameResult.class);
        grades[gameIteration] = result;
        intent.putExtra("gameResult", result);
        intent.putExtra("gameStatus", status);
        intent.putExtra("gameIteration", gameIteration);
        intent.putExtra("grades", grades);
        intent.putExtra("levelIndex", levelIndex);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_up_in, R.anim.slide_up_out);
        finish();
    }

    public static int[] getEmptyGrades() {
        return new int[]{
                PlayGame.NO_GRADE,
                PlayGame.NO_GRADE,
                PlayGame.NO_GRADE
        };
    }

    private String getViewName(View view) {
        String fullName = getResources().getResourceName(view.getId());
        return fullName.substring(fullName.lastIndexOf('/') + 1);
    }

    public class DPadTranslation implements View.OnTouchListener {
        private final Player player;
        private final Player.Direction direction;

        public DPadTranslation(Player player, Player.Direction direction) {
            this.direction = direction;
            this.player = player;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            v.performClick();
            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                MainActivity.setBackgroundTint(v, R.color.button_press_blue);
                if (isHoldAttempt) {
                    player.hold(direction);
                    isHoldAttempt = false;
                    if (!player.isHolding()) {
                        MainActivity.setForegroundTint(grab, R.color.transparent);
                    }
                    return true;
                }

                player.move(direction);
                debug.setText(direction.name());

                if (player.getScene().isWon()) {
                    goToResults(PASS, gameStatus);
                }
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                MainActivity.setBackgroundTint(v, R.color.dpad);
            }

            return true;
        }
    }
}