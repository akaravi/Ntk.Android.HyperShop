package ntk.android.hyper.fragment;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import java9.util.function.Function;
import ntk.android.base.dtomodel.hypershop.HyperShopOrderContentDtoModel;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.base.FilterDataModel;
import ntk.android.base.fragment.abstraction.AbstractionListFragment;
import ntk.android.hyper.R;
import ntk.android.hyper.adapter.hyper.HyperOrderContentAdapter;
import ntk.android.hyper.prefrense.OrderPref;

public class OrderContentListFragment extends AbstractionListFragment<HyperShopOrderContentDtoModel> {
    float amountOrder;

    @Override
    public Function<FilterDataModel, Observable<ErrorException<HyperShopOrderContentDtoModel>>> getService() {
        return dataModel -> getData();
    }

    private Observable<ErrorException<HyperShopOrderContentDtoModel>> getData() {
        return Observable.create(emitter -> {
            ErrorException<HyperShopOrderContentDtoModel> model = new ErrorException<>();
            model.IsSuccess = true;
            List<HyperShopOrderContentDtoModel> products = new OrderPref(getContext()).getOrder().Products;
            model.ListItems = new ArrayList<>();
            model.ListItems.addAll(products);
            model.TotalRowCount = products.size();
            emitter.onNext(model);
            emitter.onComplete();
        });
    }

    @Override
    protected IntegrationView viewSyncOnScrolling() {
        return new IntegrationView() {
            @Override
            public boolean isShown() {
                return getActivity().findViewById(R.id.bottomLayout).isShown();
            }

            @Override
            public void changeVisibility(boolean isVisible) {
                getActivity().findViewById(R.id.bottomLayout).setVisibility(isVisible ? View.VISIBLE : View.GONE);
            }
        };
    }

    @Override
    public boolean withToolbar() {
        return false;
    }

    @Override
    public RecyclerView.Adapter createAdapter() {
        return new HyperOrderContentAdapter(getContext(), models, this::updateTotalPrice);
    }

    private void updateTotalPrice() {
        amountOrder = 0;
        for (HyperShopOrderContentDtoModel d : models) {
            amountOrder += d.Price * d.Count;
        }
        ((TextView) getActivity().findViewById(R.id.txtTotalPrice)).setText(String.valueOf(amountOrder));
    }



    public void updateOrder() {
        new OrderPref(getContext()).updateOrderWith(((HyperOrderContentAdapter) adapter).models(),amountOrder);
    }
}
