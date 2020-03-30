package ntk.android.academy.adapter.theme;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ntk.android.academy.R;
import ntk.android.academy.activity.ActArticleContentList;
import ntk.android.academy.utill.FontManager;
import ntk.base.api.baseModel.theme.ThemeChildConfig;

public class AdCoreButtonGrid extends RecyclerView.Adapter<AdCoreButtonGrid.ViewHolder> {

    private List<ThemeChildConfig> childs;
    private Context context;

    public AdCoreButtonGrid(Context context, List<ThemeChildConfig> list) {
        this.childs = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_grid_core_button, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.txt.setTextSize(Float.parseFloat(childs.get(position).FontSize));
        holder.txt.setTextColor(Color.parseColor(childs.get(position).FrontColor));
        Log.i("likfvjeswfdes", "onBindViewHolder: "+childs.get(position).Href);
//        holder.txt.setCompoundDrawables(Drawable.createFromPath(childs.get(position).Href),null,null,null);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true).build();
        ImageLoader.getInstance().displayImage(childs.get(position).Href,  holder.img, options);
        holder.txt.setCompoundDrawables(context.getResources().getDrawable(R.drawable.background),null,null,null);
        holder.txt.setText(childs.get(position).Title);
        holder.layout.setOnClickListener(v -> {
            if (childs.get(position).ActionName.equals("WebClick")) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(childs.get(position).ActionRequest));
                context.startActivity(i);
            } else if (childs.get(position).ActionName.equals("ArticleContentList")) {
                Intent intent = new Intent(context, ActArticleContentList.class);
                intent.putExtra("Request", childs.get(position).ActionRequest);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return childs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.TxtRecyclerGridButton)
        TextView txt;

        @BindView(R.id.ImgRecyclerGridButton)
        ImageView img;

        @BindView(R.id.LayoutRecyclerGridButton)
        LinearLayout layout;



        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            txt.setTypeface(FontManager.GetTypeface(context, FontManager.IranSans));
        }
    }
}
