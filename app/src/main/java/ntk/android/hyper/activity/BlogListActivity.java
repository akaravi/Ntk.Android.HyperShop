package ntk.android.hyper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import io.reactivex.Observable;
import java9.util.function.Function;
import ntk.android.base.activity.abstraction.AbstractionListActivity;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.base.FilterDataModel;
import ntk.android.base.entitymodel.blog.BlogContentModel;
import ntk.android.base.services.blog.BlogContentService;
import ntk.android.hyper.R;
import ntk.android.hyper.adapter.BlogAdapter;

public class BlogListActivity extends AbstractionListActivity<BlogContentModel> {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((TextView) findViewById(R.id.lblTitle)).setText("مقالات");
    }

    @Override
    public Function<FilterDataModel, Observable<ErrorException<BlogContentModel>>> getService() {
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
