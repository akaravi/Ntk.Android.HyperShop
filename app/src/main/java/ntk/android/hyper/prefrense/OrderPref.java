package ntk.android.hyper.prefrense;

import android.content.Context;

import com.google.gson.Gson;

import java.util.ArrayList;

import ntk.android.base.dtomodel.hypershop.HyperShopOrderContentDtoModel;
import ntk.android.base.dtomodel.hypershop.HyperShopOrderDtoModel;
import ntk.android.base.entitymodel.hypershop.HyperShopContentModel;
import ntk.android.base.utill.prefrense.EasyPreference;

public class OrderPref {
    private final Context c;

    public OrderPref(Context context) {
        this.c = context;
    }

    public void addShopContent(HyperShopContentModel model, int count) {
        HyperShopOrderDtoModel order = getOrder();
        HyperShopOrderContentDtoModel p = new HyperShopOrderContentDtoModel();
        p.Code = model.Code;
        p.Name = model.Name;
        p.Price = model.Price;
        p.Memo = model.Memo;
        p.Image = model.Image;
        p.Count = count;
        order.Products.add(p);
        saveOrder(order);
    }

    private void saveOrder(HyperShopOrderDtoModel model) {
        EasyPreference.with(c).addString("HypershopOrder", new Gson().toJson(model));
    }

    public HyperShopOrderDtoModel getOrder() {
        String hypershopOrder = EasyPreference.with(c).getString("HypershopOrder", "");
        if (!hypershopOrder.equalsIgnoreCase("")) {
            HyperShopOrderDtoModel hyperShopOrderDtoModel = new Gson().fromJson(hypershopOrder, HyperShopOrderDtoModel.class);
            hyperShopOrderDtoModel.Products = new ArrayList<>();
            return hyperShopOrderDtoModel;
        }
        HyperShopOrderDtoModel hyperShopOrderDtoModel = new HyperShopOrderDtoModel();
        hyperShopOrderDtoModel.Products = new ArrayList<>();
        return hyperShopOrderDtoModel;
    }
}
