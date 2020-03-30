package ntk.android.academy.adapter.theme.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import ntk.android.academy.R;
import ntk.android.academy.utill.FontManager;

public class HoButton extends RecyclerView.ViewHolder {

    @BindView(R.id.RecyclerButtonRecyclerHome)
    public RecyclerView Rv;

    public HoButton(Context context, View view) {
        super(view);
        ButterKnife.bind(this, view);
    }
}
