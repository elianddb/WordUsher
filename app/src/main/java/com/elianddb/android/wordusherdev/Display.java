package com.elianddb.android.wordusherdev;

import android.app.Activity;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

public class Display {
    private final int numOfRows;
    private final int numOfCols;
    private final double scaleConstant;
    private double zoomOffset;
    private double zoom;

    private final Activity activity;
    private final ImageView[][] imgTable;

    public Display(Activity activity, int numOfRows, int numOfCols) {
        this(activity, numOfRows, numOfCols, 20);
    }

    public Display(Activity activity, int numOfRows, int numOfCols, int zoom) {
        this.numOfRows = numOfRows;
        this.numOfCols = numOfCols;
        final double eulerNum = 2.71828;
        scaleConstant = 179.29 * Math.pow(eulerNum, (-0.1849 * numOfRows));
        this.zoom = zoom;
        zoomOffset = numOfRows * (zoom / 100D);

        this.activity = activity;
        TableLayout rootTableForContents = activity.findViewById(R.id.game_display);
        imgTable = new ImageView[numOfRows][numOfCols];

        final int minIndex = 0;
        TableRow curTR;
        for (int row = minIndex; row < numOfRows; ++row) {
            curTR = new TableRow(activity);
            curTR.setGravity(Gravity.CENTER);
            rootTableForContents.addView(curTR);

            for (int col = minIndex; col < numOfCols; ++col) {
                imgTable[row][col] = new ImageView(activity);
                curTR.addView(imgTable[row][col]);
                setDefaultParamaters(imgTable[row][col]);
            }
        }
    }

    public void clearWith(int imgRsc) {
        for (ImageView[] imgViewRow : imgTable) {
            for (ImageView imgView : imgViewRow) {
                imgView.setImageResource(imgRsc);
            }
        }
    }

    public void setImageResourceAt(int x, int y, int imgRsc) {
        imgTable[y][x].setImageResource(imgRsc);
    }

    public double getZoom() {
        return zoom;
    }

    public void setZoom(double zoom) {
        this.zoom = zoom;
        zoomOffset = numOfRows * (zoom / 100D);
        for (ImageView[] imgTableRow : imgTable) {
            for (ImageView imgView : imgTableRow) {
                imgView.requestLayout();
                imgView.getLayoutParams().width = getPxToFitTable();
                imgView.getLayoutParams().height = getPxToFitTable();
            }
        }
    }

    public Activity getActivity() {
        return activity;
    }

    public int getNumOfRows() {
        return numOfRows;
    }

    public int getNumOfCols() {
        return numOfCols;
    }

    public int getPxToFitTable() {
        return dpToPx(scaleConstant + zoomOffset);
    }

    public void setDefaultParamaters(ImageView imgView) {
        imgView.requestLayout();
        imgView.setPadding(0, 0, 4, 0);
        imgView.setImageResource(R.drawable.ic_launcher_background);
        imgView.getLayoutParams().width = getPxToFitTable();
        imgView.getLayoutParams().height = getPxToFitTable();
    }

    public int dpToPx(double dp) {
        float density = activity.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }
}
