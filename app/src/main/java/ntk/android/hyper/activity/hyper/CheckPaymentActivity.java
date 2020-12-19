package ntk.android.hyper.activity.hyper;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;

import es.dmoral.toasty.Toasty;
import io.reactivex.annotations.NonNull;
import ntk.android.base.activity.BaseActivity;
import ntk.android.base.config.NtkObserver;
import ntk.android.base.config.ServiceExecute;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.hypershop.HyperShopOrderModel;
import ntk.android.base.services.hypershop.HyperShopOrderService;
import ntk.android.hyper.prefrense.HyperPref;

public class CheckPaymentActivity extends BaseActivity {
    public static long Last_Order_Id;

    public static void LAST_ORDER(Context context, Long orderId) {
        new HyperPref(context).setLastOrder(orderId);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPayment();
    }

    private void checkPayment() {
        ServiceExecute.execute(new HyperShopOrderService(this).getOne(new HyperPref(this).lastOrder()))
                .subscribe(new NtkObserver<ErrorException<HyperShopOrderModel>>() {
                    @Override
                    public void onNext(@NonNull ErrorException<HyperShopOrderModel> resposne) {
                        if (resposne.IsSuccess)
                            Toasty.success(CheckPaymentActivity.this, "پرداخت با موفقیت انجام شد").show();
                        else
                            Toasty.error(CheckPaymentActivity.this, resposne.ErrorMessage).show();
                        finish();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toasty.error(CheckPaymentActivity.this, e.toString()).show();
                        finish();
                    }
                });
    }
}
