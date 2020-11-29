package ntk.android.hyper.adapter.hyper;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.dmoral.toasty.Toasty;
import ntk.android.base.dtomodel.hypershop.HyperShopOrderContentDtoModel;
import ntk.android.hyper.R;

public class HyperOrderContentAdapter extends  RecyclerView.Adapter {


    private final Context context;
    private List<HyperShopOrderContentDtoModel> products;
    Runnable changePriceMethod;

    public HyperOrderContentAdapter(Context context, List<HyperShopOrderContentDtoModel> products, Runnable o) {
        this.context = context;
        this.products = products;
        this.changePriceMethod = o;
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
        HyperShopOrderContentDtoModel item = products.get(position);
        holder.txtItemName.setText(item.Name);
        holder.etCount.setText(products.get(position).Count);
        holder.txtProductPrice.setText(String.format("%.2f", item.Price));

        holder.imgAdd.setOnClickListener(view -> {
            String s = holder.etCount.getText().toString();
            if (s.equalsIgnoreCase(""))
                s = "0";
            int count = Integer.parseInt(s) + 1;
            if (count <= products.get(position).TotalCount)
                holder.etCount.setText(String.valueOf(count));
            else
                Toasty.error(view.getContext(), "این تعداد از کالا موجود نمی باشد").show();

        });
        holder.imgRemove.setOnClickListener(view -> {
            String s = holder.etCount.getText().toString();
            if (s.equalsIgnoreCase(""))
                s = "0";
            int count = Integer.parseInt(s) - 1;
            if (count < 0)
                count = 0;
            holder.etCount.setText(String.valueOf(count));

        });
        holder.etCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().equals("")) {
                    int count = Integer.parseInt(charSequence.toString());
                    if (count <= products.get(position).TotalCount) {
                        products.get(position).Count = count;
                        changePriceMethod.run();
                    } else {
                        Toasty.error(context, "این تعداد از کالا موجود نمی باشد").show();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public List<HyperShopOrderContentDtoModel> models() {
        return products;
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        final TextView txtItemName;
        final TextView txtProductPrice;

        final ImageView imgAdd;
        final EditText etCount;

        public View imgRemove;

        ItemViewHolder(View itemView) {
            super(itemView);
            this.txtItemName = itemView.findViewById(R.id.txtItemName);
            this.txtProductPrice = itemView.findViewById(R.id.txtProductPrice);
            this.imgAdd = itemView.findViewById(R.id.imgAddProduct);
            this.imgRemove = itemView.findViewById(R.id.imgRemoveProduct);

            this.etCount = itemView.findViewById(R.id.etProductCount);


        }

    }
}
