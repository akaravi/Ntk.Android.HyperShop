package ntk.android.hyper.activity.hyper;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import ntk.android.base.activity.hyper.BaseHyperShopContentDetail_1_Activity;
import ntk.android.base.view.NViewUtils;
import ntk.android.hyper.R;
import ntk.android.hyper.event.UpdateCartViewEvent;
import ntk.android.hyper.view.BuyView;
import ntk.android.hyper.view.CartView;


/**
 * Created by m.parishani on 12/20/2017.
 */

public class ShopContentDetailActivity extends BaseHyperShopContentDetail_1_Activity {
    View loading;

    @Override
    protected void onCreated() {
        setContentView(R.layout.hypercontent_detail_actvitiy);
        loading = findViewById(R.id.loadingProgress);
        findViewById(R.id.cartView).setOnClickListener(view -> startActivity(new Intent(ShopContentDetailActivity.this, OrderActivity.class)));
    }
    @Override
    protected void onResume() {
        super.onResume();
        UpdateCard();
    }


    @Override
    protected void bindData() {
        findViewById(R.id.back_button).setOnClickListener(view -> finish());
        ((TextView) findViewById(R.id.txtProductName)).setText(model.Name);
        ((TextView) findViewById(R.id.txtProductCount)).setText(model.Count + "addad");
        ((TextView) findViewById(R.id.txtProductPrice)).setText(NViewUtils.PriceFormat(model.Price) + " " + model.CURRENCY_UNIT);
        ((TextView) findViewById(R.id.txtDescriptionl)).setText(model.Memo);
        if (model.Category == null || model.Category.equalsIgnoreCase(""))
            ((TextView) findViewById(R.id.txtCategory)).setText("نامشخص");
        else
            ((TextView) findViewById(R.id.txtCategory)).setText(model.Category);
//      todo      ((TextView) findViewById(R.id.txtProductCount)).setText(model.Count + model.QueantityName);
        ((BuyView) findViewById(R.id.buyView)).bind(model);
        DisplayImageOptions options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.empty_product)
                .showImageOnFail(R.drawable.empty_product).cacheOnDisk(true).build();
        ImageLoader.getInstance().displayImage(model.Image, (ImageView) findViewById(R.id.imgProduct), options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                loading.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                loading.setVisibility(View.GONE);

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                loading.setVisibility(View.GONE);
//                ((ImageView) view).setImageResource(R.drawable.logo);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
    @Subscribe
    public void EventRemove(UpdateCartViewEvent event) {
        UpdateCard();
    }

    public void UpdateCard() {
        ((CartView) findViewById(R.id.cartView)).updateCount();
    }
    //    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        setContentView(R.layout.activity_shop);
//        super.onCreate(savedInstanceState);
//        Id = getIntent().getExtras().getLong(ExtrasString.EXTRA_ID, 0);
//        handleData();
//
//        ((PersianTextView) findViewById(R.id.newsTitle)).setText(entity.title);
//        findViewById(R.id.add_item).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showCount(++count);
//            }
//        });
//        findViewById(R.id.remove_item).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showCount(--count);
//            }
//        });
//
//        findViewById(R.id.AddToCardBtn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Invoice("Add");
//            }
//        });
//        WebView browser = (WebView) findViewById(R.id.webview);
//        browser.getSettings().setJavaScriptEnabled(true);
//        browser.loadData(HtmlCreaterHelper.Load(entity.Description), "text/html; charset=utf-8", "utf-8");
//        SliderLayout mslider = findViewById(R.id.slider);
//        //اضافه کردن عکس ها به آرایه برای نمایش در اسلایدر
//        List<String> images = new ArrayList<>();
//        if (entity.MainImageSrc != null)
//            images.add(entity.MainImageSrc);
//        if (entity.contentSlider != null)
//            images.addAll(entity.contentSlider);
//        SlidersHelper.InitClickable(mslider, images);
//
//    }
//
//    private void handleData() {
//        if (AppUtill.isNetworkAvailable(this)) {
//            new HyperShopMicroService(this).getOneContent(Id.toString())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribeOn(Schedulers.io())
//                    .subscribe(new NtkObserver<ErrorException<HyperShopContentModel>>() {
//                        @Override
//                        public void onNext(@NonNull ErrorException<HyperShopContentModel> result) {
//                            if (result.IsSuccess) {
//                                entity = result.Item;
//                                SetData();
//                            }
//                        }
//
//                        @Override
//                        public void onError(@NonNull Throwable e) {
//
//                        }
//                    }
//        }
//
//        private void showCount ( int i){
//            if (i == 1)
//                findViewById(R.id.remove_item).setEnabled(false);
//            else findViewById(R.id.remove_item).setEnabled(true);
//            ((TextView) findViewById(R.id.iteam_amount)).setText(String.valueOf(i));
//        }
//
//
//        private void Invoice (String actionDo){
//            ShopInvoiceActionRequest req = new ShopInvoiceActionRequest().setActionDo(actionDo).setContentId(entity.androidId).setContentCount(Integer.valueOf(((TextView) findViewById(R.id.iteam_amount)).getText().toString()));
//            ControlerParams<ShopInvoiceActionRequest, ShopInvoiceSaleSourceLayout, ShopInvoiceSaleEntity> controlerParams = new ControlerParams<>();
//            controlerParams.setLayout("ShopInvoiceCart");
//            controlerParams.setRequestBody(req);
//            controlerParams.setShowResponseView(true);
//            controlerParams.databaseparam.iSourceLayoutConverter = new ISourceLayoutConverter<ShopInvoiceSaleSourceLayout, ShopInvoiceSaleEntity>() {
//                @Override
//                public ShopInvoiceSaleEntity ConvertTo(ShopInvoiceSaleSourceLayout s) {
//                    ShopInvoiceSaleEntity e = new ShopInvoiceSaleEntity();
//                    e.androidId = s.id;
//                    return e;
//                }
//
//                @Override
//                public ShopInvoiceSaleSourceLayout ConvertTo(ShopInvoiceSaleEntity e) {
//                    return null;
//                }
//            };
//            controlerParams.setRequestModelClass(ShopInvoiceActionRequest.class).setSourcelayoutModelClass(ShopInvoiceSaleSourceLayout.class).setEntityClass(ShopInvoiceSaleEntity.class);
//            sendModels_toServer(controlerParams, new ShopInvoiceActionEvent());
//        }
//
//        @Override
//        protected void ViewSwitcher () {
//
//        }
//
//        @Subscribe(threadMode = ThreadMode.MAIN)
//        public void InvioceActionRaised (ShopInvoiceActionEvent event){
//            OnErrorViewListener retryError = new OnErrorViewListener() {
//                @Override
//                public void onErrorViewClicked() {
//                    findViewById(R.id.AddToCardBtn).performClick();
//                }
//            };
//            if (Show_RequestingErrors(true, event, retryError, serverError)) {
//                if (event.getItemList().get(0).shopInvoiceSale.InvoiceSaleDetails != null) {
//                    PrefrenceHelper.setInvoiceCount(this, event.getItemList().get(0).shopInvoiceSale.InvoiceSaleDetails.size());
//                    EventBus.getDefault().post(new UiBadgeEvent());
//                }
//            }
//        }
//    }
//
//    private void SetData() {
//        ImageLoader.getInstance().displayImage(model.Item.MainImageSrc, ImgHeader);
//        if (entity.Body != null)
//            webViewBody.loadData("<html dir=\"rtl\" lang=\"\"><body>" + model.Item.body + "</body></html>", "text/html; charset=utf-8", "UTF-8");
//
//    }
}
