package ntk.android.academy.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.zxing.WriterException;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
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
import ntk.android.academy.adapter.AdArticle;
import ntk.android.academy.adapter.AdComment;
import ntk.android.academy.adapter.AdTab;
import ntk.android.academy.config.ConfigRestHeader;
import ntk.android.academy.config.ConfigStaticValue;
import ntk.android.academy.event.EvHtmlBody;
import ntk.android.academy.utill.AppUtill;
import ntk.android.academy.utill.EasyPreference;
import ntk.android.academy.utill.FontManager;
import ntk.base.api.article.interfase.IArticle;
import ntk.base.api.article.model.ArticleCommentAddRequest;
import ntk.base.api.article.model.ArticleCommentListRequest;
import ntk.base.api.article.model.ArticleCommentResponse;
import ntk.base.api.article.model.ArticleContentCategoryListRequest;
import ntk.base.api.article.model.ArticleContentFavoriteAddRequest;
import ntk.base.api.article.model.ArticleContentFavoriteAddResponse;
import ntk.base.api.article.model.ArticleContentFavoriteRemoveRequest;
import ntk.base.api.article.model.ArticleContentFavoriteRemoveResponse;
import ntk.base.api.article.entity.ArticleContentOtherInfo;
import ntk.base.api.article.model.ArticleContentOtherInfoRequest;
import ntk.base.api.article.model.ArticleContentOtherInfoResponse;
import ntk.base.api.article.model.ArticleContentResponse;
import ntk.base.api.article.model.ArticleContentSimilarListRequest;
import ntk.base.api.article.model.ArticleContentViewRequest;
import ntk.base.api.article.model.ArticleTagRequest;
import ntk.base.api.article.model.ArticleTagResponse;
import ntk.base.api.core.entity.CoreMain;
import ntk.base.api.baseModel.ErrorException;
import ntk.base.api.baseModel.Filters;
import ntk.base.api.utill.RetrofitManager;

public class ActDetail extends AppCompatActivity {

    @BindView(R.id.progressActDetail)
    ProgressBar Progress;

    @BindView(R.id.rowProgressActDetail)
    LinearLayout Loading;

    @BindViews({R.id.lblTitleActDetail,
            R.id.lblNameCommandActDetail,
            R.id.lblKeySeenActDetail,
            R.id.lblValueSeenActDetail,
            R.id.lblPhotoExtraActDetail,
            R.id.lblCalActDetail,
            R.id.lblTimerOne,
            R.id.lblTimerTwo,
            R.id.lblTimerThree,
            R.id.lblTimerFour,
            R.id.lblTimerFive,
            R.id.lblTimerSix,
            R.id.lblMenuActDetail,
            R.id.lblMenuTwoActDetail,
            R.id.lblCommentActDetail,
            R.id.lblProgressActDetail
    })
    List<TextView> Lbls;

    @BindView(R.id.imgHeaderActDetail)
    ImageView ImgHeader;

    @BindView(R.id.recyclerMenuActDetail)
    RecyclerView RvSimilarArticle;

    @BindView(R.id.recyclerMenuTwoActDetail)
    RecyclerView RvSimilarCategory;

    @BindView(R.id.WebViewActDetail)
    WebView webView;

    @BindView(R.id.recyclerTabActDetail)
    RecyclerView RvTab;

    @BindView(R.id.recyclerCommentActDetail)
    RecyclerView RvComment;

    @BindView(R.id.ratingBarActDetail)
    RatingBar Rate;

    @BindView(R.id.PageActDetail)
    LinearLayout Page;


    @BindView(R.id.mainLayoutActDetail)
    CoordinatorLayout layout;

