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

//main application of App
public class MyApplication extends NTKApplication {
    @Override
    public void onCreate() {
        applicationStyle = new ApplicationStyle() {
            @Override
            public ViewController getViewController() {
                //define app default views such as loading
                ViewController vc = new ViewController() {
                };
                vc.setLoading_view(R.layout.sub_base_loading)
                        .setEmpty_view(R.layout.sub_base_empty)
                        .setError_view(R.layout.sub_base_error)
                        .setError_button(R.id.btn_error_tryAgain)
                        .setError_label(R.id.tvError);
                return vc;
            }
            //define main Activity of app
            @Override
            public Class<?> getMainActivity() {
                return MainActivity.class;
            }
        };
        super.onCreate();
        //@note should be comment  for release  (or false)
        DEBUG = true;
        //create image cache folder
        if (!new File(getCacheDir(), "image").exists()) {
            new File(getCacheDir(), "image").mkdirs();
        }
        //define default of Image Loader API
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .diskCache(new UnlimitedDiskCache(new File(getCacheDir(), "image")))
                .diskCacheFileNameGenerator(imageUri -> {
                    String[] Url = imageUri.split("/");
                    return Url[Url.length];
                })
                .build();
        ImageLoader.getInstance().init(config);
        //define default of toasty library
        Toasty.Config.getInstance()
                .setToastTypeface(FontManager.T1_Typeface(getApplicationContext()))
                .setTextSize(14).apply();
        //create style of application

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    @Override
    protected ApplicationStaticParameter getConfig() {
        //for change default parameters of application like api url ,
        //@note should be comment  for release
        ApplicationStaticParameter applicationStaticParameter = new ApplicationStaticParameter();
//         ApplicationStaticParameter.PACKAGE_NAME = "ntk.android.hypershop";
//         ApplicationStaticParameter.URL = "https://47f2b488bf59.ngrok.io";

        return applicationStaticParameter;
    }

    @Override
    public ApplicationParameter getApplicationParameter() {
        //default parameters for baseActivity
        return new ApplicationParameter(BuildConfig.APPLICATION_ID, BuildConfig.APPLICATION_ID, BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE);
    }
}
