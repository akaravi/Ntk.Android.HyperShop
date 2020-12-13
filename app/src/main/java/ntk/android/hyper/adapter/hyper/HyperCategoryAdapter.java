package ntk.android.hyper.adapter.hyper;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ntk.android.base.adapter.BaseRecyclerAdapter;
import ntk.android.base.entitymodel.hypershop.HyperShopCategoryModel;
import ntk.android.hyper.R;

public class HyperCategoryAdapter extends BaseRecyclerAdapter<HyperShopCategoryModel, HyperCategoryAdapter.VH> {
    public HyperCategoryAdapter(List<HyperShopCategoryModel> list) {
        super(list);
        drawable=R.drawable.logo;
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
    }

    public class VH extends RecyclerView.ViewHolder {
        TextView name;
        ImageView image;
        View loading;

        public VH(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.catName);
            image = itemView.findViewById(R.id.catImage);
            loading = itemView.findViewById(R.id.loadingProgress);
        }
    }
}
