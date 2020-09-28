package ntk.android.hypershop.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import ntk.android.hypershop.BuildConfig;
import ntk.android.hypershop.R;
import ntk.android.hypershop.config.ConfigRestHeader;
import ntk.android.hypershop.config.ConfigStaticValue;
import ntk.android.hypershop.utill.AppUtill;
import ntk.android.hypershop.utill.EasyPreference;
import ntk.android.hypershop.utill.FontManager;
import ntk.base.api.core.entity.CoreMain;
import ntk.base.api.core.interfase.ICore;
import ntk.base.api.core.entity.CoreTheme;
import ntk.base.api.core.model.MainCoreResponse;
import ntk.base.api.utill.RetrofitManager;

public class ActSplash extends AppCompatActivity {

    @BindView(R.id.AnimationActSplash)
    LottieAnimationView Loading;

    @BindView(R.id.lblVersionActSplash)
    TextView Lbl;

    @BindView(R.id.btnRefreshActSplash)
    Button BtnRefresh;

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
        BtnRefresh.setTypeface(FontManager.GetTypeface(this, FontManager.IranSans));
    }

    @Override
    protected void onStart() {
        super.onStart();
        GetTheme();
        HandelData();
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
                            if (!mainCoreResponse.IsSuccess) {
                                Loading.cancelAnimation();
                                Loading.setVisibility(View.GONE);
                                BtnRefresh.setVisibility(View.VISIBLE);
                                Toasty.warning(ActSplash.this, mainCoreResponse.ErrorMessage, Toasty.LENGTH_LONG, true).show();
                                return;

                            }
                            HandelDataAction(mainCoreResponse.Item);

                        }


                        @Override
                        public void onError(Throwable e) {
                            Loading.cancelAnimation();
                            Loading.setVisibility(View.GONE);
                            BtnRefresh.setVisibility(View.VISIBLE);
                            Toasty.warning(ActSplash.this, "خطای سامانه مجددا تلاش کنید", Toasty.LENGTH_LONG, true).show();

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } else {
            Loading.setVisibility(View.GONE);
            BtnRefresh.setVisibility(View.VISIBLE);
            Toasty.warning(this, "عدم دسترسی به اینترنت", Toasty.LENGTH_LONG, true).show();
        }
    }

    private void HandelDataAction(CoreMain model) {

        EasyPreference.with(ActSplash.this).addLong("MemberUserId", model.MemberUserId);
        EasyPreference.with(ActSplash.this).addLong("UserId", model.UserId);
        EasyPreference.with(ActSplash.this).addLong("SiteId", model.SiteId);
        EasyPreference.with(ActSplash.this).addString("configapp", new Gson().toJson(model));
        if (model.UserId <= 0)
            EasyPreference.with(ActSplash.this).addBoolean("Registered", false);

        Loading.cancelAnimation();
        Loading.setVisibility(View.GONE);

        if (!EasyPreference.with(ActSplash.this).getBoolean("Intro", false)) {
            new Handler().postDelayed(() -> {
                Loading.setVisibility(View.GONE);
                startActivity(new Intent(ActSplash.this, ActIntro.class));
                finish();
            }, 3000);
            return;
        }
        if (!EasyPreference.with(ActSplash.this).getBoolean("Registered", false)) {
            new Handler().postDelayed(() -> {
                Loading.setVisibility(View.GONE);
                startActivity(new Intent(ActSplash.this, ActRegister.class));
                finish();
            }, 3000);
            return;
        }
        new Handler().postDelayed(() -> {
            Loading.setVisibility(View.GONE);
            startActivity(new Intent(ActSplash.this, ActMain.class));
            finish();
        }, 3000);
    }

    private void GetTheme() {
        Loading.playAnimation();
        Loading.setVisibility(View.VISIBLE);
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
                        Loading.cancelAnimation();
                        Loading.setVisibility(View.GONE);
                        BtnRefresh.setVisibility(View.VISIBLE);
                        Toasty.warning(ActSplash.this, "خطای سامانه مجددا تلاش کنید", Toasty.LENGTH_LONG, true).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @OnClick(R.id.btnRefreshActSplash)
    public void ClickRefresh() {
        Loading.playAnimation();
        Loading.setVisibility(View.VISIBLE);
        BtnRefresh.setVisibility(View.GONE);
        HandelData();
    }
}
