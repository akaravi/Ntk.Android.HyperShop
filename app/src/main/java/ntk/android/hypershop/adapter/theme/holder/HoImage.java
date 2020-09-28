package ntk.android.hypershop.adapter.theme.holder;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import ntk.android.hypershop.R;

public class HoImage extends RecyclerView.ViewHolder {

    @BindView(R.id.RecyclerImageRecyclerHome)
    public RecyclerView Rv;

    public HoImage(@NonNull View view) {
        super(view);
        ButterKnife.bind(this, view);
    }
}
