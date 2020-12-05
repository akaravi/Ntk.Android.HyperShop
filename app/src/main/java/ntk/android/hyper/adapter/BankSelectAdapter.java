package ntk.android.hyper.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ntk.android.base.NTKApplication;
import ntk.android.base.adapter.BaseRecyclerAdapter;
import ntk.android.base.config.NtkObserver;
import ntk.android.base.dtomodel.bankpayment.BankPaymentOnlineTransactionModel;
import ntk.android.base.dtomodel.hypershop.HyperShopOrderPaymentDtoModel;
import ntk.android.base.entitymodel.bankpayment.BankPaymentPrivateSiteConfigModel;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.services.hypershop.HyperShopOrderService;
import ntk.android.base.utill.FontManager;
import ntk.android.hyper.R;
import ntk.android.hyper.activity.hyper.CheckPaymentActivity;

public class BankSelectAdapter extends BaseRecyclerAdapter<BankPaymentPrivateSiteConfigModel, BankSelectAdapter.ViewHolder> {

    Long orderId;
    private final Context context;

    public BankSelectAdapter(Context context, Long orderId, List<BankPaymentPrivateSiteConfigModel> arrayList) {
        super(arrayList);
        this.orderId = orderId;
        this.context = context;
    }

    @Override
    public BankSelectAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_bank_payment, viewGroup, false);
        return new BankSelectAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.LblTitle.setText(getItem(position).Title);
        loadImage(getItem(position).LinkModuleFileLogoIdSrc, holder.Img);
        holder.itemView.setOnClickListener(view -> {


            HyperShopOrderPaymentDtoModel req = new HyperShopOrderPaymentDtoModel();
            req.LinkOrderId = orderId;
            CheckPaymentActivity.Last_Order_Id=orderId;
            req.BankPaymentPrivateId = list.get(position).Id;
            req.LastUrlAddressInUse= "oco.ir/"+NTKApplication.get().getApplicationParameter().APPLICATION_ID();
            new HyperShopOrderService(view.getContext()).orderPayment(req)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io()).subscribe(new NtkObserver<ErrorException<BankPaymentOnlineTransactionModel>>() {
                @Override
                public void onNext(@io.reactivex.annotations.NonNull ErrorException<BankPaymentOnlineTransactionModel> response) {
                    if (response.IsSuccess) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(response.Item.LastUrlAddressInUse));
                        context.startActivity(browserIntent);
                    } else {
                        Toasty.error(context, response.ErrorMessage).show();

                    }
                }

                @Override
                public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                    Toasty.error(context, "خطای سامانه").show();
                }
            });
        });
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.textview)
        TextView LblTitle;

        @BindView(R.id.imageview)
        ImageView Img;


        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            LblTitle.setTypeface(FontManager.GetTypeface(context, FontManager.IranSans));
        }
    }
}