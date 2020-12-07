package ntk.android.hyper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.yalantis.guillotine.animation.GuillotineAnimation;
import com.yalantis.guillotine.interfaces.GuillotineListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import ntk.android.base.activity.abstraction.AbstractMainActivity;
import ntk.android.base.activity.common.NotificationsActivity;
import ntk.android.base.activity.poling.PolingActivity;
import ntk.android.base.activity.ticketing.TicketListActivity;
import ntk.android.base.activity.ticketing.TicketSearchActivity;
import ntk.android.hyper.R;
import ntk.android.hyper.activity.hyper.OrderActivity;
import ntk.android.hyper.fragment.ShopContentListFragment;
import ntk.android.hyper.fragment.ShopContentList_1_Fragment;

public class MainActivity extends AbstractMainActivity {

    private static final long RIPPLE_DURATION = 250;
    List<LinearLayout> btn;
    GuillotineAnimation menuAnim;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.panel_activity);
        View guillotineMenu = LayoutInflater.from(this).inflate(R.layout.panel_drawer, null);
        ((FrameLayout) findViewById(R.id.root)).addView(guillotineMenu);
        ButterKnife.bind(this);
        findViewById(R.id.cartView).setOnClickListener(view -> startActivity(new Intent(MainActivity.this, OrderActivity.class)));
        menuAnim = new GuillotineAnimation.GuillotineBuilder(guillotineMenu,
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
                        setAnimation(guillotineMenu, false);
                    }
                })
                .build();
        ShopContentList_1_Fragment fragment = new ShopContentList_1_Fragment();
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

    @OnClick(R.id.supportBtn)
    public void onSupportClick() {
        this.startActivity(new Intent(this, TicketListActivity.class));
    }

    @OnClick(R.id.searchBtn)
    public void onSearchClick() {
        this.startActivity(new Intent(this, TicketSearchActivity.class));
    }

    @OnClick(R.id.messageBtn)
    public void onInboxClick() {
        this.startActivity(new Intent(this, NotificationsActivity.class));
    }

    @OnClick(R.id.newsBtn)
    public void onNewsClick() {
        this.startActivity(new Intent(this, NewsListActivity.class));
    }

    @OnClick(R.id.feedbackBtn)
    public void onFeedBackClick() {
        onFeedbackClick();
    }

    @OnClick(R.id.poolingBtn)
    public void onPoolingClick() {
        this.startActivity(new Intent(this, PolingActivity.class));
    }

    @OnClick(R.id.inviteBtn)
    public void onInviteClick() {
        onInviteMethod();
    }

    @OnClick(R.id.questionBtn)
    public void onQuestionClick() {
        menuAnim.close();
    }

    @OnClick(R.id.blogBtn)
    public void onBlogClick() {
        this.startActivity(new Intent(this, BlogListActivity.class));
    }

    @OnClick(R.id.aboutUsBtn)
    public void onAboutUsClick() {
        this.startActivity(new Intent(this, AboutUsActivity.class));
    }

    @OnClick(R.id.introBtn)
    public void onIntroClick() {
        onMainIntro();
    }
}
