package ntk.android.hyper.prefrense;

import android.content.Context;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import ntk.android.base.dtomodel.hypershop.HyperShopOrderContentDtoModel;
import ntk.android.base.dtomodel.hypershop.HyperShopOrderDtoModel;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.hypershop.HyperShopContentModel;
import ntk.android.base.utill.prefrense.EasyPreference;

public class OrderPref {
    private final Context c;

    public OrderPref(Context context) {
        this.c = context;
    }

    public void addShopContent(HyperShopContentModel model, int count) {
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
        p.Count = count;
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
        EasyPreference.with(c).addString("HypershopOrder", new Gson().toJson(model));
    }

    public HyperShopOrderDtoModel getOrder() {
        String hypershopOrder = EasyPreference.with(c).getString("HypershopOrder", "");

        if (!hypershopOrder.equalsIgnoreCase("")) {
            HyperShopOrderDtoModel hyperShopOrderDtoModel = new Gson().fromJson(hypershopOrder, HyperShopOrderDtoModel.class);
            return hyperShopOrderDtoModel;
        }
        HyperShopOrderDtoModel hyperShopOrderDtoModel = new HyperShopOrderDtoModel();
        hyperShopOrderDtoModel.Products = new ArrayList<>();
        return hyperShopOrderDtoModel;
    }

    public void addDetails(String name, String family, String mobile, String address) {
        HyperShopOrderDtoModel order = getOrder();
        order.Name = name;
        order.Family = family;
        order.Mobile = mobile;
        order.Address = address;
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
}
