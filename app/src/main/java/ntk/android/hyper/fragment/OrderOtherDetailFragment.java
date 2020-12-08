package ntk.android.hyper.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import es.dmoral.toasty.Toasty;
import ntk.android.base.entitymodel.enums.enumHyperShopPaymentType;
import ntk.android.base.fragment.BaseFragment;
import ntk.android.hyper.R;
import ntk.android.hyper.activity.hyper.OrderActivity;
import ntk.android.hyper.prefrense.HyperPref;
import ntk.android.hyper.prefrense.OrderPref;

public class OrderOtherDetailFragment extends BaseFragment {
    @Override
    public void onCreateFragment() {
        setContentView(R.layout.order_other_detail);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        switcher.showContentView();
        findViewById(R.id.btnSubmit).setOnClickListener(view1 -> submit());
        ((EditText) findViewById(R.id.etName)).setText(new HyperPref(getContext()).name());
        ((EditText) findViewById(R.id.etFamily)).setText(new HyperPref(getContext()).lastName());
        ((EditText) findViewById(R.id.etMobile)).setText(new HyperPref(getContext()).mobile());
        ((EditText) findViewById(R.id.etAddress)).setText(new HyperPref(getContext()).address());
    }

    private void submit() {
        EditText name = findViewById(R.id.etName);
        EditText family = findViewById(R.id.etFamily);
        EditText mobile = findViewById(R.id.etMobile);
        EditText address = findViewById(R.id.etAddress);
        EditText desc = findViewById(R.id.etDescription);
        if (name.getText().toString().equalsIgnoreCase("")) {
            Toasty.warning(getContext(), "نام خود را وارد نمایید").show();
            return;
        } else if (family.getText().toString().equalsIgnoreCase("")) {
            Toasty.warning(getContext(), "نام خانوادگی را وارد نمایید").show();
            return;
        } else if (mobile.getText().toString().equalsIgnoreCase("")) {
            Toasty.warning(getContext(), "شماره تلفن همراه خود را وارد نمایید").show();
            return;
        } else if (address.getText().toString().equalsIgnoreCase("")) {
            Toasty.warning(getContext(), "آدرس خود را وارد نمایید").show();
            return;
        } else {
            enumHyperShopPaymentType type = enumHyperShopPaymentType.Online;
            HyperPref hyperPref = new HyperPref(getContext());
            hyperPref.setName(name.getText().toString());
            hyperPref.setLastName(family.getText().toString());
            hyperPref.setMobile(mobile.getText().toString());
            hyperPref.setAddress(address.getText().toString());
            new OrderPref(getContext()).addDetails(
                    name.getText().toString(),
                    family.getText().toString(),
                    mobile.getText().toString(),
                    address.getText().toString(),
                    type
                    );
            ((OrderActivity) getActivity()).addOrder();
        }
    }
}
