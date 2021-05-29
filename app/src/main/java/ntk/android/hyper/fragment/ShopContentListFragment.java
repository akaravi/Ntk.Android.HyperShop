package ntk.android.hyper.fragment;

import androidx.recyclerview.widget.RecyclerView;

import io.reactivex.Observable;
import java9.util.function.Function;
import ntk.android.base.entitymodel.base.ErrorException;
import ntk.android.base.entitymodel.base.FilterModel;
import ntk.android.base.entitymodel.hypershop.HyperShopContentModel;
import ntk.android.base.fragment.abstraction.AbstractionListFragment;
import ntk.android.base.fragment.common.BaseFilterModelFragment;
import ntk.android.base.services.hypershop.HyperShopContentService;
import ntk.android.hyper.adapter.hyper.PrevHypershopContentAdapter;


public class ShopContentListFragment extends BaseFilterModelFragment<HyperShopContentModel> {
    @Override
    protected void requestOnIntent() {

    }

    @Override
    public Function<FilterModel, Observable<ErrorException<HyperShopContentModel>>> getService() {
        return new HyperShopContentService(getContext())::getAllMicroService;
    }


    @Override
    public boolean withToolbar() {
        return false;
    }

    @Override
    public RecyclerView.Adapter createAdapter() {
        return new PrevHypershopContentAdapter(models);
    }

