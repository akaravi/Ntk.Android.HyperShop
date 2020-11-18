package ntk.android.hyper.activity.hyper;

import android.os.Bundle;

import org.jetbrains.annotations.Nullable;

import ntk.android.base.activity.BaseActivity;
import ntk.android.hyper.R;
import ntk.android.hyper.fragment.ShopContentListFragment;

/**
 * Created by m.parishani on 12/20/2017.
 */

public class ShopContentListActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frag_container);
        showFragment();
    }

    public void showFragment() {
        ShopContentListFragment fragment = new ShopContentListFragment();
        fragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commitNow();

    }

}
