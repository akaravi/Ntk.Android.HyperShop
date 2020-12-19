package ntk.android.hyper.activity.hyper;

import android.graphics.Color;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import io.reactivex.Observable;
import java9.util.function.Function;
import ntk.android.base.activity.common.BaseFilterModelListActivity;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.base.FilterDataModel;
import ntk.android.base.entitymodel.hypershop.HyperShopPaymentModel;
import ntk.android.base.services.hypershop.HyperShopPaymentService;
import ntk.android.hyper.R;
import ntk.android.hyper.adapter.hyper.HyperTransitionAdapter;

public class HyperTransactionListActivity extends BaseFilterModelListActivity<HyperShopPaymentModel> {
    @Override
    public void afterInit() {
        super.afterInit();
        findViewById(R.id.imgSearch).setVisibility(View.GONE);

    }

    @Override
    public Function<FilterDataModel, Observable<ErrorException<HyperShopPaymentModel>>> getService() {
        return new HyperShopPaymentService(this)::getAll;
    }

    @Override
    public RecyclerView.Adapter createAdapter() {
        return new HyperTransitionAdapter(models);
    }

}
