package ntk.android.hyper.activity;

import android.content.Intent;

import androidx.recyclerview.widget.RecyclerView;

import io.reactivex.Observable;
import java9.util.function.Function;
import ntk.android.base.activity.common.BaseFilterModelListActivity;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.base.FilterDataModel;
import ntk.android.base.entitymodel.base.FilterModel;
import ntk.android.base.entitymodel.news.NewsContentModel;
import ntk.android.base.services.news.NewsContentService;
import ntk.android.hyper.adapter.NewsAdapter;

/**
 * activity for showing News list
 */
public class NewsListActivity extends BaseFilterModelListActivity<NewsContentModel> {
    @Override
    protected void onCreated() {
        super.onCreated();
        //set custom title
        LblTitle.setText("اخبار");
    }

    @Override
    public Function<FilterModel, Observable<ErrorException<NewsContentModel>>> getService() {
        return new NewsContentService(this)::getAll;
    }


    @Override
    public RecyclerView.Adapter createAdapter() {
        return new NewsAdapter(this, models);
    }

    @Override
    public void ClickSearch() {
        startActivity(new Intent(this, NewsSearchActivity.class));
    }

}
