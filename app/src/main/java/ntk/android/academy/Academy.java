package ntk.android.academy;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;

import es.dmoral.toasty.Toasty;
import ntk.android.academy.utill.Constant;
import ntk.android.academy.utill.FontManager;

public class Academy extends MultiDexApplication {

    public static boolean Inbox = false;

    @Override
    public void onCreate() {
        super.onCreate();

        if (!new File(getCacheDir(), "image").exists()) {
            new File(getCacheDir(), "image").mkdirs();
        }
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .diskCache(new UnlimitedDiskCache(new File(getCacheDir(), "image")))
                .diskCacheFileNameGenerator(imageUri -> {
                    String[] Url = imageUri.split("/");
                    return Url[Url.length];
                })
                .build();
        ImageLoader.getInstance().init(config);

        Toasty.Config.getInstance()
                .setToastTypeface(FontManager.GetTypeface(getApplicationContext(), FontManager.IranSans))
                .setTextSize(14).apply();


        new Constant().SetMap();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }
}
