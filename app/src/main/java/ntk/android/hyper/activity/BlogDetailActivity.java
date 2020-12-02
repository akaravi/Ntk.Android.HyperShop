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
import ntk.android.base.activity.blog.BaseBlogDetail_1_Activity;
import ntk.android.base.dtomodel.core.ScoreClickDtoModel;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.base.ErrorExceptionBase;
import ntk.android.base.entitymodel.base.FilterDataModel;
import ntk.android.base.entitymodel.base.Filters;
import ntk.android.base.entitymodel.blog.BlogCommentModel;
import ntk.android.base.entitymodel.blog.BlogContentModel;
import ntk.android.base.entitymodel.blog.BlogContentOtherInfoModel;
import ntk.android.base.services.blog.BlogCommentService;
import ntk.android.base.services.blog.BlogContentOtherInfoService;
import ntk.android.base.services.blog.BlogContentService;
import ntk.android.hyper.R;
import ntk.android.hyper.adapter.BlogAdapter;
import ntk.android.hyper.adapter.BlogCommentAdapter;
import ntk.android.hyper.adapter.TabBlogAdapter;

public class BlogDetailActivity extends BaseBlogDetail_1_Activity {

    @Override
    public RecyclerView.Adapter createCommentAdapter(List<BlogCommentModel> listItems) {
        return new BlogCommentAdapter(this, listItems);
    }

    @Override
    protected RecyclerView.Adapter createOtherInfoAdapter(List<BlogContentOtherInfoModel> info) {
        return new TabBlogAdapter(this, info);
    }
}