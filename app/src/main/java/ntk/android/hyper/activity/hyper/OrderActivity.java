package ntk.android.hyper.activity.hyper;

import android.os.Bundle;

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
import ntk.android.hyper.prefrense.OrderPref;

class OrderActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frag_container);
        showFragment();
    }

    public void showFragment() {
        OrderContentListFragment fragment = new OrderContentListFragment();
        fragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commitNow();

    }

    public void addOrder() {
        HyperShopOrderDtoModel order = new OrderPref(this).getOrder();
        new HyperShopOrderService(this).orderAdd(order).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new NtkObserver<ErrorException<HyperShopOrderDtoModel>>() {
            @Override
            public void onNext(@NonNull ErrorException<HyperShopOrderDtoModel> response) {
                if (response.IsSuccess)
                    //todo show animate
                    ;
                else
                    Toasty.error(OrderActivity.this, response.ErrorMessage).show();
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Toasty.error(OrderActivity.this, "خطای سامانه").show();
            }
        });
    }
}
