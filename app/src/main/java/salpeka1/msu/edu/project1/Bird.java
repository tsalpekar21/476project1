package salpeka1.msu.edu.project1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

/**
 * Created by Alex on 2/10/2015.
 */
public class Bird {

    final static float SCALE_IN_VIEW = 0.9f;

    private float X = 0;  // x coordinate for the bird
    private float Y = 0;  // y coordinate for the bird

    private int marginX;
    private int marginY;

    private Rect rect;
    // Rectangle we will use for intersection testing
    private Rect overlap = new Rect();

    private Bitmap imageBird;  // image of the bird to draw in
    private int imageID;

    Game game;



    public void setX(float x) {
        X = x;
        setRect();
    }
    public void setY(float y) {
        Y = y;
        setRect();
    }

    public float getX() { return X; }

    public float getY() { return Y; }

    public float getHeight() { return imageBird.getHeight(); }

    public float getWidth() { return imageBird.getWidth(); }

    public Rect getRect() { return rect; }

    public Bird(Context context, int id, Game pgame) {
        X = Y = (float) (.5 * pgame.getGameField());

        this.game = pgame;
        this.imageID = id;
        imageBird = BitmapFactory.decodeResource(context.getResources(), id);

        rect = new Rect();
        setRect();
    }

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    private void setRect() {
        rect.set((int)X, (int)Y, (int)X+imageBird.getWidth(), (int)Y+imageBird.getHeight());
    }

    public void draw(Canvas canvas) {

        int wid = canvas.getWidth();
        int hit = canvas.getHeight();

        // Determine the minimum of the two dimensions
        int minDim = wid < hit ? wid : hit;

        int screenSize = (int)(minDim * SCALE_IN_VIEW);

        float scaleFactor = (float) screenSize / (float) canvas.getWidth();

        // Compute the margins
        marginX = (wid - screenSize) / 2;
        marginY = (hit - screenSize) / 2;

        canvas.save();

        // Convert x,y to pixels and add the margin, then draw
        canvas.translate(X + marginX, Y + marginY);

        canvas.scale(scaleFactor, scaleFactor);

        // This magic code makes the center of the piece at 0, 0
        canvas.translate(-imageBird.getWidth() / 2, -imageBird.getHeight() / 2);

        Log.i("Bird Draw X", Float.toString(X));
        Log.i("Bird Draw Y", Float.toString(Y));

        // Draw the bitmap
        canvas.drawBitmap(imageBird, 0, 0, null);

        canvas.restore();
    }

    public void move(float dx, float dy) {
        X += dx;
        Y += dy;
        setRect();
    }


    // assuming this is for touch event to see if we've touched the bird itself.
    public boolean hit(float testX, float testY) {
        int pX = (int)((testX - X));
        int pY = (int)((testY - Y));

        if(pX < 0 || pX >= imageBird.getWidth() ||
                pY < 0 || pY >= imageBird.getHeight()) {
            return false;
        }

        // We are within the rectangle of the piece.
        // Are we touching actual picture?
        return (imageBird.getPixel(pX, pY) & 0xff000000) != 0;
    }

    /**
     * Collision detection between two birds. This object is
     * compared to the one referenced by other
     * @param other Bird to compare to.
     * @return True if there is any overlap between the two birds.
     */
    public boolean collisionTest(Bird other) {
        // Do the rectangles overlap?
        if(!Rect.intersects(rect, other.rect)) {
            return false;
        }

        // Determine where the two images overlap
        overlap.set(rect);
        overlap.intersect(other.rect);

        // We have overlap. Now see if we have any pixels in common
        for(int r=overlap.top; r<overlap.bottom;  r++) {
            int aY = (int)((r - Y));
            int bY = (int)((r - other.Y));

            for(int c=overlap.left;  c<overlap.right;  c++) {

                int aX = (int)((c - X));
                int bX = (int)((c - other.X));

                if( (imageBird.getPixel(aX, aY) & 0x80000000) != 0 &&
                        (other.imageBird.getPixel(bX, bY) & 0x80000000) != 0) {
                    return true;
                }
            }
        }

        return false;
    }


}
