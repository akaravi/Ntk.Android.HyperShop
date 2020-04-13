package ntk.android.hypershop.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
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
import ntk.android.hypershop.R;
import ntk.android.hypershop.adapter.AdArticleGrid;
import ntk.android.hypershop.adapter.AdHyperShopGrid;
import ntk.android.hypershop.config.ConfigRestHeader;
import ntk.android.hypershop.config.ConfigStaticValue;
import ntk.android.hypershop.utill.EndlessRecyclerViewScrollListener;
import ntk.android.hypershop.utill.FontManager;
import ntk.base.api.article.entity.ArticleContent;
import ntk.base.api.article.interfase.IArticle;
import ntk.base.api.article.model.ArticleContentListRequest;
import ntk.base.api.article.model.ArticleContentResponse;
import ntk.base.api.hyperShop.entity.HyperShopContent;
import ntk.base.api.hyperShop.interfase.IHyperShop;
import ntk.base.api.hyperShop.model.HyperShopContentListRequest;
import ntk.base.api.hyperShop.model.HyperShopContentResponse;
import ntk.base.api.utill.RetrofitManager;

public class ActHyperContentList extends AppCompatActivity {

    @BindView(R.id.lblTitleActArticleContentList)
    TextView Lbl;

    @BindView(R.id.recyclerActArticleContentList)
    RecyclerView Rv;

    private String RequestStr;

    private int TotalItem = 0;
    private AdHyperShopGrid adapter;
    private List<HyperShopContent> contents = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_article_content_list);
        ButterKnife.bind(this);
        configStaticValue = new ConfigStaticValue(this);
        init();
    }

    private void init() {
        Lbl.setTypeface(FontManager.GetTypeface(this, FontManager.IranSans));
        Lbl.setText(getIntent().getExtras().getString("Title"));
        Rv.setHasFixedSize(true);
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        Rv.setLayoutManager(manager);
        adapter = new AdHyperShopGrid(this, contents);
        Rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        RequestStr = getIntent().getExtras().getString("Request");
        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(manager) {

            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (totalItemsCount <= TotalItem) {
                    HandelData((page + 1), new Gson().fromJson(RequestStr, HyperShopContentListRequest.class));
                }
            }
        };
        Rv.addOnScrollListener(scrollListener);
        HandelData(1, new Gson().fromJson(RequestStr, HyperShopContentListRequest.class));
    }

    private ConfigStaticValue configStaticValue;

    private void HandelData(int i, HyperShopContentListRequest request) {
        RetrofitManager retro = new RetrofitManager(this);
        IHyperShop iHyperShop = retro.getRetrofitUnCached(configStaticValue.GetApiBaseUrl()).create(IHyperShop.class);
        Map<String, String> headers = new ConfigRestHeader().GetHeaders(this);
        request.RowPerPage = 16;
        request.CurrentPageNumber = i;
        Observable<HyperShopContentResponse> call = iHyperShop.GetContentList(headers, request);
        call.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<HyperShopContentResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HyperShopContentResponse hyperShopContentResponse) {
                        contents.addAll(hyperShopContentResponse.ListItems);
                        Rv.setItemViewCacheSize(contents.size());
                        adapter.notifyDataSetChanged();
                        TotalItem = hyperShopContentResponse.TotalRowCount;
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toasty.warning(ActHyperContentList.this, "خطای سامانه", Toasty.LENGTH_LONG, true).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @OnClick(R.id.imgBackArticleContentList)
    public void Back() {
        finish();
    }
}