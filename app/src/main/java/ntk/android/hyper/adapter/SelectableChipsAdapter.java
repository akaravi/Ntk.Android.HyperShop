package ntk.android.hyper.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;

import java.util.List;

import ntk.android.base.adapter.BaseRecyclerAdapter;
import ntk.android.hyper.R;

public class SelectableChipsAdapter extends BaseRecyclerAdapter<String, SelectableChipsAdapter.VH> {
    public SelectableChipsAdapter(List<String> list) {
        super(list);
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(inflate(parent, R.layout.chip_row_item));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Chip chips = (Chip) holder.itemView;
        chips.setText(list.get(position));
        chips.setOnClickListener(view -> {});
    }

    public class VH extends RecyclerView.ViewHolder {
        public VH(@NonNull View itemView) {
            super(itemView);
        }
    }
}
