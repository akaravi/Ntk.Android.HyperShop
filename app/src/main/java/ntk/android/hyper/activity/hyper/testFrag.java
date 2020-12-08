package ntk.android.hyper.activity.hyper;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import ntk.android.base.activity.BaseActivity;
import ntk.android.hyper.R;
import ntk.android.hyper.fragment.OrderOtherDetailFragment;

public class testFrag extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_other_detail);

//        OrderOtherDetailFragment fragment = new OrderOtherDetailFragment();
//        fragment.setArguments(getIntent().getExtras());
//        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commitNow();s
        switcher.showContentViewImmediately();
    }
}
