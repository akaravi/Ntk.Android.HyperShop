package ntk.android.academy.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.codekidlabs.storagechooser.StorageChooser;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.tedpark.tedpermission.rx2.TedRx2Permission;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
import ntk.android.academy.adapter.AdAttach;
import ntk.android.academy.adapter.AdSpinner;
import ntk.android.academy.config.ConfigRestHeader;
import ntk.android.academy.config.ConfigStaticValue;
import ntk.android.academy.event.EvRemoveAttach;
import ntk.android.academy.utill.AppUtill;
import ntk.android.academy.utill.EasyPreference;
import ntk.android.academy.utill.FontManager;
import ntk.android.academy.utill.Regex;
import ntk.base.api.article.interfase.IArticle;
import ntk.base.api.file.interfase.IFile;
import ntk.base.api.ticket.interfase.ITicket;
import ntk.base.api.ticket.entity.TicketingDepartemen;
import ntk.base.api.ticket.model.TicketingDepartemenList;
import ntk.base.api.ticket.model.TicketingSubmitRequest;
import ntk.base.api.ticket.model.TicketingSubmitResponse;
import ntk.base.api.utill.RetrofitManager;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ActSendTicket extends AppCompatActivity {

    @BindViews({R.id.SpinnerService,
            R.id.SpinnerState})
    List<Spinner> spinners;

    @BindViews({R.id.lblTitleActSendTicket,
            R.id.lblImportantActSendTicket,
            R.id.lblServiceActSendTicket})
    List<TextView> Lbls;

    @BindViews({R.id.txtSubjectActSendTicket,
            R.id.txtMessageActSendTicket,
            R.id.txtNameFamilyActSendTicket,
            R.id.txtPhoneNumberActSendTicket,
            R.id.txtEmailActSendTicket})
    List<EditText> Txts;

    @BindViews({R.id.inputSubjectActSendTicket,
            R.id.inputMessageActSendTicket,
            R.id.inputNameFamilytActSendTicket,
            R.id.inputPhoneNumberActSendTicket,
            R.id.inputEmailtActSendTicket})
    List<TextInputLayout> Inputs;

    @BindView(R.id.btnSubmitActSendTicket)
    Button Btn;

    @BindView(R.id.RecyclerAttach)
    RecyclerView Rv;

    @BindView(R.id.progressAttachActSendTicket)
    ProgressBar progressBar;

    @BindView(R.id.mainLayoutActSendTicket)
    CoordinatorLayout layout;

    private TicketingSubmitRequest request = new TicketingSubmitRequest();
    private List<String> attaches = new ArrayList<>();
    private AdAttach adapter;
    String linkFileIds = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_send_ticket);
        ButterKnife.bind(this);
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void init() {
        Lbls.get(0).setTypeface(FontManager.GetTypeface(this, FontManager.IranSans));
        Lbls.get(1).setTypeface(FontManager.GetTypeface(this, FontManager.IranSans));
        Lbls.get(2).setTypeface(FontManager.GetTypeface(this, FontManager.IranSans));

        Inputs.get(0).setTypeface(FontManager.GetTypeface(this, FontManager.IranSans));
        Inputs.get(1).setTypeface(FontManager.GetTypeface(this, FontManager.IranSans));
        Inputs.get(2).setTypeface(FontManager.GetTypeface(this, FontManager.IranSans));
        Inputs.get(3).setTypeface(FontManager.GetTypeface(this, FontManager.IranSans));
        Inputs.get(4).setTypeface(FontManager.GetTypeface(this, FontManager.IranSans));

        Txts.get(0).setTypeface(FontManager.GetTypeface(this, FontManager.IranSans));
        Txts.get(1).setTypeface(FontManager.GetTypeface(this, FontManager.IranSans));
        Txts.get(2).setTypeface(FontManager.GetTypeface(this, FontManager.IranSans));
        Txts.get(2).setText(EasyPreference.with(this).getString("NameFamily", ""));
        Txts.get(3).setTypeface(FontManager.GetTypeface(this, FontManager.IranSans));
        Txts.get(3).setText(EasyPreference.with(this).getString("PhoneNumber", ""));
        Txts.get(4).setTypeface(FontManager.GetTypeface(this, FontManager.IranSans));
        Txts.get(4).setText(EasyPreference.with(this).getString("Email", ""));

        Btn.setTypeface(FontManager.GetTypeface(this, FontManager.IranSans));

        Rv.setHasFixedSize(true);
        Rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new AdAttach(this, attaches);
        Rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        AdSpinner<String> adapter_state = new AdSpinner<>(this, R.layout.spinner_item, new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.StateTicket))));
        spinners.get(1).setAdapter(adapter_state);
        spinners.get(1).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                request.Priority = (position + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        RetrofitManager retro = new RetrofitManager(this);
        ITicket iTicket = retro.getRetrofitUnCached(new ConfigStaticValue(this).GetApiBaseUrl()).create(ITicket.class);
        Map<String, String> headers = new ConfigRestHeader().GetHeaders(this);
        Observable<TicketingDepartemenList> Call = iTicket.GetTicketDepartman(headers);
        Call.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<TicketingDepartemenList>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(TicketingDepartemenList model) {
                        List<String> list = new ArrayList<>();
                        for (TicketingDepartemen td : model.ListItems) {
                            list.add(td.Title);
                            AdSpinner<String> adapter_dpartman = new AdSpinner<>(ActSendTicket.this, R.layout.spinner_item, list);
                            spinners.get(0).setAdapter(adapter_dpartman);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toasty.warning(ActSendTicket.this, "خطای سامانه", Toasty.LENGTH_LONG, true).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @OnClick(R.id.btnSubmitActSendTicket)
    public void ClickSubmit() {
        if (Txts.get(0).getText().toString().isEmpty()) {
            YoYo.with(Techniques.Tada).duration(700).playOn(Txts.get(0));
        } else {
            if (Txts.get(1).getText().toString().isEmpty()) {
                YoYo.with(Techniques.Tada).duration(700).playOn(Txts.get(1));
            } else {
                if (Txts.get(2).getText().toString().isEmpty()) {
                    YoYo.with(Techniques.Tada).duration(700).playOn(Txts.get(2));
                } else {
                    EasyPreference.with(this).addString("NameFamily", Txts.get(2).getText().toString());
                    if (Txts.get(3).getText().toString().isEmpty()) {
                        YoYo.with(Techniques.Tada).duration(700).playOn(Txts.get(3));
                    } else {
                        EasyPreference.with(this).addString("PhoneNumber", Txts.get(3).getText().toString());
                        if (Txts.get(4).getText().toString().isEmpty()) {
                            YoYo.with(Techniques.Tada).duration(700).playOn(Txts.get(4));
                        } else {
                            EasyPreference.with(this).addString("Email", Txts.get(4).getText().toString());
                            if (Regex.ValidateEmail(Txts.get(4).getText().toString())) {
                                if (AppUtill.isNetworkAvailable(this)) {
                                    request.Email = Txts.get(4).getText().toString();
                                    request.PhoneNo = Txts.get(3).getText().toString();
                                    request.Name = Txts.get(2).getText().toString();
                                    request.HtmlBody = Txts.get(1).getText().toString();
                                    request.Title = Txts.get(0).getText().toString();
                                    //request.uploadName = attaches;
                                    request.LinkFileIds = linkFileIds;

                                    RetrofitManager retro = new RetrofitManager(this);
                                    Map<String, String> headers = new ConfigRestHeader().GetHeaders(this);
                                    ITicket iTicket = retro.getRetrofitUnCached(new ConfigStaticValue(this).GetApiBaseUrl()).create(ITicket.class);
                                    Observable<TicketingSubmitResponse> Call = iTicket.SetTicketSubmit(headers, request);
                                    Call.observeOn(AndroidSchedulers.mainThread())
                                            .subscribeOn(Schedulers.io())
                                            .subscribe(new Observer<TicketingSubmitResponse>() {
                                                @Override
                                                public void onSubscribe(Disposable d) {

                                                }

                                                @Override
                                                public void onNext(TicketingSubmitResponse model) {
                                                    Toasty.info(ActSendTicket.this, model.Item.virtual_Departemen.DefaultAnswerBody, Toasty.LENGTH_LONG, true).show();
                                                    finish();
                                                }

                                                @Override
                                                public void onError(Throwable e) {
                                                    Toasty.warning(ActSendTicket.this, "خطای سامانه", Toasty.LENGTH_LONG, true).show();
                                                }

                                                @Override
                                                public void onComplete() {

                                                }
                                            });
                                } else {
                                    Toasty.warning(this, "عدم دسترسی به اینترنت", Toasty.LENGTH_LONG, true).show();
                                }
                            } else {
                                Toasty.warning(this, "آدرس پست الکترونیکی صحیح نمیباشد", Toasty.LENGTH_LONG, true).show();
                            }
                        }
                    }
                }
            }
        }
    }

    @OnClick(R.id.imgBackActSendTicket)
    public void Clickback() {
        finish();
    }

    @SuppressLint("CheckResult")
    @OnClick(R.id.RippleAttachActSendTicket)
    public void ClickAttach() {
        if (CheckPermission()) {
            TedRx2Permission.with(this)
                    .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                    .request()
                    .subscribe(tedPermissionResult -> {
                        if (tedPermissionResult.isGranted()) {
                            StorageChooser.Theme theme = new StorageChooser.Theme(getApplicationContext());
                            theme.setScheme(getResources().getIntArray(R.array.paranoid_theme));
                            StorageChooser chooser = new StorageChooser.Builder()
                                    .withActivity(this)
                                    .allowCustomPath(true)
                                    .setType(StorageChooser.FILE_PICKER)
                                    .disableMultiSelect()
                                    .setTheme(theme)
                                    .withMemoryBar(true)
                                    .withFragmentManager(getFragmentManager())
                                    .build();
                            chooser.show();
                            chooser.setOnSelectListener(this::UploadFile);
                            progressBar.setVisibility(View.VISIBLE);
                            Btn.setVisibility(View.GONE);
                        } else {
                        }
                    }, throwable -> {
                    });
        } else {
            ActivityCompat.requestPermissions(ActSendTicket.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 220);
        }
    }

    private boolean CheckPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            return checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }
    }

    private void UploadFile(String s) {
        UploadFileToServer(s);
        Map<String, String> headers = new ConfigRestHeader().GetHeaders(this);

        RetrofitManager manager = new RetrofitManager(ActSendTicket.this);
        Observable<String> observable = manager.FileUpload(null, s, headers);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String url) {
                        String[] strs = s.split("/");
                        String FileName = strs[strs.length - 1];
                        attaches.add(FileName + " - " + url);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toasty.warning(ActSendTicket.this, "خطای سامانه", Toasty.LENGTH_LONG, true).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void UploadFileToServer(String url) {
        if (AppUtill.isNetworkAvailable(this)) {
            File file = new File(String.valueOf(Uri.parse(url)));
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            RetrofitManager retro = new RetrofitManager(this);
            Map<String, String> headers = new ConfigRestHeader().GetHeaders(this);
            IFile iFile = retro.getRetrofitUnCached(new ConfigStaticValue(this).GetApiBaseUrl()).create(IFile.class);
            Observable<String> Call = iFile.uploadFileWithPartMap(headers, new HashMap<>(), MultipartBody.Part.createFormData("File", file.getName(), requestFile));
            Call.observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<String>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onNext(String model) {
                            progressBar.setVisibility(View.GONE);
                            Btn.setVisibility(View.VISIBLE);
                            adapter.notifyDataSetChanged();
                            linkFileIds = linkFileIds + model + ",";
                        }

                        @Override
                        public void onError(Throwable e) {
                            progressBar.setVisibility(View.GONE);
                            Btn.setVisibility(View.VISIBLE);
                            Snackbar.make(layout, "خطای سامانه مجددا تلاش کنید", Snackbar.LENGTH_INDEFINITE).setAction("تلاش مجددا", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    init();
                                }
                            }).show();
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } else {
            Btn.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            Toasty.warning(this, "عدم دسترسی به اینترنت", Toasty.LENGTH_LONG, true).show();
        }
    }

    @Subscribe
    public void EventRemove(EvRemoveAttach event) {
        attaches.remove(event.GetPosition());
        adapter.notifyDataSetChanged();
    }
}
