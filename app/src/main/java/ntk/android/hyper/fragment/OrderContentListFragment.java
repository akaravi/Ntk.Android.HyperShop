package ntk.android.hyper.fragment;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.subjects.BehaviorSubject;
import java9.util.function.Function;
import ntk.android.base.config.NtkObserver;
import ntk.android.base.config.ServiceExecute;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.base.FilterModel;
import ntk.android.base.entitymodel.hypershop.HyperShopOrderContentModel;
import ntk.android.base.entitymodel.hypershop.HyperShopOrderModel;
import ntk.android.base.fragment.abstraction.AbstractionListFragment;
import ntk.android.base.services.hypershop.HyperShopOrderService;
import ntk.android.hyper.R;
import ntk.android.hyper.activity.hyper.OrderActivity;
import ntk.android.hyper.adapter.hyper.HyperOrderContentAdapter;
import ntk.android.hyper.prefrense.OrderPref;

public class OrderContentListFragment extends AbstractionListFragment<HyperShopOrderContentModel> {
    float amountOrder;

    @Override
    public Function<FilterModel, Observable<ErrorException<HyperShopOrderContentModel>>> getService() {
        return filterModel -> {
            BehaviorSubject<ErrorException<HyperShopOrderContentModel>> lastOrder = BehaviorSubject.create();
            ServiceExecute.execute(new HyperShopOrderService(getContext()).lastOrder())
                    .subscribe(new NtkObserver<ErrorException<HyperShopOrderModel>>() {
                        @Override
                        public void onNext(@NonNull ErrorException<HyperShopOrderModel> req) {
                            HyperShopOrderModel order = new OrderPref(getContext()).getOrder();
                            if (req.Item.Products == null)
                                req.Item.Products = new ArrayList<>();
                            List<HyperShopOrderContentModel> Product = new ArrayList<>(req.Item.Products);
                            for (HyperShopOrderContentModel p : order.Products)
                                if (!req.Item.Products.contains(p))
                                    req.Item.Products.add(p);
                            ErrorException<HyperShopOrderContentModel> models = new ErrorException<>();
                            models.IsSuccess = req.IsSuccess;
                            models.ErrorMessage = req.ErrorMessage;
                            models.TotalRowCount = req.TotalRowCount;
                            models.CurrentPageNumber = req.CurrentPageNumber;
                            models.RowPerPage = req.RowPerPage;
                            models.Status = req.Status;
                            models.ListItems = Product;
                            new OrderPref(getContext()).saveLastOrder(req.Item);
                            lastOrder.onNext(models);
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            lastOrder.onError(e);
                        }

                    });
            return lastOrder;
        }

                ;
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
        for (HyperShopOrderContentModel d : models) {
            amountOrder += d.Price * d.Count;
        }
        ((TextView) getActivity().findViewById(R.id.txtTotalPrice)).setText(new DecimalFormat("###,###,###,###").format(amountOrder) + " " + HyperShopOrderContentModel.CURRENCY_UNIT);
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
