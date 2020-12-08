package ntk.android.hyper.activity.hyper;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.jetbrains.annotations.Nullable;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import ntk.android.base.Extras;
import ntk.android.base.activity.BaseActivity;
import ntk.android.base.config.NtkObserver;
import ntk.android.base.dtomodel.hypershop.HyperShopOrderDtoModel;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.hypershop.HyperShopOrderModel;
import ntk.android.base.services.hypershop.HyperShopOrderService;
import ntk.android.hyper.R;
import ntk.android.hyper.fragment.BankPaymentListFragment;
import ntk.android.hyper.fragment.OrderContentListFragment;
import ntk.android.hyper.fragment.OrderOtherDetailFragment;
import ntk.android.hyper.prefrense.OrderPref;

public class OrderActivity extends BaseActivity {
    TextView title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_activity);
        title = findViewById(R.id.txtToolbar);

        showProductFragment();
    }

    public void showProductFragment() {
        title.setText("سبد خرید");

        View deleteIcon = findViewById(R.id.imgDeleteOrder);
        deleteIcon.setVisibility(View.VISIBLE);
        deleteIcon.setOnClickListener(view -> deleteOrderDialog());
        OrderContentListFragment fragment = new OrderContentListFragment();
        findViewById(R.id.bottomLayout).setVisibility(View.VISIBLE);
        findViewById(R.id.btnGoToDetail).setOnClickListener(view -> {
            fragment.updateOrder();
            showOrderDetail();
        });

        fragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commitNow();
    }

    private void deleteOrderDialog() {
        //todo show dialog
        Toasty.success(this, "سبد شما کامل حذف گردید").show();
    }

    public void showOrderDetail() {
        title.setText("تایید نهایی");
        findViewById(R.id.bottomLayout).setVisibility(View.GONE);
        OrderOtherDetailFragment fragment = new OrderOtherDetailFragment();
        fragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commitNow();

    }

    public void showBankPayments(long orderId) {
        BankPaymentListFragment fragment = new BankPaymentListFragment();
        Bundle b = new Bundle();
        b.putLong(Extras.EXTRA_FIRST_ARG, orderId);
        fragment.setArguments(b);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commitNow();
    }

    public void addOrder() {
        HyperShopOrderDtoModel order = new OrderPref(this).getOrder();
        switcher.showProgressView();
        new HyperShopOrderService(this).orderAdd(order).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new NtkObserver<ErrorException<HyperShopOrderModel>>() {
            @Override
            public void onNext(@NonNull ErrorException<HyperShopOrderModel> response) {
                switcher.showContentView();
                if (response.IsSuccess) {
                    new OrderPref(OrderActivity.this).clear();
                    Toasty.success(OrderActivity.this, "سفارش شما ثبت شد").show();
                    if (response.Item.Id != null && response.Item.Id > 0)
                        showBankPayments(response.Item.Id);
                    else
                        Toasty.error(OrderActivity.this, response.ErrorMessage).show();

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
