package com.elianddb.android.wordusherdev;

import android.widget.ImageView;
import android.widget.LinearLayout;

public class StarsManager {
    private static final int NUM_STARS = 3;
    private final LinearLayout layout;

    StarsManager(LinearLayout linearLayout) {
        layout = linearLayout;
        layout.setOrientation(LinearLayout.HORIZONTAL);

        for (int i = 0; i < NUM_STARS; i++) {
            ImageView imageView = new ImageView(linearLayout.getContext());
            imageView.setImageResource(android.R.drawable.star_big_off);
            layout.addView(imageView);
        }
    }

    public ImageView getStarAt(int index) {
        return (ImageView) layout.getChildAt(index);
    }

    public void setStarOff(int index) {
        getStarAt(index).setImageResource(android.R.drawable.star_big_off);
    }

    public void setStarOn(int index) {
        getStarAt(index).setImageResource(android.R.drawable.star_big_on);
    }

    public void clearStars() {
        for (int index = 0; index < NUM_STARS; ++index) {
            setStarOff(index);
        }
    }

    // Method to turn a specific number of stars yellow
    public void setStarLevel(int starLevel) {
        for (int index = 0; index < starLevel; ++index) {
            setStarOn(index);
        }
    }
}
