package ntk.android.hypershop.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.PorterDuff;
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
import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
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
import ntk.android.hypershop.R;
import ntk.android.hypershop.adapter.AdArticle;
import ntk.android.hypershop.adapter.AdComment;
import ntk.android.hypershop.adapter.AdTab;
import ntk.android.hypershop.config.ConfigRestHeader;
import ntk.android.hypershop.config.ConfigStaticValue;
import ntk.android.hypershop.event.EvHtmlBody;
import ntk.android.hypershop.utill.AppUtill;
import ntk.android.hypershop.utill.EasyPreference;
import ntk.android.hypershop.utill.FontManager;
import ntk.base.api.article.entity.ArticleContentOtherInfo;
import ntk.base.api.article.interfase.IArticle;
import ntk.base.api.article.model.ArticleCommentAddRequest;
import ntk.base.api.article.model.ArticleCommentListRequest;
import ntk.base.api.article.model.ArticleCommentResponse;
import ntk.base.api.article.model.ArticleContentCategoryListRequest;
import ntk.base.api.article.model.ArticleContentFavoriteAddRequest;
import ntk.base.api.article.model.ArticleContentFavoriteAddResponse;
import ntk.base.api.article.model.ArticleContentFavoriteRemoveRequest;
import ntk.base.api.article.model.ArticleContentFavoriteRemoveResponse;
import ntk.base.api.article.model.ArticleContentOtherInfoRequest;
import ntk.base.api.article.model.ArticleContentOtherInfoResponse;
import ntk.base.api.article.model.ArticleContentResponse;
import ntk.base.api.article.model.ArticleContentSimilarListRequest;
import ntk.base.api.article.model.ArticleContentViewRequest;
import ntk.base.api.baseModel.ErrorException;
import ntk.base.api.baseModel.Filters;
import ntk.base.api.core.entity.CoreMain;
import ntk.base.api.hyperShop.entity.HyperShopContent;
import ntk.base.api.hyperShop.interfase.IHyperShop;
import ntk.base.api.hyperShop.model.HyperShopContentResponse;
import ntk.base.api.hyperShop.model.HyperShopContentViewRequest;
import ntk.base.api.utill.RetrofitManager;

public class ActDetailHyperShop extends AppCompatActivity {

    @BindView(R.id.progressActDetail)
    ProgressBar Progress;

    @BindView(R.id.rowProgressActDetail)
    LinearLayout Loading;

    @BindViews({
            R.id.lblTitleActDetail,
            R.id.lblDescriptionActDetailHyperShop,
            R.id.lblCommentActDetail,
            R.id.lblMenuTwoActDetail,
            R.id.lblProgressActDetail
    })
    List<TextView> Lbls;

    @BindView(R.id.imgHeaderActDetail)
    ImageView ImgHeader;

    @BindView(R.id.recyclerMenuTwoActDetail)
    RecyclerView RvSimilarCategory;

    @BindView(R.id.recyclerCommentActDetail)
    RecyclerView RvComment;

    @BindView(R.id.PageActDetail)
    LinearLayout Page;

    @BindView(R.id.mainLayoutActDetail)
    CoordinatorLayout layout;

    @BindView(R.id.btnOrderActDetailHyperShop)
    Button Btn;

