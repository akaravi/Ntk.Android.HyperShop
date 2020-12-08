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

}
