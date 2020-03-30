package ntk.android.academy.adapter.theme.holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ntk.android.academy.R;

public class HoImage extends RecyclerView.ViewHolder {

    @BindView(R.id.RecyclerImageRecyclerHome)
    public RecyclerView Rv;

    public HoImage(@NonNull View view) {
        super(view);
        ButterKnife.bind(this, view);
    }
}
