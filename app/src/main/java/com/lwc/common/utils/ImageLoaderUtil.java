package com.lwc.common.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.lwc.common.R;

import java.io.File;

/**
 * 图片加载的引擎
 *
 * Created by 何栋 on 2017/10/15.
 * 294663966@qq.com
 */
public class ImageLoaderUtil implements ImageLoaderInterface {

    private static ImageLoaderUtil loader = null;

    public static ImageLoaderUtil getInstance() {
        if (loader == null) {
            synchronized (ImageLoaderUtil.class) {
                if (loader == null)
                    loader = new ImageLoaderUtil();
            }
        }
        return loader;
    }
    public void displayFromNetD(Context context, String url, ImageView imageView) {
        if (context != null) {
            Glide.with(context).load(url).error(R.drawable.default_portrait_100).fitCenter().priority(Priority.HIGH)
                    .into(imageView);
        }
    }

    public void displayLongPic(final Context context, String url, final ImageView imageView) {
        if (context != null) {
            Glide.with(context).load(url).downloadOnly(new SimpleTarget<File>() {
                @Override
                public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
                    Bitmap bitmap = BitmapFactory.decodeFile(resource.getAbsolutePath(),getBitmapOption(1));
                    Glide.with(context).load(new File(resource.getAbsolutePath())).override(bitmap.getWidth(),bitmap.getHeight()).into(imageView);
                   // imageView.setImageBitmap(bitmap);
                }
            });

        }
    }

    private BitmapFactory.Options getBitmapOption(int inSampleSize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPurgeable = true;
        options.inSampleSize = inSampleSize;
        return options;
    }


    @Override
    public void displayFromNet(Context context, String url, ImageView imageView) {
        if (context != null) {
            Glide.with(context).load(url).thumbnail(0.6f).error(R.drawable.default_portrait_100).override(140, 140).fitCenter().priority( Priority.HIGH)
                    .into(imageView);
        }
    }

    @Override
    public void displayFromNet(Context context, String url, ImageView imageView,int imageResouse) {
        if (context != null) {
            Glide.with(context).load(url).thumbnail(0.6f).error(imageResouse).override(140, 140).fitCenter().priority( Priority.HIGH)
                    .into(imageView);
        }
    }

    @Override
    public void displayFromNetMin(Context context, String url, ImageView imageView) {
        if (context != null) {
            Glide.with(context).load(url).thumbnail(0.1f).error(R.drawable.default_portrait_100).override(60, 60).fitCenter().priority( Priority.HIGH)
                    .into(imageView);
        }
    }

    @Override
    public void displayFromLocal(Context context, ImageView imageView,
                                 int resourceId) {
        if (context != null) {
            Glide.with(context).load(resourceId).fitCenter().into(imageView);
        }
    }

    @Override
    public void displayFromLocal(Context context, ImageView imageView, String path) {
        if (context != null) {
            Glide.with(context).load(new File(path)).fitCenter().into(imageView);
        }
    }


    @Override
    public void displayFromLocal(Context context, ImageView imageView,
                                 int resourceId,int size) {
        if (context != null) {
            Glide.with(context).load(resourceId).fitCenter().override(size,size).into(imageView);
        }
    }

    @Override
    public void displayFromFile(Context context, ImageView imageView, File file) {
        if (context != null) {
            Glide.with(context).load(file).fitCenter().into(imageView);
        }
    }
}
