package ntk.android.hyper.adapter.hyper;

import android.content.Intent;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ntk.android.base.Extras;
import ntk.android.base.adapter.BaseRecyclerAdapter;
import ntk.android.base.entitymodel.hypershop.HyperShopContentModel;
import ntk.android.base.utill.FontManager;
import ntk.android.base.view.NViewUtils;
import ntk.android.hyper.R;
import ntk.android.hyper.activity.hyper.ShopContentDetailActivity;
import ntk.android.hyper.view.BuyView;

public class HyperShopContent_1_Adapter extends BaseRecyclerAdapter<HyperShopContentModel, HyperShopContent_1_Adapter.HyperVH> {


    private boolean divideScreen = false;

    public HyperShopContent_1_Adapter(List<HyperShopContentModel> list) {
        super(list);
        drawable = R.drawable.empty_product;
    }

    public HyperShopContent_1_Adapter(List<HyperShopContentModel> list, boolean halfView) {
        super(list);
        drawable = R.drawable.empty_product;
        divideScreen = halfView;
    }

    @NonNull
    @Override
    public HyperShopContent_1_Adapter.HyperVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = inflate(parent, R.layout.shop_content_row_item1);
        if (divideScreen) {
            ViewGroup.LayoutParams ret = v.getLayoutParams();
            ret.width = getScreenWidth() / 2;
            v.setLayoutParams(ret);
        }
        HyperShopContent_1_Adapter.HyperVH h = new HyperShopContent_1_Adapter.HyperVH(v);
        return h;
    }

    @Override
    public void onBindViewHolder(@NonNull HyperShopContent_1_Adapter.HyperVH holder, int position) {

        HyperShopContentModel item = getItem(position);
        holder.title.setText(item.Name);
        holder.price.setText(NViewUtils.PriceFormat(item.Price) + " " + item.CURRENCY_UNIT);
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), ShopContentDetailActivity.class);
            intent.putExtra(Extras.EXTRA_FIRST_ARG, item.Code);
            view.getContext().startActivity(intent);
        });
        loadImage(item.Image, holder.image, holder.loadingProgress);
        holder.buyview.bind(item);

    }

    protected class HyperVH extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView title;
        public ProgressBar loadingProgress;
        public TextView price;
        BuyView buyview;

        public HyperVH(View v) {
            super(v);
            Typeface typeface = FontManager.T1_Typeface(v.getContext());
            image = itemView.findViewById(R.id.imgShopImage);
            title = itemView.findViewById(R.id.txtShopName);
            title.setTypeface(typeface);
            price = itemView.findViewById(R.id.txtShopPrice);
            price.setTypeface(typeface);
            loadingProgress = itemView.findViewById(R.id.loadingProgress);
            buyview = itemView.findViewById(R.id.buyView);
        }
    }
}