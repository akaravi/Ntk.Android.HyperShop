package ntk.android.hypershop.fragment;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ntk.android.hypershop.R;
import ntk.android.hypershop.adapter.AdCategory;
import ntk.android.hypershop.adapter.AdCategoryHyper;
import ntk.android.hypershop.config.ConfigRestHeader;
import ntk.android.hypershop.config.ConfigStaticValue;
import ntk.android.hypershop.utill.AppUtill;
import ntk.android.hypershop.utill.FontManager;
import ntk.base.api.article.interfase.IArticle;
import ntk.base.api.article.model.ArticleCategoryRequest;
import ntk.base.api.article.model.ArticleCategoryResponse;
import ntk.base.api.hyperShop.entity.HyperShopCategory;
import ntk.base.api.hyperShop.interfase.IHyperShop;
import ntk.base.api.hyperShop.model.HyperShopCategoryListRequest;
import ntk.base.api.hyperShop.model.HyperShopCategoryResponse;
import ntk.base.api.utill.RetrofitManager;

public class FrCommand extends Fragment {

    @BindView(R.id.recyclerCategory)
    RecyclerView Rv;

    @BindView(R.id.RefreshFrCategory)
    SwipeRefreshLayout Refresh;

    @BindView(R.id.lblProgressFrCategory)
    TextView LblProgress;

    @BindView(R.id.progressFrCategory)
    ProgressBar Progress;

    @BindView(R.id.rowProgressFrCategory)
    LinearLayout Loading;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_category, container, false);
        ButterKnife.bind(this, view);
        configStaticValue = new ConfigStaticValue(this.getContext());
        init();
        return view;
    }

    private ConfigStaticValue configStaticValue;

    private void init() {
        LblProgress.setTypeface(FontManager.GetTypeface(getContext(), FontManager.IranSans));
        Progress.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);

        Rv.setHasFixedSize(true);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        Rv.setLayoutManager(manager);
//        Loading.setVisibility(View.VISIBLE);

        HandelRest();

        Refresh.setColorSchemeResources(
                R.color.colorAccent,
                R.color.colorAccent,
                R.color.colorAccent);

        Refresh.setOnRefreshListener(() -> {
            HandelRest();
            Refresh.setRefreshing(false);
        });
    }

    private void HandelRest() {
        RetrofitManager manager = new RetrofitManager(getContext());
//        IArticle iArticle = manager.getCachedRetrofit(configStaticValue.GetApiBaseUrl()).create(IArticle.class);
        IHyperShop iHyperShop = manager.getRetrofitUnCached(configStaticValue.GetApiBaseUrl()).create(IHyperShop.class);
        Map<String, String> headers = new ConfigRestHeader().GetHeaders(getContext());

        HyperShopCategoryListRequest listRequest = new HyperShopCategoryListRequest();
        listRequest.RowPerPage = 20;
        Observable<HyperShopCategoryResponse> callHyper = iHyperShop.GetCategoryList(headers, listRequest);
        callHyper.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HyperShopCategoryResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HyperShopCategoryResponse hyperShopCategoryResponse) {
                        AdCategoryHyper adapter = new AdCategoryHyper(getContext(), hyperShopCategoryResponse.ListItems);
                        Rv.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        Loading.setVisibility(View.GONE);
                        Rv.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Loading.setVisibility(View.GONE);
                        Toasty.error(getContext(), "خطای سامانه مجددا تلاش کنیدِ", Toasty.LENGTH_LONG, true).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}