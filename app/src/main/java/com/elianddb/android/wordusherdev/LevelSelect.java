package com.elianddb.android.wordusherdev;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;

public class LevelSelect extends MediaManager.MediaActivity {
    public static final int WORDS_PER_LEVEL = 3;
    public static final String STARS_GRADES_FILE = "stars_grades";
    public static final String[] LEVEL_NAMES = {
            "nature",
            "computers",
            "sci-fi"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_select);
        if (!FileManager.fileExists(this, STARS_GRADES_FILE)) {
            Log.d("LevelSelect.onCreate()", "CREATING NEW FILE");
            FileManager.writeToFile(this, STARS_GRADES_FILE, "0\n0\n0");
        }

        ArrayList<ImageButton> levels = new ArrayList<>();
        levels.add(findViewById(R.id.level_button_0));
        levels.add(findViewById(R.id.level_button_1));
        levels.add(findViewById(R.id.level_button_2));

        ArrayList<StarsManager> stars = new ArrayList<>();
        stars.add(new StarsManager(findViewById(R.id.stars_level_0)));
        stars.get(0).setStarLevel(getStarLevel(0));
        stars.add(new StarsManager(findViewById(R.id.stars_level_1)));
        stars.get(1).setStarLevel(getStarLevel(1));
        stars.add(new StarsManager(findViewById(R.id.stars_level_2)));
        stars.get(2).setStarLevel(getStarLevel(2));

        for (ImageButton imageButton : levels) {
            imageButton.setOnClickListener(
                    new LoadLevelOnClickListener(levels.indexOf(imageButton)));
        }
    }

    public int getStarLevel(int levelIndex) {
        return Integer.parseInt(FileManager.readLineFromFile(this, STARS_GRADES_FILE, levelIndex));
    }

    public class LoadLevelOnClickListener implements View.OnClickListener {
        private final int index;

        public LoadLevelOnClickListener(int index) {
            this.index = index;
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), PlayGame.class);
            Log.d("LevelSelect.LoadLevelOnClickListener.onClick()", "Level Index: " + index);
            intent.putExtra("levelIndex", index);
            intent.putExtra("gameIteration", 0);
            intent.putExtra("gameStatus", PlayGame.CONTINUE);
            intent.putExtra("grades", PlayGame.getEmptyGrades());
            startActivity(intent);
            finish();
        }
    }
}
