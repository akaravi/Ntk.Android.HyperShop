package ntk.android.academy.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.balysv.materialripple.MaterialRippleLayout;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ntk.android.academy.R;
import ntk.android.academy.activity.ActArticleContentList;
import ntk.android.academy.utill.FontManager;
import ntk.base.api.article.entity.ArticleCategory;
import ntk.base.api.article.model.ArticleContentListRequest;
import ntk.base.api.baseModel.Filters;

public class AdCategory extends RecyclerView.Adapter<AdCategory.ViewHolder> {

    private List<ArticleCategory> arrayList;
    private Context context;

    public AdCategory(Context context, List<ArticleCategory> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_recycler_category, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.LblName.setText(arrayList.get(position).Title);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true).build();
        ImageLoader.getInstance().displayImage(arrayList.get(position).LinkMainImageSrc, holder.Img, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                holder.Progress.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                holder.Progress.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
        if (arrayList.get(position).Children.size() == 0) {
            holder.ImgDrop.setVisibility(View.GONE);
        }
        holder.Img.setOnClickListener(view -> {
            ArticleContentListRequest request = new ArticleContentListRequest();
            List<Filters> filters = new ArrayList<>();
            Filters f = new Filters();
            f.PropertyName = "LinkCategoryId";
            f.IntValue1 = arrayList.get(position).Id;
            filters.add(f);
            request.filters = filters;
            Intent intent = new Intent(context, ActArticleContentList.class);
            intent.putExtra("Request", new Gson().toJson(request));
            context.startActivity(intent);
        });
        holder.ImgDrop.setOnClickListener(view -> {
            if (holder.Rv.getVisibility() == View.GONE) {
                holder.ImgArrow.setRotation(180);
                AdCategory adapter = new AdCategory(context, arrayList.get(position).Children);
                holder.Rv.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                holder.Rv.setVisibility(View.VISIBLE);
            } else {
                holder.Rv.setAdapter(null);
                holder.Rv.setVisibility(View.GONE);
                holder.ImgArrow.setRotation(0);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.lblRowRecyclerCategory)
        TextView LblName;

        @BindView(R.id.imgRowRecyclerCategory)
        ImageView Img;

        @BindView(R.id.imgArrow)
        ImageView ImgArrow;

        @BindView(R.id.recyclerSubCategory)
        RecyclerView Rv;

        @BindView(R.id.imgArrowRecyclerCategory)
        MaterialRippleLayout ImgDrop;

        @BindView(R.id.ProgressRecyclerCategory)
        ProgressBar Progress;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            LblName.setTypeface(FontManager.GetTypeface(context, FontManager.IranSans));
            Rv.setHasFixedSize(true);
            RecyclerView.LayoutManager manager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, true);
            Rv.setLayoutManager(manager);
            Progress.getIndeterminateDrawable().setColorFilter(context.getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_IN);
        }
    }
}
