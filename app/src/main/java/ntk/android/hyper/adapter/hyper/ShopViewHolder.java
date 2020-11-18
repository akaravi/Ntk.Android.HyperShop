package ntk.android.hyper.adapter.hyper;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import ntk.android.hyper.R;


/**
 * shop Holder for showing on ShopAdapter@{@link ntkcms.shopbase.adapters.ShopContentListAdapter}
 *
 */
public class ShopViewHolder extends RecyclerView.ViewHolder{
    public ImageView image;
    public TextView title;
    public ProgressView loadingProgress;
    public TextView price;
    public TextView description;
    public View addProduct ;

    /**
     * default constructor of class
     * @param itemView image for
     */
    public ShopViewHolder(View itemView) {
        super(itemView);
        image = (ImageView) itemView.findViewById(R.id.image);
        addProduct =  itemView.findViewById(R.id.addProduct);
        title =  itemView.findViewById(R.id.titleText);
        price =  itemView.findViewById(R.id.priceText);
        description = itemView.findViewById(R.id.description);
        loadingProgress = itemView.findViewById(R.id.loadingProgress);


    }

}
