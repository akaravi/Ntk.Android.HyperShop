package ntk.android.academy.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import ntk.android.academy.activity.ActDetailPooling;
import ntk.android.academy.utill.FontManager;
import ntk.base.api.baseModel.Filters;
import ntk.base.api.pooling.entity.PoolingCategory;
import ntk.base.api.pooling.model.PoolingContentListRequest;

public class AdPoolCategory extends RecyclerView.Adapter<AdPoolCategory.ViewHolder> {

    private List<PoolingCategory> arrayList;
    private Context context;

    public AdPoolCategory(Context context, List<PoolingCategory> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_recycler_pooling, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.LblTitle.setText(arrayList.get(position).Title);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true).build();
        ImageLoader.getInstance().displayImage(arrayList.get(position).imageSrc, holder.img, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                holder.progress.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                holder.progress.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
        holder.Root.setOnClickListener(v -> {
            PoolingContentListRequest request = new PoolingContentListRequest();
            List<Filters> filters = new ArrayList<>();
            Filters f = new Filters();
            f.PropertyName = "LinkCategoryId";
            f.IntValue1 = arrayList.get(position).Id;
            filters.add(f);
            request.filters = filters;
            Intent intent = new Intent(context, ActDetailPooling.class);
            intent.putExtra("Request", new Gson().toJson(request));
            intent.putExtra("Title", arrayList.get(position).Title);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.lblRowRecyclerPooling)
        TextView LblTitle;

        @BindView(R.id.imgRowRecyclerPooling)
        ImageView img;

        @BindView(R.id.ProgressRecyclerPooling)
        ProgressBar progress;

        @BindView(R.id.rootPooling)
        LinearLayout Root;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            LblTitle.setTypeface(FontManager.GetTypeface(context, FontManager.IranSans));
        }
    }
}