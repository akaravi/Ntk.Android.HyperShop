package ntk.android.hyper.activity;

import android.content.Intent;

import androidx.recyclerview.widget.RecyclerView;

import io.reactivex.Observable;
import java9.util.function.Function;
import ntk.android.base.activity.common.BaseFilterModelListActivity;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.base.FilterDataModel;
import ntk.android.base.entitymodel.base.FilterModel;
import ntk.android.base.entitymodel.blog.BlogContentModel;
import ntk.android.base.services.blog.BlogContentService;
import ntk.android.hyper.adapter.BlogAdapter;

/**
 * blog list activity that extends from module baseActivity
 */
public class BlogListActivity extends BaseFilterModelListActivity<BlogContentModel> {
    @Override
    protected void onCreated( ) {
        super.onCreated();
        //set title of activity
        LblTitle.setText("مقالات");
    }

    @Override
    public Function<FilterModel, Observable<ErrorException<BlogContentModel>>> getService() {
        return new BlogContentService(this)::getAll;
    }


    @Override
    public RecyclerView.Adapter createAdapter() {
        return new BlogAdapter(this, models);
    }

    @Override
    public void ClickSearch() {
        startActivity(new Intent(this, BlogSearchActivity.class));
    }
}