    @Override
    public void ClickSearch() {

    }


//     ShopContentListAdapter adapter;
//    /**
//     * if activity recently has been get data from server set true
//     */
//    private boolean recentllyResponsed = false;
//    /**
//     * if ui of activty is destroied and reCreate Again
//     */
//    private boolean uiRefreshed = false;
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            this_CategoryID = getArguments().getLong(ExtrasString.EXTRA_CATEGORY_ID, -1);//get selected Category
//            paramFilter = (FilterModel) getArguments().getSerializable(ExtrasString.EXTRA_FRIST_ENTITY);//get selected Category
//        }
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View rootView = inflater.from(getActivity()).inflate(R.layout.fragment_list_with_categories, container, false);
//        //init view
//        super.setCategoryListener(rootView);
//        rc = (XRecyclerView) rootView.findViewById(R.id.xrecycler_view);
//        rc.setLoadingMoreEnabled(true);
//        rc.setPullRefreshEnabled(true);
//        /**
//         * {@link #onRefresh()} {@link #onLoadMore()} refer to this line
//         */
//        rc.setLoadingListener(this);
//        rc.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
//        return rootView;
//    }
//
//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        getFromServer();
//    }
//
//
//    /**
//     * get data from server and show data on this fragment base on raising ShopEvent
//     */
//
//    public void getFromServer() {
//        if (paramFilter == null) {
//            //#help# controler parameters init
//            ControlerParams<ShopSourceLayoutRequest, ShopSourceLayout, ShopEntity> params = new ControlerParams();
//            //#help# layout
//            params.serverparam.layout = "shopcontent";
//            params.serverparam.CacheUsed = true;
//            params.forceCallToServer = CurrentPage == 1 || recentllyResponsed;
//            params.needToLoadFrist = !recentllyResponsed || CurrentPage == 1;
//            // #help# initialize of request Class
//            params.serverparam.requestBody = new ShopSourceLayoutRequest().setCurrentPageNumber(CurrentPage).setRowPerPage(ShopSourceLayoutRequest.ROW_PER_PAGE)
//                    .addFilter(new FilterDataModel().setPropertyName("LinkCategoryId").setIntValue1(this_CategoryID == -1 ? null : this_CategoryID).setSearchType(SearchType.Equal));
//            // #help# Dao object for accessing Db
////        Dao<ShopEntity, Integer> doa = new DataBaseRecord<>(ShopEntity.class, getBaseActivity().getHelper()).getDoa(this.getBaseActivity());
////        try {
////            //#help query String base on Request Class
////            // init query base on request
////            QueryBuilder<ShopEntity, Integer> query = doa.queryBuilder().limit((long) reqest.rowPerPage).offset((long) (reqest.rowPerPage * (reqest.CurrentPageNumber - 1))).orderBy("androidId", false);
////            if (this_CategoryID != -1)
////                params.databaseparam.queryFilterrModel = query.where().eq("linkCategoryId", String.valueOf(this_CategoryID)).prepare();
////            else
////                params.databaseparam.queryFilterrModel = query.prepare();
////        } catch (SQLException e) {
////            e.printStackTrace();
////        }
//            if (CurrentPage == 1)//show loading only in page 1
//            {
//                params.showWaitView = true;
//
//            }
//            params.databaseparam.clearDataBase = CurrentPage == 1;
//            params.setRequestModelClass(ShopSourceLayoutRequest.class).setSourcelayoutModelClass(ShopSourceLayout.class).setEntityClass(ShopEntity.class);
//            params.databaseparam.iSourceLayoutConverter = new ShopSourceLayoutConverter();
//            params.databaseparam.iRelationCreator = new ShopContentRelationCreator(getBaseActivity().getHelper());
//            getBaseActivity().<ShopSourceLayoutRequest, ShopSourceLayout, ShopEntity>getModels_fromServer(params, new ShopEvent());
//        } else {
//            ControlerParams<FilterModel, ShopSourceLayout, ShopEntity> params = new ControlerParams();
//            //#help# layout
//            params.serverparam.layout = "shopcontent";
//            params.serverparam.CacheUsed = true;
//            params.forceCallToServer = CurrentPage == 1 || recentllyResponsed;
//            params.needToLoadFrist = !recentllyResponsed || CurrentPage == 1;
//            // #help# initialize of request Class
//            paramFilter.CurrentPageNumber=CurrentPage;
//            params.serverparam.requestBody = paramFilter;
//            if (CurrentPage == 1)//show loading only in page 1
//            {
//                params.showWaitView = true;
//
//            }
//            params.databaseparam.clearDataBase = CurrentPage == 1;
//            params.setRequestModelClass(FilterModel.class).setSourcelayoutModelClass(ShopSourceLayout.class).setEntityClass(ShopEntity.class);
//            params.databaseparam.iSourceLayoutConverter = new ShopSourceLayoutConverter();
//            params.databaseparam.iRelationCreator = new ShopContentRelationCreator(getBaseActivity().getHelper());
//            getBaseActivity().<FilterModel, ShopSourceLayout, ShopEntity>getModels_fromServer(params, new ShopEvent());
//        }
//    }
//
//
//    /**
//     * RaisedEvent that submit by EventBus catch with this method
//     *
//     * @param response ShopEvent instance
//     * @see Subscribe
//     */
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void EventRaise(ShopEvent response) {
//        Log.i("ShopCOntentList", "Event Rised :" + response.reqEnumResult + " " + recentllyResponsed + " " + uiRefreshed + " ");
//        if (response.reqEnumResult == BaseEventResult.LOAD_PREV) {
//
//            if (response.getItemList() != null) {
//                if (!recentllyResponsed) {
//                    rc.loadMoreComplete();
//                    rc.refreshComplete();
//                    if (response.getItemList().size() < ShopSourceLayoutRequest.ROW_PER_PAGE)//fake pageing
//                        rc.setNoMore(true);//disable pagination
//
//                    showData(response.getItemList());
//                }
//            } else
//                getBaseActivity().switcher.showProgressView();
//        } else {
//            OnErrorViewListener retryError = new OnErrorViewListener() {
//                @Override
//                public void onErrorViewClicked() {
//                    onRefresh();
//                }
//            };
//            if (getBaseActivity().Show_RequestingErrors(!atLeastOnce, response, retryError, getBaseActivity().serverError)) {
//                rc.loadMoreComplete();
//                rc.refreshComplete();
//                if (response.getCurrentServerStatus() == BaseEvent.ServerStatus.ONLINE__RETRO_SUC__SERVER_OK) {
//                    recentllyResponsed = true;
//                    if (!uiRefreshed) {
//                        if (adapter != null)
//                            adapter.ClearAll();
//                        CurrentPage = 1;//same as onRefresh called but no need to call to server
//                        uiRefreshed = true;
//                    }
//                    if (response.getItemList().size() < ShopSourceLayoutRequest.ROW_PER_PAGE)//fake pageing
//                        rc.setNoMore(true);//disable pagination
//                    showData(response.getItemList());
//                } else {
//                    recentllyResponsed = false;
//                    uiRefreshed = false;
//                }
//            }
//        }
//    }
//
//    /**
//     * show geted data from Controller on Ui
//     *
//     * @param resposnes
//     */
//
//    private void showData(List<ShopEntity> resposnes) {
//        Log.i("NEWS", "showData:");
//        atLeastOnce = true;
//        getBaseActivity().switcher.showContentView();
//        if (adapter == null || adapter.getItemCount() == 0) {
//            //data of page 1 and show reCreate Adapter
//            adapter = new ShopContentListAdapter(resposnes);
//            rc.setAdapter(adapter);
//        } else {//data of next page is ready and should add
//            adapter.AddItems(resposnes);
//        }
//    }
//
//    /**
//     * used for refreshing page
//     * {@link XRecyclerView}
//     */
//    @Override
//    public void onRefresh() {
//        if (adapter != null)
//            adapter.ClearAll();
//        CurrentPage = 1; //refresh page and go to page 1
//        recentllyResponsed = false;
//        uiRefreshed = false;
//        getFromServer();
//
//    }
//
//    /**
//     * used for load more item on list
//     */
//    @Override
//    public void onLoadMore() {
//        CurrentPage++;//go to next page
//        getFromServer();
//    }
}