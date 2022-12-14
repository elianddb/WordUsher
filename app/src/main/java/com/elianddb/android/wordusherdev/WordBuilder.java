package com.elianddb.android.wordusherdev;

import android.util.Log;

import com.elianddb.android.wordusherdev.Entity.Direction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class WordBuilder {
    private static final Random RANDOM = new Random();

    private final Scene scene;
    private final LetterMovable[] letters;
    private final HashMap<Integer, ArrayList<Integer>> imgRscMap;

    WordBuilder(String word, Scene scene) {
        this.scene = scene;
        letters = new LetterMovable[word.length()];
        imgRscMap = new HashMap<>();
        final int indexStart = 0;

        for (int index = indexStart; index < word.length(); ++index) {
            int x, y;
            int count = 0;
            boolean clear;

            // Generate random position for the letter to placed in.
            do {
                // Get a random location.
                x = RANDOM.nextInt(scene.getNumOfCols());
                y = RANDOM.nextInt(scene.getNumOfRows());
                ++count;

                // Read the position within the scene and the objects surrounding on the left
                // and right
                GameObject.Type read = scene.getObject(x, y).getType();
                GameObject.Type left = scene
                        .getObject(Direction.parseX(x, Direction.LEFT),
                                Direction.parseY(y, Direction.LEFT))
                        .getType();
                GameObject.Type right = scene
                        .getObject(Direction.parseX(x, Direction.RIGHT),
                                Direction.parseY(y, Direction.RIGHT))
                        .getType();

                // See if the objects meet the requirements in order to place the letter
                clear = read.isBackground() && !left.isLetter() && !right.isLetter();
            } while (!clear);

            // Place the letter in the scene and add it to the map with its own ArrayList if
            // it is unique
            letters[index] = new LetterMovable(x, y, word.charAt(index), this, scene);
            imgRscMap.putIfAbsent(letters[index].getImgRsc(), new ArrayList<>());
            Log.d("WordBuilder", "Letter " + index + " took " + count + " attempts.");

            // Within the original word grab the letter to its left, and add it as a possible
            // pair by putting it in the array list corresponding letter.
            final int leftIndex = index - 1;
            int leftPair = scene.getBackground().getImgRsc();
            if (leftIndex >= indexStart) {
                leftPair = letters[leftIndex].getImgRsc();
            }

            Log.d("WordBuilder",
                    "Creating pair: " + letters[index].getImgRsc() + " " + leftPair);
            Objects.requireNonNull(imgRscMap.get(letters[index].getImgRsc())).add(leftPair);
        }
    }

    public boolean isComplete() {
        // Create a copy of the HashMap but a shallow copy of the array objects for each letter
        // image resource
        HashMap<Integer, List<Integer>> imgRscMap = new HashMap<>();
        this.imgRscMap.forEach((key, value) -> {
            @SuppressWarnings("unchecked")
            List<Integer> valCopy = (ArrayList<Integer>) value.clone();
            imgRscMap.put(key, valCopy);
        });

        // Go through each letter in the letters array and check if its image resource can
        // pair up with the image resource of the letter to the left
        final int indexStart = 0;
        for (int index = indexStart; index < letters.length; ++index) {
            final int keyImgRsc = letters[index].getImgRsc();
            final GameObject testObject = letters[index].look(Direction.LEFT);
            int testImgRsc = testObject.getImgRsc();
            // If the object were checking for is not a letter replace it with a background object
            // representing no letters
            if (!testObject.getType().isLetter()) {
                testImgRsc = scene.getBackground().getImgRsc();
            }

            // Check if the letter image resource pairs with the image resource above
            final boolean paired = Objects.requireNonNull(imgRscMap.get(keyImgRsc))
                    .contains(testImgRsc);
            Log.d("WordBuilder.isComplete()",
                    keyImgRsc + " " + testImgRsc + " : " + paired);
            if (!paired) {
                Log.d("WordBuilder.isComplete()", "Pair does not exist.");
                return false;
            }

            // Remove the image resource that was paired so that we don't have a false-positive
            // pairing later on
            Objects.requireNonNull(imgRscMap.get(keyImgRsc)).remove((Integer) testImgRsc);
            Log.d("WordBuilder.isComplete()",
                    "Stopped searching at letter index: " + index);
        }

        return true;
    }
}

