package com.elianddb.android.wordusherdev;

public abstract class GameObject {
    public static final int NO_ID = -1;

    private int id;
    private int x;
    private int y;
    private Type type;
    private int imgRsc;
    private Scene scene;

    public GameObject(int x, int y, Type type, int imgRsc, Scene scene) {
        id = NO_ID;
        this.x = x;
        this.y = y;
        this.type = type;
        this.imgRsc = imgRsc;
        this.scene = scene;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getImgRsc() {
        return imgRsc;
    }

    public void setImgRsc(int imgRsc) {
        this.imgRsc = imgRsc;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public enum Type {
        VOID,
        MATERIAL_IMMOVABLE,
        MATERIAL_MOVABLE,
        MATERIAL_BACKGROUND,
        ENTITY,
        LETTER_MOVABLE,
        PLAYER;

        public boolean isMovable() {
            boolean movable;
            switch(this) {
                case MATERIAL_MOVABLE:
                case LETTER_MOVABLE:
                case PLAYER:
                    movable = true;
                    break;
                default:
                    movable = false;
            }
            return movable;
        }

        public boolean isBackground() {
            boolean background;
            switch(this) {
                case MATERIAL_BACKGROUND:
                    background = true;
                    break;
                default:
                    background = false;
            }
            return background;
        }

        public boolean isLetter() {
            switch(this) {
                case LETTER_MOVABLE:
                    return true;
                default:
                    return false;
            }
        }
    }
}
