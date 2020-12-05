package ntk.android.hyper.fragment;

import androidx.recyclerview.widget.RecyclerView;

import io.reactivex.Observable;
import java9.util.function.Function;
import ntk.android.base.Extras;
import ntk.android.base.entitymodel.bankpayment.BankPaymentPrivateSiteConfigModel;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.base.FilterDataModel;
import ntk.android.base.fragment.abstraction.AbstractionListFragment;
import ntk.android.base.services.bankpayment.BankPaymentPrivateSiteConfigService;
import ntk.android.hyper.adapter.BankSelectAdapter;

public class BankPaymentListFragment extends AbstractionListFragment<BankPaymentPrivateSiteConfigModel> {
    Long OrderId;

    @Override
    public void onCreated() {
        super.onCreated();
        assert getArguments() != null;
        OrderId = getArguments().getLong(Extras.EXTRA_FIRST_ARG, 0);

    }

    @Override
    public Function<FilterDataModel, Observable<ErrorException<BankPaymentPrivateSiteConfigModel>>> getService() {
        return new BankPaymentPrivateSiteConfigService(getContext())::getAll;
    }

    @Override
    public boolean withToolbar() {
        return false;
    }

    @Override
    public RecyclerView.Adapter createAdapter() {
        return new BankSelectAdapter(getContext(), OrderId, models);
    }

    @Override
    public void ClickSearch() {

    }
}
