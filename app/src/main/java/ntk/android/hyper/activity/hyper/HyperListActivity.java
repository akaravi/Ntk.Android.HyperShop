package ntk.android.hyper.activity.hyper;

import androidx.recyclerview.widget.RecyclerView;

import io.reactivex.Observable;
import java9.util.function.Function;
import ntk.android.base.activity.common.BaseFilterModelListActivity;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.base.FilterDataModel;
import ntk.android.base.entitymodel.hypershop.HyperShopContentModel;
import ntk.android.base.services.hypershop.HyperShopContentService;
import ntk.android.hyper.adapter.hyper.PrevHypershopContentAdapter;

public class HyperListActivity extends BaseFilterModelListActivity<HyperShopContentModel> {


    @Override
    public Function<FilterDataModel, Observable<ErrorException<HyperShopContentModel>>> getService() {
        return new HyperShopContentService(this)::getAllMicroService;
    }

    @Override
    public RecyclerView.Adapter createAdapter() {
        return new PrevHypershopContentAdapter(models);
    }

    @Override
    public void ClickSearch() {

    }
}
