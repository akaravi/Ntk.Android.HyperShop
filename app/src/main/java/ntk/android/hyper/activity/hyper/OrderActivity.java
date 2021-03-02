package ntk.android.hyper.activity.hyper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.jetbrains.annotations.Nullable;

import es.dmoral.toasty.Toasty;
import io.reactivex.annotations.NonNull;
import ntk.android.base.Extras;
import ntk.android.base.activity.BaseActivity;
import ntk.android.base.activity.common.AuthWithSmsActivity;
import ntk.android.base.config.NtkObserver;
import ntk.android.base.config.ServiceExecute;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.enums.enumHyperShopPaymentType;
import ntk.android.base.entitymodel.hypershop.HyperShopOrderModel;
import ntk.android.base.services.hypershop.HyperShopOrderService;
import ntk.android.base.utill.prefrense.Preferences;
import ntk.android.base.view.dialog.SweetAlertDialog;
import ntk.android.hyper.R;
import ntk.android.hyper.fragment.BankPaymentListFragment;
import ntk.android.hyper.fragment.OrderContentListFragment;
import ntk.android.hyper.fragment.OrderOtherDetailFragment;
import ntk.android.hyper.prefrense.OrderPref;

public class OrderActivity extends BaseActivity {

    public static void START_ORDER_ACTIVITY(Context c) {
        if (Preferences.with(c).UserInfo().userId() > 0)
            c.startActivity(new Intent(c, OrderActivity.class));
        else {
            SweetAlertDialog dialog = new SweetAlertDialog(c, SweetAlertDialog.ERROR_TYPE);
            dialog.setTitle("خطا در انجام عملیات");
            dialog.setContentText("برای ادامه فرایند خرید نیاز است که به حساب خود وارد شوید. آیا مایلید به صفحه ی ورود هدایت شوید؟");
            dialog.setConfirmButton("بلی", d -> {
                Preferences.with(d.getContext()).appVariableInfo().set_registerNotInterested(false);
                Intent i = new Intent(d.getContext(), AuthWithSmsActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                d.getContext().startActivity(i);
                d.dismiss();
            });
            dialog.setCancelButton("تمایل ندارم", SweetAlertDialog::dismiss);
            dialog.show();
        }
    }

    TextView title;
    private int stepNumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_activity);
        title = findViewById(R.id.txtToolbar);
        findViewById(R.id.back_button).setOnClickListener(view -> onBackPressed());
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
        title.setText("مشخصات");
        findViewById(R.id.imgDeleteOrder).setVisibility(View.GONE);
        findViewById(R.id.bottomLayout).setVisibility(View.GONE);
        OrderOtherDetailFragment fragment = new OrderOtherDetailFragment();
        fragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commitNow();

    }

    public void showBankPayments(HyperShopOrderModel item) {
        stepNumber = 3;
        long orderId = item.Id;
        BankPaymentListFragment fragment = new BankPaymentListFragment();
        Bundle b = new Bundle();
        b.putLong(Extras.EXTRA_FIRST_ARG, orderId);

        fragment.setArguments(b);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commitNow();
    }

    public void addOrder() {
        HyperShopOrderModel order = new OrderPref(this).getOrder();
        switcher.showProgressView();

        ServiceExecute.execute((order.Id==null||order.Id == 0) ? new HyperShopOrderService(this).add(order) : new HyperShopOrderService(this).edit(order))
                .subscribe(new NtkObserver<ErrorException<HyperShopOrderModel>>() {
                    @Override
                    public void onNext(@NonNull ErrorException<HyperShopOrderModel> response) {
                        switcher.showContentView();
                        if (response.IsSuccess) {
                            new OrderPref(OrderActivity.this).clear();
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
