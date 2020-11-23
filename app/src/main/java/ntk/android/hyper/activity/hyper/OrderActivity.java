package ntk.android.hyper.activity.hyper;

import android.os.Bundle;

import org.jetbrains.annotations.Nullable;

import ntk.android.base.activity.BaseActivity;
import ntk.android.hyper.R;
import ntk.android.hyper.fragment.OrderContentListFragment;

class OrderActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frag_container);
        showFragment();
    }

    public void showFragment() {
        OrderContentListFragment fragment = new OrderContentListFragment();
        fragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commitNow();

    }
}
