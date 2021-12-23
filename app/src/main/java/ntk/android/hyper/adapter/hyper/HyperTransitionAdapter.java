package ntk.android.hyper.adapter.hyper;

import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;

import ntk.android.base.adapter.BaseRecyclerAdapter;
import ntk.android.base.entitymodel.bankpayment.BankPaymentTransactionModel;
import ntk.android.base.entitymodel.enums.EnumTransactionBankStatus;
import ntk.android.base.entitymodel.enums.EnumTransactionRecordStatus;
import ntk.android.base.utill.AppUtil;
import ntk.android.base.utill.FontManager;
import ntk.android.hyper.R;

public class HyperTransitionAdapter extends BaseRecyclerAdapter<BankPaymentTransactionModel, HyperTransitionAdapter.VH> {
    public HyperTransitionAdapter(List<BankPaymentTransactionModel> list) {
        super(list);
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(inflate(parent, R.layout.row_hyper_transactions));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        BankPaymentTransactionModel item = getItem(position);
        holder.title.setText("پرداخت مبلغ " + item.Amount + " " + item.CurrencyUnit);
        holder.date.setText(AppUtil.GregorianToPersian(item.CreatedDate) + "");
        holder.time.setText(new SimpleDateFormat("HH:mm:ss").format(item.CreatedDate) + "");
        holder.totalPrice.setText(EnumTransactionRecordStatus.get(item.TransactionStatus) + "");
        holder.showPayState(item);
    }

    public class VH extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView date;
        public TextView time;
        public TextView totalPrice;

        public VH(@NonNull View itemView) {
            super(itemView);
            Typeface tBold = FontManager.T1_BOLD_Typeface(itemView.getContext());
            Typeface t1 = FontManager.T1_Typeface(itemView.getContext());
            title = itemView.findViewById(R.id.txtTitle);
            date = itemView.findViewById(R.id.txtDate);
            time = itemView.findViewById(R.id.txtTime);
            totalPrice = itemView.findViewById(R.id.txtPrice);
            title.setTypeface(tBold);
            time.setTypeface(t1);
            date.setTypeface(t1);
            totalPrice.setTypeface(t1);
            ((TextView) itemView.findViewById(R.id.txt1)).setTypeface(t1);
            ((TextView) itemView.findViewById(R.id.txt2)).setTypeface(t1);
        }

        public void showPayState(BankPaymentTransactionModel item) {
            View successType = itemView.findViewById(R.id.successPayment);
            View errorType = itemView.findViewById(R.id.errorPayment);
            if (item.BankStatus == EnumTransactionBankStatus.Paid.index()) {
                successType.setVisibility(View.VISIBLE);
                errorType.setVisibility(View.GONE);
            } else {
                successType.setVisibility(View.GONE);
                errorType.setVisibility(View.VISIBLE);
                ((TextView) itemView.findViewById(R.id.txt2)).setText(EnumTransactionBankStatus.get(item.BankStatus).toString());
            }
        }
    }
}
