
package ntk.android.hyper.adapter.holder;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ntk.android.base.utill.FontManager;
import ntk.android.base.view.NViewUtils;
import ntk.android.hyper.R;
import ntk.android.hyper.activity.HyperCategoryListActivity;
import ntk.android.hyper.adapter.hyper.HyperCategoryAdapter;

public class CategoryVH extends RecyclerView.ViewHolder {

    CategoryVH(@NonNull View item) {
        super(item);
    }

    public static RecyclerView.ViewHolder create(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_category, parent, false);
        ((TextView) inflate.findViewById(R.id.categoryTitle)).setTypeface(FontManager.T1_Typeface(parent.getContext()));
        inflate.setBackgroundColor(Color.parseColor("#F7F8FA"));
        return new CategoryVH(inflate);
    }

    public void bind(List list, String title) {
        RecyclerView rc = itemView.findViewById(R.id.rc);
        ((TextView) itemView.findViewById(R.id.categoryTitle)).setText(title);
        int i = NViewUtils.dpToPx(itemView.getContext(), 8);
        rc.setPadding(i, i, i, i);
        rc.setNestedScrollingEnabled(true);
        rc.setHasFixedSize(true);
        rc.setLayoutManager(new GridLayoutManager(itemView.getContext(), 3));
        rc.setAdapter(new HyperCategoryAdapter(list));
        itemView.findViewById(R.id.moreBtn).setOnClickListener(view -> {
            Intent intent = new Intent(itemView.getContext(), HyperCategoryListActivity.class);
            itemView.getContext().startActivity(intent);
        });
    }
}