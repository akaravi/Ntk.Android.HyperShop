package ntk.android.hyper.fragment;

import androidx.recyclerview.widget.RecyclerView;

import io.reactivex.Observable;
import java9.util.function.Function;
import ntk.android.base.dtomodel.hypershop.HyperShopOrderContentDtoModel;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.base.FilterDataModel;
import ntk.android.base.fragment.abstraction.AbstractionListFragment;
import ntk.android.hyper.adapter.hyper.HyperOrderContentAdapter;
import ntk.android.hyper.prefrense.OrderPref;

public class OrderContentListFragment extends AbstractionListFragment<HyperShopOrderContentDtoModel> {


    @Override
    public Function<FilterDataModel, Observable<ErrorException<HyperShopOrderContentDtoModel>>> getService() {
        return dataModel -> getData();
    }

    private Observable<ErrorException<HyperShopOrderContentDtoModel>> getData() {
        return Observable.create(emitter -> {
            ErrorException<HyperShopOrderContentDtoModel> model = new ErrorException<>();
            model.IsSuccess = true;
            model.ListItems = new OrderPref(getContext()).getOrder().Products;
            emitter.onNext(model);
            emitter.onComplete();
        });
    }

    @Override
    public boolean withToolbar() {
        return false;
    }

    @Override
    public RecyclerView.Adapter createAdapter() {
        return new HyperOrderContentAdapter(getContext(),models);
    }

    @Override
    public void ClickSearch() {

    }
}
