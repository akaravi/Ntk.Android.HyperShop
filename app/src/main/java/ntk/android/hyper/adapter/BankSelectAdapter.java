package ntk.android.hyper.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ntk.android.base.NTKApplication;
import ntk.android.base.config.NtkObserver;
import ntk.android.base.dtomodel.bankpayment.BankPaymentOnlineTransactionModel;
import ntk.android.base.dtomodel.hypershop.HyperShopOrderPaymentDtoModel;
import ntk.android.base.entitymodel.bankpayment.BankPaymentPrivateSiteConfigModel;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.services.hypershop.HyperShopOrderService;
import ntk.android.hyper.R;
import ntk.android.hyper.activity.hyper.CheckPaymentActivity;

public class BankSelectAdapter extends ArrayAdapter<BankPaymentPrivateSiteConfigModel> {


    private final Context context;
    List<BankPaymentPrivateSiteConfigModel> list;

    public BankSelectAdapter(Context context,  List<BankPaymentPrivateSiteConfigModel> arrayList) {
        super(context, R.layout.row_bank_payment, arrayList);

        this.context = context;
        list = arrayList;
    }

    @Override
    public BankPaymentPrivateSiteConfigModel getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @NonNull
    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_bank_payment, parent, false);
        }
        ((TextView) view.findViewById(R.id.textview)).setText(getItem(position).Title);

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnFail(R.drawable.payment_type).cacheOnDisk(true).build();
        ImageLoader.getInstance().displayImage(getItem(position).LinkModuleFileLogoIdSrc, (ImageView) view.findViewById((R.id.imageview)), options);
        return view;
    }
}
//    @Override
//    public BankSelectAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
//        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_bank_payment, viewGroup, false);
//        return new BankSelectAdapter.ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        holder.LblTitle.setText(getItem(position).Title);
//        loadImage(getItem(position).LinkModuleFileLogoIdSrc, holder.Img);
//        holder.itemView.setOnClickListener(view -> {
//
//
//
//    }
//
//
//    public class ViewHolder extends RecyclerView.ViewHolder {
//
//        @BindView(R.id.textview)
//        TextView LblTitle;
//
//        @BindView(R.id.imageview)
//        ImageView Img;
//
//
//        public ViewHolder(View view) {
//            super(view);
//            ButterKnife.bind(this, view);
//            LblTitle.setTypeface(FontManager.GetTypeface(context, FontManager.IranSans));
//        }
//    }
//}