package ntk.android.hyper.activity;

import android.text.Html;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import java9.util.function.BiFunction;
import java9.util.function.Function;
import ntk.android.base.activity.abstraction.AbstractionDetailActivity;
import ntk.android.base.activity.news.BaseNewsDetail_1_Activity;
import ntk.android.base.dtomodel.core.ScoreClickDtoModel;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.base.ErrorExceptionBase;
import ntk.android.base.entitymodel.base.FilterDataModel;
import ntk.android.base.entitymodel.base.Filters;
import ntk.android.base.entitymodel.news.NewsCommentModel;
import ntk.android.base.entitymodel.news.NewsContentModel;
import ntk.android.base.entitymodel.news.NewsContentOtherInfoModel;
import ntk.android.base.services.news.NewsCommentService;
import ntk.android.base.services.news.NewsContentOtherInfoService;
import ntk.android.base.services.news.NewsContentService;
import ntk.android.hyper.R;
import ntk.android.hyper.adapter.NewsCommentAdapter;
import ntk.android.hyper.adapter.NewsAdapter;
import ntk.android.hyper.adapter.TabNewsAdapter;

;

public class NewsDetailActivity extends BaseNewsDetail_1_Activity {


    @Override
    protected void initChild() {
        favoriteDrawableId = R.drawable.ic_fav_full;
        unFavoriteDrawableId = R.drawable.ic_fav;
    }

    @Override
    public RecyclerView.Adapter createCommentAdapter(List<NewsCommentModel> listItems) {
        return new NewsCommentAdapter(this, listItems);
    }

    @Override
    protected RecyclerView.Adapter createOtherInfoAdapter(List<NewsContentOtherInfoModel> info) {
        return new TabNewsAdapter(NewsDetailActivity.this, info);
    }

}
