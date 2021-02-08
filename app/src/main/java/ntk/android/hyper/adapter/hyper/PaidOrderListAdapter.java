package ntk.android.hyper.adapter.hyper;

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
import ntk.android.hyper.R;

public class PaidOrderListAdapter extends BaseRecyclerAdapter<HyperShopOrderContentModel, PaidOrderListAdapter.VH> {
    public PaidOrderListAdapter(List<HyperShopOrderContentModel> list) {
        super(list);
    }


    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(inflate(parent, R.layout.paid_order_item_recycler));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        HyperShopOrderContentModel item = getItem(position);
        holder.row.setText((position + 1) + "");
        holder.name.setText(item.Name);
        holder.price.setText(new DecimalFormat("###,###,###,###").format(item.Price));
        holder.count.setText(item.Count + "");
        holder.totalPrice.setText(new DecimalFormat("###,###,###,###").format(item.Price * item.Count));
    }

    class VH extends RecyclerView.ViewHolder {
        TextView name;
        TextView totalPrice;
        TextView price;
        TextView count;
        TextView row;

        public VH(@NonNull View itemView) {
            super(itemView);
            row = itemView.findViewById(R.id.txtRow);
            totalPrice = itemView.findViewById(R.id.txtTotalPrice);
            price = itemView.findViewById(R.id.txtPrice);
            name = itemView.findViewById(R.id.txtProductName);
            count = itemView.findViewById(R.id.txtCount);
            Typeface typeface = FontManager.T1_Typeface(itemView.getContext());
            row.setTypeface(typeface);
            totalPrice.setTypeface(typeface);
            price.setTypeface(typeface);
            name.setTypeface(typeface);
            count.setTypeface(typeface);
        }
    }
}
