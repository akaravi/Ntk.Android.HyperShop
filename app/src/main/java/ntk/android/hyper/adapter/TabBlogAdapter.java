package ntk.android.hyper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import com.balysv.materialripple.MaterialRippleLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ntk.android.base.adapter.BaseRecyclerAdapter;
import ntk.android.base.entitymodel.blog.BlogContentOtherInfoModel;
import ntk.android.base.event.HtmlBodyEvent;
import ntk.android.base.utill.FontManager;
import ntk.android.hyper.R;
import ntk.android.hyper.event.HtmlBodyBlogEvent;

public class TabBlogAdapter extends BaseRecyclerAdapter<BlogContentOtherInfoModel, TabBlogAdapter.ViewHolder> {


    private final Context context;

    public TabBlogAdapter(Context context, List<BlogContentOtherInfoModel> arrayList) {
        super(arrayList);
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_recycler_tab, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        BlogContentOtherInfoModel item = list.get(position);
        holder.Btn.setText(item.Title);
        if (item.TypeId == 0) {
            EventBus.getDefault().post(new HtmlBodyEvent(item.HtmlBody));
        }
        holder.Ripple.setOnClickListener(v -> EventBus.getDefault().post(new HtmlBodyBlogEvent(item.HtmlBody)));
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
