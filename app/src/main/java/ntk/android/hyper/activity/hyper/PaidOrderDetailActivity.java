package ntk.android.hyper.activity.hyper;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import io.reactivex.annotations.NonNull;
import ntk.android.base.Extras;
import ntk.android.base.activity.BaseActivity;
import ntk.android.base.config.GenericErrors;
import ntk.android.base.config.NtkObserver;
import ntk.android.base.config.ServiceExecute;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.hypershop.HyperShopOrderModel;
import ntk.android.base.services.hypershop.HyperShopOrderService;
import ntk.android.hyper.R;
import ntk.android.hyper.adapter.hyper.PaidOrderListAdapter;

public class PaidOrderDetailActivity extends BaseActivity {
    long OrderId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paid_order_activity);
        OrderId = getIntent().getExtras().getLong(Extras.EXTRA_FIRST_ARG, 0);
        getApi();
    }

    private void getApi() {
        switcher.showProgressView();
        ServiceExecute.execute(new HyperShopOrderService(this).getOne(OrderId)).subscribe(new NtkObserver<ErrorException<HyperShopOrderModel>>() {
            @Override
            public void onNext(@NonNull ErrorException<HyperShopOrderModel> response) {
                switcher.showContentView();
                showData(response.Item);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                new GenericErrors().throwableException(switcher::showErrorView, e, PaidOrderDetailActivity.this::getApi);
            }
        });
    }

    private void showData(HyperShopOrderModel item) {
        RecyclerView rc = findViewById(R.id.rc);
        rc.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        rc.setAdapter(new PaidOrderListAdapter(item.Products));
    }
}
