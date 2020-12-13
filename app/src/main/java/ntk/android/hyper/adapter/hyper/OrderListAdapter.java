package ntk.android.hyper.adapter.hyper;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ntk.android.base.adapter.BaseRecyclerAdapter;
import ntk.android.base.entitymodel.enums.enumHyperShopPaymentType;
import ntk.android.base.entitymodel.hypershop.HyperShopOrderModel;
import ntk.android.hyper.R;

public class OrderListAdapter extends BaseRecyclerAdapter<HyperShopOrderModel, OrderListAdapter.VH> {
    public OrderListAdapter(Context context, List<HyperShopOrderModel> arrays) {
        super(arrays);

    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(inflate(parent, R.layout.order_list_item_recycler));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        HyperShopOrderModel item = getItem(position);
        holder.title.setText("فاکتور # " + (position + 1));
        holder.date.setText(item.CreatedDate.getDate() + "");
        holder.time.setText(item.CreatedDate.getDate() + "");
        holder.totalPrice.setText("مجموع : " + item.Amount);
        holder.showPaymentType(item);
        holder.showPayState(item);

    }

    public class VH extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView date;
        public TextView time;
        public TextView totalPrice;

        public VH(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txtOrderNum);
            date = itemView.findViewById(R.id.txtDate);
            time = itemView.findViewById(R.id.txtTime);
            totalPrice = itemView.findViewById(R.id.txtTotalPrice);
        }

        public void showPaymentType(HyperShopOrderModel model) {

            View onlineType = itemView.findViewById(R.id.onlinePaymentType);
            View onPlaceType = itemView.findViewById(R.id.placeType);
            View instalmentType = itemView.findViewById(R.id.instalmentType);
            if (model.PaymentType == enumHyperShopPaymentType.Online.index()) {
                onlineType.setVisibility(View.VISIBLE);
                onPlaceType.setVisibility(View.GONE);
                instalmentType.setVisibility(View.GONE);
            } else if (model.PaymentType == enumHyperShopPaymentType.OnPLace.index()) {
                onPlaceType.setVisibility(View.VISIBLE);
                onlineType.setVisibility(View.GONE);
                instalmentType.setVisibility(View.GONE);
            } else if (model.PaymentType == enumHyperShopPaymentType.OnlineAndOnPlace.index()) {
                instalmentType.setVisibility(View.VISIBLE);
                onPlaceType.setVisibility(View.GONE);
                onlineType.setVisibility(View.GONE);
            } else {
                onPlaceType.setVisibility(View.GONE);
                onlineType.setVisibility(View.GONE);
                instalmentType.setVisibility(View.GONE);
            }

        }

        public void showPayState(HyperShopOrderModel item) {
            View successType = itemView.findViewById(R.id.successPayment);
            View errorType = itemView.findViewById(R.id.errorPayment);

        }
    }
}
