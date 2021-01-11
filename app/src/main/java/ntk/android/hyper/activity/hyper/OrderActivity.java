package ntk.android.hyper.activity.hyper;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.jetbrains.annotations.Nullable;

import es.dmoral.toasty.Toasty;
import io.reactivex.annotations.NonNull;
import ntk.android.base.Extras;
import ntk.android.base.activity.BaseActivity;
import ntk.android.base.config.NtkObserver;
import ntk.android.base.config.ServiceExecute;
import ntk.android.base.dtomodel.hypershop.HyperShopOrderDtoModel;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.enums.enumHyperShopPaymentType;
import ntk.android.base.entitymodel.hypershop.HyperShopOrderModel;
import ntk.android.base.services.hypershop.HyperShopOrderService;
import ntk.android.hyper.R;
import ntk.android.hyper.fragment.BankPaymentListFragment;
import ntk.android.hyper.fragment.OrderContentListFragment;
import ntk.android.hyper.fragment.OrderOtherDetailFragment;
import ntk.android.hyper.prefrense.OrderPref;

public class OrderActivity extends BaseActivity {
    TextView title;
    private int stepNumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_activity);
        title = findViewById(R.id.txtToolbar);
        findViewById(R.id.back_button).setOnClickListener(view -> finish());
        showProductFragment();
    }

    public void showBottom() {
        findViewById(R.id.bottomLayout).setVisibility(View.VISIBLE);
        findViewById(R.id.imgDeleteOrder).setVisibility(View.VISIBLE);
    }

    public void showProductFragment() {
        stepNumber = 1;
        title.setText("سبد خرید");
        findViewById(R.id.imgDeleteOrder).setVisibility(View.VISIBLE);

        OrderContentListFragment fragment = new OrderContentListFragment();
        findViewById(R.id.btnGoToDetail).setOnClickListener(view -> {
            fragment.updateOrder();
            showOrderDetail();
        });

        fragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commitNow();
    }


    public void showOrderDetail() {
        stepNumber = 2;
        title.setText("تایید نهایی");
        findViewById(R.id.imgDeleteOrder).setVisibility(View.GONE);
        findViewById(R.id.bottomLayout).setVisibility(View.GONE);
        OrderOtherDetailFragment fragment = new OrderOtherDetailFragment();
        fragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commitNow();

    }

    public void showBankPayments(HyperShopOrderModel item) {
        stepNumber = 3;
        long orderId = item.Id;
        String TotalProductPrice = item.ProducsSumPrice;
        String DeliveryPrice = item.DelivaryPrice;
        BankPaymentListFragment fragment = new BankPaymentListFragment();
        Bundle b = new Bundle();
        b.putLong(Extras.EXTRA_FIRST_ARG, orderId);
        b.putString(Extras.EXTRA_SECOND_ARG, TotalProductPrice);
        b.putString(Extras.Extra_THIRD_ARG, DeliveryPrice);
//        b.putString(Extras.Extra_FORTH_ARG, DeliveryPrice);
        fragment.setArguments(b);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commitNow();
    }

    public void addOrder() {
        HyperShopOrderDtoModel order = new OrderPref(this).getOrder();
        switcher.showProgressView();
        ServiceExecute.execute(new HyperShopOrderService(this).orderAdd(order))
                .subscribe(new NtkObserver<ErrorException<HyperShopOrderModel>>() {
                    @Override
                    public void onNext(@NonNull ErrorException<HyperShopOrderModel> response) {
                        switcher.showContentView();
                        if (response.IsSuccess) {
                    new OrderPref(OrderActivity.this).clear();
                            Toasty.success(OrderActivity.this, "سفارش شما ثبت شد").show();
                            if (response.Item.PaymentType == enumHyperShopPaymentType.Online.index() || response.Item.PaymentType == enumHyperShopPaymentType.OnlineAndOnPlace.index())
                                showBankPayments(response.Item);
                            else
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

    @Override
    public void onBackPressed() {
        stepNumber--;
        if (stepNumber == 2)
            showOrderDetail();
        else if (stepNumber == 1)
            showProductFragment();
        else super.onBackPressed();
    }
}
