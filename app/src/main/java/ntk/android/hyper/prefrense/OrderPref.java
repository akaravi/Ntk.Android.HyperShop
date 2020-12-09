package ntk.android.hyper.prefrense;

import android.content.Context;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import ntk.android.base.dtomodel.hypershop.HyperShopOrderContentDtoModel;
import ntk.android.base.dtomodel.hypershop.HyperShopOrderDtoModel;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.enums.enumHyperShopPaymentType;
import ntk.android.base.entitymodel.hypershop.HyperShopContentModel;
import ntk.android.base.utill.prefrense.EasyPreference;

public class OrderPref {
    private final Context c;

    public OrderPref(Context context) {
        this.c = context;
    }

    public void updateShopContent(HyperShopContentModel model, int count) {
        HyperShopOrderDtoModel order = getOrder();
        HyperShopOrderContentDtoModel p = null;
        for (int i = 0; i < order.Products.size(); i++) {
            if (order.Products.get(i).Code.equalsIgnoreCase(model.Code)) {
                p = order.Products.remove(i);
                break;
            }
        }
        if (p == null)
            p = new HyperShopOrderContentDtoModel();
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

    public void updateShopContent(HyperShopOrderContentDtoModel model, int count) {
        HyperShopOrderDtoModel order = getOrder();
        HyperShopOrderContentDtoModel p = null;
        for (int i = 0; i < order.Products.size(); i++) {
            if (order.Products.get(i).Code.equalsIgnoreCase(model.Code)) {
                p = order.Products.remove(i);
                break;
            }
        }
        if (p == null)
            p = new HyperShopOrderContentDtoModel();
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

    public void updateOrderWith(List<HyperShopOrderContentDtoModel> models, float amountOrder) {
        HyperShopOrderDtoModel order = getOrder();
        order.Products = models;
        order.Amount = amountOrder;
        saveOrder(order);

    }

    private void saveOrder(HyperShopOrderDtoModel model) {
        EasyPreference.with(c).addString("NTK_HyperShopOrder", new Gson().toJson(model));
    }

    public HyperShopOrderDtoModel getOrder() {
        String hypershopOrder = EasyPreference.with(c).getString("NTK_HyperShopOrder", "");

        if (!hypershopOrder.equalsIgnoreCase("")) {
            HyperShopOrderDtoModel hyperShopOrderDtoModel = new Gson().fromJson(hypershopOrder, HyperShopOrderDtoModel.class);
            return hyperShopOrderDtoModel;
        }
        HyperShopOrderDtoModel hyperShopOrderDtoModel = new HyperShopOrderDtoModel();
        hyperShopOrderDtoModel.Products = new ArrayList<>();
        return hyperShopOrderDtoModel;
    }

    public void addDetails(String name, String family, String mobile, String address, int type) {
        HyperShopOrderDtoModel order = getOrder();
        order.Name = name;
        order.Family = family;
        order.Mobile = mobile;
        order.Address = address;
        order.PaymentType =type;
        saveOrder(order);
    }

    public Observable<ErrorException<HyperShopOrderContentDtoModel>> getLastShopping() {
        return Observable.create(emitter -> {
            ErrorException<HyperShopOrderContentDtoModel> model = new ErrorException<>();
            model.IsSuccess = true;
            List<HyperShopOrderContentDtoModel> products = getOrder().Products;
            model.ListItems = new ArrayList<>();
            model.ListItems.addAll(products);
            model.TotalRowCount = products.size();
            emitter.onNext(model);
            emitter.onComplete();
        });
    }

    public HyperShopOrderContentDtoModel getProduct(String code) {
        HyperShopOrderDtoModel order = getOrder();
        HyperShopOrderContentDtoModel p = null;
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
}
