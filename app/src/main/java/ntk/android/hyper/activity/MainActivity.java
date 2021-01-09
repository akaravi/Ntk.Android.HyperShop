package ntk.android.hyper.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.yalantis.guillotine.animation.GuillotineAnimation;
import com.yalantis.guillotine.interfaces.GuillotineListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import ntk.android.base.activity.abstraction.AbstractMainActivity;
import ntk.android.base.activity.common.NotificationsActivity;
import ntk.android.base.activity.poling.PolingActivity;
import ntk.android.base.activity.ticketing.TicketListActivity;
import ntk.android.base.activity.ticketing.TicketSearchActivity;
import ntk.android.base.utill.FontManager;
import ntk.android.hyper.R;
import ntk.android.hyper.activity.hyper.HyperTransactionListActivity;
import ntk.android.hyper.activity.hyper.OrderActivity;
import ntk.android.hyper.activity.hyper.OrderListActivity;
import ntk.android.hyper.event.UpdateCartViewEvent;
import ntk.android.hyper.fragment.MainFragment;
import ntk.android.hyper.view.CartView;

public class MainActivity extends AbstractMainActivity {

    private static final long RIPPLE_DURATION = 250;
    List<LinearLayout> btn;
    GuillotineAnimation menuAnim;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.panel_activity);
        startActivity(new Intent(this, Test.class));
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
        MainFragment fragment = new MainFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commitNow();
        setFont();
    }

    private void setFont() {
        Typeface t1_req = FontManager.T1_Typeface(this);
        Typeface t1_bold = FontManager.T1_Typeface(this);
        Typeface t2_req = FontManager.T2_Typeface(this);
        ((TextView) findViewById(R.id.txt_toolbar)).setTypeface(t1_req);

        ((TextView) findViewById(R.id.txtPanel)).setTypeface(t2_req);
        ((TextView) findViewById(R.id.txtOrder)).setTypeface(t2_req);
        ((TextView) findViewById(R.id.category)).setTypeface(t2_req);
        ((TextView) findViewById(R.id.transactionsTxt)).setTypeface(t2_req);
        ((TextView) findViewById(R.id.message)).setTypeface(t2_req);
        ((TextView) findViewById(R.id.search)).setTypeface(t2_req);
        ((TextView) findViewById(R.id.support)).setTypeface(t2_req);
        ((TextView) findViewById(R.id.question)).setTypeface(t2_req);
        ((TextView) findViewById(R.id.feedback)).setTypeface(t2_req);
        ((TextView) findViewById(R.id.news)).setTypeface(t2_req);
        ((TextView) findViewById(R.id.intro)).setTypeface(t2_req);
        ((TextView) findViewById(R.id.pooling)).setTypeface(t2_req);
        ;
        ((TextView) findViewById(R.id.aboutUs)).setTypeface(t2_req);
        ;
        ((TextView) findViewById(R.id.invite)).setTypeface(t2_req);
        ((TextView) findViewById(R.id.blog)).setTypeface(t2_req);
        ((TextView) findViewById(R.id.blog)).setTypeface(t2_req);
    }

    @Override
    protected void onResume() {
        super.onResume();
        UpdateCard();
    }

    public void UpdateCard() {
        ((CartView) findViewById(R.id.cartView)).updateCount();
    }

    @Subscribe
    public void EventRemove(UpdateCartViewEvent event) {
        UpdateCard();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void setAnimation(View v, boolean b) {
        btn = new ArrayList() {{
            add(findViewById(R.id.categoryBtn));
            add(findViewById(R.id.transactionBtn));
            add(findViewById(R.id.orderListBtn));
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

    @OnClick(R.id.orderListBtn)
    public void orderListClick() {
        this.startActivity(new Intent(this, OrderListActivity.class));
    }

    @OnClick(R.id.categoryBtn)
    public void categoryListClick() {
        this.startActivity(new Intent(this, HyperCategoryListActivity.class));
    }

    @OnClick(R.id.transactionBtn)
    public void transActiosListClick() {
        this.startActivity(new Intent(this, HyperTransactionListActivity.class));
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
