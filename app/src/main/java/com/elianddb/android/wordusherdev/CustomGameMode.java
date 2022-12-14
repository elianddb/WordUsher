package com.elianddb.android.wordusherdev;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CustomGameMode extends MediaManager.MediaActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_game_mode);

        Button startGame = findViewById(R.id.custom_button_start);
        EditText input = findViewById(R.id.text_input_custom);
        input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8),
                new InputFilter.AllCaps()});

        Toast notEnoughLetters = Toast.makeText(this,
                R.string.not_enough_letters, Toast.LENGTH_SHORT);
        startGame.setOnClickListener(v -> {
            if (input.getText().length() < 2) {
                notEnoughLetters.show();
                return;
            }

            Intent intent = new Intent(this, PlayGame.class);
            intent.putExtra("customWord", input.getText().toString());
            intent.putExtra("grades", PlayGame.getEmptyGrades());
            startActivity(intent);
            finish();
        });
    }
}