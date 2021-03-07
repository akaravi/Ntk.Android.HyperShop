package ntk.android.hyper.activity.hyper;

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
 * activity for showing detail of each product
 */

public class ShopContentDetailActivity extends BaseHyperShopContentDetail_1_Activity {
    View loading;

    @Override
    protected void onCreated() {
        setContentView(R.layout.hypercontent_detail_actvitiy);
      //loading placeholder until load products
        loading = findViewById(R.id.loadingProgress);
        //start order activity
        findViewById(R.id.cartView).setOnClickListener(view -> OrderActivity.START_ORDER_ACTIVITY(ShopContentDetailActivity.this));
    }

    /**
     * when user come back to activity,maybe change count of products in his order, so Card should be updated
     */
    @Override
    protected void onResume() {
        super.onResume();
        UpdateCard();
    }


    /**
     * bind model to view
     */
    @Override
    protected void bindData() {

        findViewById(R.id.back_button).setOnClickListener(view -> finish());
        ((TextView) findViewById(R.id.txtProductName)).setText(model.Name);
        ((TextView) findViewById(R.id.txtProductCount)).setText(model.Count + model.Unit);
        ((TextView) findViewById(R.id.txtProductPrice)).setText(NViewUtils.PriceFormat(model.Price) + " " + model.CURRENCY_UNIT);
        ((TextView) findViewById(R.id.txtDescriptionl)).setText(model.Memo);
        //category not set ot this content
        if (model.Category == null || model.Category.equalsIgnoreCase(""))
            ((TextView) findViewById(R.id.txtCategory)).setText("نامشخص");
        else
            ((TextView) findViewById(R.id.txtCategory)).setText(model.Category);
        //bind model to buy button
        ((BuyView) findViewById(R.id.buyView)).bind(model);
        //load image
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
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
    }
    //enable listen to Eventbus library event raised
    @Override
    protected void onStart() {
        super.onStart();

        EventBus.getDefault().register(this);
    }

    //disable listen to Eventbus library event raised
    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    /**
     * when user click on add to cart or + or - button this event raised
     *
     * @param event
     */
    @Subscribe
    public void EventRemove(UpdateCartViewEvent event) {
        UpdateCard();
    }

    /**
     * update products count in order
     */
    public void UpdateCard() {
        ((CartView) findViewById(R.id.cartView)).updateCount();
    }

}
