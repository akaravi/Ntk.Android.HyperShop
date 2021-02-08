package ntk.android.hyper.fragment;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;

import es.dmoral.toasty.Toasty;
import io.reactivex.Observable;
import java9.util.function.Function;
import ntk.android.base.dtomodel.hypershop.HyperShopOrderContentDtoModel;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.base.FilterDataModel;
import ntk.android.base.entitymodel.base.FilterModel;
import ntk.android.base.fragment.abstraction.AbstractionListFragment;
import ntk.android.hyper.R;
import ntk.android.hyper.activity.hyper.OrderActivity;
import ntk.android.hyper.adapter.hyper.HyperOrderContentAdapter;
import ntk.android.hyper.prefrense.OrderPref;

public class OrderContentListFragment extends AbstractionListFragment<HyperShopOrderContentDtoModel> {
    float amountOrder;

    @Override
    public Function<FilterModel, Observable<ErrorException<HyperShopOrderContentDtoModel>>> getService() {
        return dataModel -> new OrderPref(getContext()).getLastShopping();
    }

    @Override
    public void onCreated() {
        super.onCreated();
        getActivity().findViewById(R.id.imgDeleteOrder).setOnClickListener(view -> clearOrders());
    }

    private void clearOrders() {
        new OrderPref(getContext()).clear();
        models.clear();
        updateList();
        Toasty.success(getContext(), "سبد شما کامل حذف گردید", Toasty.LENGTH_LONG, true).show();
    }

    @Override
    protected void onListCreate() {
        if (models.size() > 0) {
            updateTotalPrice();
            ((OrderActivity) getActivity()).showBottom();
        }
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
        return new HyperOrderContentAdapter(getContext(), models, this::updateTotalPrice, this::updateList);
    }

    private void updateTotalPrice() {
        amountOrder = 0;
        for (HyperShopOrderContentDtoModel d : models) {
            amountOrder += d.Price * d.Count;
        }
        ((TextView) getActivity().findViewById(R.id.txtTotalPrice)).setText(new DecimalFormat("###,###,###,###").format(amountOrder) + " " + HyperShopOrderContentDtoModel.CURRENCY_UNIT);
    }

    public void updateList() {
        if (adapter.getItemCount() == 0) {
            switcher.showEmptyView();
            getActivity().findViewById(R.id.bottomLayout).setVisibility(View.GONE);
            getActivity().findViewById(R.id.imgDeleteOrder).setVisibility(View.GONE);
        }


    }

    public void updateOrder() {
        new OrderPref(getContext()).updateOrderWith(((HyperOrderContentAdapter) adapter).list(), amountOrder);
    }
}
