package ntk.android.hyper.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ntk.android.base.api.news.model.NewsContentViewRequest;
import ntk.android.base.entitymodel.news.NewsContentModel;
import ntk.android.base.utill.FontManager;
import ntk.android.hyper.R;
import ntk.android.hyper.activity.NewsDetailActivity;

public class CoreImageAdapter extends RecyclerView.Adapter<CoreImageAdapter.ViewHolder> {
    private List<NewsContentModel> list;
    private Context context;

    public CoreImageAdapter(Context context, List<NewsContentModel> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_recycler_image, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true).build();
        ImageLoader.getInstance().displayImage(list.get(position).MainImageSrc, holder.Img, options);
        holder.Lbl.setText(list.get(position).Title);
        holder.Img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsContentViewRequest request = new NewsContentViewRequest();
                request.Id = list.get(position).Id;
                context.startActivity(new Intent(context, NewsDetailActivity.class).putExtra("Request", new Gson().toJson(request)));
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ImageRecyclerImage)
        ImageView Img;

        @BindView(R.id.LblRecyclerImage)
        TextView Lbl;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            Lbl.setTypeface(FontManager.GetTypeface(context, FontManager.IranSans));
        }
    }
}
