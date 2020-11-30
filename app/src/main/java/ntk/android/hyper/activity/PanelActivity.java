package ntk.android.hyper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.BounceInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.yalantis.guillotine.animation.GuillotineAnimation;
import com.yalantis.guillotine.interfaces.GuillotineListener;

import java.util.ArrayList;
import java.util.List;

import ntk.android.base.activity.BaseActivity;
import ntk.android.hyper.R;
import ntk.android.hyper.activity.hyper.OrderActivity;
import ntk.android.hyper.fragment.ShopContentListFragment;

public class PanelActivity extends BaseActivity {

    private static final long RIPPLE_DURATION = 250;
    List<LinearLayout> btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.panel_activity);

        View guillotineMenu = LayoutInflater.from(this).inflate(R.layout.panel_drawer, null);
        ((FrameLayout) findViewById(R.id.root)).addView(guillotineMenu);
        findViewById(R.id.cartView).setOnClickListener(view -> startActivity(new Intent(PanelActivity.this, OrderActivity.class)));
        new GuillotineAnimation.GuillotineBuilder(guillotineMenu,
                guillotineMenu.findViewById(R.id.guillotine_hamburger),
                findViewById(R.id.content_hamburger))
                .setStartDelay(RIPPLE_DURATION)
                .setActionBarViewForAnimation(findViewById(R.id.toolbar))
                .setClosedOnStart(true)
                .setGuillotineListener(new GuillotineListener() {
                    @Override
                    public void onGuillotineOpened() {
                        setAnimation(guillotineMenu, true);
                    }

                    @Override
                    public void onGuillotineClosed() {
                        setAnimation(guillotineMenu,false);
                    }
                })
                .build();
        ShopContentListFragment fragment = new ShopContentListFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commitNow();
    }

    private void setAnimation(View v, boolean b) {
        btn = new ArrayList() {{
            add(findViewById(R.id.newsBtn));
            add(findViewById(R.id.poolingBtn));
            add(findViewById(R.id.searchBtn));
            add(findViewById(R.id.inviteBtn));
            add(findViewById(R.id.feedbackBtn));
            add(findViewById(R.id.questionBtn));
            add(findViewById(R.id.introBtn));
            add(findViewById(R.id.blogBtn));
            add(findViewById(R.id.aboutUsBtn));
            add(findViewById(R.id.supportBtn));
            add(findViewById(R.id.messageBtn));

        }};
        if (b) {
//            AlphaAnimation alphaAnimation = new AlphaAnimation(0.5f, 1.0f);
//            alphaAnimation.setDuration(1000);
//            alphaAnimation.setFillBefore(true);
//            alphaAnimation.setFillAfter(true);
//            alphaAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
            ScaleAnimation scaleAnimation = new ScaleAnimation(0.5f, 1.0f, 0.5f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            scaleAnimation.setDuration(1500);
            scaleAnimation.setFillAfter(true);
            scaleAnimation.setInterpolator(new BounceInterpolator());
            AnimationSet animationSet = new AnimationSet(false);
//            animationSet.addAnimation(alphaAnimation);
            animationSet.addAnimation(scaleAnimation);
            for (int i = 0; i < btn.size(); i++) {
                btn.get(i).startAnimation(scaleAnimation);
            }
        } else {
//            AlphaAnimation alphaAnimation = new AlphaAnimation( 1.0f,0.0f);
//            alphaAnimation.setDuration(1000);
//            alphaAnimation.setFillAfter(true);
//            for (int i = 0; i < btn.size(); i++) {
//                btn.get(i).startAnimation(alphaAnimation);
//            }
        }
//        v.startAnimation(alphaAnimation);
    }

}
