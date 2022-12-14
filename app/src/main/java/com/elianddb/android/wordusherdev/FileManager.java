package com.elianddb.android.wordusherdev;

import android.content.Context;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    public static String getAssetLine(Context context, String fileName, int index) {
        if (index < 0 || index >= LevelSelect.WORDS_PER_LEVEL)
            throw new IllegalArgumentException("There are only three words per file.");

        String line = "NO_WORD";

        try {
            InputStream input = context.getAssets().open(fileName);
            InputStreamReader inputReader = new InputStreamReader(input);
            BufferedReader bufferedReader = new BufferedReader(inputReader);
            do {
                line = bufferedReader.readLine();
            } while (index-- > 0);
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return line;
    }

    public static String readLineFromFile(Context context, String fileName, int lineIndex) {
        // Read the file into a list of lines
        List<String> lines = new ArrayList<>(0);
        try {
            FileInputStream inputStream = context.openFileInput(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Return the line at the specified index
        return lines.get(lineIndex);
    }

    public static void writeToFile(Context context, String fileName, String content) {
        FileOutputStream outputStream;
        try {
            File file = new File(context.getFilesDir(), fileName);
            //noinspection ResultOfMethodCallIgnored
            file.createNewFile();
            outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(content.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void modifyLineInFile(Context context, String fileName, int lineIndex, String newLine) {
        // Read the file into a list of lines
        List<String> lines = new ArrayList<>();
        try {
            FileInputStream inputStream = context.openFileInput(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Add new lines to the file if the index is out of bounds
        if (lineIndex >= lines.size()) {
            int numLinesToAdd = lineIndex - lines.size() + 1;
            for (int i = 0; i < numLinesToAdd; i++) {
                lines.add("");
            }
        }

        // Replace the line at the specified index with the new line
        lines.set(lineIndex, newLine);

        // Write the modified list of lines back to the file
        String content = TextUtils.join("\n", lines);
        writeToFile(context, fileName, content);
    }

    public static boolean fileExists(Context context, String fileName) {
        File file = new File(context.getFilesDir(), fileName);
        return file.exists();
    }

}
