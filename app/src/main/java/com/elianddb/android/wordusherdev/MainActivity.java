package com.elianddb.android.wordusherdev;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.core.content.ContextCompat;

public class MainActivity extends MediaManager.MediaActivity {

    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button startButton = findViewById(R.id.main_button_start);
        Button multiplayerButton = findViewById(R.id.main_button_multiplayer);

        startButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, LevelSelect.class);
            startActivity(intent);
        });

        multiplayerButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, CustomGameMode.class);
            startActivity(intent);
        });

        MediaManager.play(this, R.raw.robot_city);
    }

    public static void setBackgroundTint(View view, int colorId) {
        view.setBackgroundTintList(ContextCompat
                .getColorStateList(view.getContext().getApplicationContext(), colorId));
    }

    public static void setForegroundTint(View view, int colorId) {
        view.setForegroundTintList(ContextCompat
                .getColorStateList(view.getContext().getApplicationContext(), colorId));
    }

    public static void showGoBackDialog(Context context, String title, String message) {
        // Create a new AlertDialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // Set the message and title of the dialog
        builder.setTitle(title);
        builder.setMessage(message);

        // Set the positive and negative buttons of the dialog
        builder.setPositiveButton("Yes", (dialog, id) -> {
            // Go back to the title screen when the user presses the "Yes" button
            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);
            ((Activity) context).finish();
        });

        builder.setNegativeButton("No", (dialog, id) -> {
            // Close the dialog when the user presses the "No" button
            dialog.dismiss();
        });

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}