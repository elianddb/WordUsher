package com.elianddb.android.wordusherdev;

import android.util.Log;

public class Entity extends GameObject {
    public Entity(int x, int y, int imgRsc, Scene scene) {
        super(x, y, Type.ENTITY, imgRsc, scene);
        scene.addEntity(this);
        scene.getDisplayManager().draw(this);
    }

    public boolean move(int x, int y) {
        if (!getScene().isSafe(x, y)) {
            return false;
        }

        getScene().clearObject(this);
        setX(x);
        setY(y);
        getScene().setObject(this);
        getScene().getDisplayManager().draw(this);
        return true;
    }

    public boolean move(Direction direction) {
        GameObject inFront = look(direction);
        if (inFront.getType().isBackground()) {
            return move(parseX(direction), parseY(direction));
        }

        if (inFront.getType().isMovable()) {
//            System.out.println("Item in direction is movable." + " object id: " + nextObject.getId());
            Log.d("Entity.move()", "Attempting to push entity: " + inFront.getId());
            Entity entity = getScene().getEntity(inFront.getId());
            entity.move(direction);
        }

        return move(parseX(direction), parseY(direction));
    }

    public boolean willMove(Direction direction) {
        GameObject laser = look(direction);
        if (laser.getType().isBackground())
            return true;

        while (laser.getType().isMovable()) {
            laser = getScene().getEntity(laser.getId()).look(direction);
            if (laser.getType().isBackground()) {
                Log.d("Entity.willMove()", "I can move this direction!");
                return true;
            }
        }

        Log.d("Entity.willMove()","I cannot move this direction!");
        return false;
    }

    public GameObject look(Direction direction) {
        return getScene().getObject(parseX(direction), parseY(direction));
    }

    public enum Direction {
        RIGHT,
        LEFT,
        UP,
        DOWN;
        public static int parseX(int x, Direction direction) {
            switch (direction) {
                case LEFT:
                    return x - 1;
                case RIGHT:
                    return x + 1;
                default:
                    return x;
            }
        }

        public static int parseY(int y, Direction direction) {
            switch (direction) {
                case UP:
                    return y - 1;
                case DOWN:
                    return y + 1;
                default:
                    return y;
            }
        }
    }

    private int parseX(Direction direction) {
        return Direction.parseX(getX(), direction);
    }

    private int parseY(Direction direction) {
        return Direction.parseY(getY(), direction);
    }
}
