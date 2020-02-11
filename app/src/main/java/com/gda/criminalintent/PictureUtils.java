package com.gda.criminalintent;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

public class PictureUtils {

    public static Bitmap getScaledBitmap(String path, Activity activity) {
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);

        return getScaledBitmap(path, size.x, size.y);
    }

    public static Bitmap getScaledBitmap(String path, int destWidth, int destHeight) {
        // Read image from disk and get dimensions
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path);

        int imageWidth = options.outWidth;
        int imageHeight = options.outHeight;

        int sampleSize = 1;
        if (imageHeight > destHeight || imageWidth > destWidth) {
            if (imageHeight > destHeight) {
                sampleSize = Math.round(imageHeight / destHeight);
            } else {
                sampleSize = Math.round(imageWidth / destWidth);
            }
        }

        options = new BitmapFactory.Options();
        options.inSampleSize = sampleSize;
        return BitmapFactory.decodeFile(path, options);
    }
}
