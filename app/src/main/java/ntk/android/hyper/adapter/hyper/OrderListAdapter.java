package ntk.android.hyper.adapter.hyper;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.List;

import ntk.android.base.Extras;
import ntk.android.base.adapter.BaseRecyclerAdapter;
import ntk.android.base.entitymodel.enums.enumHyperShopPaymentType;
import ntk.android.base.entitymodel.hypershop.HyperShopOrderModel;
import ntk.android.base.utill.AppUtill;
import ntk.android.base.utill.FontManager;
import ntk.android.hyper.R;
import ntk.android.hyper.activity.hyper.PaidOrderDetailActivity;

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
        holder.date.setText(AppUtill.GregorianToPersian(item.CreatedDate) + "");
        holder.time.setText(new SimpleDateFormat("HH:mm:ss").format(item.CreatedDate) + "");
        holder.totalPrice.setText("مجموع : " + item.Amount);
        holder.showPaymentType(item);
        holder.showPayState(item);
        holder.factorDetails.setOnClickListener(view -> {
            Intent i = new Intent(view.getContext(), PaidOrderDetailActivity.class);
            i.putExtra(Extras.EXTRA_FIRST_ARG, item.Id);
            view.getContext().startActivity(i);
        });

    }

    public class VH extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView date;
        public TextView time;
        public TextView totalPrice;
        MaterialButton factorDetails;

        public VH(@NonNull View itemView) {
            super(itemView);
            Typeface tBold = FontManager.T1_BOLD_Typeface(itemView.getContext());
            Typeface t1 = FontManager.T1_Typeface(itemView.getContext());
            title = itemView.findViewById(R.id.txtOrderNum);
            date = itemView.findViewById(R.id.txtDate);
            time = itemView.findViewById(R.id.txtTime);
            totalPrice = itemView.findViewById(R.id.txtTotalPrice);
            title.setTypeface(tBold);
            time.setTypeface(t1);
            date.setTypeface(t1);
            totalPrice.setTypeface(t1);
            ((TextView) itemView.findViewById(R.id.txt1)).setTypeface(t1);
            ((TextView) itemView.findViewById(R.id.txt2)).setTypeface(t1);
            ((TextView) itemView.findViewById(R.id.txt3)).setTypeface(t1);
            ((TextView) itemView.findViewById(R.id.txt4)).setTypeface(t1);
            ((TextView) itemView.findViewById(R.id.txt5)).setTypeface(t1);
            factorDetails = itemView.findViewById(R.id.paidFactorBtn);
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
            if (item.SystemMicroServiceIsSuccess) {
                itemView.findViewById(R.id.paidFactorBtn).setVisibility(View.VISIBLE);
                successType.setVisibility(View.VISIBLE);
                errorType.setVisibility(View.GONE);
            } else {
                successType.setVisibility(View.GONE);
                errorType.setVisibility(View.VISIBLE);
            }
        }
    }
}
