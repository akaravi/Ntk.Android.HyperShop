package ntk.android.hyper.activity.hyper;

import androidx.recyclerview.widget.RecyclerView;

import io.reactivex.Observable;
import java9.util.function.Function;
import ntk.android.base.activity.common.BaseFilterModelListActivity;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.base.FilterDataModel;
import ntk.android.base.entitymodel.hypershop.HyperShopPaymentModel;

class HyperTransactionListActivity extends BaseFilterModelListActivity<HyperShopPaymentModel> {
    @Override
    public Function<FilterDataModel, Observable<ErrorException<HyperShopPaymentModel>>> getService() {
        return null;
    }

    @Override
    public RecyclerView.Adapter createAdapter() {
        return null;
    }

    @Override
    public void ClickSearch() {

    }
}
