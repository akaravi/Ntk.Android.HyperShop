package ntk.android.hyper.adapter.hyper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ntk.android.base.adapter.BaseRecyclerAdapter;
import ntk.android.base.entitymodel.hypershop.HyperShopContentModel;
import ntk.android.hyper.R;
import ntk.android.hyper.activity.hyper.ShopContentDetailDialog;

public class PrevHypershopContentAdapter extends BaseRecyclerAdapter<HyperShopContentModel, PrevHypershopContentAdapter.HyperVH> {


    public PrevHypershopContentAdapter(List<HyperShopContentModel> list) {
        super(list);
    }

    @NonNull
    @Override
    public HyperVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_list_row, parent, false);
        HyperVH h = new HyperVH(v);
        return h;
    }

    @Override
    public void onBindViewHolder(@NonNull HyperVH holder, int position) {

        HyperShopContentModel item = getItem(position);
        holder.title.setText(item.Name);
        holder.itemView.setOnClickListener(view -> ShopContentDetailDialog.show((AppCompatActivity) holder.itemView.getContext(), item.Code));
        holder.description.setText(item.Memo);
        loadImage(item.Image, holder.image, holder.loadingProgress);
    }

    protected class HyperVH extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView title;
        public ProgressBar loadingProgress;
        public TextView price;
        public TextView description;

        public HyperVH(View v) {
            super(v);
            image = (ImageView) itemView.findViewById(R.id.image);

            title = itemView.findViewById(R.id.titleText);
            price = itemView.findViewById(R.id.priceText);
            description = itemView.findViewById(R.id.description);
            loadingProgress = itemView.findViewById(R.id.loadingProgress);

        }
    }
}