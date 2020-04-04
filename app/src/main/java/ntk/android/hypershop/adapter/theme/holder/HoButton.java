package ntk.android.hypershop.adapter.theme.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import ntk.android.hypershop.R;
import ntk.android.hypershop.utill.FontManager;

public class HoButton extends RecyclerView.ViewHolder {

    @BindView(R.id.RecyclerButtonRecyclerHome)
    public RecyclerView Rv;

    public HoButton(Context context, View view) {
        super(view);
        ButterKnife.bind(this, view);
    }
}
