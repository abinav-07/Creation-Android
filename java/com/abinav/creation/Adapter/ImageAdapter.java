package com.abinav.creation.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.abinav.creation.R;

import java.util.ArrayList;


//PagerAdapter is used for SlidingImageView
public class ImageAdapter extends PagerAdapter {

    //list of path of images from directory
    private ArrayList<String> images;
    private LayoutInflater inflater;
    private Context context;

    //Constructor
    public ImageAdapter(Context context, ArrayList<String> images) {
        this.context = context;
        this.images = images;
    }

    //Size of Array List
    @Override
    public int getCount() {
        return this.images.size();
    }

    //On Image Change, the previous Image gets destroyed
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    //checks for the value returned by instantiateItem Method
    //if null displays blank screen
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    //Creates a page for the given position
    // The adapter is responsible for adding the view to the container given here
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.sliding_imageview, container, false);

        imageView = (ImageView) view.findViewById(R.id.imageDisplay);

        //Decoding the path from images arraylist to bitmap image
        Bitmap bitmap = BitmapFactory.decodeFile(images.get(position));
        imageView.setImageBitmap(bitmap);

        ((ViewPager) container).addView(view);

        return view;
    }
}
