package ntk.android.hyper.activity.hyper;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import io.reactivex.Observable;
import java9.util.function.Function;
import ntk.android.base.activity.common.BaseFilterModelListActivity;
import ntk.android.base.entitymodel.bankpayment.BankPaymentTransactionModel;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.base.FilterDataModel;
import ntk.android.base.entitymodel.base.FilterModel;
import ntk.android.base.services.bankpayment.BankPaymentTransactionService;
import ntk.android.hyper.R;
import ntk.android.hyper.adapter.hyper.HyperTransitionAdapter;

public class HyperTransactionListActivity extends BaseFilterModelListActivity<BankPaymentTransactionModel> {
    @Override
    public void afterInit() {
        super.afterInit();
        findViewById(R.id.imgSearch).setVisibility(View.GONE);

    }

    @Override
    public Function<FilterModel, Observable<ErrorException<BankPaymentTransactionModel>>> getService() {
        return new BankPaymentTransactionService(this)::getAll;
    }

    @Override
    public RecyclerView.Adapter createAdapter() {
        return new HyperTransitionAdapter(models);
    }

}
