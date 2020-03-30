package ntk.android.academy.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ntk.android.academy.R;
import ntk.android.academy.config.ConfigRestHeader;
import ntk.android.academy.config.ConfigStaticValue;
import ntk.android.academy.event.EvMessage;
import ntk.android.academy.utill.AppUtill;
import ntk.android.academy.utill.EasyPreference;
import ntk.android.academy.utill.FontManager;
import ntk.base.api.core.interfase.ICore;
import ntk.base.api.core.model.CoreUserRegisterByMobileRequest;
import ntk.base.api.core.model.CoreUserRegisterByMobileResponse;
import ntk.base.api.core.model.CoreUserResponse;
import ntk.base.api.core.model.MainCoreResponse;
import ntk.base.api.utill.RetrofitManager;

public class ActRegister extends AppCompatActivity {

    @BindView(R.id.progressActRegister)
    ProgressBar Loading;

    @BindView(R.id.txtActRegister)
    EditText Txt;

    @BindViews({R.id.lblVerificationActRegister,
            R.id.lblNoPhoneActRegister,
            R.id.lblCounterActRegister})
    List<TextView> Lbls;

    private CountDownTimer Timer;
    private String PhoneNumber = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_register);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        Loading.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
        Loading.setVisibility(View.GONE);
        Txt.setTypeface(FontManager.GetTypeface(this, FontManager.IranSans));
        Lbls.get(0).setTypeface(FontManager.GetTypeface(this, FontManager.IranSans));
        Lbls.get(1).setTypeface(FontManager.GetTypeface(this, FontManager.IranSans));
        Lbls.get(2).setTypeface(FontManager.GetTypeface(this, FontManager.IranSans));
        ((Button) findViewById(R.id.btnActRegister)).setTypeface(FontManager.GetTypeface(this, FontManager.IranSans));
    }

    @OnClick(R.id.btnActRegister)
    public void ClickBtn() {
        if (((Button) findViewById(R.id.btnActRegister)).getText().equals("تایید شماره")) {
            if (Txt.getText().toString().isEmpty()) {
                Toast.makeText(this, "شماره موبایل خود را وارد نمایید", Toast.LENGTH_SHORT).show();
            } else {
                PhoneNumber = Txt.getText().toString();
                if (AppUtill.isNetworkAvailable(this)) {
                    if (CheckPermission()) {
                        Register();
                    } else {
                        ActivityCompat.requestPermissions(ActRegister.this, new String[]{Manifest.permission.RECEIVE_SMS}, 100);
                    }
                } else {
                    Toast.makeText(this, "عدم دسترسی به اینترنت", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            if (((Button) findViewById(R.id.btnActRegister)).getText().equals("ادامــه")) {
                if (Txt.getText().toString().isEmpty()) {
                    Toast.makeText(this, "کد اعتبار سنجی را وارد نمایید", Toast.LENGTH_SHORT).show();
                } else {
                    if (AppUtill.isNetworkAvailable(this)) {
                        Verify();
                    } else {
                        Toast.makeText(this, "عدم دسترسی به اینترنت", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    private void Verify() {
        if (AppUtill.isNetworkAvailable(this)) {
            Loading.setVisibility(View.VISIBLE);
            findViewById(R.id.cardActRegister).setVisibility(View.GONE);
            RetrofitManager manager = new RetrofitManager(this);
            ICore iCore = manager.getCachedRetrofit(new ConfigStaticValue(this).GetApiBaseUrl()).create(ICore.class);
            Map<String, String> headers = new ConfigRestHeader().GetHeaders(this);

            CoreUserRegisterByMobileRequest request = new CoreUserRegisterByMobileRequest();
            request.Mobile = PhoneNumber;
            request.Code = Txt.getText().toString();

            Observable<CoreUserResponse> observable = iCore.RegisterWithMobile(headers, request);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<CoreUserResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(CoreUserResponse response) {
                            Loading.setVisibility(View.GONE);
                            EasyPreference.with(ActRegister.this).addString("register", "1");
                            findViewById(R.id.cardActRegister).setVisibility(View.VISIBLE);
                            startActivity(new Intent(ActRegister.this, ActMain.class));
                            finish();
                        }

                        @Override
                        public void onError(Throwable e) {
                            findViewById(R.id.cardActRegister).setVisibility(View.VISIBLE);
                            Loading.setVisibility(View.GONE);
                            Toasty.warning(ActRegister.this, "خطای سامانه مجددا تلاش کنید", Toasty.LENGTH_LONG, true).show();

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } else {
            Loading.setVisibility(View.GONE);
            Toasty.warning(this, "عدم دسترسی به اینترنت", Toasty.LENGTH_LONG, true).show();
        }

    }

    private void Register() {
        if (AppUtill.isNetworkAvailable(this)) {
            Loading.setVisibility(View.VISIBLE);
            findViewById(R.id.cardActRegister).setVisibility(View.GONE);
            RetrofitManager manager = new RetrofitManager(this);
            ICore iCore = manager.getCachedRetrofit(new ConfigStaticValue(this).GetApiBaseUrl()).create(ICore.class);
            Map<String, String> headers = new ConfigRestHeader().GetHeaders(this);

            CoreUserRegisterByMobileRequest request = new CoreUserRegisterByMobileRequest();
            PhoneNumber = Txt.getText().toString();
            request.Mobile = PhoneNumber;

            Observable<CoreUserResponse> observable = iCore.RegisterWithMobile(headers, request);
            observable.observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<CoreUserResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(CoreUserResponse response) {
                            Loading.setVisibility(View.GONE);
                            findViewById(R.id.cardActRegister).setVisibility(View.VISIBLE);
                            InputFilter[] filterArray = new InputFilter[1];
                            filterArray[0] = new InputFilter.LengthFilter(4);
                            Txt.setFilters(filterArray);
                            Txt.setText("");
                            Txt.setHint("کد اعتبار سنجی");
                            ((Button) findViewById(R.id.btnActRegister)).setText("ادامــه");
                            Txt.setInputType(InputType.TYPE_CLASS_NUMBER);
                            Timer = new CountDownTimer(180000, 1000) {
                                @SuppressLint({"SetTextI18n", "DefaultLocale"})
                                @Override
                                public void onTick(long l) {
                                    int seconds = (int) (l / 1000) % 60;
                                    int minutes = (int) ((l / (1000 * 60)) % 60);
                                    Lbls.get(2).setClickable(true);
                                    Lbls.get(2).setText(" لطفا منتظر دریافت کد اعتبار سنجی بمانید " + String.format("%d:%d", minutes, seconds));
                                }

                                @Override
                                public void onFinish() {
                                    Lbls.get(2).setText("ارسال مجدد کد اعتبار سنجی ");
                                    Lbls.get(2).setClickable(true);
                                    Timer.cancel();
                                }
                            }.start();
                        }

                        @Override
                        public void onError(Throwable e) {
                            Loading.setVisibility(View.GONE);
                            Toasty.warning(ActRegister.this, "خطای سامانه مجددا تلاش کنید", Toasty.LENGTH_LONG, true).show();

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } else {
            Loading.setVisibility(View.GONE);
            Toasty.warning(this, "عدم دسترسی به اینترنت", Toasty.LENGTH_LONG, true).show();
        }
    }

    @OnClick(R.id.lblCounterActRegister)
    public void ClickCounter() {
        Register();
    }

    @OnClick(R.id.RowNoPhoneActRegister)
    public void ClickNoPhone() {
        EasyPreference.with(this).addString("register", "1");
        startActivity(new Intent(this, ActMain.class));
        finish();
    }

    private boolean CheckPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100:
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "عدم اجازه برای گرفتن پیامک کد اعتبار سنجی", Toast.LENGTH_SHORT).show();
                } else {
                    Register();
                }
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void SetMessage(EvMessage event) {
        Txt.setText(event.GetMessage());
    }
}
