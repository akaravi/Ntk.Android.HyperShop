package ntk.android.hypershop.adapter.theme.holder;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import ntk.android.hypershop.R;
import ss.com.bannerslider.views.BannerSlider;

public class HoSlider extends RecyclerView.ViewHolder {

    @BindView(R.id.RecyclerSliderRecyclerHome)
    public BannerSlider Slider;

    public HoSlider(@NonNull View view) {
        super(view);
        ButterKnife.bind(this, view);
    }
}
