package ntk.android.hyper.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import es.dmoral.toasty.Toasty;
import ntk.android.base.entitymodel.hypershop.HyperShopContentModel;
import ntk.android.hyper.R;
import ntk.android.hyper.prefrense.OrderPref;

public class BuyView extends FrameLayout {
    int count = 0;
    HyperShopContentModel model;

    public BuyView(Context context) {
        super(context, null);
    }

    public BuyView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View inflate = inflater.inflate(R.layout.sub_buy_view, this);
        inflate.findViewById(R.id.mbtnAdd).setOnClickListener(view -> {
            findViewById(R.id.mbtnAdd).setVisibility(GONE);
            findViewById(R.id.buy_view_plus).setVisibility(VISIBLE);
            findViewById(R.id.buy_view_txtCount).setVisibility(VISIBLE);
            findViewById(R.id.buy_view_mines).setVisibility(VISIBLE);
            increaseCount();
        });
        findViewById(R.id.buy_view_plus).setOnClickListener(view -> increaseCount());
        findViewById(R.id.buy_view_mines).setOnClickListener(view -> decreaseCount());
    }

    private void decreaseCount() {
        count--;
        if (count == 0) {
            findViewById(R.id.mbtnAdd).setVisibility(VISIBLE);
            findViewById(R.id.buy_view_plus).setVisibility(GONE);
            findViewById(R.id.buy_view_txtCount).setVisibility(GONE);
            findViewById(R.id.buy_view_mines).setVisibility(GONE);
        } else {
            ((TextView) findViewById(R.id.buy_view_txtCount)).setText(count + "");
            updatePref();
        }
    }

    private void increaseCount() {
        if (model.Count < count) {
            count++;
            ((TextView) findViewById(R.id.buy_view_txtCount)).setText(count + "");
            updatePref();
        } else {
            Toasty.error(getContext(), "تعداد موجودی این کالا کمتر از مقدار درخواستی شما است").show();
            if (count == 0) {
                findViewById(R.id.mbtnAdd).setVisibility(VISIBLE);
                findViewById(R.id.buy_view_plus).setVisibility(GONE);
                findViewById(R.id.buy_view_txtCount).setVisibility(GONE);
                findViewById(R.id.buy_view_mines).setVisibility(GONE);
            }
        }
    }

    private void updatePref() {
        new OrderPref(getContext()).addShopContent(model, count);
    }


    public void bind(HyperShopContentModel item) {
        model = item;
    }
}
