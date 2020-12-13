package ntk.android.hyper.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
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
import ntk.android.hyper.adapter.MainFragment1_1Adapter;

public class MainFragment extends BaseFragment {
    MainFragment1_1Adapter adapter;

    @Override
    public void onCreateFragment() {
        setContentView(R.layout.main_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViewById(R.id.imgSearchContent).setOnClickListener(view1 -> startActivity(new Intent(getContext(), HyperShopContentSearchActivity.class)));
        switcher.showProgressView();
        List<String> titles = new ArrayList() {{
            add("دسته بندی ها");
            add("کالاهای جدید");
        }};
        List<Integer> tags = new ArrayList() {{
            add(0);
            add(1);
        }};
        ChipGroup chipGroup = findViewById(R.id.chip_group_main);
        for (int i = 0; i < titles.size(); i++) {
            int index = titles.size() - 1 - i;
            String name = titles.get(index);
            Chip inflate = (Chip) getLayoutInflater().inflate(R.layout.chip_row_item, null);
            inflate.setId(ViewCompat.generateViewId());
            inflate.setTag(index);
            inflate.setText(name);
            chipGroup.addView(inflate);
        }
        chipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            for (int i = 0; i < group.getChildCount(); i++) {
                if (group.getChildAt(i).getId() == checkedId)
                    ((RecyclerView) findViewById(R.id.rc)).scrollToPosition(((Integer) group.getChildAt(i).getTag()));
            }
        });
        RecyclerView rcAllView = findViewById(R.id.rc);
        rcAllView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        adapter = (new MainFragment1_1Adapter(titles));
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
            public void onNext(@io.reactivex.annotations.NonNull ErrorException<HyperShopContentModel> response) {
                if (response.IsSuccess) {
                    adapter.put(1, response.ListItems);
                    if (adapter.getItemCount() == 2) {

                        ((RecyclerView) findViewById(R.id.rc)).setAdapter(adapter);
                        switcher.showContentView();
                    }
                } else
                    Toasty.error(getContext(), response.ErrorMessage).show();
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
            public void onNext(@io.reactivex.annotations.NonNull ErrorException<HyperShopCategoryModel> response) {
                if (response.IsSuccess) {
                    adapter.put(0, response.ListItems);
                    if (adapter.getItemCount() == 2) {
                        ((RecyclerView) findViewById(R.id.rc)).setAdapter(adapter);
                        switcher.showContentView();
                    }
                } else
                    Toasty.error(getContext(), response.ErrorMessage).show();
            }

            @Override
            public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                Toasty.error(getContext(), e.toString()).show();
            }
        });

    }
}
