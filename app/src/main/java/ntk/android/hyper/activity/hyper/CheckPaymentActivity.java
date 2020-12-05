package ntk.android.hyper.activity.hyper;

import android.os.Bundle;

import androidx.annotation.Nullable;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import ntk.android.base.activity.BaseActivity;
import ntk.android.base.config.NtkObserver;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.hypershop.HyperShopOrderModel;
import ntk.android.base.services.hypershop.HyperShopOrderService;

public class CheckPaymentActivity extends BaseActivity {
    public static long Last_Order_Id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPayment();
    }

    private void checkPayment() {
        new HyperShopOrderService(this).getOne(Last_Order_Id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new NtkObserver<ErrorException<HyperShopOrderModel>>() {
                    @Override
                    public void onNext(@NonNull ErrorException<HyperShopOrderModel> resposne) {
                        if (resposne.IsSuccess)
                            Toasty.success(CheckPaymentActivity.this, "پرداخت با موفقیت انجام شد").show();
                        else
                            Toasty.error(CheckPaymentActivity.this, resposne.ErrorMessage).show();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toasty.error(CheckPaymentActivity.this, e.toString()).show();
                    }
                });
    }
}
