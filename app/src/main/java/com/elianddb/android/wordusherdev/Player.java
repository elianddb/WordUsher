package com.elianddb.android.wordusherdev;

import android.util.Log;

public class Player extends MaterialMovable {
    Entity held;
    private final int imgRscNormal;
    private final int imgRscAction;
    public Player(int x, int y, int imgRscNormal, int imgRscAction, Scene scene) {
        super(x, y, imgRscNormal, scene);
        this.imgRscNormal = imgRscNormal;
        this.imgRscAction = imgRscAction;
        setType(Type.PLAYER);
    }

    @Override
    public boolean move(Direction direction) {
        if (!isHolding() || look(direction).getId() == held.getId()) {
            return super.move(direction);
        }

        Log.d("Player.move()", "Checking ability to move...");
        if (!willMove(direction) || !held.willMove(direction)) {
            Log.d("Player.move()", "I can't move!");
            return false;
        }

        Log.d("Player.move()", "Attempting to move held item...");
        super.move(direction);
        return held.move(direction);
    }

    public void hold(Direction direction) {
        GameObject take = look(direction);
        if (!take.getType().isMovable()) {
            return;
        }

        held = getScene().getEntity(take.getId());
        Log.d("Player.hold()", "I am holding entity: " + held.getId());
        setImgRsc(imgRscAction);
        getScene().getDisplayManager().draw(this);
    }

    public boolean isHolding() {
        return held != null;
    }

    public void letGo() {
        held = null;
        setImgRsc(imgRscNormal);
        getScene().getDisplayManager().draw(this);
    }
}
