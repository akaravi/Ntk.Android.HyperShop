package ntk.android.hyper.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ntk.android.hyper.R;
import ntk.android.hyper.prefrense.OrderPref;

public class CartView extends FrameLayout {
    public CartView(Context context) {
        super(context, null);
    }

    public CartView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View inflate = inflater.inflate(R.layout.sub_card_view, this);

        updateCount();
    }

    public void updateCount() {
        TextView tv = findViewById(R.id.txtCount);
        int products = new OrderPref(getContext()).getOrder().Products.size();
        if (products > 0) {
            tv.setVisibility(VISIBLE);
            tv.setText((String.valueOf(products)));
        }
    }

}
