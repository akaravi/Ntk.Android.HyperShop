package ntk.android.academy.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ntk.android.academy.utill.FontManager;

public class AdSpinner<S> extends ArrayAdapter<String> {

    public AdSpinner(Context context, int resource, List<String> items) {
        super(context, resource, items);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView textView = (TextView) super.getView(position, convertView, parent);
        textView.setTypeface(FontManager.GetTypeface(getContext(), FontManager.IranSans));
        return textView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView textView = (TextView) super.getDropDownView(position, convertView, parent);
        textView.setTypeface(FontManager.GetTypeface(getContext(), FontManager.IranSans));
        return textView;
    }
}
