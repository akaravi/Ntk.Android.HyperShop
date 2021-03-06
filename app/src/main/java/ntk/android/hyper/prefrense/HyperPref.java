package ntk.android.hyper.prefrense;

import android.content.Context;

import ntk.android.base.utill.prefrense.EasyPreference;

public class HyperPref {
    private final Context c;

    public HyperPref(Context context) {
        this.c = context;
    }

    public String mobile() {
        return EasyPreference.with(c).getString("ntk_hyper_mobile", "");
    }

    public void setMobile(String s) {
        EasyPreference.with(c).addString("ntk_hyper_mobile", s);
    }

    public String name() {
        return EasyPreference.with(c).getString("ntk_hyper_name", "");
    }

    public void setName(String s) {
        EasyPreference.with(c).addString("ntk_hyper_name", s);
    }

    public String lastName() {
        return EasyPreference.with(c).getString("ntk_hyper_lastname", "");
    }

    public void setLastName(String s) {
        EasyPreference.with(c).addString("ntk_hyper_lastname", s);
    }

    public String address() {
        return EasyPreference.with(c).getString("ntk_hyper_address", "");
    }

    public void setAddress(String s) {
        EasyPreference.with(c).addString("ntk_hyper_address", s);
    }

    public void setLastOrder(Long orderId) {
        EasyPreference.with(c).addLong("ntk_hyper_Last_order", orderId);
    }

    public Long lastOrder() {
        return EasyPreference.with(c).getLong("ntk_hyper_Last_order", 0);
    }

    public void setLastBank(String bankUrl) {
        EasyPreference.with(c).addString("ntk_hyper_last_bank_url", bankUrl);
    }

    public String lastBank() {
        return EasyPreference.with(c).getString("ntk_hyper_last_bank_url", "");
    }
}