    private String RequestStr;
    private HyperShopContentResponse model;
    private HyperShopContentViewRequest Request;
    private ConfigStaticValue configStaticValue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_detail_hypershop);
        ButterKnife.bind(this);
        configStaticValue = new ConfigStaticValue(this);
        init();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void init() {
        for (TextView tv : Lbls) {
            tv.setTypeface(FontManager.GetTypeface(this, FontManager.IranSans));
        }
        Btn.setTypeface(FontManager.GetTypeface(this, FontManager.IranSans));
        Progress.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
        RequestStr = getIntent().getExtras().getString("Request");
        Request = new Gson().fromJson(RequestStr, HyperShopContentViewRequest.class);
        HandelDataContent(Request);
        Loading.setVisibility(View.VISIBLE);

        RvComment.setHasFixedSize(true);
        RvComment.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        RvSimilarCategory.setHasFixedSize(true);
        RvSimilarCategory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
    }


    private void HandelDataContent(HyperShopContentViewRequest request) {
        if (AppUtill.isNetworkAvailable(this)) {
            RetrofitManager retro = new RetrofitManager(this);
            IHyperShop iHyperShop = retro.getCachedRetrofit(configStaticValue.GetApiBaseUrl()).create(IHyperShop.class);
            Map<String, String> headers = new ConfigRestHeader().GetHeaders(this);

            Observable<HyperShopContentResponse> call = iHyperShop.GetContentView(headers, request);
            call.observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<HyperShopContentResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(HyperShopContentResponse response) {
                            model = response;
                            if (model.Item != null) {
                                SetData(model);
//                                HandelSimilary(Request.Id);
//                                HandelSimilaryCategory(Request.Id);
                            }
                            Loading.setVisibility(View.GONE);
                            Page.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onError(Throwable e) {
                            Loading.setVisibility(View.GONE);
                            Toasty.warning(ActDetailHyperShop.this, "خطای سامانه", Toasty.LENGTH_LONG, true).show();
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
                                AdArticle adapter = new AdArticle(ActDetailHyperShop.this, response.ListItems);
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
                                    AdComment adapter = new AdComment(ActDetailHyperShop.this, model.ListItems);
                                    RvComment.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                    findViewById(R.id.RowCommentActDetail).setVisibility(View.VISIBLE);
                                }
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Toasty.warning(ActDetailHyperShop.this, "خطای سامانه", Toasty.LENGTH_LONG, true).show();
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

    private void SetData(HyperShopContentResponse model) {
        ImageLoader.getInstance().displayImage(model.Item.image, ImgHeader);
        Lbls.get(0).setText(model.Item.name);
        Lbls.get(1).setText(model.Item.memo);
        ((ImageView) findViewById(R.id.imgHeartActDetail)).setImageResource(R.drawable.ic_fav);
        Btn.setText("قیمت: " + model.Item.price + " اضافه کردن به سبد خرید");
    }

    @OnClick(R.id.imgBackActDetail)
    public void ClickBack() {
        finish();
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
                    Toast.makeText(ActDetailHyperShop.this, "لطفا مقادیر را وارد نمایید", Toast.LENGTH_SHORT).show();
                } else {
                    if (Txt[1].getText().toString().isEmpty()) {
                        Toast.makeText(ActDetailHyperShop.this, "لطفا مقادیر را وارد نمایید", Toast.LENGTH_SHORT).show();
                    } else {
//                        ArticleCommentAddRequest add = new ArticleCommentAddRequest();
//                        add.Writer = Txt[0].getText().toString();
//                        add.Comment = Txt[1].getText().toString();
//                        add.LinkContentId = Request.code;
//                        RetrofitManager retro = new RetrofitManager(this);
//                        IArticle iArticle = retro.getRetrofitUnCached(configStaticValue.GetApiBaseUrl()).create(IArticle.class);
//                        Map<String, String> headers = new ConfigRestHeader().GetHeaders(this);
//
//
//                        Observable<ArticleCommentResponse> call = iArticle.SetComment(headers, add);
//                        call.subscribeOn(Schedulers.io())
//                                .observeOn(AndroidSchedulers.mainThread())
//                                .subscribe(new Observer<ErrorException>() {
//                                    @Override
//                                    public void onSubscribe(Disposable d) {
//
//                                    }
//
//                                    @Override
//                                    public void onNext(ErrorException e) {
//                                        if (e.IsSuccess) {
//                                            HandelDataComment(Request.Id);
//                                            dialog.dismiss();
//                                            Toasty.success(ActDetailHyperShop.this, "نظر شما با موفقیت ثبت شد").show();
//                                        } else {
//                                            Toasty.warning(ActDetailHyperShop.this, "لطفا مجددا تلاش کنید").show();
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onError(Throwable e) {
//                                        Snackbar.make(layout, "خطای سامانه مجددا تلاش کنید", Snackbar.LENGTH_INDEFINITE).setAction("تلاش مجددا", new View.OnClickListener() {
//                                            @Override
//                                            public void onClick(View v) {
//                                                init();
//                                            }
//                                        }).show();
//                                    }
//
//                                    @Override
//                                    public void onComplete() {
//
//                                    }
//                                });
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
        if (!model.Item.status) {
//            Fav();
            ((ImageView) findViewById(R.id.imgHeartActDetail)).setImageResource(R.drawable.ic_fav_full);
        } else {
//            UnFav();
            ((ImageView) findViewById(R.id.imgHeartActDetail)).setImageResource(R.drawable.ic_fav);
        }
    }

//    private void UnFav() {
//        if (AppUtill.isNetworkAvailable(this)) {
//            RetrofitManager retro = new RetrofitManager(this);
//            IArticle iArticle = retro.getRetrofitUnCached(configStaticValue.GetApiBaseUrl()).create(IArticle.class);
//            Map<String, String> headers = new ConfigRestHeader().GetHeaders(this);
//
//            ArticleContentFavoriteRemoveRequest add = new ArticleContentFavoriteRemoveRequest();
//            add.Id = model.Item.Id;
//
//            Observable<ArticleContentFavoriteRemoveResponse> Call = iArticle.SetContentFavoriteRemove(headers, add);
//            Call.subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Observer<ArticleContentFavoriteRemoveResponse>() {
//                        @Override
//                        public void onSubscribe(Disposable d) {
//
//                        }
//
//                        @Override
//                        public void onNext(ArticleContentFavoriteRemoveResponse e) {
//                            if (e.IsSuccess) {
//                                Toasty.success(ActDetailHyperShop.this, "با موفقیت ثبت شد").show();
//                                model.Item.Favorited = !model.Item.Favorited;
//                                if (model.Item.Favorited) {
//                                    ((ImageView) findViewById(R.id.imgHeartActDetail)).setImageResource(R.drawable.ic_fav_full);
//                                } else {
//                                    ((ImageView) findViewById(R.id.imgHeartActDetail)).setImageResource(R.drawable.ic_fav);
//                                }
//                            } else {
//                                Toasty.error(ActDetailHyperShop.this, e.ErrorMessage, Toast.LENGTH_LONG, true).show();
//                            }
//
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//                            Snackbar.make(layout, "خطای سامانه مجددا تلاش کنید", Snackbar.LENGTH_INDEFINITE).setAction("تلاش مجددا", new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    init();
//                                }
//                            }).show();
//                        }
//
//                        @Override
//                        public void onComplete() {
//
//                        }
//                    });
//        } else {
//            Snackbar.make(layout, "عدم دسترسی به اینترنت", Snackbar.LENGTH_INDEFINITE).setAction("تلاش مجددا", new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    init();
//                }
//            }).show();
//        }
//    }
//
//    private void Fav() {
//        if (AppUtill.isNetworkAvailable(this)) {
//            RetrofitManager retro = new RetrofitManager(this);
//            IArticle iArticle = retro.getRetrofitUnCached(configStaticValue.GetApiBaseUrl()).create(IArticle.class);
//            Map<String, String> headers = new ConfigRestHeader().GetHeaders(this);
//
//            ArticleContentFavoriteAddRequest add = new ArticleContentFavoriteAddRequest();
//            add.Id = model.Item.Id;
//
//            Observable<ArticleContentFavoriteAddResponse> Call = iArticle.SetContentFavoriteAdd(headers, add);
//            Call.subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Observer<ArticleContentFavoriteAddResponse>() {
//                        @Override
//                        public void onSubscribe(Disposable d) {
//
//                        }
//
//                        @Override
//                        public void onNext(ArticleContentFavoriteAddResponse e) {
//                            if (e.IsSuccess) {
//                                Toasty.success(ActDetailHyperShop.this, "با موفقیت ثبت شد").show();
//                                model.Item.Favorited = !model.Item.Favorited;
//                                if (model.Item.Favorited) {
//                                    ((ImageView) findViewById(R.id.imgHeartActDetail)).setImageResource(R.drawable.ic_fav_full);
//                                } else {
//                                    ((ImageView) findViewById(R.id.imgHeartActDetail)).setImageResource(R.drawable.ic_fav);
//                                }
//                            } else {
//                                Toasty.error(ActDetailHyperShop.this, e.ErrorMessage, Toast.LENGTH_LONG, true).show();
//                            }
//
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//                            Snackbar.make(layout, "خطای سامانه مجددا تلاش کنید", Snackbar.LENGTH_INDEFINITE).setAction("تلاش مجددا", new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    init();
//                                }
//                            }).show();
//                        }
//
//                        @Override
//                        public void onComplete() {
//
//                        }
//                    });
//        } else {
//            Snackbar.make(layout, "عدم دسترسی به اینترنت", Snackbar.LENGTH_INDEFINITE).setAction("تلاش مجددا", new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    init();
//                }
//            }).show();
//        }
//    }

    @OnClick(R.id.imgShareActDetail)
    public void ClickShare() {
        String st = EasyPreference.with(this).getString("configapp", "");
        CoreMain mcr = new Gson().fromJson(st, CoreMain.class);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        String message = model.Item.name + "\n" + model.Item.memo + "\n";
        if (model.Item.memo != null) {
            message = message + Html.fromHtml(model.Item.memo
                    .replace("<p>", "")
                    .replace("</p>", ""));
        }
        shareIntent.putExtra(Intent.EXTRA_TEXT, message + "\n\n\n" + this.getString(R.string.app_name) + "\n" + "لینک دانلود:" + "\n" + mcr.AppUrl);
        shareIntent.setType("text/txt");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        this.startActivity(Intent.createChooser(shareIntent, "به اشتراک گزاری با...."));
    }
}
