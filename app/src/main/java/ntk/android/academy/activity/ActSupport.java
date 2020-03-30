package ntk.android.academy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ntk.android.academy.R;
import ntk.android.academy.adapter.AdTicket;
import ntk.android.academy.config.ConfigRestHeader;
import ntk.android.academy.config.ConfigStaticValue;
import ntk.android.academy.utill.AppUtill;
import ntk.android.academy.utill.EndlessRecyclerViewScrollListener;
import ntk.android.academy.utill.FontManager;
import ntk.base.api.article.interfase.IArticle;
import ntk.base.api.ticket.interfase.ITicket;
import ntk.base.api.ticket.model.TicketingListRequest;
import ntk.base.api.ticket.model.TicketingListResponse;
import ntk.base.api.ticket.entity.TicketingTask;
import ntk.base.api.utill.NTKUtill;
import ntk.base.api.utill.RetrofitManager;


public class ActSupport extends AppCompatActivity {

    @BindView(R.id.recyclerFrSupport)
    RecyclerView Rv;

    @BindView(R.id.FabFrSupport)
    FloatingActionButton Fab;

    @BindView(R.id.RefreshTicket)
    SwipeRefreshLayout Refresh;

    private ArrayList<TicketingTask> tickets = new ArrayList<>();
    private AdTicket adapter;

    private EndlessRecyclerViewScrollListener scrollListener;
    private int TotalTag = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_support);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        ((TextView) findViewById(R.id.lblStateActSupport)).setTypeface(FontManager.GetTypeface(this, FontManager.IranSans));
        Rv.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        Rv.setLayoutManager(manager);

        adapter = new AdTicket(this, tickets);
        Rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        scrollListener = new EndlessRecyclerViewScrollListener(manager) {

            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (totalItemsCount <= TotalTag) {
                    HandelData((page + 1));
                }
            }

            @Override
            public void onScrolled(RecyclerView view, int dx, int dy) {
                if (dy > 0 || dy < 0 && Fab.isShown())
                    Fab.hide();
            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Fab.show();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        };

        Rv.addOnScrollListener(scrollListener);

        Refresh.setColorSchemeResources(
                R.color.colorAccent,
                R.color.colorAccent,
                R.color.colorAccent);

        Refresh.setOnRefreshListener(() -> {
            tickets.clear();
            HandelData(1);
            Refresh.setRefreshing(false);
        });

        HandelData(1);
    }


    private void HandelData(int i) {
        RetrofitManager retro = new RetrofitManager(this);
        ITicket iTicket = retro.getCachedRetrofit(new ConfigStaticValue(this).GetApiBaseUrl()).create(ITicket.class);
        Map<String, String> headers = new ConfigRestHeader().GetHeaders(this);

        TicketingListRequest request = new TicketingListRequest();
        request.RowPerPage = 10;
        request.CurrentPageNumber = i;
        request.SortType = NTKUtill.Descnding_Sort;
        request.SortColumn = "Id";

        Observable<TicketingListResponse> Call = iTicket.GetTicketList(headers, request);
        Call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TicketingListResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(TicketingListResponse model) {
                        if (model.ListItems.isEmpty())
                            findViewById(R.id.lblStateActSupport).setVisibility(View.VISIBLE);
                        else{
                            findViewById(R.id.lblStateActSupport).setVisibility(View.GONE);
                            tickets.addAll(model.ListItems);
                            adapter.notifyDataSetChanged();
                            TotalTag = model.TotalRowCount;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toasty.warning(ActSupport.this, "خطای سامانه", Toasty.LENGTH_LONG, true).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @OnClick(R.id.FabFrSupport)
    public void ClickSendTicket() {
        startActivity(new Intent(this, ActSendTicket.class));
    }
}