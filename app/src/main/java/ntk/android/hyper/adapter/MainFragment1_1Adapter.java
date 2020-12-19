package ntk.android.hyper.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ntk.android.hyper.adapter.holder.MoreItemsVH;
import ntk.android.hyper.adapter.holder.CategoryVH;

public class MainFragment1_1Adapter extends RecyclerView.Adapter {
    HashMap<Integer, List> models;
    List<String> strings;

    public MainFragment1_1Adapter(List<String> titles) {
        strings = titles;
        models = new HashMap<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 1:
                return CategoryVH.create(parent);
            default:
                return MoreItemsVH.create(parent);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case 1:
                ((CategoryVH) holder).bind(models.get(viewType));
                break;
            default:
                ((MoreItemsVH) holder).bind(models.get(viewType));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    @Override
    public int getItemViewType(int position) {
        List<Integer> keys = new ArrayList(models.keySet());
        return keys.get(position);
    }

    public void put(int i, List<?> listItems) {
        models.put(i, listItems);
    }
}
