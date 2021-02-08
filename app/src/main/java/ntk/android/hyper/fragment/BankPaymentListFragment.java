package ntk.android.hyper.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;

import androidx.annotation.Nullable;

import java.util.List;

import es.dmoral.toasty.Toasty;
import io.reactivex.annotations.NonNull;
import ntk.android.base.Extras;
import ntk.android.base.NTKApplication;
import ntk.android.base.config.NtkObserver;
import ntk.android.base.config.ServiceExecute;
import ntk.android.base.dtomodel.bankpayment.BankPaymentOnlineTransactionModel;
import ntk.android.base.dtomodel.hypershop.HyperShopOrderPaymentDtoModel;
import ntk.android.base.entitymodel.bankpayment.BankPaymentPrivateSiteConfigModel;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.base.FilterModel;
import ntk.android.base.fragment.BaseFragment;
import ntk.android.base.services.bankpayment.BankPaymentPrivateSiteConfigService;
import ntk.android.base.services.hypershop.HyperShopOrderService;
import ntk.android.hyper.R;
import ntk.android.hyper.activity.hyper.CheckPaymentActivity;
import ntk.android.hyper.adapter.BankSelectAdapter;

public class BankPaymentListFragment extends BaseFragment {
    Long OrderId;
    long BankId;
    double productsPrice;
    double taxPrice;
    double transportPrice;
    double totalPrice;

    @Override
    public void onCreateFragment() {
        setContentView(R.layout.hypershop_bankpayment_fragment);
    }

    @Override
    public void onCreated() {
        super.onCreated();
        assert getArguments() != null;
        OrderId = getArguments().getLong(Extras.EXTRA_FIRST_ARG, 0);
        taxPrice = getArguments().getDouble(Extras.EXTRA_SECOND_ARG, 0);
        transportPrice = getArguments().getDouble(Extras.Extra_THIRD_ARG, 0);
        productsPrice = getArguments().getDouble(Extras.Extra_4_ARG, 0);

    }

    @Override
    public void onViewCreated(@androidx.annotation.NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FilterModel filterDataModel = new FilterModel();
        filterDataModel.RowPerPage = 20;
        switcher.showProgressView();
        ServiceExecute.execute(new BankPaymentPrivateSiteConfigService(getContext()).getAll(filterDataModel))
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

        AutoCompleteTextView paymentType = findViewById(R.id.etPaymentBank);
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
        CheckPaymentActivity.SET_LAST_ORDER(getContext(), OrderId);
        req.BankPaymentPrivateId = BankId;
        req.LastUrlAddressInUse = "" + NTKApplication.get().getApplicationParameter().APPLICATION_ID() + "://" + "hypershop";
        ServiceExecute.execute(new HyperShopOrderService(getContext()).orderPayment(req))
                .subscribe(new NtkObserver<ErrorException<BankPaymentOnlineTransactionModel>>() {
                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull ErrorException<BankPaymentOnlineTransactionModel> response) {
                        if (response.IsSuccess) {
                            if (response.Item.UrlToPay != null && !response.Item.UrlToPay.equalsIgnoreCase("")) {
                                CheckPaymentActivity.SET_LAST_PAY_URL(getContext(), response.Item.UrlToPay);
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(response.Item.UrlToPay));
                                getContext().startActivity(browserIntent);
                                getActivity().finish();
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
