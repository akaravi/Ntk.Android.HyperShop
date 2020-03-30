package ntk.android.academy.fragment;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ntk.android.academy.R;
import ntk.android.academy.adapter.AdArticleGrid;
import ntk.android.academy.config.ConfigRestHeader;
import ntk.android.academy.config.ConfigStaticValue;
import ntk.android.academy.utill.AppUtill;
import ntk.android.academy.utill.EndlessRecyclerViewScrollListener;
import ntk.android.academy.utill.FontManager;
import ntk.base.api.article.interfase.IArticle;
import ntk.base.api.article.entity.ArticleContent;
import ntk.base.api.article.model.ArticleContentFavoriteListRequest;
import ntk.base.api.article.model.ArticleContentFavoriteListResponse;
import ntk.base.api.utill.RetrofitManager;

public class FrFav extends Fragment {

    @BindView(R.id.RecyclerFav)
    RecyclerView Rv;

    @BindView(R.id.swipRefreshFrFav)
    SwipeRefreshLayout Refresh;

    @BindView(R.id.lblProgressFrFav)
    TextView LblProgress;

    @BindView(R.id.progressFrFav)
    ProgressBar Progress;

    @BindView(R.id.rowProgressFrFav)
    LinearLayout Loading;

    List<ArticleContent> contents = new ArrayList<>();
    AdArticleGrid adapter;

    private int TotalArticle = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_fav, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        LblProgress.setTypeface(FontManager.GetTypeface(getContext(), FontManager.IranSans));
        Progress.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);

        Rv.setHasFixedSize(true);
        GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
        Rv.setLayoutManager(manager);
        Loading.setVisibility(View.VISIBLE);

        adapter = new AdArticleGrid(getContext(), contents);
        Rv.setAdapter(adapter);

        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(manager) {

            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (totalItemsCount <= TotalArticle) {
                    HandleCategory((page + 1));
                }
            }
        };

        Rv.addOnScrollListener(scrollListener);
        HandleCategory(1);

        Refresh.setColorSchemeResources(
                R.color.colorAccent,
                R.color.colorAccent,
                R.color.colorAccent);

        Refresh.setOnRefreshListener(() -> {
            contents.clear();
            HandleCategory(1);
            Refresh.setRefreshing(false);
        });
    }

    private void HandleCategory(int i) {
        RetrofitManager manager = new RetrofitManager(getContext());
        IArticle iArticle = manager.getRetrofitUnCached(new ConfigStaticValue(getContext()).GetApiBaseUrl()).create(IArticle.class);

        ArticleContentFavoriteListRequest request = new ArticleContentFavoriteListRequest();
        request.RowPerPage = 20;
        request.CurrentPageNumber = i;
        Observable<ArticleContentFavoriteListResponse> call = iArticle.GetContentFavoriteList(new ConfigRestHeader().GetHeaders(getContext()), request);
        call.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<ArticleContentFavoriteListResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ArticleContentFavoriteListResponse articleContentResponse) {
                        Loading.setVisibility(View.GONE);
                        contents.addAll(articleContentResponse.ListItems);
                        adapter.notifyDataSetChanged();
                        Rv.setItemViewCacheSize(contents.size());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Loading.setVisibility(View.GONE);
                        Toasty.error(getContext(), "خطای سامانه مجددا تلاش کنید", Toasty.LENGTH_LONG, true).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
