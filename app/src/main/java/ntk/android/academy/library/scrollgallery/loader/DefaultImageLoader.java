package ntk.android.academy.library.scrollgallery.loader;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public class DefaultImageLoader implements MediaLoader {
    private String Url;

    public DefaultImageLoader(String url) {
        Url = url;
    }

    @Override
    public boolean isImage() {
        return true;
    }

    @Override
    public void loadMedia(Context context, final ImageView imageView, final MediaLoader.SuccessCallback callback) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true).build();
        ImageLoader.getInstance().displayImage(Url, imageView, options);
    }

    @Override
    public void loadThumbnail(Context context, final ImageView thumbnailView, final SuccessCallback callback) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true).build();
        ImageSize targetSize = new ImageSize(100, 100);
        ImageLoader.getInstance().loadImage(Url, targetSize, options, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                thumbnailView.setImageBitmap(loadedImage);
            }
        });
    }
}