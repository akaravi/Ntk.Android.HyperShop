package ntk.android.academy.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import ntk.android.academy.R;
import ntk.android.academy.utill.FontManager;
import ntk.base.api.ticket.entity.TicketingTask;

public class AdFaq extends RecyclerView.Adapter<AdFaq.ViewHolder> {

    private List<TicketingTask> arrayList;
    private Context context;

    public AdFaq(Context context, List<TicketingTask> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recycler_faq, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.Web.setTextDirection(View.TEXT_DIRECTION_RTL);
        holder.Lbls.get(0).setText(arrayList.get(position).Question);
        holder.Lbls.get(1).setText(arrayList.get(position).Answer);
        holder.Root.get(0).setOnClickListener(view -> {
            if (holder.Web.getVisibility() == View.GONE) {
                holder.Web.loadData("<html dir=\"rtl\" lang=\"\"><body>" + arrayList.get(position).Answer + "</body></html>", "text/html; charset=utf-8", "UTF-8");
                holder.Web.setVisibility(View.VISIBLE);
                holder.Root.get(1).setVisibility(View.VISIBLE);
            } else {
                holder.Web.setVisibility(View.GONE);
                holder.Root.get(1).setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindViews({R.id.lblTitleFaq, R.id.lblMessageFaq})
        List<TextView> Lbls;

        @BindViews({R.id.rootFaq, R.id.rowDetailFaq})
        List<ViewGroup> Root;

        @BindView(R.id.WebViewFaqList)
        WebView Web;

        @SuppressLint("SetJavaScriptEnabled")
        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            Lbls.get(0).setTypeface(FontManager.GetTypeface(context, FontManager.IranSans));
            Lbls.get(1).setTypeface(FontManager.GetTypeface(context, FontManager.IranSans));
            Web.getSettings().setJavaScriptEnabled(true);
            Web.getSettings().setBuiltInZoomControls(true);
            Web.setVisibility(View.GONE);
        }
    }
}
