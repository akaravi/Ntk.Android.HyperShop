package ntk.android.hyper.activity.hyper;

import android.animation.Animator;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.steelkiwi.library.IncrementProductView;
import com.steelkiwi.library.listener.OnStateListener;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ntk.android.base.Extras;
import ntk.android.base.config.NtkObserver;
import ntk.android.base.dialog.baseFragmentDialog;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.hypershop.HyperShopContentModel;
import ntk.android.base.services.hypershop.HyperShopContentService;
import ntk.android.hyper.R;
import ntk.android.hyper.prefrense.OrderPref;
import ntk.android.hyper.view.CartView;
import ntk.android.hyper.view.CircleAnimationUtil;

public class ShopContentDetailDialog extends baseFragmentDialog {

    private String code;
    private int productCount = 1;

    public static void show(AppCompatActivity context, String Id) {
        ShopContentDetailDialog d = new ShopContentDetailDialog();
        Bundle bundle = new Bundle();
        bundle.putString(Extras.EXTRA_FIRST_ARG, Id);
        d.setArguments(bundle);
        d.show(context.getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onCreateFragment() {
        setContentView(R.layout.dialog_item_detail);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow()
                .getAttributes().windowAnimations = R.style.NoAnimDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        code = getArguments().getString(Extras.EXTRA_FIRST_ARG, "");
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        switcher.showProgressView();
        new HyperShopContentService(getActivity()).getOneMicroService(code)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new NtkObserver<ErrorException<HyperShopContentModel>>() {
                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull ErrorException<HyperShopContentModel> response) {
                        if (response.IsSuccess)
                            new Handler().postDelayed(() -> showModel(response.Item), 10000);
                        else
                            Toasty.warning(ShopContentDetailDialog.this.getContext(), response.ErrorMessage).show();
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        Toasty.warning(getContext(), "خطای سامانه", Toasty.LENGTH_LONG, true).show();
                    }
                });
    }

    private void showModel(HyperShopContentModel item) {
        switcher.showContentView();
        View view = getView();
        IncrementProductView incrementProductView = (IncrementProductView) view.findViewById(R.id.productView);
        TextView txtItemName = view.findViewById(R.id.txtItemName);
        TextView txtUnitPrice = view.findViewById(R.id.txtUnitPrice);
        TextView txtExtendedPrice = view.findViewById(R.id.txtExtendedPrice);
        TextView txtQuantity = view.findViewById(R.id.txtQuantity);
        ImageView imgThumbnail = view.findViewById(R.id.imgThumbnail);
        Button btnCancel = view.findViewById(R.id.btnCancel);
        Button btnOk = view.findViewById(R.id.btnOk);

        txtItemName.setText(item.Name);
        txtUnitPrice.setText(String.format("%.2f", item.Price));
        txtQuantity.setText("1");
        txtExtendedPrice.setText(String.format("%.2f", item.Price * 1));


//        this.getDialog().getWindow().getAttributes().windowAnimations = R.style.DetailDialogAnimation;

        ImageLoader.getInstance().displayImage(item.Image, imgThumbnail);
//
        incrementProductView.getAddIcon();
//
        incrementProductView.setOnStateListener(new OnStateListener() {

            @Override
            public void onCountChange(int count) {
                txtQuantity.setText(String.valueOf(count));
                txtExtendedPrice.setText(String.format("%.2f", item.Price * count));
                //todo check count be < entitiy.count
            }

            @Override
            public void onConfirm(int count) {
                productCount = count;
            }

            @Override
            public void onClose() {

            }
        });

        btnCancel.setOnClickListener(view12 -> dismiss());

        btnOk.setOnClickListener(view1 -> {
            new OrderPref(getContext()).addShopContent(item, productCount);
            addItemToCartAnimation(imgThumbnail);
            dismiss();
        });
    }

    private void addItemToCartAnimation(ImageView targetView) {
        CartView destView = getActivity().findViewById(R.id.cartView);
        if (destView != null)
            new CircleAnimationUtil().attachActivity(getActivity()).setTargetView(targetView).setMoveDuration(300).setDestView(destView).setAnimationListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    destView.updateCount();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            }).startAnimation();
    }
}
