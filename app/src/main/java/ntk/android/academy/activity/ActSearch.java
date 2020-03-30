package ntk.android.academy.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

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
import ntk.android.academy.R;
import ntk.android.academy.adapter.AdArticle;
import ntk.android.academy.config.ConfigRestHeader;
import ntk.android.academy.config.ConfigStaticValue;
import ntk.android.academy.utill.FontManager;
import ntk.base.api.article.interfase.IArticle;
import ntk.base.api.article.model.ArticleContentListRequest;
import ntk.base.api.article.model.ArticleContentResponse;
import ntk.base.api.baseModel.Filters;
import ntk.base.api.utill.NTKUtill;
import ntk.base.api.utill.RetrofitManager;

public class ActSearch extends AppCompatActivity {

    @BindView(R.id.txtSearchActSearch)
    EditText Txt;

    @BindView(R.id.recyclerSearch)
    RecyclerView Rv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_search);
        ButterKnife.bind(this);
        configStaticValue=new ConfigStaticValue(this);
        init();
    }

    private void init() {

        Rv.setHasFixedSize(true);
        Rv.setLayoutManager(new GridLayoutManager(this, 2));

        Txt.setTypeface(FontManager.GetTypeface(this, FontManager.IranSans));
        Txt.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                Search();
                return true;
            }
            return false;
        });

    }
    private ConfigStaticValue configStaticValue;

    private void Search() {
        RetrofitManager manager = new RetrofitManager(this);
        IArticle iArticle = manager.getRetrofitUnCached(configStaticValue.GetApiBaseUrl()).create(IArticle.class);
        Map<String, String> headers = new ConfigRestHeader().GetHeaders(this);


        ArticleContentListRequest request = new ArticleContentListRequest();
        List<Filters> filters = new ArrayList<>();
        Filters ft = new Filters();
        ft.PropertyName = "Title";
        ft.StringValue1 = Txt.getText().toString();
        ft.ClauseType = NTKUtill.ClauseType_Or;
        ft.SearchType = NTKUtill.Search_Type_Contains;
        filters.add(ft);

        Filters fd = new Filters();
        fd.PropertyName = "Description";
        fd.StringValue1 = Txt.getText().toString();
        fd.ClauseType = NTKUtill.ClauseType_Or;
        fd.SearchType = NTKUtill.Search_Type_Contains;
        filters.add(fd);

        Filters fb = new Filters();
        fb.PropertyName = "Body";
        fb.StringValue1 = Txt.getText().toString();
        fb.ClauseType = NTKUtill.ClauseType_Or;
        fb.SearchType = NTKUtill.Search_Type_Contains;

        filters.add(fb);

        request.filters = filters;


        Observable<ArticleContentResponse> call = iArticle.GetContentList(headers, request);
        call.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<ArticleContentResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ArticleContentResponse articleContentResponse) {
                        AdArticle adArticle = new AdArticle(ActSearch.this, articleContentResponse.ListItems);
                        Rv.setAdapter(adArticle);
                        adArticle.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toasty.warning(ActSearch.this, "خطای سامانه", Toasty.LENGTH_LONG, true).show();

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @OnClick(R.id.imgBackActSearch)
    public void ClickBack() {
        finish();
    }
}
