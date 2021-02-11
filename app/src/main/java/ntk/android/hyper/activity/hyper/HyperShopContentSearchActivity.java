package ntk.android.hyper.activity.hyper;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Observable;
import java9.util.function.Function;
import ntk.android.base.activity.abstraction.AbstractSearchActivity;
import ntk.android.base.api.utill.NTKUtill;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.base.FilterDataModel;
import ntk.android.base.entitymodel.base.FilterModel;
import ntk.android.base.entitymodel.hypershop.HyperShopContentModel;
import ntk.android.base.services.hypershop.HyperShopContentService;
import ntk.android.hyper.adapter.hyper.HyperShopContent_1_Adapter;

public class HyperShopContentSearchActivity extends AbstractSearchActivity<HyperShopContentModel> {
    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new HyperShopContent_1_Adapter(models);
    }

    @NotNull
    @Override
    protected FilterModel getDefaultFilterDataModel(String stringValue) {
        FilterModel request = new FilterModel();
        FilterDataModel ft = new FilterDataModel();
        ft.PropertyName = "Name";
        ft.setStringValue(stringValue);
        ft.ClauseType = NTKUtill.ClauseType_Or;
        ft.SearchType = NTKUtill.Search_Type_Contains;
        request.addFilter(ft);

        FilterDataModel fd = new FilterDataModel();
        fd.PropertyName = "Cat";
        fd.setStringValue(stringValue);
        fd.ClauseType = NTKUtill.ClauseType_Or;
        fd.SearchType = NTKUtill.Search_Type_Contains;
        request.addFilter(fd);

        FilterDataModel fb = new FilterDataModel();
        fb.PropertyName = "Memo";
        fb.setStringValue(stringValue);
        fb.ClauseType = NTKUtill.ClauseType_Or;
        fb.SearchType = NTKUtill.Search_Type_Contains;
        request.addFilter(fb);
        return request;
    }

    @Override
    public Function<FilterModel, Observable<ErrorException<HyperShopContentModel>>> getService() {
        return new HyperShopContentService(this)::getAllMicroService;
    }
}
