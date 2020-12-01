package ntk.android.hyper.adapter.hyper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ntk.android.base.adapter.BaseRecyclerAdapter;
import ntk.android.base.entitymodel.hypershop.HyperShopContentModel;
import ntk.android.hyper.R;

/**
 * Created by m.parishani on 12/20/2017.
 */

public class ShopContentListAdapter extends BaseRecyclerAdapter<HyperShopContentModel,RecyclerView.ViewHolder> {
//
//    /**
//     * defult constructor
//     *
//     * @param list list of entities
//     */
    public ShopContentListAdapter(List<HyperShopContentModel> list) {
        super(new ArrayList(list));
    }
//
//    /**
//     * {@link RecyclerView.Adapter#onCreateViewHolder(ViewGroup, int)}
//     *
//     * @param parent
//     * @param viewType: can be 0 or other for right and left aligning
//     * @return Holder
//     */
    @Override
    public ShopViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemCardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_list_row, parent, false);
        ShopViewHolder rcv = new ShopViewHolder(itemCardView);
        return rcv;
    }
//
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder baseholder, int position) {
//        final ShopViewHolder holder = (ShopViewHolder) baseholder;
//        holder.title.setText("" + list.get(position).Name);
//        DecimalFormat df = new DecimalFormat("###,###,###,###");
//        holder.price.setText(list.get(position).CURRENCY_UNIT + "   " + df.format(list.get(position).Price) + "  ");
//        holder.itemView.setOnClickListener(view -> {
//            /**
//             *  when click on each row start NewsActivity for showing clicked-Entity {@link NewsActivity}
//             */
//            Intent intent = new Intent(view.getContext(), ShopContentDetailActivity.class);
//            intent.putExtra("1", list.get(position).);
//            view.getContext().startActivity(intent);
//        });
//    }
//
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder baseholder, final int position) {
//        ShopViewHolder holder= (ShopViewHolder) baseholder;
//        holder.loadingProgress.start();
//        holder.description.setText(list.get(position).Memo);
//        //display image of this newsEntity
//        if (list.get(position).Image == null) {
//            holder.loadingProgress.stop(); //cat get image from server
//        } else {
//            ImageLoaderHelper.displayImage(holder.itemView.getContext(), holder.image, list.get(position).MainImageSrc, null, new RequestListener<Drawable>() {
//                @Override
//                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                    holder.loadingProgress.stop(); //image successfully load
//                    holder.image.setImageDrawable(ContextCompat.getDrawable(holder.image.getContext(), R.drawable.shop_icon));
//                    return false;
//                }
//
//                @Override
//                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                    holder.loadingProgress.stop(); //cat get image from server
//                    return false;
//                }
//            });
//
//        }
    }
//
//
//    @Override
//    public int getItemViewType(int position) {
//        //change align of each row to left & right
//        return position % 2;
//    }
}
