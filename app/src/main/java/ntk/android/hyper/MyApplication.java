package ntk.android.hyper;

import android.content.Context;

import androidx.multidex.MultiDex;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;

import es.dmoral.toasty.Toasty;
import ntk.android.base.ApplicationParameter;
import ntk.android.base.ApplicationStaticParameter;
import ntk.android.base.ApplicationStyle;
import ntk.android.base.NTKApplication;
import ntk.android.base.utill.FontManager;
import ntk.android.base.view.ViewController;
import ntk.android.hyper.activity.MainActivity;

public class MyApplication extends NTKApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        DEBUG = true;
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
        applicationStyle = new ApplicationStyle() {
            @Override
            public ViewController getViewController() {
                ViewController vc = new ViewController() {
                };
                vc.setLoading_view(R.layout.sub_base_loading);
                vc.setEmpty_view(R.layout.sub_base_empty);
                vc.setError_view(R.layout.sub_base_error);
                return vc;
            }

            @Override
            public Class<?> getMainActivity() {
                return MainActivity.class;
            }
        };
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    @Override
    protected ApplicationStaticParameter getConfig() {
        ApplicationStaticParameter applicationStaticParameter = new ApplicationStaticParameter();
        // ApplicationStaticParameter.PACKAGE_NAME = "ntk.android.hypershop";
//         ApplicationStaticParameter.URL = "http://4f91f20e3626.ngrok.io";

        return applicationStaticParameter;
    }

    @Override
    public ApplicationParameter getApplicationParameter() {
        return new ApplicationParameter(BuildConfig.APPLICATION_ID, BuildConfig.APPLICATION_ID, BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE);
    }
}
