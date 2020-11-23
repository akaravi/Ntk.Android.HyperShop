package ntk.android.hyper.adapter.hyper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import ntk.android.base.dtomodel.hypershop.HyperShopOrderContentDtoModel;
import ntk.android.hyper.R;
import ntk.android.hyper.library.about.Element;

public class HyperOrderContentAdapter extends RecyclerView.Adapter {


    private final Context context;
    private List<HyperShopOrderContentDtoModel> products;

    public HyperOrderContentAdapter(Context context, List<HyperShopOrderContentDtoModel> products) {
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_content_item_recycler, parent, false);
        return new ItemViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder base, int position) {
        ItemViewHolder holder = (ItemViewHolder) base;
        HyperShopOrderContentDtoModel  item = products.get(position);
        holder.txtItemName.setText(item.Name);

        ImageLoader.getInstance().displayImage(item.Image, holder.imgThumbnail);


        holder.txtPrice.setText(String.format("%.2f", item.Price));

        holder.imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //todo
            }
        });
        holder.imgRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo
            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        final TextView txtItemName;
        final ImageView imgThumbnail;
        final ImageView imgAdd;
        final TextView txtPrice;
        final TextView title;
        final CardView cardItem;
        public View imgRemove;

        ItemViewHolder(View itemView) {
            super(itemView);
            this.txtItemName = itemView.findViewById(R.id.txtItemName);
            this.imgThumbnail = itemView.findViewById(R.id.imgThumbnail);
            this.imgAdd = itemView.findViewById(R.id.imgAdd);
            //todo add imgRemove
            this.imgRemove = itemView.findViewById(R.id.imgAdd);
            this.txtPrice = itemView.findViewById(R.id.txtPrice);
            this.title = itemView.findViewById(R.id.title);
            this.cardItem = itemView.findViewById(R.id.cardItem);

        }

    }
}
