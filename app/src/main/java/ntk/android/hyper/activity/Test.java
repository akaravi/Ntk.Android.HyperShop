package ntk.android.hyper.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;

import ntk.android.base.activity.BaseActivity;
import ntk.android.hyper.R;
import ntk.android.hyper.view.BuyView;

public class Test extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BuyView s = new BuyView(this);
        setContentView(R.layout.common_splash_activity);
    }
}
