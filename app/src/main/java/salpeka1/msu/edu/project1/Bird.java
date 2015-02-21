package salpeka1.msu.edu.project1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

/**
 * Created by Alex on 2/10/2015.
 */
public class Bird {

    final static float SCALE_IN_VIEW = 0.9f;

    private float X;  // x coordinate for the bird
    private float Y;  // y coordinate for the bird

    private Bitmap imageBird;  // image of the bird to draw in

    Game game;


    public void setX(float x) {
        X = x;
    }
    public void setY(float y) {
        Y = y;
    }

    public Bird(Context context, int id, Game pgame) {
        X = Y = .5f;

        this.game = pgame;
        imageBird = BitmapFactory.decodeResource(context.getResources(), id);

    }

    public void draw(Canvas canvas) {

        int wid = canvas.getWidth();
        int hit = canvas.getHeight();

        // Determine the minimum of the two dimensions
        int minDim = wid < hit ? wid : hit;

        int screenSize = (int)(minDim * SCALE_IN_VIEW);

        float scaleFactor = (float) screenSize / (float) canvas.getWidth();

        // Compute the margins so we center the puzzle
        int marginX = (wid - screenSize) / 2;
        int marginY = (hit - screenSize) / 2;

        canvas.save();

        // Convert x,y to pixels and add the margin, then draw
        canvas.translate(marginX + X * screenSize, marginY + Y * screenSize);

        canvas.scale(scaleFactor, scaleFactor);

        // This magic code makes the center of the piece at 0, 0
        canvas.translate(-imageBird.getWidth() / 2, -imageBird.getHeight() / 2);

        // Draw the bitmap
        canvas.drawBitmap(imageBird, 0, 0, null);

        canvas.restore();
    }

    public void move(float dx, float dy) {
        X += dx;
        Y += dy;
    }
}
