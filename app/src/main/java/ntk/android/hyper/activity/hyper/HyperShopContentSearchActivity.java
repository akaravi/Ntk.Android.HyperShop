package ntk.android.hyper.activity.hyper;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import io.reactivex.Observable;
import java9.util.function.Function;
import ntk.android.base.activity.abstraction.AbstractionSearchActivity;
import ntk.android.base.api.utill.NTKUtill;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.base.FilterDataModel;
import ntk.android.base.entitymodel.base.Filters;
import ntk.android.base.entitymodel.hypershop.HyperShopContentModel;
import ntk.android.base.services.hypershop.HyperShopContentService;
import ntk.android.hyper.adapter.hyper.HyperShopContent_1_Adapter;

public class HyperShopContentSearchActivity extends AbstractionSearchActivity<HyperShopContentModel> {
    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new HyperShopContent_1_Adapter(models);
    }

    @NotNull
    @Override
    protected FilterDataModel getDefaultFilterDataModel(String stringValue) {
        FilterDataModel request = new FilterDataModel();
        Filters ft = new Filters();
        ft.PropertyName = "Name";
        ft.StringValue = stringValue;
        ft.ClauseType = NTKUtill.ClauseType_Or;
        ft.SearchType = NTKUtill.Search_Type_Contains;
        request.addFilter(ft);

        Filters fd = new Filters();
        fd.PropertyName = "Cat";
        fd.StringValue = stringValue;
        fd.ClauseType = NTKUtill.ClauseType_Or;
        fd.SearchType = NTKUtill.Search_Type_Contains;
        request.addFilter(fd);

        Filters fb = new Filters();
        fb.PropertyName = "Memo";
        fb.StringValue = stringValue;
        fb.ClauseType = NTKUtill.ClauseType_Or;
        fb.SearchType = NTKUtill.Search_Type_Contains;
        request.addFilter(fb);
        return request;
    }

    @Override
    public Function<FilterDataModel, Observable<ErrorException<HyperShopContentModel>>> getService() {
        return new HyperShopContentService(this)::getAllMicroService;
    }
}
