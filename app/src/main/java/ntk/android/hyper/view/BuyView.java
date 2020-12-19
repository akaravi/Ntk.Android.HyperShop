package ntk.android.hyper.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import es.dmoral.toasty.Toasty;
import ntk.android.base.dtomodel.hypershop.HyperShopOrderContentDtoModel;
import ntk.android.base.entitymodel.hypershop.HyperShopContentModel;
import ntk.android.base.utill.FontManager;
import ntk.android.hyper.R;
import ntk.android.hyper.event.UpdateCartViewEvent;
import ntk.android.hyper.prefrense.OrderPref;

public class BuyView extends FrameLayout {
    int count = 0;
    HyperShopContentModel model;
    HyperShopOrderContentDtoModel dtoModel;
    Runnable changePriceMethod;
    Runnable deleteProduct;

    public BuyView(Context context) {
        super(context, null);
    }

    public BuyView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View inflate = inflater.inflate(R.layout.sub_buy_view, this);
        inflate.findViewById(R.id.mbtnAdd).setOnClickListener(view -> {
            findViewById(R.id.mbtnAdd).setVisibility(INVISIBLE);
            findViewById(R.id.linear).setVisibility(VISIBLE);

            increaseCount();
        });
        ((Button) findViewById(R.id.mbtnAdd)).setTypeface(FontManager.T1_Typeface(getContext()));
        findViewById(R.id.buy_view_plus).setOnClickListener(view -> increaseCount());
        findViewById(R.id.buy_view_mines).setOnClickListener(view -> decreaseCount());
    }

    private void decreaseCount() {
        count--;
        setModelCount();
        if (count == 0) {
            findViewById(R.id.mbtnAdd).setVisibility(VISIBLE);
            findViewById(R.id.linear).setVisibility(GONE);
            if (deleteProduct != null)
                deleteProduct.run();
        }
        ((TextView) findViewById(R.id.buy_view_txtCount)).setText(count + "");
        updatePref();
    }

    private void increaseCount() {

        int modelCount = model != null ? model.Count : dtoModel.TotalCount;
        if (modelCount > count) {
            count++;
            setModelCount();
            ((TextView) findViewById(R.id.buy_view_txtCount)).setText(count + "");
            updatePref();
        } else {
            Toasty.error(getContext(), "تعداد موجودی این کالا کمتر از مقدار درخواستی شما است").show();
            if (count == 0) {
                findViewById(R.id.mbtnAdd).setVisibility(VISIBLE);
                findViewById(R.id.linear).setVisibility(GONE);
            }
        }
    }

    private void setModelCount() {
//        if (model != null)
//            model.Count = count;
        if (dtoModel != null)
            dtoModel.Count = count;
    }

    private void updatePref() {
        if (model != null) {
            new OrderPref(getContext()).updateShopContent(model, count);
            EventBus.getDefault().post(new UpdateCartViewEvent());
        }       else {
            new OrderPref(getContext()).updateShopContent(dtoModel, count);
            changePriceMethod.run();
            EventBus.getDefault().post(new UpdateCartViewEvent());
        }
    }


    public void bind(HyperShopContentModel item) {
        model = item;
        HyperShopOrderContentDtoModel product = new OrderPref(getContext()).getProduct(item.Code);
        if (product != null) {
            count = product.Count;
            ((TextView) findViewById(R.id.buy_view_txtCount)).setText(count + "");
            findViewById(R.id.mbtnAdd).setVisibility(INVISIBLE);
            findViewById(R.id.linear).setVisibility(VISIBLE);
        } else {
            findViewById(R.id.mbtnAdd).setVisibility(VISIBLE);
            findViewById(R.id.linear).setVisibility(GONE);
        }
    }

    public void bind(HyperShopOrderContentDtoModel item, Runnable o, Runnable d) {
        changePriceMethod = o;
        deleteProduct = d;
        dtoModel = item;
        count = dtoModel.Count;
        ((TextView) findViewById(R.id.buy_view_txtCount)).setText(count + "");
        findViewById(R.id.mbtnAdd).setVisibility(INVISIBLE);
        findViewById(R.id.linear).setVisibility(VISIBLE);
    }

}
