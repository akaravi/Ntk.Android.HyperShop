package ntk.android.hyper.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import ntk.android.base.activity.BaseActivity;
import ntk.android.base.config.NtkObserver;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.base.FilterDataModel;
import ntk.android.base.entitymodel.polling.PollingCategoryModel;
import ntk.android.base.services.pooling.PollingCategoryService;
import ntk.android.base.utill.AppUtill;
import ntk.android.base.utill.FontManager;
import ntk.android.hyper.R;
import ntk.android.hyper.adapter.PoolCategoryAdapter;

public class PoolingActivity extends BaseActivity {

    @BindView(R.id.lblTitleActPooling)
    TextView LblTitle;

    @BindView(R.id.recyclerPooling)
    RecyclerView Rv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_pooling);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        LblTitle.setTypeface(FontManager.GetTypeface(this, FontManager.IranSans));
        Rv.setHasFixedSize(true);
        Rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        if (AppUtill.isNetworkAvailable(this)) {
            // show loading
            switcher.showProgressView();
            new PollingCategoryService(this).getAll(new FilterDataModel())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new NtkObserver<ErrorException<PollingCategoryModel>>() {
                        @Override
                        public void onNext(@NonNull ErrorException<PollingCategoryModel> poolingCategoryResponse) {
                            if (poolingCategoryResponse.IsSuccess) {
                                PoolCategoryAdapter adapter = new PoolCategoryAdapter(PoolingActivity.this, poolingCategoryResponse.ListItems);
                                Rv.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                if (adapter.getItemCount() > 0)
                                    switcher.showContentView();
                                else
                                    switcher.showEmptyView();

                            }
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            switcher.showErrorView("خطای سامانه مجددا تلاش کنید", () -> init());
                        }
                    });
        } else {
            switcher.showErrorView("عدم دسترسی به اینترنت", () -> init());

        }
    }

    @OnClick(R.id.imgBackActPooling)
    public void ClickBack() {
        finish();
    }
}
