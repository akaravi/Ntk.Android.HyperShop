package ntk.android.hyper.adapter.hyper;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.List;

import ntk.android.base.Extras;
import ntk.android.base.adapter.BaseRecyclerAdapter;
import ntk.android.base.entitymodel.base.FilterDataModel;
import ntk.android.base.entitymodel.base.Filters;
import ntk.android.base.entitymodel.hypershop.HyperShopCategoryModel;
import ntk.android.base.utill.FontManager;
import ntk.android.hyper.R;
import ntk.android.hyper.activity.hyper.ShopContentListActivity;

public class HyperCategoryAdapter extends BaseRecyclerAdapter<HyperShopCategoryModel, HyperCategoryAdapter.VH> {
    public HyperCategoryAdapter(List<HyperShopCategoryModel> list) {
        super(list);
        drawable = R.drawable.logo;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(inflate(parent, R.layout.row_hyper_cat_item));

    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        loadImage(list.get(position).Image, holder.image, holder.loading);
        holder.name.setText(getItem(position).name);
        holder.itemView.setOnClickListener(view -> {
            Intent i = new Intent(view.getContext(), ShopContentListActivity.class);
            FilterDataModel filterDataModel = new FilterDataModel();
            filterDataModel.addFilter(new Filters().setPropertyName("CategoryCode").setStringValue(getItem(position).Code));
            i.putExtra(Extras.EXTRA_FIRST_ARG, new Gson().toJson(filterDataModel));
            i.putExtra(Extras.EXTRA_SECOND_ARG, "لیست محصولات " + list.get(position).name);
            i.putExtra(Extras.Extra_THIRD_ARG, true);
            view.getContext().startActivity(i);
        });
    }

    public class VH extends RecyclerView.ViewHolder {
        TextView name;
        ImageView image;
        View loading;

        public VH(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.catName);
            name.setTypeface(FontManager.T1_BOLD_Typeface(itemView.getContext()));
            image = itemView.findViewById(R.id.catImage);
            loading = itemView.findViewById(R.id.loadingProgress);
        }
    }
}
