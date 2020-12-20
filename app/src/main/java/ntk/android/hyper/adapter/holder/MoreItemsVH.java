package ntk.android.hyper.adapter.holder;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ntk.android.base.Extras;
import ntk.android.base.entitymodel.hypershop.HyperShopContentModel;
import ntk.android.hyper.R;
import ntk.android.hyper.activity.hyper.ShopContentListActivity;
import ntk.android.hyper.adapter.hyper.HyperShopContent_1_Adapter;

public class MoreItemsVH extends RecyclerView.ViewHolder {
    MoreItemsVH(@NonNull View itemView) {
        super(itemView);
    }

    public static RecyclerView.ViewHolder create(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.base_recycler_more, parent, false);
        return new MoreItemsVH(inflate);
    }

    public void bind(List<HyperShopContentModel> list) {
        ((TextView) itemView.findViewById(R.id.moreTextTitle)).setText("لیست محصولات");
        itemView.findViewById(R.id.moreBtn).setOnClickListener(view -> {
            Intent intent = new Intent(itemView.getContext(), ShopContentListActivity.class);
            intent.putExtra(Extras.EXTRA_SECOND_ARG, "لیست پیشفرض محصولات");
            intent.putExtra(Extras.Extra_THIRD_ARG, true);
            itemView.getContext().startActivity(intent);
        });
        RecyclerView rc = itemView.findViewById(R.id.moreRc);
        rc.setNestedScrollingEnabled(true);
        rc.setHasFixedSize(true);
        rc.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        rc.setAdapter(new HyperShopContent_1_Adapter(list, true));
        LinearSnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(rc);
    }
}
