package ntk.android.hyper.adapter.hyper;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.List;

import ntk.android.base.adapter.BaseRecyclerAdapter;
import ntk.android.base.entitymodel.hypershop.HyperShopOrderContentModel;
import ntk.android.base.utill.FontManager;
import ntk.android.base.view.NViewUtils;
import ntk.android.hyper.R;
import ntk.android.hyper.view.BuyView;

public class HyperOrderContentAdapter extends BaseRecyclerAdapter<HyperShopOrderContentModel, HyperOrderContentAdapter.ItemViewHolder> {


    private final Context context;

    Runnable changePriceMethod;
    Runnable showEmptyMethod;


    public HyperOrderContentAdapter(Context context, List<HyperShopOrderContentModel> products, Runnable o, Runnable updateList) {
        super(products);
        this.context = context;
        this.changePriceMethod = o;
        showEmptyMethod = updateList;
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
        } else
            holder.itemView.setPadding(0, 0, 0, 0);
        HyperShopOrderContentModel item = list.get(position);
        holder.txtItemName.setText(item.Name);
        holder.txtProductPrice.setText(NViewUtils.PriceFormat(item.Price) + " " + item.CURRENCY_UNIT);
        holder.buyView.bind(item, changePriceMethod, () -> {
            list.remove(position);
            notifyDataSetChanged();
            showEmptyMethod.run();
        });
    }


    protected class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView txtItemName;
        TextView txtProductPrice;
        BuyView buyView;

        ItemViewHolder(View itemView) {
            super(itemView);
            Typeface typeface = FontManager.T1_Typeface(context);
            Typeface typefaceBold = FontManager.T1_BOLD_Typeface(context);
            this.txtItemName = itemView.findViewById(R.id.txtShopName);
            txtItemName.setTypeface(typeface);
            this.txtProductPrice = itemView.findViewById(R.id.txtShopPrice);
            txtProductPrice.setTypeface(typefaceBold);
            this.buyView = itemView.findViewById(R.id.buyView);

        }

    }
}
