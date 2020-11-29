package ntk.android.hyper.activity.hyper;

import android.os.Bundle;
import android.view.View;

import org.jetbrains.annotations.Nullable;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import ntk.android.base.activity.BaseActivity;
import ntk.android.base.config.NtkObserver;
import ntk.android.base.dtomodel.hypershop.HyperShopOrderDtoModel;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.services.hypershop.HyperShopOrderService;
import ntk.android.hyper.R;
import ntk.android.hyper.fragment.OrderContentListFragment;
import ntk.android.hyper.fragment.OrderOtherDetailFragment;
import ntk.android.hyper.prefrense.OrderPref;

public class OrderActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_activity);
        showProductFragment();
    }

    public void showProductFragment() {
        OrderContentListFragment fragment = new OrderContentListFragment();

        findViewById(R.id.bottomLayout).setVisibility(View.VISIBLE);
        findViewById(R.id.btnGoToDetail).setOnClickListener(view -> {
            fragment.updateOrder();
            showOrderDetail();
        });

        fragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commitNow();
    }

    public void showOrderDetail() {
        findViewById(R.id.bottomLayout).setVisibility(View.GONE);
        OrderOtherDetailFragment fragment = new OrderOtherDetailFragment();
        fragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commitNow();

    }

    public void addOrder() {
        HyperShopOrderDtoModel order = new OrderPref(this).getOrder();
        switcher.showProgressView();
        new HyperShopOrderService(this).orderAdd(order).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new NtkObserver<ErrorException<HyperShopOrderDtoModel>>() {
            @Override
            public void onNext(@NonNull ErrorException<HyperShopOrderDtoModel> response) {
                switcher.showContentView();
                if (response.IsSuccess) {
                    Toasty.success(OrderActivity.this, "سفارش شما ثبت شد").show();
                    //todo show animate
                    finish();
                } else
                    Toasty.error(OrderActivity.this, response.ErrorMessage).show();
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Toasty.error(OrderActivity.this, "خطای سامانه").show();
            }
        });
    }
}
