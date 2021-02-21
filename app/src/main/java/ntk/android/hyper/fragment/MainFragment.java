package ntk.android.hyper.fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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
import ntk.android.base.appclass.AboutUsClass;
import ntk.android.base.config.NtkObserver;
import ntk.android.base.config.ServiceExecute;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.base.FilterModel;
import ntk.android.base.entitymodel.hypershop.HyperShopCategoryModel;
import ntk.android.base.entitymodel.hypershop.HyperShopContentModel;
import ntk.android.base.fragment.BaseFragment;
import ntk.android.base.services.hypershop.HyperShopCategoryService;
import ntk.android.base.services.hypershop.HyperShopContentService;
import ntk.android.base.utill.FontManager;
import ntk.android.base.utill.prefrense.Preferences;
import ntk.android.hyper.R;
import ntk.android.hyper.activity.AboutUsActivity;
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
            add("فروش ویژه");
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
            Chip chip = (Chip) getLayoutInflater().inflate(R.layout.chip_row_item, chipGroup, false);
//          inflate.setId(index);
//            Chip chip=inflate.findViewById(R.id.chip);
            chip.setId(ViewCompat.generateViewId());
            chip.setTag(index);
            chip.setTypeface(FontManager.T1_BOLD_Typeface(getContext()));
            chip.setText(name);
            chipGroup.addView(chip);
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
        showStoreInfo();
        getPrize();
        getCategory();
        getContent();
        setfont();
    }

    private void showStoreInfo() {
        AboutUsClass aboutUsClass = Preferences.with(getContext()).appVariableInfo().aboutUs();
        ((TextView) findViewById(R.id.txtStoreTitle)).setText(aboutUsClass.AboutUsTitle + "");
        ((TextView) findViewById(R.id.txtScore)).setText(aboutUsClass.AboutUsScoreClick + "");
        ((TextView) findViewById(R.id.txtTotal)).setText(aboutUsClass.AboutUsScoreSum + "");
        findViewById(R.id.mainAboutUs).setOnClickListener(view -> startActivity(new Intent(getContext(), AboutUsActivity.class)));
    }

    private void setfont() {
        Typeface t1 = FontManager.T1_BOLD_Typeface(getContext());
        Typeface t2 = FontManager.T1_Typeface(getContext());
        ((TextView) findViewById(R.id.txtStoreTitle)).setTypeface(t1);
        ((TextView) findViewById(R.id.informataionTxt)).setTypeface(t1);
        ((TextView) findViewById(R.id.txtTotal)).setTypeface(t2);
        ((TextView) findViewById(R.id.txtScore)).setTypeface(t2);
    }

    private void getPrize() {
        FilterModel f = new FilterModel();
        f.RowPerPage = 1;
        ServiceExecute.execute(new HyperShopContentService(getContext()).getAllMicroService(f))
                .subscribe(new NtkObserver<ErrorException<HyperShopContentModel>>() {
                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull ErrorException<HyperShopContentModel> response) {
                        if (response.IsSuccess) {
                            adapter.put(0, response.ListItems);
                            if (adapter.getItemCount() == 3) {

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

    private void getContent() {
        FilterModel f = new FilterModel();
        f.RowPerPage = 20;
        ServiceExecute.execute(new HyperShopContentService(getContext()).getAllMicroService(f))
                .subscribe(new NtkObserver<ErrorException<HyperShopContentModel>>() {
                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull ErrorException<HyperShopContentModel> response) {
                        if (response.IsSuccess) {
                            adapter.put(2, response.ListItems);
                            if (adapter.getItemCount() == 3) {

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
        FilterModel f = new FilterModel();
        f.RowPerPage = 8;
        ServiceExecute.execute(new HyperShopCategoryService(getContext()).getAllMicroService(f))
                .subscribe(new NtkObserver<ErrorException<HyperShopCategoryModel>>() {
                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull ErrorException<HyperShopCategoryModel> response) {
                        if (response.IsSuccess) {
                            adapter.put(1, response.ListItems);
                            if (adapter.getItemCount() == 3) {
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
