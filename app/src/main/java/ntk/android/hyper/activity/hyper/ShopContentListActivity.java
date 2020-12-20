package ntk.android.hyper.activity.hyper;

import android.os.Bundle;

import org.jetbrains.annotations.Nullable;

import ntk.android.base.Extras;
import ntk.android.base.activity.BaseActivity;
import ntk.android.hyper.R;
import ntk.android.hyper.fragment.ShopContentList_1_Fragment;

/**
 * argument for shopContentActivity
 * Title : arg 1
 * Searchshow :arg 2
 */

public class ShopContentListActivity extends BaseActivity {

    boolean showToolbar = false;
    String tittle = "لیست محصولات";
    String filterModel="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frag_container);
        if (getIntent() != null && getIntent().getExtras() != null) {
            filterModel = getIntent().getExtras().getString(Extras.EXTRA_FIRST_ARG, "");
            tittle = getIntent().getExtras().getString(Extras.EXTRA_SECOND_ARG, "");
            showToolbar = getIntent().getExtras().getBoolean(Extras.Extra_THIRD_ARG, false);
        }
        showFragment();
    }

    public void showFragment() {
        ShopContentList_1_Fragment fragment = new ShopContentList_1_Fragment();
        Bundle i = new Bundle();
        if (!filterModel.equalsIgnoreCase(""))
            i.putString(Extras.EXTRA_FIRST_ARG, filterModel);
        i.putString(Extras.EXTRA_SECOND_ARG, tittle);
        i.putBoolean(Extras.Extra_THIRD_ARG, showToolbar);
        fragment.setArguments(i);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commitNow();

    }


}
