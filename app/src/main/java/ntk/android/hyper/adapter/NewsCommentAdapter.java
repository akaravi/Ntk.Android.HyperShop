package ntk.android.hyper.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import io.reactivex.annotations.NonNull;
import ntk.android.base.adapter.BaseRecyclerAdapter;
import ntk.android.base.config.GenericErrors;
import ntk.android.base.config.NtkObserver;
import ntk.android.base.config.ServiceExecute;
import ntk.android.base.entitymodel.base.ErrorExceptionBase;
import ntk.android.base.entitymodel.news.NewsCommentModel;
import ntk.android.base.services.news.NewsCommentService;
import ntk.android.base.utill.AppUtill;
import ntk.android.base.utill.FontManager;
import ntk.android.hyper.R;

public class NewsCommentAdapter extends BaseRecyclerAdapter<NewsCommentModel, NewsCommentAdapter.ViewHolder> {


    private final Context context;

    public NewsCommentAdapter(Context context, List<NewsCommentModel> arrayList) {
        super(arrayList);
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = inflate(viewGroup, R.layout.row_recycler_comment);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final NewsCommentModel item = list.get(position);
        holder.Lbls.get(0).setText(item.Writer);
        if (item.CreatedDate != null) {
            holder.Lbls.get(1).setText(AppUtill.GregorianToPersian(item.CreatedDate));
        } else {
            holder.Lbls.get(1).setText("");
        }
        holder.Lbls.get(2).setText(String.valueOf(item.SumDisLikeClick));
        holder.Lbls.get(3).setText(String.valueOf(item.SumLikeClick));
        holder.Lbls.get(4).setText(String.valueOf(item.Comment));

        holder.ImgLike.setOnClickListener(v -> {

            long id = item.Id;
            holder.loading.setVisibility(View.VISIBLE);
            ServiceExecute.execute(new NewsCommentService(context).like(id))
                    .subscribe(new NtkObserver<ErrorExceptionBase>() {
                        @Override
                        public void onNext(@NonNull ErrorExceptionBase model) {
                            holder.loading.setVisibility(View.GONE);
                            if (model.IsSuccess) {
                                item.SumLikeClick = item.SumLikeClick + 1;
                                notifyDataSetChanged();
                            } else {
                                Toasty.warning(context, model.ErrorMessage, Toasty.LENGTH_LONG, true).show();
                            }
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            holder.loading.setVisibility(View.GONE);
                            new GenericErrors().throwableException((error, tryAgain) -> {
                                Toasty.warning(context, error, Toasty.LENGTH_LONG, true).show();
                            }, e, () -> {
                            });
                        }
                    });
        });

        holder.ImgDisLike.setOnClickListener(v -> {
            long id = item.Id;
            holder.loading.setVisibility(View.VISIBLE);
            ServiceExecute.execute(new NewsCommentService(context).dislike(id))
                    .subscribe(new NtkObserver<ErrorExceptionBase>() {

                        @Override
                        public void onNext(ErrorExceptionBase model) {
                            holder.loading.setVisibility(View.GONE);
                            if (model.IsSuccess) {
                                item.SumDisLikeClick = item.SumDisLikeClick - 1;
                                notifyDataSetChanged();
                            } else {
                                Toasty.warning(context, model.ErrorMessage, Toasty.LENGTH_LONG, true).show();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            holder.loading.setVisibility(View.GONE);
                            new GenericErrors().throwableException((error, tryAgain) -> {
                                Toasty.warning(context, error, Toasty.LENGTH_LONG, true).show();
                            }, e, () -> {
                            });
                        }

                    });
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindViews({R.id.lblUserNameRecyclerComment,
                R.id.lblDateRecyclerComment,
                R.id.lblDesLikeRecyclerComment,
                R.id.lblLikeRecyclerComment,
                R.id.lblContentRecyclerComment
        })
        List<TextView> Lbls;

        @BindView(R.id.imgDisLikeRecyclerComment)
        ImageView ImgDisLike;

        @BindView(R.id.imgLikeRecyclerComment)
        ImageView ImgLike;

        @BindView(R.id.relativeLoading)
        RelativeLayout loading;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            Lbls.get(0).setTypeface(FontManager.T1_Typeface(context));
            Lbls.get(1).setTypeface(FontManager.T1_Typeface(context));
            Lbls.get(2).setTypeface(FontManager.T1_Typeface(context));
            Lbls.get(3).setTypeface(FontManager.T1_Typeface(context));
            Lbls.get(4).setTypeface(FontManager.T1_Typeface(context));
        }
    }
}
