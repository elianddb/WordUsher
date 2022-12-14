package com.elianddb.android.wordusherdev;

public class MaterialImmovable extends GameObject {
    public MaterialImmovable(int x, int y, int imgRsc, Scene scene) {
        super(x, y, Type.MATERIAL_IMMOVABLE, imgRsc, scene);
        getScene().setObject(this);
        getScene().getDisplayManager().draw(this);
    }
}
