package ntk.android.academy.library.scrollgallery.loader;

import android.content.Context;
import android.widget.ImageView;

public interface MediaLoader {
    boolean isImage();

    void loadMedia(Context context, ImageView imageView, SuccessCallback callback);

    void loadThumbnail(Context context, ImageView thumbnailView, SuccessCallback callback);

    interface SuccessCallback {
        void onSuccess();
    }
}