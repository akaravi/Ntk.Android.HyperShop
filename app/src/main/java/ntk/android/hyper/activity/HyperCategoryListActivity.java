package ntk.android.hyper.activity;

import android.graphics.Color;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import io.reactivex.Observable;
import java9.util.function.Function;
import ntk.android.base.activity.common.BaseFilterModelListActivity;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.base.FilterDataModel;
import ntk.android.base.entitymodel.base.FilterModel;
import ntk.android.base.entitymodel.hypershop.HyperShopCategoryModel;
import ntk.android.base.services.hypershop.HyperShopCategoryService;
import ntk.android.base.view.NViewUtils;
import ntk.android.hyper.R;
import ntk.android.hyper.adapter.hyper.HyperCategoryAdapter;

public class HyperCategoryListActivity extends BaseFilterModelListActivity<HyperShopCategoryModel> {
    @Override
    public void afterInit() {
        super.afterInit();
        //hide search icon
        findViewById(R.id.imgSearch).setVisibility(View.GONE);
        //add custom padding to recyclerView
        int i = NViewUtils.dpToPx(this, 8);
        findViewById(R.id.recycler).setPadding(i, i*2, i, i*2);
        //add custom color to recycler
        findViewById(R.id.recycler).setBackgroundColor(Color.parseColor("#F7F8FA"));

    }

    @Override
    public Function<FilterModel, Observable<ErrorException<HyperShopCategoryModel>>> getService() {
        return new HyperShopCategoryService(this)::getAllMicroService;
    }

    @Override
    public RecyclerView.LayoutManager getRvLayoutManager() {
        return new GridLayoutManager(this, 3);
    }

    @Override
    public RecyclerView.Adapter createAdapter() {
        return new HyperCategoryAdapter(models);
    }


}
