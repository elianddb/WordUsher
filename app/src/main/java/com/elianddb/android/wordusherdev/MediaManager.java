package com.elianddb.android.wordusherdev;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.View;

public class MediaManager {
    private static MediaPlayer mediaPlayer;

    public static void play(Context context, int rsc) {
        MediaManager.stop();

        mediaPlayer = MediaPlayer.create(context, rsc);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    public static void stop() {
        if (MediaManager.isNonNull()) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public static void pause() {
        if (MediaManager.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public static void resume() {
        if (MediaManager.isNonNull() && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    public static boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

    public static boolean isNonNull() {
        return mediaPlayer != null;
    }

    public static class MediaActivity extends androidx.appcompat.app.AppCompatActivity {
        private static boolean userLeft = false;

        @Override
        protected void onUserLeaveHint() {
            super.onUserLeaveHint();
            MediaManager.pause();
            userLeft = true;
        }

        @Override
        protected void onResume() {
            super.onResume();
            if (userLeft) {
                MediaManager.resume();
            }
        }

        @Override
        public void onWindowFocusChanged(boolean hasFocus) {
            super.onWindowFocusChanged(hasFocus);
            if (hasFocus) {
                View decorView = getWindow().getDecorView();
                int flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE;
                decorView.setSystemUiVisibility(flags);
            }
        }
    }
}
