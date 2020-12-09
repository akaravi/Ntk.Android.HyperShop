package ntk.android.hyper.fragment;

import android.content.Intent;
import android.net.Uri;
import android.widget.AutoCompleteTextView;

import java.util.List;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import ntk.android.base.Extras;
import ntk.android.base.NTKApplication;
import ntk.android.base.config.NtkObserver;
import ntk.android.base.dtomodel.bankpayment.BankPaymentOnlineTransactionModel;
import ntk.android.base.dtomodel.hypershop.HyperShopOrderPaymentDtoModel;
import ntk.android.base.entitymodel.bankpayment.BankPaymentPrivateSiteConfigModel;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.base.FilterDataModel;
import ntk.android.base.fragment.BaseFragment;
import ntk.android.base.services.bankpayment.BankPaymentPrivateSiteConfigService;
import ntk.android.base.services.hypershop.HyperShopOrderService;
import ntk.android.hyper.R;
import ntk.android.hyper.activity.hyper.CheckPaymentActivity;
import ntk.android.hyper.adapter.BankSelectAdapter;

public class BankPaymentListFragment extends BaseFragment {
    Long OrderId;
    long BankId;

    @Override
    public void onCreateFragment() {
        setContentView(R.layout.hypershop_bankpayment_fragment);
    }

    @Override
    public void onCreated() {
        super.onCreated();
        assert getArguments() != null;
        OrderId = getArguments().getLong(Extras.EXTRA_FIRST_ARG, 0);
        FilterDataModel filterDataModel = new FilterDataModel();
        filterDataModel.RowPerPage = 20;
        switcher.showProgressView();
        new BankPaymentPrivateSiteConfigService(getContext()).getAll(filterDataModel)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new NtkObserver<ErrorException<BankPaymentPrivateSiteConfigModel>>() {
                    @Override
                    public void onNext(@NonNull ErrorException<BankPaymentPrivateSiteConfigModel> banks) {
                        switcher.showContentView();
                        if (banks.IsSuccess)
                            bindView(banks.ListItems);
                        else
                            Toasty.error(getContext(), banks.ErrorMessage).show();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toasty.error(getContext(), e.toString()).show();

                    }
                });
    }

    private void bindView(List<BankPaymentPrivateSiteConfigModel> listItems) {


        AutoCompleteTextView paymentType = (AutoCompleteTextView) findViewById(R.id.etPaymentBank);
        paymentType.setAdapter(new BankSelectAdapter(getContext(), listItems));
        paymentType.setOnItemClickListener((adapterView, view12, i, l) -> {
            BankId = ((BankPaymentPrivateSiteConfigModel) adapterView.getItemAtPosition(i)).Id;
            if (i >= 0)
                paymentType.setText(((BankPaymentPrivateSiteConfigModel) adapterView.getItemAtPosition(i)).Title);
            else
                paymentType.setText("");
        });
        findViewById(R.id.btnSubmit).setOnClickListener(view -> {
            if (BankId <= 0)
                Toasty.error(getContext(), "لطفا یک درگاه را انتخاب نمایید").show();
            else
                callPayment();
        });
    }

    private void callPayment() {
        HyperShopOrderPaymentDtoModel req = new HyperShopOrderPaymentDtoModel();
        req.LinkOrderId = OrderId;
        CheckPaymentActivity.Last_Order_Id = OrderId;
        req.BankPaymentPrivateId = BankId;
        req.LastUrlAddressInUse = "oco.ir/" + NTKApplication.get().getApplicationParameter().APPLICATION_ID();
        new HyperShopOrderService(getContext()).orderPayment(req)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new NtkObserver<ErrorException<BankPaymentOnlineTransactionModel>>() {
            @Override
            public void onNext(@io.reactivex.annotations.NonNull ErrorException<BankPaymentOnlineTransactionModel> response) {
                if (response.IsSuccess) {
                    if (response.Item.LastUrlAddressInUse != null) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(response.Item.LastUrlAddressInUse));
                        getContext().startActivity(browserIntent);
                    } else {
                        Toasty.error(getContext(), "وبگاه پرداخت فعلا در دسترس نیست لطفا بعدا تلاش فرمایید " +
                                "").show();
                    }
                } else {
                    Toasty.error(getContext(), response.ErrorMessage).show();

                }
            }

            @Override
            public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                Toasty.error(getContext(), "خطای سامانه").show();
            }
        });
    }


}
