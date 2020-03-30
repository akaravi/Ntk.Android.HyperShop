package ntk.android.academy.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.airbnb.lottie.LottieAnimationView;
import com.google.gson.Gson;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ntk.android.academy.BuildConfig;
import ntk.android.academy.R;
import ntk.android.academy.config.ConfigRestHeader;
import ntk.android.academy.config.ConfigStaticValue;
import ntk.android.academy.utill.AppUtill;
import ntk.android.academy.utill.EasyPreference;
import ntk.android.academy.utill.FontManager;
import ntk.base.api.core.interfase.ICore;
import ntk.base.api.core.entity.CoreTheme;
import ntk.base.api.core.model.MainCoreResponse;
import ntk.base.api.utill.RetrofitManager;

public class ActSplash extends AppCompatActivity {

    @BindView(R.id.lblVersionActSplash)
    TextView Lbl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_splash);
        ButterKnife.bind(this);
        init();
    }

    @SuppressLint("SetTextI18n")
    private void init() {
        Lbl.setTypeface(FontManager.GetTypeface(this, FontManager.IranSans));
        Lbl.setText("نسخه  " + (int) Float.parseFloat(BuildConfig.VERSION_NAME) + "." + BuildConfig.VERSION_CODE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        GetTheme();
        if (!EasyPreference.with(this).getString("configapp", "").isEmpty()) {
            if (EasyPreference.with(this).getBoolean("Intro", false)) {
                new Handler().postDelayed(() -> {
                    startActivity(new Intent(ActSplash.this, ActMain.class));
                    finish();
                }, 3000);
            } else {
                new Handler().postDelayed(() -> {
                    startActivity(new Intent(ActSplash.this, ActIntro.class));
                    finish();
                }, 3000);
            }
        } else {
            HandelData();
        }
    }

    private void HandelData() {
        if (AppUtill.isNetworkAvailable(this)) {
            RetrofitManager manager = new RetrofitManager(this);
            ICore iCore = manager.getCachedRetrofit(new ConfigStaticValue(this).GetApiBaseUrl()).create(ICore.class);
            Map<String, String> headers = new ConfigRestHeader().GetHeaders(this);
            Observable<MainCoreResponse> observable = iCore.GetResponseMain(headers);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<MainCoreResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(MainCoreResponse mainCoreResponse) {
                            EasyPreference.with(ActSplash.this).addString("configapp", new Gson().toJson(mainCoreResponse.Item));
                            if (EasyPreference.with(ActSplash.this).getBoolean("Intro", false)) {
                                new Handler().postDelayed(() -> {
                                    startActivity(new Intent(ActSplash.this, ActMain.class));
                                    finish();
                                }, 3000);
                            } else {
                                new Handler().postDelayed(() -> {
                                    startActivity(new Intent(ActSplash.this, ActIntro.class));
                                    finish();
                                }, 3000);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Toasty.warning(ActSplash.this, "خطای سامانه مجددا تلاش کنید", Toasty.LENGTH_LONG, true).show();

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } else {
            Toasty.warning(this, "عدم دسترسی به اینترنت", Toasty.LENGTH_LONG, true).show();
        }
    }

    private void GetTheme() {
        if (AppUtill.isNetworkAvailable(this)) {
            RetrofitManager manager = new RetrofitManager(this);
            ICore iCore = manager.getCachedRetrofit(new ConfigStaticValue(this).GetApiBaseUrl()).create(ICore.class);
            Map<String, String> headers = new ConfigRestHeader().GetHeaders(this);
            Observable<CoreTheme> call = iCore.GetThemeCore(headers);
            call.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<CoreTheme>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(CoreTheme theme) {
                            EasyPreference.with(ActSplash.this).addString("Theme", new Gson().toJson(theme.Item.ThemeConfigJson));
                        }

                        @Override
                        public void onError(Throwable e) {
                            Toasty.warning(ActSplash.this, "خطای سامانه مجددا تلاش کنید", Toasty.LENGTH_LONG, true).show();
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } else {
            Toasty.warning(this, "عدم دسترسی به اینترنت", Toasty.LENGTH_LONG, true).show();
        }
    }
}