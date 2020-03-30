package ntk.android.academy.adapter.theme;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ntk.android.academy.R;
import ntk.android.academy.activity.ActArticleContentList;
import ntk.android.academy.adapter.AdArticle;
import ntk.android.academy.adapter.AdTag;
import ntk.android.academy.adapter.theme.holder.HoArticle;
import ntk.android.academy.adapter.theme.holder.HoButton;
import ntk.android.academy.adapter.theme.holder.HoImage;
import ntk.android.academy.adapter.theme.holder.HoSlider;
import ntk.android.academy.adapter.theme.holder.HoTag;
import ntk.android.academy.config.ConfigRestHeader;
import ntk.android.academy.config.ConfigStaticValue;
import ntk.android.academy.utill.Constant;
import ntk.android.academy.utill.EasyPreference;
import ntk.android.academy.utill.EndlessRecyclerViewScrollListener;
import ntk.base.api.article.interfase.IArticle;
import ntk.base.api.article.entity.ArticleContent;
import ntk.base.api.article.model.ArticleContentListRequest;
import ntk.base.api.article.model.ArticleContentResponse;
import ntk.base.api.article.model.ArticleContentViewRequest;
import ntk.base.api.article.entity.ArticleTag;
import ntk.base.api.article.model.ArticleTagRequest;
import ntk.base.api.article.model.ArticleTagResponse;
import ntk.base.api.baseModel.theme.ThemeChild;
import ntk.base.api.baseModel.theme.ThemeChildConfig;
import ntk.base.api.utill.RetrofitManager;
import ss.com.bannerslider.banners.Banner;
import ss.com.bannerslider.banners.RemoteBanner;
import ss.com.bannerslider.events.OnBannerClickListener;

