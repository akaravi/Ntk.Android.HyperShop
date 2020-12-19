package ntk.android.hyper.adapter.hyper;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ntk.android.base.adapter.BaseRecyclerAdapter;
import ntk.android.base.entitymodel.hypershop.HyperShopPaymentModel;
import ntk.android.hyper.R;

public class HyperTransitionAdapter extends BaseRecyclerAdapter<HyperShopPaymentModel, HyperTransitionAdapter.VH> {
    public HyperTransitionAdapter(List<HyperShopPaymentModel> list) {
        super(list);
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(inflate(parent, R.layout.row_hyper_transactions));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {

    }

    public class VH extends RecyclerView.ViewHolder {
        public VH(@NonNull View itemView) {
            super(itemView);
        }
    }
}
