package com.elianddb.android.wordusherdev;

import java.util.ArrayList;

public class Scene {
    private final int minX = 0;
    private final int minY = 0;
    private final int maxX;
    private final int maxY;

    private final int numOfRows;
    private final int numOfCols;

    private final MaterialBackground background;
    private final MaterialBackground outOfBounds;

    private final DisplayManager displayManager;
    private final GameObject[][] objects;
    private final ArrayList<Entity> entities;
    private boolean win;

    Scene(Display display, int backgroundImgRsc) {
        displayManager = new DisplayManager(display);
        background = new MaterialBackground(backgroundImgRsc, this);
        display.clearWith(background.getImgRsc());
        outOfBounds = new MaterialBackground(R.drawable.ic_launcher_background, this);
        outOfBounds.setType(GameObject.Type.VOID);

        numOfRows = display.getNumOfRows();
        numOfCols = display.getNumOfCols();
        maxY = numOfRows - 1;
        maxX = numOfCols - 1;

        objects = new GameObject[numOfRows][numOfCols];
        for (int row = minY; row <= maxY; ++row) {
            for (int index = minX; index <= maxX; ++index) {
                objects[row][index] = background;
            }
        }
        entities = new ArrayList<>();
        win = false;
    }

    public GameObject getBackground() {
        return background;
    }

    public boolean isWon() {
        return win;
    }

    public void setWin(boolean win) {
        this.win = win;
    }

    public int getNumOfRows() {
        return numOfRows;
    }

    public int getNumOfCols() {
        return numOfCols;
    }

    public void clearObject(GameObject object) {
        displayManager.addToClearBuffer(object.getX(), object.getY());
        objects[object.getY()][object.getX()] = background;
    }

    public void setObject(GameObject object) {
        if (!objects[object.getY()][object.getX()].getType().isBackground())
            throw new IllegalArgumentException("Attempted to place an object on a non-background object.");

        objects[object.getY()][object.getX()] = object;
    }

    public Entity getEntity(int id) {
        return entities.get(id);
    }

    public void addEntity(Entity entity) {
        if (entities.contains(entity))
            throw new IllegalArgumentException("Entity already exists in scene.");
        entities.add(entity);
        entity.setId(entities.indexOf(entity));
        setObject(entity);
    }

    public GameObject getObject(int x, int y) {
        if (!isWithinBounds(x, y)) {
            return outOfBounds;
        }

        return objects[y][x];
    }

    public boolean isWithinBounds(int x, int y) {
        return x >= minX && x <= maxX && y >= minY && y <= maxY;
    }

    public boolean isSafe(int x, int y) {
        return isWithinBounds(x, y) && getObject(x, y).getType().isBackground();
    }

    public DisplayManager getDisplayManager() {
        return displayManager;
    }

    public class DisplayManager {
        Display display;
        ArrayList<int[]> clearBuffer;

        DisplayManager(Display display) {
            this.display = display;
            clearBuffer = new ArrayList<>(0);
        }

        public void addToClearBuffer(int x, int y) {
            clearBuffer.add(new int[]{x, y});
        }

        public void drawClearBuffer() {
            for (int[] coords : clearBuffer) {
                int x = coords[0];
                int y = coords[1];
                objects[y][x] = background;
                display.setImageResourceAt(x, y, background.getImgRsc());
            }
        }

        public void draw(GameObject obj) {
            drawClearBuffer();
            clearBuffer.clear();

            display.setImageResourceAt(obj.getX(), obj.getY(), obj.getImgRsc());
        }
    }
}
