package ntk.android.hypershop.adapter.theme.holder;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import ntk.android.hypershop.R;

public class HoTag extends RecyclerView.ViewHolder {

    @BindView(R.id.RecyclerTagRecyclerHome)
    public RecyclerView RvTag;

    public HoTag(Context context , View view) {
        super(view);
        ButterKnife.bind(this, view);
        RvTag.setHasFixedSize(true);
    }
}
