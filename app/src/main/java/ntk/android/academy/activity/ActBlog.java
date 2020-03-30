package ntk.android.academy.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ntk.android.academy.R;
import ntk.android.academy.adapter.AdBlog;
import ntk.android.academy.adapter.AdNews;
import ntk.android.academy.config.ConfigRestHeader;
import ntk.android.academy.config.ConfigStaticValue;
import ntk.android.academy.utill.EndlessRecyclerViewScrollListener;
import ntk.android.academy.utill.FontManager;
import ntk.base.api.blog.interfase.IBlog;
import ntk.base.api.blog.entity.BlogContent;
import ntk.base.api.blog.model.BlogContentListRequest;
import ntk.base.api.blog.model.BlogContentListResponse;
import ntk.base.api.blog.model.BlogContentResponse;
import ntk.base.api.news.interfase.INews;
import ntk.base.api.news.entity.NewsContent;
import ntk.base.api.news.model.NewsContentListRequest;
import ntk.base.api.news.model.NewsContentResponse;
import ntk.base.api.utill.RetrofitManager;

public class ActBlog extends AppCompatActivity {

    @BindView(R.id.lblTitleActBlog)
    TextView LblTitle;

    @BindView(R.id.recyclerBlog)
    RecyclerView Rv;

    private int Total = 0;
    private List<BlogContent> blogs = new ArrayList<>();
    private AdBlog adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_blog);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        LblTitle.setTypeface(FontManager.GetTypeface(this, FontManager.IranSans));
        LblTitle.setText("مجلات آموزشی دورهمی");
        Rv.setHasFixedSize(true);
        LinearLayoutManager LMC = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        Rv.setLayoutManager(LMC);
        adapter = new AdBlog(this, blogs);
        Rv.setAdapter(adapter);

        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(LMC) {

            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (totalItemsCount <= Total) {
                    RestCall((page + 1));
                }
            }
        };
        Rv.addOnScrollListener(scrollListener);

        RestCall(1);
    }

    private void RestCall(int i) {
        RetrofitManager manager = new RetrofitManager(this);
        IBlog iBlog = manager.getRetrofitUnCached(new ConfigStaticValue(this).GetApiBaseUrl()).create(IBlog.class);

        BlogContentListRequest request = new BlogContentListRequest();
        request.RowPerPage = 20;
        request.CurrentPageNumber = i;
        Observable<BlogContentListResponse> call = iBlog.GetContentList(new ConfigRestHeader().GetHeaders(this), request);
        call.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<BlogContentListResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BlogContentListResponse response) {
                        if (response.IsSuccess) {
                            blogs.addAll(response.ListItems);
                            Total = response.TotalRowCount;
                            adapter.notifyDataSetChanged();
                        }else {
                            Toasty.warning(ActBlog.this, "موردی یافت نشد", Toasty.LENGTH_LONG, true).show();
                            finish();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toasty.warning(ActBlog.this, "خطای سامانه", Toasty.LENGTH_LONG, true).show();

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @OnClick(R.id.imgBackActBlog)
    public void ClickBack() {
        finish();
    }
}