    private String RequestStr;
    private ArticleContentResponse model;
    private ArticleContentOtherInfoResponse Info;
    private ArticleContentViewRequest Request;
    private ConfigStaticValue configStaticValue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_detail);
        ButterKnife.bind(this);
        configStaticValue = new ConfigStaticValue(this);
        init();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void init() {
        for (TextView tv : Lbls) {
            tv.setTypeface(FontManager.GetTypeface(this, FontManager.IranSans));
        }
        Progress.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        RvTab.setHasFixedSize(true);
        RvTab.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
        RequestStr = getIntent().getExtras().getString("Request");
        Request = new Gson().fromJson(RequestStr, ArticleContentViewRequest.class);
        HandelDataContent(Request);
        Loading.setVisibility(View.VISIBLE);

        RvComment.setHasFixedSize(true);
        RvComment.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        RvSimilarArticle.setHasFixedSize(true);
        RvSimilarArticle.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));

        RvSimilarCategory.setHasFixedSize(true);
        RvSimilarCategory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));

        Rate.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (!fromUser) return;

                if (AppUtill.isNetworkAvailable(ActDetail.this)) {
                    ArticleContentViewRequest request = new ArticleContentViewRequest();
                    request.Id = Request.Id;
                    request.ActionClientOrder = 55;
                    if (rating == 0.5) {
                        request.ScorePercent = 10;
                    }
                    if (rating == 1) {
                        request.ScorePercent = 20;
                    }
                    if (rating == 1.5) {
                        request.ScorePercent = 30;
                    }
                    if (rating == 2) {
                        request.ScorePercent = 40;
                    }
                    if (rating == 2.5) {
                        request.ScorePercent = 50;
                    }
                    if (rating == 3) {
                        request.ScorePercent = 60;
                    }
                    if (rating == 3.5) {
                        request.ScorePercent = 70;
                    }
                    if (rating == 4) {
                        request.ScorePercent = 80;
                    }
                    if (rating == 4.5) {
                        request.ScorePercent = 90;
                    }
                    if (rating == 5) {
                        request.ScorePercent = 100;
                    }
                    RetrofitManager manager = new RetrofitManager(ActDetail.this);
                    IArticle iArticle = manager.getRetrofitUnCached(new ConfigStaticValue(ActDetail.this).GetApiBaseUrl()).create(IArticle.class);
                    Map<String, String> headers = new ConfigRestHeader().GetHeaders(ActDetail.this);

                    Observable<ArticleContentResponse> Call = iArticle.GetContentView(headers, request);
                    Call.observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe(new Observer<ArticleContentResponse>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onNext(ArticleContentResponse response) {
                                    Loading.setVisibility(View.GONE);
                                    if (response.IsSuccess) {
                                        Toasty.success(ActDetail.this, "نظر شمابا موفقیت ثبت گردید").show();
                                    } else {
                                        Toasty.warning(ActDetail.this, response.ErrorMessage).show();
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Loading.setVisibility(View.GONE);
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
                    Loading.setVisibility(View.GONE);
                    Snackbar.make(layout, "عدم دسترسی به اینترنت", Snackbar.LENGTH_INDEFINITE).setAction("تلاش مجددا", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            init();
                        }
                    }).show();
                }
            }
        });
    }


    private void HandelDataContent(ArticleContentViewRequest request) {
        if (AppUtill.isNetworkAvailable(this)) {
            RetrofitManager retro = new RetrofitManager(this);
            IArticle iArticle = retro.getRetrofitUnCached(configStaticValue.GetApiBaseUrl()).create(IArticle.class);
            Map<String, String> headers = new ConfigRestHeader().GetHeaders(this);

            Observable<ArticleContentResponse> call = iArticle.GetContentView(headers, request);
            call.observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<ArticleContentResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(ArticleContentResponse articleContentResponse) {
                            model = articleContentResponse;
                            if (model.Item != null) {
                                SetData(model);
                                HandelSimilary(Request.Id);
                                HandelSimilaryCategory(Request.Id);
                                if (Request.Id > 0) {
                                    HandelDataContentOtherInfo(Request.Id);
                                    HandelDataComment(Request.Id);
                                }
                            }
                            Loading.setVisibility(View.GONE);
                            Page.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError(Throwable e) {
                            Loading.setVisibility(View.GONE);
                            Toasty.warning(ActDetail.this, "خطای سامانه", Toasty.LENGTH_LONG, true).show();
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } else {
            Snackbar.make(layout, "عدم دسترسی به اینترنت", Snackbar.LENGTH_INDEFINITE).setAction("تلاش مجددا", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    init();
                }
            }).show();
        }
    }

    private void HandelSimilary(long id) {
        if (AppUtill.isNetworkAvailable(this)) {
            RetrofitManager manager = new RetrofitManager(this);
            IArticle iArticle = manager.getCachedRetrofit(new ConfigStaticValue(this).GetApiBaseUrl()).create(IArticle.class);
            Map<String, String> headers = new ConfigRestHeader().GetHeaders(this);

            ArticleContentSimilarListRequest request = new ArticleContentSimilarListRequest();
            request.LinkContetnId = id;

            Observable<ArticleContentResponse> call = iArticle.GetContentSimilarList(headers, request);
            call.observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<ArticleContentResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(ArticleContentResponse response) {
                            if (response.ListItems.size() == 0) {
                                findViewById(R.id.RowSimilaryActDetail).setVisibility(View.GONE);
                            } else {
                                AdArticle adapter = new AdArticle(ActDetail.this, response.ListItems);
                                RvSimilarArticle.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                findViewById(R.id.RowSimilaryActDetail).setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } else {
            Snackbar.make(layout, "عدم دسترسی به اینترنت", Snackbar.LENGTH_INDEFINITE).setAction("تلاش مجددا", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    init();
                }
            }).show();
        }
    }

    private void HandelSimilaryCategory(long id) {
        if (AppUtill.isNetworkAvailable(this)) {
            RetrofitManager manager = new RetrofitManager(this);
            IArticle iArticle = manager.getCachedRetrofit(new ConfigStaticValue(this).GetApiBaseUrl()).create(IArticle.class);
            Map<String, String> headers = new ConfigRestHeader().GetHeaders(this);

            ArticleContentCategoryListRequest request = new ArticleContentCategoryListRequest();
            request.LinkContetnId = id;

            Observable<ArticleContentResponse> call = iArticle.GetContentCategoryList(headers, request);
            call.observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<ArticleContentResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(ArticleContentResponse response) {
                            if (response.ListItems.size() == 0) {
                                findViewById(R.id.RowSimilaryCategoryActDetail).setVisibility(View.GONE);
                            } else {
                                findViewById(R.id.RowSimilaryCategoryActDetail).setVisibility(View.VISIBLE);
                                AdArticle adapter = new AdArticle(ActDetail.this, response.ListItems);
                                RvSimilarCategory.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } else {
            Snackbar.make(layout, "عدم دسترسی به اینترنت", Snackbar.LENGTH_INDEFINITE).setAction("تلاش مجددا", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    init();
                }
            }).show();
        }
    }

    private void HandelDataComment(long ContentId) {
        if (AppUtill.isNetworkAvailable(this)) {
            List<Filters> filters = new ArrayList<>();
            ArticleCommentListRequest Request = new ArticleCommentListRequest();
            Filters f = new Filters();
            f.PropertyName = "LinkContentId";
            f.IntValue1 = ContentId;
            filters.add(f);
            Request.filters = filters;
            RetrofitManager retro = new RetrofitManager(this);
            IArticle iArticle = retro.getRetrofitUnCached(configStaticValue.GetApiBaseUrl()).create(IArticle.class);
            Map<String, String> headers = new ConfigRestHeader().GetHeaders(this);
            Observable<ArticleCommentResponse> call = iArticle.GetCommentList(headers, Request);
            call.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ArticleCommentResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(ArticleCommentResponse model) {
                            if (model.IsSuccess) {
                                if (model.ListItems.size() == 0) {
                                    findViewById(R.id.RowCommentActDetail).setVisibility(View.GONE);
                                } else {
                                    AdComment adapter = new AdComment(ActDetail.this, model.ListItems);
                                    RvComment.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                    findViewById(R.id.RowCommentActDetail).setVisibility(View.VISIBLE);
                                }
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Toasty.warning(ActDetail.this, "خطای سامانه", Toasty.LENGTH_LONG, true).show();
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } else {
            Snackbar.make(layout, "عدم دسترسی به اینترنت", Snackbar.LENGTH_INDEFINITE).setAction("تلاش مجددا", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    init();
                }
            }).show();
        }
    }

    private void HandelDataContentOtherInfo(long ContentId) {

        List<Filters> filters = new ArrayList<>();
        ArticleContentOtherInfoRequest Request = new ArticleContentOtherInfoRequest();
        Filters f = new Filters();
        f.PropertyName = "LinkContentId";
        f.IntValue1 = ContentId;
        filters.add(f);
        Request.filters = filters;
        RetrofitManager retro = new RetrofitManager(this);
        IArticle iArticle = retro.getRetrofitUnCached(configStaticValue.GetApiBaseUrl()).create(IArticle.class);
        Map<String, String> headers = new ConfigRestHeader().GetHeaders(this);


        Observable<ArticleContentOtherInfoResponse> call = iArticle.GetContentOtherInfoList(headers, Request);
        call.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<ArticleContentOtherInfoResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ArticleContentOtherInfoResponse articleContentOtherInfoResponse) {
                        SetDataOtherinfo(articleContentOtherInfoResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toasty.warning(ActDetail.this, "خطای سامانه", Toasty.LENGTH_LONG, true).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    private void SetDataOtherinfo(ArticleContentOtherInfoResponse model) {
        Info = model;
        if (model.ListItems == null || model.ListItems.size() == 0) {
            findViewById(R.id.RowTimeActDetail).setVisibility(View.GONE);
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            p.weight = 3;
            return;
        }
        findViewById(R.id.RowTimeActDetail).setVisibility(View.GONE);
        List<ArticleContentOtherInfo> Info = new ArrayList<>();
        ArticleContentOtherInfo i = new ArticleContentOtherInfo();
        i.Title = "طرز تهیه";
        i.TypeId = 0;
        i.HtmlBody = this.model.Item.Body;
        Info.add(i);

        for (ArticleContentOtherInfo ai : model.ListItems) {
            switch (ai.TypeId) {
                case 21:
                    Lbls.get(7).setText(ai.Title);
                    ai.HtmlBody = ai.HtmlBody.replace("<p>", "");
                    ai.HtmlBody = ai.HtmlBody.replace("</p>", "");
                    Lbls.get(6).setText(Html.fromHtml(ai.HtmlBody));
                    findViewById(R.id.RowTimeActDetail).setVisibility(View.VISIBLE);
                    break;
                case 22:
                    Lbls.get(9).setText(ai.Title);
                    ai.HtmlBody = ai.HtmlBody.replace("<p>", "");
                    ai.HtmlBody = ai.HtmlBody.replace("</p>", "");
                    Lbls.get(8).setText(Html.fromHtml(ai.HtmlBody));
                    findViewById(R.id.RowTimeActDetail).setVisibility(View.VISIBLE);
                    break;
                case 23:
                    Lbls.get(11).setText(ai.Title);
                    ai.HtmlBody = ai.HtmlBody.replace("<p>", "");
                    ai.HtmlBody = ai.HtmlBody.replace("</p>", "");
                    Lbls.get(10).setText(Html.fromHtml(ai.HtmlBody));
                    findViewById(R.id.RowTimeActDetail).setVisibility(View.VISIBLE);
                    break;
                default:
                    Info.add(ai);
                    break;
            }
        }
        AdTab adapter = new AdTab(ActDetail.this, Info);
        RvTab.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void SetData(ArticleContentResponse model) {
        double rating = 0.0;
        int sumClick = model.Item.ScoreSumClick;
        if (model.Item.ScoreSumClick == 0) sumClick = 1;
        if (model.Item.ScoreSumPercent / sumClick > 0 && model.Item.ScoreSumPercent / sumClick <= 10) {
            rating = 0.5;
        } else if (model.Item.ScoreSumPercent / sumClick > 10 && model.Item.ScoreSumPercent / sumClick <= 20) {
            rating = 1.0;
        } else if (model.Item.ScoreSumPercent / sumClick > 20 && model.Item.ScoreSumPercent / sumClick <= 30) {
            rating = 1.5;
        } else if (model.Item.ScoreSumPercent / sumClick > 30 && model.Item.ScoreSumPercent / sumClick <= 40) {
            rating = 2.0;
        } else if (model.Item.ScoreSumPercent / sumClick > 40 && model.Item.ScoreSumPercent / sumClick <= 50) {
            rating = 2.5;
        } else if (model.Item.ScoreSumPercent / sumClick > 50 && model.Item.ScoreSumPercent / sumClick <= 60) {
            rating = 3.0;
        } else if (model.Item.ScoreSumPercent / sumClick > 60 && model.Item.ScoreSumPercent / sumClick <= 70) {
            rating = 3.5;
        } else if (model.Item.ScoreSumPercent / sumClick > 70 && model.Item.ScoreSumPercent / sumClick <= 80) {
            rating = 4.0;
        } else if (model.Item.ScoreSumPercent / sumClick > 80 && model.Item.ScoreSumPercent / sumClick <= 90) {
            rating = 4.5;
        } else if (model.Item.ScoreSumPercent / sumClick > 90) {
            rating = 5.0;
        }
        Rate.setRating((float) rating);
        ImageLoader.getInstance().displayImage(model.Item.imageSrc, ImgHeader);
        Lbls.get(0).setText(model.Item.Title);
        Lbls.get(1).setText(model.Item.Title);
        Lbls.get(3).setText(String.valueOf(model.Item.viewCount));
        if (model.Item.Favorited) {
            ((ImageView) findViewById(R.id.imgHeartActDetail)).setImageResource(R.drawable.ic_fav_full);
        }
    }

    @OnClick(R.id.imgBackActDetail)
    public void ClickBack() {
        finish();
    }

    @OnClick(R.id.RowGalleryActDetail)
    public void ClickGalley() {
        if (model.Item.LinkFileIdsSrc != null && model.Item.LinkFileIdsSrc.size() != 0) {
            String request = "";
            for (String s : model.Item.LinkFileIdsSrc) {
                if (!request.equals("")) {
                    request = request + "@";
                }
                request = request + s;
            }
            Intent intent = new Intent(this, ActPhotoGallery.class);
            intent.putExtra("Request", request);
            startActivity(intent);
        }
    }

    @Subscribe
    public void EventHtmlBody(EvHtmlBody event) {
        webView.loadData("<html dir=\"rtl\" lang=\"\"><body>" + event.GetMessage() + "</body></html>", "text/html; charset=utf-8", "UTF-8");
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

    @OnClick(R.id.imgCommentActDetail)
    public void ClickCommentAdd() {
        if (AppUtill.isNetworkAvailable(this)) {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCanceledOnTouchOutside(true);
            Window window = dialog.getWindow();
            window.setLayout(LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.CENTER);
            dialog.setContentView(R.layout.dialog_comment_add);

            TextView Lbl = dialog.findViewById(R.id.lblTitleDialogAddComment);
            Lbl.setTypeface(FontManager.GetTypeface(this, FontManager.IranSans));

            EditText Txt[] = new EditText[2];

            Txt[0] = dialog.findViewById(R.id.txtNameDialogAddComment);
            Txt[0].setTypeface(FontManager.GetTypeface(this, FontManager.IranSans));

            Txt[1] = dialog.findViewById(R.id.txtContentDialogAddComment);
            Txt[1].setTypeface(FontManager.GetTypeface(this, FontManager.IranSans));

            Button Btn = dialog.findViewById(R.id.btnSubmitDialogCommentAdd);
            Btn.setTypeface(FontManager.GetTypeface(this, FontManager.IranSans));

            Btn.setOnClickListener(v -> {
                if (Txt[0].getText().toString().isEmpty()) {
                    Toast.makeText(ActDetail.this, "لطفا مقادیر را وارد نمایید", Toast.LENGTH_SHORT).show();
                } else {
                    if (Txt[1].getText().toString().isEmpty()) {
                        Toast.makeText(ActDetail.this, "لطفا مقادیر را وارد نمایید", Toast.LENGTH_SHORT).show();
                    } else {
                        ArticleCommentAddRequest add = new ArticleCommentAddRequest();
                        add.Writer = Txt[0].getText().toString();
                        add.Comment = Txt[1].getText().toString();
                        add.LinkContentId = Request.Id;
                        RetrofitManager retro = new RetrofitManager(this);
                        IArticle iArticle = retro.getRetrofitUnCached(configStaticValue.GetApiBaseUrl()).create(IArticle.class);
                        Map<String, String> headers = new ConfigRestHeader().GetHeaders(this);


                        Observable<ArticleCommentResponse> call = iArticle.SetComment(headers, add);
                        call.subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Observer<ErrorException>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onNext(ErrorException e) {
                                        if (e.IsSuccess) {
                                            HandelDataComment(Request.Id);
                                            dialog.dismiss();
                                            Toasty.success(ActDetail.this, "نظر شما با موفقیت ثبت شد").show();
                                        } else {
                                            Toasty.warning(ActDetail.this, "لطفا مجددا تلاش کنید").show();
                                        }
                                    }

                                    @Override
                                    public void onError(Throwable e) {
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
                    }
                }
            });
            dialog.show();
        } else {
            Snackbar.make(layout, "عدم دسترسی به اینترنت", Snackbar.LENGTH_INDEFINITE).setAction("تلاش مجددا", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    init();
                }
            }).show();
        }
    }

    @OnClick(R.id.imgFavActDetail)
    public void ClickFav() {
        if (!model.Item.Favorited) {
            Fav();
        } else {
            UnFav();
        }
    }

    private void UnFav() {
        if (AppUtill.isNetworkAvailable(this)) {
            RetrofitManager retro = new RetrofitManager(this);
            IArticle iArticle = retro.getRetrofitUnCached(configStaticValue.GetApiBaseUrl()).create(IArticle.class);
            Map<String, String> headers = new ConfigRestHeader().GetHeaders(this);

            ArticleContentFavoriteRemoveRequest add = new ArticleContentFavoriteRemoveRequest();
            add.Id = model.Item.Id;

            Observable<ArticleContentFavoriteRemoveResponse> Call = iArticle.SetContentFavoriteRemove(headers, add);
            Call.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ArticleContentFavoriteRemoveResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(ArticleContentFavoriteRemoveResponse e) {
                            if (e.IsSuccess) {
                                Toasty.success(ActDetail.this, "با موفقیت ثبت شد").show();
                                model.Item.Favorited = !model.Item.Favorited;
                                if (model.Item.Favorited) {
                                    ((ImageView) findViewById(R.id.imgHeartActDetail)).setImageResource(R.drawable.ic_fav_full);
                                } else {
                                    ((ImageView) findViewById(R.id.imgHeartActDetail)).setImageResource(R.drawable.ic_fav);
                                }
                            } else {
                                Toasty.error(ActDetail.this, e.ErrorMessage, Toast.LENGTH_LONG, true).show();
                            }

                        }

                        @Override
                        public void onError(Throwable e) {
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
            Snackbar.make(layout, "عدم دسترسی به اینترنت", Snackbar.LENGTH_INDEFINITE).setAction("تلاش مجددا", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    init();
                }
            }).show();
        }
    }

    private void Fav() {
        if (AppUtill.isNetworkAvailable(this)) {
            RetrofitManager retro = new RetrofitManager(this);
            IArticle iArticle = retro.getRetrofitUnCached(configStaticValue.GetApiBaseUrl()).create(IArticle.class);
            Map<String, String> headers = new ConfigRestHeader().GetHeaders(this);

            ArticleContentFavoriteAddRequest add = new ArticleContentFavoriteAddRequest();
            add.Id = model.Item.Id;

            Observable<ArticleContentFavoriteAddResponse> Call = iArticle.SetContentFavoriteAdd(headers, add);
            Call.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<ArticleContentFavoriteAddResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(ArticleContentFavoriteAddResponse e) {
                            if (e.IsSuccess) {
                                Toasty.success(ActDetail.this, "با موفقیت ثبت شد").show();
                                model.Item.Favorited = !model.Item.Favorited;
                                if (model.Item.Favorited) {
                                    ((ImageView) findViewById(R.id.imgHeartActDetail)).setImageResource(R.drawable.ic_fav_full);
                                } else {
                                    ((ImageView) findViewById(R.id.imgHeartActDetail)).setImageResource(R.drawable.ic_fav);
                                }
                            } else {
                                Toasty.error(ActDetail.this, e.ErrorMessage, Toast.LENGTH_LONG, true).show();
                            }

                        }

                        @Override
                        public void onError(Throwable e) {
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
            Snackbar.make(layout, "عدم دسترسی به اینترنت", Snackbar.LENGTH_INDEFINITE).setAction("تلاش مجددا", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    init();
                }
            }).show();
        }
    }

    @OnClick(R.id.imgShareActDetail)
    public void ClickShare() {
        String st = EasyPreference.with(this).getString("configapp", "");
        CoreMain mcr = new Gson().fromJson(st, CoreMain.class);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        String message = model.Item.Title + "\n" + model.Item.description + "\n";
        if (model.Item.Body != null) {
            message = message + Html.fromHtml(model.Item.Body
                    .replace("<p>", "")
                    .replace("</p>", ""));
        }
        shareIntent.putExtra(Intent.EXTRA_TEXT, message + "\n\n\n" + this.getString(R.string.app_name) + "\n" + "لینک دانلود:" + "\n" + mcr.AppUrl);
        shareIntent.setType("text/txt");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        this.startActivity(Intent.createChooser(shareIntent, "به اشتراک گزاری با...."));
    }

    @OnClick(R.id.playActDetail)
    public void onPlayClick() {
        Toasty.warning(ActDetail.this, "موردی یافت نشد").show();
    }

    @OnClick(R.id.calActDetail)
    public void onCalClick() {
        Toasty.warning(ActDetail.this, "موردی یافت نشد").show();
    }
}
