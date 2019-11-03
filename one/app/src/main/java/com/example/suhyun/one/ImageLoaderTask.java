package com.example.suhyun.one;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2016-05-19.
 */
public class ImageLoaderTask extends AsyncTask<Void, Void, Bitmap> {
    private ImageView imageView;

    private String imageAddress;

    public ImageLoaderTask(ImageView imageView, String imageAddress) {
        this.imageView = imageView;
        this.imageAddress = imageAddress;
        System.out.println(imageAddress);
    }

    @Override
    protected Bitmap doInBackground (Void... params) {
        Bitmap bitmap = null;
        try {
            InputStream is = new java.net.URL(this.imageAddress).openStream();
            bitmap = BitmapFactory.decodeStream(is);
            System.out.println("동작함");
        } catch (IOException e) {
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        this.imageView.setImageBitmap(bitmap);
    }
}
