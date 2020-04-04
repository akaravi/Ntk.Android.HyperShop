package ntk.android.hypershop.library.scrollgallery;

import ntk.android.hypershop.library.scrollgallery.loader.MediaLoader;

public class MediaInfo {

    private MediaLoader mLoader;

    public static MediaInfo mediaLoader(MediaLoader mediaLoader) {
        return new MediaInfo().setLoader(mediaLoader);
    }

    public MediaLoader getLoader() {
        return mLoader;
    }

    public MediaInfo setLoader(MediaLoader loader) {
        mLoader = loader;
        return this;
    }
}