public class AdHome extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ThemeChild> themes;
    private Context context;
    private List<ArticleTag> tags = new ArrayList<>();
    private AdTag adTag;
    private Map<Integer, List<ArticleContent>> map_articles = new HashMap<>();
    private Map<Integer, AdArticle> map_adapter = new HashMap<>();

    private int TotalTag = 0, TotalArticle = 0;

    public AdHome(Context context, List<ThemeChild> list) {
        this.themes = list;
        this.context = context;
        adTag = new AdTag(context, tags);
        for (int i = 0; i < themes.size(); i++) {
            if (themes.get(i).LayoutName.equals("ArticleContentList")) {
                List<ArticleContent> contents = new ArrayList<>();
                map_articles.put(i, contents);
                AdArticle adArticle = new AdArticle(context, contents);
                map_adapter.put(i, adArticle);
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        switch (viewType) {
            case 1:
                View view_article_category = inflater.inflate(R.layout.row_home_tag, viewGroup, false);
                viewHolder = new HoTag(context, view_article_category);
                break;
            case 2:
                View view_image = inflater.inflate(R.layout.row_home_image, viewGroup, false);
                viewHolder = new HoImage(view_image);
                break;
            case 3:
                View view_button = inflater.inflate(R.layout.row_home_button, viewGroup, false);
                viewHolder = new HoButton(context, view_button);
                break;
            case 4:
                View view_article = inflater.inflate(R.layout.row_home_article, viewGroup, false);
                viewHolder = new HoArticle(context, view_article);
                break;
            case 5:
                View view_slider = inflater.inflate(R.layout.row_home_slider, viewGroup, false);
                viewHolder = new HoSlider(view_slider);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case 1:
                HoTag hoTag = (HoTag) holder;
                configTag(hoTag, position);
                break;
            case 2:
                HoImage hoImage = (HoImage) holder;
                ConfigImage(hoImage, position);
                break;
            case 3:
                HoButton hoButton = (HoButton) holder;
                ConfigButton(hoButton, position);
                break;
            case 4:
                HoArticle hoArticle = (HoArticle) holder;
                ConfigArticle(hoArticle, position);
                break;
            case 5:
                HoSlider hoSlider = (HoSlider) holder;
                ConfigSlider(hoSlider, position);
                break;
        }
    }

    private void configTag(HoTag hoTag, int position) {
        LinearLayoutManager LMC = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true);
        hoTag.RvTag.setLayoutManager(LMC);
        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(LMC) {

            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (totalItemsCount <= TotalTag) {
                    RestCategory((page + 1), hoTag, position);
                }
            }
        };
        hoTag.RvTag.addOnScrollListener(scrollListener);
        hoTag.RvTag.setAdapter(adTag);

        RestCategory(1, hoTag, position);

    }

    private void RestCategory(int i, HoTag hoTag, int position) {
        RetrofitManager manager = new RetrofitManager(context);
        IArticle iArticle = manager.getCachedRetrofit(new ConfigStaticValue(context).GetApiBaseUrl()).create(IArticle.class);
        Map<String, String> headers = new ConfigRestHeader().GetHeaders(context);

        ArticleTagRequest request = new Gson().fromJson(themes.get(position).LayoutRequest, ArticleTagRequest.class);
        request.RowPerPage = 8;
        request.CurrentPageNumber = i;
        headers.put("body", new Gson().toJson(request));

        Observable<ArticleTagResponse> call = iArticle.GetTagList(headers, request);
        call.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<ArticleTagResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ArticleTagResponse articleTagResponse) {
                        tags.addAll(articleTagResponse.ListItems);
                        adTag.notifyDataSetChanged();
                        TotalTag = articleTagResponse.TotalRowCount;
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private void ConfigImage(HoImage hoImage, int position) {
        AdCoreImage adapter = new AdCoreImage(context, themes.get(position).LayoutChildConfigs);
        hoImage.Rv.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true);
        hoImage.Rv.setLayoutManager(manager);
        hoImage.Rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        if (position == 1) {
            new Handler().postDelayed(() -> {
                if (themes.get(position).LayoutChildConfigs.size() > 1) {
                    hoImage.Rv.smoothScrollToPosition(1);
                    new Handler().postDelayed(() -> hoImage.Rv.smoothScrollToPosition(0), 1000);
                }
            }, 3000);
        }
    }

    private void ConfigButton(HoButton hoButton, int position) {
        hoButton.Rv.setHasFixedSize(true);
        if (themes.get(position).LayoutTheme == 1) {
            hoButton.Rv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true));
            AdCoreButtonLinear adapter = new AdCoreButtonLinear(context, themes.get(position).LayoutChildConfigs);
            hoButton.Rv.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else if (themes.get(position).LayoutTheme == 2) {
            hoButton.Rv.setLayoutManager(new GridLayoutManager(context, 2));
            AdCoreButtonGrid adapter = new AdCoreButtonGrid(context, themes.get(position).LayoutChildConfigs);
            hoButton.Rv.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else if (themes.get(position).LayoutTheme == 3) {
            hoButton.Rv.setLayoutManager(new GridLayoutManager(context, 3));
            AdCoreButtonGrid adapter = new AdCoreButtonGrid(context, themes.get(position).LayoutChildConfigs);
            hoButton.Rv.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    private void ConfigArticle(HoArticle hoArticle, int position) {
        hoArticle.Lbls.get(0).setTextColor(Color.parseColor(themes.get(position).LayoutConfig.get(0).FrontColor));
        hoArticle.Lbls.get(0).setTextSize(Float.parseFloat(themes.get(position).LayoutConfig.get(0).FontSize));
        hoArticle.Lbls.get(1).setTextColor(Color.parseColor(themes.get(position).LayoutConfig.get(1).FrontColor));
        hoArticle.Lbls.get(1).setTextSize(Float.parseFloat(themes.get(position).LayoutConfig.get(1).FontSize));
        hoArticle.Lbls.get(0).setText(themes.get(position).LayoutConfig.get(0).Title);
        hoArticle.Lbls.get(1).setText(themes.get(position).LayoutConfig.get(1).Title);


        String RequestStr = EasyPreference.with(context).getString("ArticleContentList", "");
        if (!RequestStr.equals("")) {
            Log.i("likfvj", "ConfigArticle: " + RequestStr + "");
            ArticleContentResponse response = new Gson().fromJson(RequestStr, ArticleContentResponse.class);
            map_articles.get(position).addAll(response.ListItems);
            map_adapter.get(position).notifyDataSetChanged();
            TotalArticle = response.TotalRowCount;
            hoArticle.RvMenu.setItemViewCacheSize(map_articles.get(position).size());
            hoArticle.Progress.setVisibility(View.GONE);
        }


        hoArticle.Lbls.get(1).setOnClickListener(v -> {
            Intent intent = new Intent(context, ActArticleContentList.class);
            intent.putExtra("Request", themes.get(position).LayoutConfig.get(1).ActionRequest);
            context.startActivity(intent);
        });

        LinearLayoutManager LMC = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true);
        hoArticle.RvMenu.setLayoutManager(LMC);
        EndlessRecyclerViewScrollListener listener = new EndlessRecyclerViewScrollListener(LMC) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (totalItemsCount <= TotalArticle) {
                    RestArticle((page + 1), hoArticle, position);
                }
            }
        };
        hoArticle.RvMenu.addOnScrollListener(listener);
        hoArticle.RvMenu.setAdapter(map_adapter.get(position));
        RestArticle(1, hoArticle, position);
    }

    private void RestArticle(int i, HoArticle hoArticle, int position) {
        hoArticle.Progress.setVisibility(View.VISIBLE);
        RetrofitManager manager = new RetrofitManager(context);
        IArticle iArticle = manager.getCachedRetrofit(new ConfigStaticValue(context).GetApiBaseUrl()).create(IArticle.class);
        ArticleContentListRequest request = new Gson().fromJson(themes.get(position).LayoutRequest, ArticleContentListRequest.class);
        request.RowPerPage = 20;
        request.CurrentPageNumber = i;
        Map<String, String> headers = new ConfigRestHeader().GetHeaders(context);
        Observable<ArticleContentResponse> call = iArticle.GetContentList(headers, request);
        call.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<ArticleContentResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ArticleContentResponse articleContentResponse) {
                        Log.i("likfvj", "ConfigArticle: " + EasyPreference.with(context).addString("ArticleContentList", String.valueOf(articleContentResponse)) + "");
                        if (!EasyPreference.with(context).getString("ArticleContentList", "").equals("")) {
                            EasyPreference.with(context).addString("ArticleContentList", new Gson().toJson(articleContentResponse));
                        }
                        map_articles.get(position).addAll(articleContentResponse.ListItems);
                        map_adapter.get(position).notifyDataSetChanged();
                        TotalArticle = articleContentResponse.TotalRowCount;
                        hoArticle.RvMenu.setItemViewCacheSize(map_articles.get(position).size());
                        hoArticle.Progress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        hoArticle.Progress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void ConfigSlider(HoSlider hoSlider, int position) {
        List<Banner> banners = new ArrayList<>();
        for (ThemeChildConfig t : themes.get(position).LayoutChildConfigs) {
            banners.add(new RemoteBanner(t.Href));
        }
        hoSlider.Slider.setBanners(banners);
        hoSlider.Slider.setOnBannerClickListener(new OnBannerClickListener() {
            @Override
            public void onClick(int p) {
                if (themes.get(position).LayoutChildConfigs.get(p).ActionName.equals("WebClick")) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(themes.get(position).LayoutChildConfigs.get(p).ActionRequest));
                    context.startActivity(i);
                } else if (themes.get(position).LayoutChildConfigs.get(p).ActionName.equals("ArticleContentList")) {
                    Intent intent = new Intent(context, ActArticleContentList.class);
                    intent.putExtra("Request", themes.get(position).LayoutChildConfigs.get(p).ActionRequest);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        for (Map.Entry<String, Integer> map : Constant.MapXml.entrySet()) {
            if (themes.get(position).LayoutName.equals(map.getKey())) {
                return map.getValue();
            }
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return themes.size();
    }

}
