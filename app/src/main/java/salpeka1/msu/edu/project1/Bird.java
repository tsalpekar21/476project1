package salpeka1.msu.edu.project1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

/**
 * Created by Alex on 2/10/2015.
 */
public class Bird {

    final static float SCALE_IN_VIEW = 0.2f;

    private float X;  // x coordinate for the bird

    private float Y;  // y coordinate for the bird

    private Bitmap imageBird;  // image of the bird to draw in

    private Context context;

    private BirdView birdView;

    private int id;

    private int marginX;

    private int marginY;

    private float scaleFactor;

    public float getX() {
        return X;
    }

    public void setX(float x) {
        X = x;
    }

    public float getY() {
        return Y;
    }

    public void setY(float y) {
        Y = y;
    }



    public Bird(Context c, BirdView bv, int id) {
        X = Y = .5f;

        this.context = c;
        this.birdView = bv;
        this.id = id;

        imageBird = BitmapFactory.decodeResource(context.getResources(), id);

    }

    public void draw(Canvas canvas) {

        int wid = canvas.getWidth();
        int hit = canvas.getHeight();

        // Determine the minimum of the two dimensions
        int minDim = wid < hit ? wid : hit;

        int screenSize = (int)(minDim * SCALE_IN_VIEW);

        // Compute the margins so we center the puzzle
        marginX = (wid - screenSize) / 2;
        marginY = (hit - screenSize) / 2;

        canvas.save();

        // Convert x,y to pixels and add the margin, then draw
        canvas.translate(marginX + X * screenSize, marginY + Y * screenSize);


        // This magic code makes the center of the piece at 0, 0
        canvas.translate(-imageBird.getWidth() / 2, -imageBird.getHeight() / 2);

        // Draw the bitmap
        canvas.drawBitmap(imageBird, 0, 0, null);
        canvas.restore();
    }
}
