package ntk.android.hypershop.utill;

import java.util.HashMap;
import java.util.Map;

public class Constant {

    public static Map<String , Integer> MapXml = new HashMap<>();


    public void SetMap() {
        MapXml.put("ArticleTagList" , 1);
        MapXml.put("CoreImage" , 2);
        MapXml.put("CoreButton" , 3);
        MapXml.put("ArticleContentList" , 4);
        MapXml.put("CoreSlider" , 5);
        MapXml.put("NewsTagList" , 21);
        MapXml.put("NewsContentList" , 22);
        MapXml.put("HyperShopContentList" , 31);
    }

}
