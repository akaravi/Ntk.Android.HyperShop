package ntk.android.hyper.adapter.holder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ntk.android.hyper.R;
import ntk.android.hyper.adapter.hyper.HyperCategoryAdapter;

public class CategoryVH extends RecyclerView.ViewHolder {

    CategoryVH(@NonNull View item) {
        super(item);
    }

    public static RecyclerView.ViewHolder create(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.base_wrap_recycle, parent, false);
        return new CategoryVH(inflate);
    }

    public void bind(List list) {
        RecyclerView rc = itemView.findViewById(R.id.rc);
        rc.setNestedScrollingEnabled(true);
        rc.setHasFixedSize(true);
        rc.setLayoutManager(new GridLayoutManager(itemView.getContext(), 3));
        rc.setAdapter(new HyperCategoryAdapter(list));
    }
}