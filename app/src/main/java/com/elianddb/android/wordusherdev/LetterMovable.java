package com.elianddb.android.wordusherdev;

import android.util.Log;

public class LetterMovable extends MaterialMovable {
    private static final int MIN_CODE = 65;
    private static final int MAX_CODE = 90;
    private static final int[] LETTERS = {
            R.drawable.letter_a,
            R.drawable.letter_b,
            R.drawable.letter_c,
            R.drawable.letter_d,
            R.drawable.letter_e,
            R.drawable.letter_f,
            R.drawable.letter_g,
            R.drawable.letter_h,
            R.drawable.letter_i,
            R.drawable.letter_j,
            R.drawable.letter_k,
            R.drawable.letter_l,
            R.drawable.letter_m,
            R.drawable.letter_n,
            R.drawable.letter_o,
            R.drawable.letter_p,
            R.drawable.letter_q,
            R.drawable.letter_r,
            R.drawable.letter_s,
            R.drawable.letter_t,
            R.drawable.letter_u,
            R.drawable.letter_v,
            R.drawable.letter_w,
            R.drawable.letter_x,
            R.drawable.letter_y,
            R.drawable.letter_z
    };

    private final WordBuilder word;

    public LetterMovable(int x, int y, char letter, WordBuilder word, Scene scene) {
        super(x, y, toLetterImgRsc(letter), scene);
        setType(Type.LETTER_MOVABLE);
        this.word = word;
    }

    @Override
    public boolean move(Direction direction) {
        if (word == null) {
            return super.move(direction);
        }

        boolean moved = super.move(direction);
        getScene().setWin(word.isComplete());
        Log.d("LetterMovable.move()","Game is won: " + getScene().isWon());
        return moved;
    }

    public static int toLetterImgRsc(char ch) {
        ch = Character.toUpperCase(ch);
        if (ch < MIN_CODE || ch > MAX_CODE) {
            throw new IllegalArgumentException("Outside of character range.");
        }

        return LETTERS[ch - MIN_CODE];
    }
}
