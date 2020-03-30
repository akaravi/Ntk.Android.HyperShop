package ntk.android.academy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.balysv.materialripple.MaterialRippleLayout;
import org.greenrobot.eventbus.EventBus;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ntk.android.academy.R;
import ntk.android.academy.event.EvHtmlBody;
import ntk.android.academy.utill.FontManager;
import ntk.base.api.article.entity.ArticleContentOtherInfo;

public class AdTab extends RecyclerView.Adapter<AdTab.ViewHolder> {

    private List<ArticleContentOtherInfo> arrayList;
    private Context context;

    public AdTab(Context context, List<ArticleContentOtherInfo> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_recycler_tab, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.Btn.setText(arrayList.get(position).Title);
        if (arrayList.get(position).TypeId == 0) {
            EventBus.getDefault().post(new EvHtmlBody(arrayList.get(position).HtmlBody));
        }
        holder.Ripple.setOnClickListener(v -> EventBus.getDefault().post(new EvHtmlBody(arrayList.get(position).HtmlBody)));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.BtnRecyclerTab)
        Button Btn;

        @BindView(R.id.RippleBtnRecyclerTab)
        MaterialRippleLayout Ripple;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            Btn.setTypeface(FontManager.GetTypeface(context, FontManager.IranSans));
        }
    }
}
