package salpeka1.msu.edu.project1;

import android.graphics.Bitmap;

/**
 * Created by Alex on 2/10/2015.
 */
public class Bird {

    private int X;
    private int Y;  // x,y coordinates for where to draw bird

    public int getX() {
        return X;
    }

    public void setX(int x) {
        X = x;
    }

    public int getY() {
        return Y;
    }

    public void setY(int y) {
        Y = y;
    }

    private Bitmap imageBird;  // image of the bird to draw in

    public Bird() {
        X = Y = 0;

        // code to set bitmap here, based on some input passed to this constructor as a param
    }
}
