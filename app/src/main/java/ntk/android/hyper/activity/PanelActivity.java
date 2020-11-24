package ntk.android.hyper.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.yalantis.guillotine.animation.GuillotineAnimation;

import ntk.android.base.activity.BaseActivity;
import ntk.android.hyper.R;
import ntk.android.hyper.fragment.ShopContentListFragment;

public class PanelActivity extends BaseActivity {

    private static final long RIPPLE_DURATION = 250;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.panel_activity);

        View guillotineMenu = LayoutInflater.from(this).inflate(R.layout.panel_drawer, null);
        ((FrameLayout) findViewById(R.id.root)).addView(guillotineMenu);

        new GuillotineAnimation.GuillotineBuilder(guillotineMenu,
                guillotineMenu.findViewById(R.id.guillotine_hamburger),
                findViewById(R.id.content_hamburger))
                .setStartDelay(RIPPLE_DURATION)
                .setActionBarViewForAnimation(findViewById(R.id.toolbar))
                .setClosedOnStart(true)
                .build();
        ShopContentListFragment fragment = new ShopContentListFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commitNow();
    }
}
