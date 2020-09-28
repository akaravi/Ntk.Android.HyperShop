package ntk.android.hypershop.adapter.theme.holder;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import ntk.android.hypershop.R;

public class HoButton extends RecyclerView.ViewHolder {

    @BindView(R.id.RecyclerButtonRecyclerHome)
    public RecyclerView Rv;

    public HoButton(Context context, View view) {
        super(view);
        ButterKnife.bind(this, view);
    }
}
