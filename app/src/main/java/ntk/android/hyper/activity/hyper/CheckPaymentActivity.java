package ntk.android.hyper.activity.hyper;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.airbnb.lottie.LottieAnimationView;

import es.dmoral.toasty.Toasty;
import io.reactivex.annotations.NonNull;
import ntk.android.base.activity.BaseActivity;
import ntk.android.base.config.NtkObserver;
import ntk.android.base.config.ServiceExecute;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.hypershop.HyperShopOrderModel;
import ntk.android.base.services.hypershop.HyperShopOrderService;
import ntk.android.base.utill.FontManager;
import ntk.android.hyper.R;
import ntk.android.hyper.prefrense.HyperPref;

/**
 * activity baraye nemayesh vaziate pardakhte factor
 * bad az pardakht factor ya enseraf az pardakht dar safe web
 */
public class CheckPaymentActivity extends BaseActivity {

    /**
     * zakhire akharin id factor
     * @param context
     * @param orderId last order id
     */
    public static void SET_LAST_ORDER(Context context, Long orderId) {
        new HyperPref(context).setLastOrder(orderId);
    }

    /**
     * zakhire akharin url bank
     * @param context
     * @param bankUrl
     */
    public static void SET_LAST_PAY_URL(Context context, String bankUrl) {
        new HyperPref(context).setLastBank(bankUrl);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_check_activity);
        checkPayment();
        //set font
        ((TextView) findViewById(R.id.paymentStatus)).setTypeface(FontManager.T1_Typeface(this));
        ((TextView) findViewById(R.id.btn)).setTypeface(FontManager.T1_Typeface(this));

    }

    /**
     * api baraye check kardan vaziate pardakhte factor
     */
    private void checkPayment() {
        ServiceExecute.execute(new HyperShopOrderService(this).getOne(new HyperPref(this).lastOrder()))
                .subscribe(new NtkObserver<ErrorException<HyperShopOrderModel>>() {
                    @Override
                    public void onNext(@NonNull ErrorException<HyperShopOrderModel> resposne) {
                        //hide loading of page
                        findViewById(R.id.inc_loading).setVisibility(View.GONE);
                        //if paid is success
                        if (resposne.Item.SystemMicroServiceIsSuccess)
                            SuccessPage();
                        else
                            unSuccessPage();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toasty.error(CheckPaymentActivity.this, e.toString()).show();
                        finish();
                    }
                });
    }

    //show unSuccess Pay View
    private void unSuccessPage() {
        LottieAnimationView animation = findViewById(R.id.animLottie);
        TextView tv = findViewById(R.id.paymentStatus);
        Button btn = findViewById(R.id.btn);
        animation.setAnimation(R.raw.payment_failed);
        animation.playAnimation();
        tv.setText("عملیات پرداخت با خطا مواجه شد چنانچه وجه از حساب شما کسر گردیده است طی 24 ساعت به حساب شما باز خواهد گشت");
        btn.setText("پرداخت مجدد");
        //click to pay order with last saved bank Url
        btn.setOnClickListener(view -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(new HyperPref(this).lastBank()));
            startActivity(browserIntent);
            finish();
        });
    }
    //show Success Pay View
    private void SuccessPage() {
        LottieAnimationView animation = findViewById(R.id.animLottie);
        TextView tv = findViewById(R.id.paymentStatus);
        tv.setText("فاکتور شما به صورت آنلاین پرداخت شد");
        Button btn = findViewById(R.id.btn);
        animation.setAnimation(R.raw.payment_success);
        animation.playAnimation();
        btn.setText("بستن");
        btn.setOnClickListener(view -> finish());
    }
}
