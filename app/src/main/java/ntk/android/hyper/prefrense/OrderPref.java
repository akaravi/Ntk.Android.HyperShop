package ntk.android.hyper.prefrense;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.hypershop.HyperShopContentModel;
import ntk.android.base.entitymodel.hypershop.HyperShopOrderContentModel;
import ntk.android.base.entitymodel.hypershop.HyperShopOrderModel;
import ntk.android.base.utill.prefrense.EasyPreference;

public class OrderPref {
    private final Context c;

    public OrderPref(Context context) {
        this.c = context;
    }

    public void updateShopContent(HyperShopContentModel model, int count) {
        HyperShopOrderModel order = getOrder();
        HyperShopOrderContentModel p = null;
        for (int i = 0; i < order.Products.size(); i++) {
            if (order.Products.get(i).Code.equals(model.Code)) {
                p = order.Products.remove(i);
                break;
            }
        }
        if (p == null)
            p = new HyperShopOrderContentModel();
        p.Code = model.Code;
        p.Name = model.Name;
        p.Price = model.Price;
        p.Memo = model.Memo;
        p.Image = model.Image;
        p.TotalCount = model.Count;
        p.CURRENCY_UNIT = model.CURRENCY_UNIT;
        p.Count = count;
        if (p.Count > 0)
            order.Products.add(p);
        saveOrder(order);
    }

    public void updateShopContent(HyperShopOrderContentModel model, int count) {
        HyperShopOrderModel order = getOrder();
        HyperShopOrderContentModel p = null;
        for (int i = 0; i < order.Products.size(); i++) {
            if (order.Products.get(i).Code.equalsIgnoreCase(model.Code)) {
                p = order.Products.remove(i);
                break;
            }
        }
        if (p == null)
            p = new HyperShopOrderContentModel();
        p.Code = model.Code;
        p.Name = model.Name;
        p.Price = model.Price;
        p.Memo = model.Memo;
        p.Image = model.Image;
        p.TotalCount = model.TotalCount;
        p.CURRENCY_UNIT = model.CURRENCY_UNIT;
        p.Count = count;
        if (p.Count > 0)
            order.Products.add(p);
        saveOrder(order);
    }

    public void updateOrderWith(List<HyperShopOrderContentModel> models, float amountOrder) {
        HyperShopOrderModel order = getOrder();
        order.Products = models;
        order.Amount = amountOrder;
        saveOrder(order);

    }

    private void saveOrder(HyperShopOrderModel model) {
        EasyPreference.with(c).addString("NTK_HyperShopOrder", new Gson().toJson(model));
    }
    public void saveLastOrder(HyperShopOrderModel model) {
        EasyPreference.with(c).addString("NTK_HyperShopOrder", new Gson().toJson(model));
    }
    public HyperShopOrderModel getOrder() {
        String hypershopOrder = EasyPreference.with(c).getString("NTK_HyperShopOrder", "");

        if (!hypershopOrder.equalsIgnoreCase("")) {
            HyperShopOrderModel HyperShopOrderModel = new Gson().fromJson(hypershopOrder, HyperShopOrderModel.class);
            return HyperShopOrderModel;
        }
        HyperShopOrderModel HyperShopOrderModel = new HyperShopOrderModel();
        HyperShopOrderModel.Products = new ArrayList<>();
        return HyperShopOrderModel;
    }

    public void addDetails(String name, String family, String mobile, String address, int type, LatLng orderLocation) {
        HyperShopOrderModel order = getOrder();
        order.Name = name;
        order.Family = family;
        order.Mobile = mobile;
        order.Address = address;
        order.PaymentType = type;
        if (orderLocation != null) {
            order.GeoLocationLatitude = String.valueOf(orderLocation.latitude);
            order.GeoLocationLongitude = String.valueOf(orderLocation.longitude);
        }
        saveOrder(order);
    }

    public Observable<ErrorException<HyperShopOrderContentModel>> getLastShopping() {
        return Observable.create(emitter -> {
            ErrorException<HyperShopOrderContentModel> model = new ErrorException<>();
            model.IsSuccess = true;
            List<HyperShopOrderContentModel> products = getOrder().Products;
            model.ListItems = new ArrayList<>();
            model.ListItems.addAll(products);
            model.TotalRowCount = products.size();
            emitter.onNext(model);
            emitter.onComplete();
        });
    }

    public HyperShopOrderContentModel getProduct(String code) {
        HyperShopOrderModel order = getOrder();
        HyperShopOrderContentModel p = null;
        for (int i = 0; i < order.Products.size(); i++) {
            if (order.Products.get(i).Code.equalsIgnoreCase(code)) {
                p = order.Products.get(i);
                break;
            }
        }
        return p;
    }

    public void clear() {
        EasyPreference.with(c).addString("NTK_HyperShopOrder", "");
    }

    public void lastOrder(HyperShopOrderModel item) {
        clear();
        HyperShopOrderModel order = new HyperShopOrderModel();
        for (int i = 0; i < item.Products.size(); i++) {
            HyperShopOrderContentModel model = item.Products.get(i);
            HyperShopOrderContentModel p = new HyperShopOrderContentModel();
            p.Code = model.Code;
            p.Name = model.Name;
            p.Price = model.Price;
            p.Memo = model.Memo;
            p.Count = model.Count;
            p.CURRENCY_UNIT = "نداریم تو فاکتور";
            p.TotalCount = 100;
            order.Products.add(p);
        }
        saveOrder(order);
    }
}
