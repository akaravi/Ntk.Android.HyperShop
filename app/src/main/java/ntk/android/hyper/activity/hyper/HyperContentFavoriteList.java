package ntk.android.hyper.activity.hyper;

import androidx.recyclerview.widget.RecyclerView;

import io.reactivex.Observable;
import java9.util.function.Function;
import ntk.android.base.activity.common.BaseFilterModelListActivity;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.base.FilterModel;
import ntk.android.base.entitymodel.hypershop.HyperShopContentModel;
import ntk.android.base.services.hypershop.HyperShopContentService;
import ntk.android.hyper.adapter.hyper.HyperShopContent_1_Adapter;

class HyperContentFavoriteList extends BaseFilterModelListActivity<HyperShopContentModel> {
    @Override
    public Function<FilterModel, Observable<ErrorException<HyperShopContentModel>>> getService() {
        return new HyperShopContentService(this)::getFavoriteList;
    }

    @Override
    public RecyclerView.Adapter createAdapter() {
        return new HyperShopContent_1_Adapter( models);
    }

    @Override
    public void ClickSearch() {

    }
}
