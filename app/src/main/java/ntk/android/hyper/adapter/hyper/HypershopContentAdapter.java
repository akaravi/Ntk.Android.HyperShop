package ntk.android.hyper.adapter.hyper;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

import ntk.android.base.adapter.BaseRcAdapter;
import ntk.android.base.entitymodel.hypershop.HyperShopContentModel;
import ntk.android.hyper.R;

public class HypershopContentAdapter extends BaseRcAdapter<HyperShopContentModel> {


    public HypershopContentAdapter(List<HyperShopContentModel> list) {
        super(list);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_list_row, parent, false);
        HyperVH h = new HyperVH(v);
        return h;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder b, int position) {
        HyperVH holder = (HyperVH) b;
        holder.title.setText(getItem(position).Name);
        holder.description.setText(getItem(position).Memo);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheOnDisk(true).build();
        ImageLoader.getInstance().displayImage(getItem(position).Image, holder.image, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//                holder.Progress.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                holder.Progress.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
    }

    private class HyperVH extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView title;
        public ProgressBar loadingProgress;
        public TextView price;
        public TextView description;

        public HyperVH(View v) {
            super(v);
            image = (ImageView) itemView.findViewById(R.id.image);

            title = itemView.findViewById(R.id.titleText);
            price = itemView.findViewById(R.id.priceText);
            description = itemView.findViewById(R.id.description);
            loadingProgress = itemView.findViewById(R.id.loadingProgress);

        }
    }
}