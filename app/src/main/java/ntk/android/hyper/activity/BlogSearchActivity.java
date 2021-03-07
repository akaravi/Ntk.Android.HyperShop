package ntk.android.hyper.activity;

import androidx.recyclerview.widget.RecyclerView;

import io.reactivex.Observable;
import java9.util.function.Function;
import ntk.android.base.activity.abstraction.AbstractSearchActivity;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.base.FilterModel;
import ntk.android.base.entitymodel.blog.BlogContentModel;
import ntk.android.base.services.blog.BlogContentService;
import ntk.android.hyper.adapter.BlogAdapter;

/**
 *  search blog activity that extends from module baseActivity
 */
public class BlogSearchActivity extends AbstractSearchActivity<BlogContentModel> {

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new BlogAdapter(this, models);
    }

    @Override
    public Function<FilterModel, Observable<ErrorException<BlogContentModel>>> getService() {
        return new BlogContentService(this)::getAll;
    }


}
