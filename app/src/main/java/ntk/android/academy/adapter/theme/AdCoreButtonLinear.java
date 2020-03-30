package ntk.android.academy.adapter.theme;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ntk.android.academy.R;
import ntk.android.academy.activity.ActArticleContentList;
import ntk.android.academy.utill.FontManager;
import ntk.base.api.baseModel.theme.ThemeChildConfig;

public class AdCoreButtonLinear extends RecyclerView.Adapter<AdCoreButtonLinear.ViewHolder> {

    private List<ThemeChildConfig> childs;
    private Context context;

    public AdCoreButtonLinear(Context context, List<ThemeChildConfig> list) {
        this.childs = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_linear_core_button, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.Btn.setTextSize(Float.parseFloat(childs.get(position).FontSize));
        holder.Btn.setTextColor(Color.parseColor(childs.get(position).FrontColor));
        holder.Btn.setText(childs.get(position).Title);
        holder.Btn.setOnClickListener(v -> {
            if (childs.get(position).ActionName.equals("WebClick")) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(childs.get(position).ActionRequest));
                context.startActivity(i);
            }else if (childs.get(position).ActionName.equals("ArticleContentList")) {
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

        @BindView(R.id.BtnRecyclerLinearButton)
        Button Btn;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            Btn.setTypeface(FontManager.GetTypeface(context , FontManager.IranSans));
        }
    }
}
