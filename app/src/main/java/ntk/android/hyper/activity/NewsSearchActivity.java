package ntk.android.hyper.activity;

import androidx.recyclerview.widget.RecyclerView;

import io.reactivex.Observable;
import java9.util.function.Function;
import ntk.android.base.activity.abstraction.AbstractionSearchActivity;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.base.FilterModel;
import ntk.android.base.entitymodel.news.NewsContentModel;
import ntk.android.base.services.news.NewsContentService;
import ntk.android.hyper.adapter.NewsAdapter;

;

public class NewsSearchActivity extends AbstractionSearchActivity<NewsContentModel> {
    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new NewsAdapter(this, models);
    }

    @Override
    public Function<FilterModel, Observable<ErrorException<NewsContentModel>>> getService() {
        return new NewsContentService(this)::getAll;
    }
}
