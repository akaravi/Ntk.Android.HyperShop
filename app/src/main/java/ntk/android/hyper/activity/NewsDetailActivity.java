package ntk.android.hyper.activity;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ntk.android.base.activity.news.BaseNewsDetail_1_Activity;
import ntk.android.base.entitymodel.news.NewsCommentModel;
import ntk.android.base.entitymodel.news.NewsContentOtherInfoModel;
import ntk.android.hyper.adapter.NewsCommentAdapter;
import ntk.android.hyper.adapter.TabNewsAdapter;

;

public class NewsDetailActivity extends BaseNewsDetail_1_Activity {


    @Override
    public RecyclerView.Adapter createCommentAdapter(List<NewsCommentModel> listItems) {
        return new NewsCommentAdapter(this, listItems);
    }

    @Override
    protected RecyclerView.Adapter createOtherInfoAdapter(List<NewsContentOtherInfoModel> info) {
        return new TabNewsAdapter(NewsDetailActivity.this, info);
    }

}
