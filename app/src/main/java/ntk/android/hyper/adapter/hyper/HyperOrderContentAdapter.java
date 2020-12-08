package ntk.android.hyper.adapter.hyper;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.List;

import ntk.android.base.adapter.BaseRecyclerAdapter;
import ntk.android.base.dtomodel.hypershop.HyperShopOrderContentDtoModel;
import ntk.android.base.view.NViewUtils;
import ntk.android.hyper.R;
import ntk.android.hyper.view.BuyView;

public class HyperOrderContentAdapter extends BaseRecyclerAdapter<HyperShopOrderContentDtoModel, HyperOrderContentAdapter.ItemViewHolder> {


    private final Context context;

    Runnable changePriceMethod;

    public HyperOrderContentAdapter(Context context, List<HyperShopOrderContentDtoModel> products, Runnable o) {
        super(products);
        this.context = context;
        this.changePriceMethod = o;
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = inflate(parent, R.layout.order_content_item_recycler);
        return new ItemViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        if (position == list.size() - 1) {
            holder.itemView.setPadding(0, 0, 0, NViewUtils.dpToPx(context, 100));
        }else
            holder.itemView.setPadding(0, 0, 0,0);
        HyperShopOrderContentDtoModel item = list.get(position);
        holder.txtItemName.setText(item.Name);
        holder.txtProductPrice.setText(new DecimalFormat("###,###,###,###").format(item.Price) + " " + item.CURRENCY_UNIT);
        holder.buyView.bind(item, changePriceMethod);


    }


    protected class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView txtItemName;
        TextView txtProductPrice;
        BuyView buyView;

        ItemViewHolder(View itemView) {
            super(itemView);
            this.txtItemName = itemView.findViewById(R.id.txtShopName);
            this.txtProductPrice = itemView.findViewById(R.id.txtShopPrice);
            this.buyView = itemView.findViewById(R.id.buyView);

        }

    }
}
