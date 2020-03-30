package ntk.android.academy.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ntk.android.academy.R;
import ntk.android.academy.activity.ActDetailPooling;
import ntk.android.academy.utill.FontManager;
import ntk.base.api.baseModel.Filters;
import ntk.base.api.pooling.entity.PoolingCategory;
import ntk.base.api.pooling.entity.PoolingContent;
import ntk.base.api.pooling.model.PoolingContentListRequest;

public class AdDetailPoolCategory extends RecyclerView.Adapter<AdDetailPoolCategory.ViewHolder> {

    private List<PoolingContent> arrayList;
    private Context context;

    public AdDetailPoolCategory(Context context, List<PoolingContent> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_recycler_detail_pooling, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.LblTitle.setText(arrayList.get(position).Title);
        holder.LblDescription.setText(arrayList.get(position).Question);
        if (arrayList.get(position).ViewStatisticsBeforeVote) {
            holder.Chart.setVisibility(View.VISIBLE);
        }
        holder.Root.setOnClickListener(v -> {
            if (arrayList.get(position).MaxVoteForThisContent == 1) {
                if (holder.Rv.getVisibility() == View.GONE) {
                    AdPoolRadio adapter = new AdPoolRadio(context, arrayList.get(position).Options, arrayList.get(position), holder.Chart);
                    holder.Rv.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    holder.Rv.setVisibility(View.VISIBLE);
                    holder.ImgDropDown.setRotation(180);
                } else {
                    holder.Rv.setVisibility(View.GONE);
                    holder.Rv.setAdapter(null);
                    holder.Rv.removeAllViews();
                    holder.ImgDropDown.setRotation(0);
                }
            } else if (arrayList.get(position).MaxVoteForThisContent > 1 && arrayList.get(position).MaxVoteForEachOption == 1) {
                if (holder.Rv.getVisibility() == View.GONE) {
                    AdPoolCheckBox adapter = new AdPoolCheckBox(context, arrayList.get(position).Options, arrayList.get(position), holder.Btn, holder.Chart);
                    holder.Rv.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    holder.Rv.setVisibility(View.VISIBLE);
                    holder.ImgDropDown.setRotation(180);
                    holder.Btn.setVisibility(View.VISIBLE);
                } else {
                    holder.Rv.setVisibility(View.GONE);
                    holder.Btn.setVisibility(View.GONE);
                    holder.Rv.setAdapter(null);
                    holder.Rv.removeAllViews();
                    holder.ImgDropDown.setRotation(0);
                }
            } else {
                if (holder.Rv.getVisibility() == View.GONE) {
                    AdPoolPlusMines adapter = new AdPoolPlusMines(context, arrayList.get(position).Options, arrayList.get(position), holder.Btn, holder.Chart);
                    holder.Rv.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    holder.Rv.setVisibility(View.VISIBLE);
                    holder.ImgDropDown.setRotation(180);
                    holder.Btn.setVisibility(View.VISIBLE);
                } else {
                    holder.Rv.setVisibility(View.GONE);
                    holder.Btn.setVisibility(View.GONE);
                    holder.Rv.setAdapter(null);
                    holder.Rv.removeAllViews();
                    holder.ImgDropDown.setRotation(0);
                }
            }
        });

        holder.Chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCanceledOnTouchOutside(true);
                Window window = dialog.getWindow();
                window.setLayout(LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
                window.setGravity(Gravity.CENTER);
                dialog.setContentView(R.layout.dialog_pooling_statics);
                TextView Title = dialog.findViewById(R.id.lblTitleDialogPoolingStatic);
                Title.setTypeface(FontManager.GetTypeface(context, FontManager.IranSans));
                TextView Score = dialog.findViewById(R.id.lblScore);
                Score.setTypeface(FontManager.GetTypeface(context, FontManager.IranSans));
                TextView Owner = dialog.findViewById(R.id.lblOwnerScore);
                Owner.setTypeface(FontManager.GetTypeface(context, FontManager.IranSans));
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.lblTitleRowRecyclerDetailPooling)
        TextView LblTitle;

        @BindView(R.id.lblDescriptionRowRecyclerDetailPooling)
        TextView LblDescription;

        @BindView(R.id.imgDropDownRecyclerDetailPooling)
        ImageView ImgDropDown;

        @BindView(R.id.recyclerOptionPooling)
        RecyclerView Rv;

        @BindView(R.id.btnSendRecyclerDeialPooling)
        Button Btn;

        @BindView(R.id.btnChartRecyclerDeialPooling)
        Button Chart;

        @BindView(R.id.rootDetailPooling)
        LinearLayout Root;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            LblTitle.setTypeface(FontManager.GetTypeface(context, FontManager.IranSans));
            Rv.setHasFixedSize(true);
            RecyclerView.LayoutManager manager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            Rv.setLayoutManager(manager);
        }
    }
}