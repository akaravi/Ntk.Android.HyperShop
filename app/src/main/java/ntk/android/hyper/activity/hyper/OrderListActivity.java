package ntk.android.hyper.activity.hyper;

import androidx.recyclerview.widget.RecyclerView;

import io.reactivex.Observable;
import java9.util.function.Function;
import ntk.android.base.activity.common.BaseFilterModelListActivity;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.base.FilterDataModel;
import ntk.android.base.entitymodel.hypershop.HyperShopOrderModel;
import ntk.android.base.services.hypershop.HyperShopOrderService;
import ntk.android.hyper.adapter.hyper.OrderListAdapter;

public class OrderListActivity extends BaseFilterModelListActivity<HyperShopOrderModel> {
    @Override
    public Function<FilterDataModel, Observable<ErrorException<HyperShopOrderModel>>> getService() {
        return new HyperShopOrderService(this)::getAll;
    }

    @Override
    public RecyclerView.Adapter createAdapter() {
        return new OrderListAdapter(this,models);
    }

    @Override
    public void ClickSearch() {

    }
}
