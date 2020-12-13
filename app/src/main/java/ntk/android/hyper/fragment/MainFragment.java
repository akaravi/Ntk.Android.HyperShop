package ntk.android.hyper.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ntk.android.base.config.NtkObserver;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.base.FilterDataModel;
import ntk.android.base.entitymodel.hypershop.HyperShopCategoryModel;
import ntk.android.base.entitymodel.hypershop.HyperShopContentModel;
import ntk.android.base.fragment.BaseFragment;
import ntk.android.base.services.hypershop.HyperShopCategoryService;
import ntk.android.base.services.hypershop.HyperShopContentService;
import ntk.android.hyper.R;
import ntk.android.hyper.activity.hyper.HyperShopContentSearchActivity;

class MainFragment extends BaseFragment {
    @Override
    public void onCreateFragment() {
        setContentView(R.layout.main_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViewById(R.id.imgSearchContent).setOnClickListener(view1 -> startActivity(new Intent(getContext(), HyperShopContentSearchActivity.class)));
        getCategory();
        getContent();
    }

    private void getContent() {
        FilterDataModel f = new FilterDataModel();
        f.RowPerPage = 20;
        new HyperShopContentService(getContext()).getAllMicroService(f)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new NtkObserver<ErrorException<HyperShopContentModel>>() {
            @Override
            public void onNext(@io.reactivex.annotations.NonNull ErrorException<HyperShopContentModel> hyperShopContentModelErrorException) {

            }

            @Override
            public void onError(@io.reactivex.annotations.NonNull Throwable e) {

            }
        });
    }

    private void getCategory() {
        FilterDataModel f = new FilterDataModel();
        f.RowPerPage = 8;
        new HyperShopCategoryService(getContext()).getAllMicroService(f)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io()).subscribe(new NtkObserver<ErrorException<HyperShopCategoryModel>>() {
            @Override
            public void onNext(@io.reactivex.annotations.NonNull ErrorException<HyperShopCategoryModel> hyperShopCategoryModelErrorException) {

            }

            @Override
            public void onError(@io.reactivex.annotations.NonNull Throwable e) {

            }
        });

    }
}
