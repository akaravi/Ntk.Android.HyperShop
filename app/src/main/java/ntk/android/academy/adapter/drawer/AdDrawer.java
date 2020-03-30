package ntk.android.academy.adapter.drawer;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.google.gson.Gson;
import com.google.zxing.WriterException;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Map;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import ntk.android.academy.R;
import ntk.android.academy.activity.ActAbout;
import ntk.android.academy.activity.ActBlog;
import ntk.android.academy.activity.ActFaq;
import ntk.android.academy.activity.ActInbox;
import ntk.android.academy.activity.ActIntro;
import ntk.android.academy.activity.ActNews;
import ntk.android.academy.activity.ActPooling;
import ntk.android.academy.activity.ActSupport;
import ntk.android.academy.config.ConfigRestHeader;
import ntk.android.academy.config.ConfigStaticValue;
import ntk.android.academy.room.RoomDb;
import ntk.android.academy.utill.AppUtill;
import ntk.android.academy.utill.EasyPreference;
import ntk.android.academy.utill.FontManager;
import ntk.base.api.application.interfase.IApplication;
import ntk.base.api.application.model.ApplicationScoreRequest;
import ntk.base.api.application.model.ApplicationScoreResponse;
import ntk.base.api.core.entity.CoreMain;
import ntk.base.api.baseModel.theme.DrawerChild;
import ntk.base.api.utill.RetrofitManager;

public class AdDrawer extends RecyclerView.Adapter<AdDrawer.ViewHolder> {

    private List<DrawerChild> childs;
    private Context context;
    private int Click;
    private FlowingDrawer Drawer;

    public AdDrawer(Context context, List<DrawerChild> children, FlowingDrawer drawer) {
        this.childs = children;
        this.context = context;
        this.Drawer = drawer;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_drawer_child, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        ImageLoader.getInstance().displayImage(childs.get(position).Icon, holder.Img);
        if (childs.get(position).Type == 1) {
            int n = RoomDb.getRoomDb(context).NotificationDoa().AllUnRead().size();
            if (n != 0) {
                holder.Lbls.get(1).setText(String.valueOf(n));
                holder.Lbls.get(1).setVisibility(View.VISIBLE);
            }
        }
        holder.Lbls.get(0).setText(childs.get(position).Title);

        holder.Root.setOnClickListener(v -> {
            switch (childs.get(position).Id) {
                case 1:
                    ClickInbox();
                    break;
                case 2:
                    ClickNews();
                    break;
                case 3:
                    ClickPooling();
                    break;
                case 5:
                    ClickShare();
                    break;
                case 6:
                    ClickAbout();
                    break;
                case 7:
                    ClickContact();
                    break;
                case 8:
                    ClickFeedBack();
                    break;
                case 9:
                    ClickQuestion();
                    break;
                case 10:
                    ClickIntro();
                    break;
                case 11:
                    ClickBlog();
                    break;
            }
        });
    }

    private void ClickBlog() {
        context.startActivity(new Intent(context, ActBlog.class));
        if (Drawer != null) {
            Drawer.closeMenu(true);
        }
    }

    @Override
    public int getItemCount() {
        return childs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ImgRecyclerDrawerChild)
        ImageView Img;

        @BindViews({R.id.lblRecyclerDrawerChild, R.id.lblBadgeRecyclerDrawerChild})
        List<TextView> Lbls;

        @BindView(R.id.RootContainerRecyclerDrawerChild)
        RelativeLayout Root;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            Lbls.get(0).setTypeface(FontManager.GetTypeface(context, FontManager.IranSans));
            Lbls.get(1).setTypeface(FontManager.GetTypeface(context, FontManager.IranSans));
        }
    }

    private void ClickIntro() {
        Bundle bundle = new Bundle();
        bundle.putInt("Help", 1);
        Intent intent = new Intent(context, ActIntro.class);
        intent.putExtra("Help", bundle);
        context.startActivity(intent);
        if (Drawer != null) {
            Drawer.closeMenu(true);
        }
    }

    private void ClickInbox() {
        context.startActivity(new Intent(context, ActInbox.class));
        if (Drawer != null) {
            Drawer.closeMenu(true);
        }
    }

    private void ClickNews() {
        context.startActivity(new Intent(context, ActNews.class));
        if (Drawer != null) {
            Drawer.closeMenu(true);
        }
    }

    private void ClickPooling() {
        context.startActivity(new Intent(context, ActPooling.class));
        if (Drawer != null) {
            Drawer.closeMenu(true);
        }
    }

    private void ClickShare() {
        String st = EasyPreference.with(context).getString("configapp", "");
        CoreMain mcr = new Gson().fromJson(st, CoreMain.class);

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        window.setLayout(LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        dialog.setContentView(R.layout.dialog_qrcode);
        dialog.show();
        TextView Lbl = dialog.findViewById(R.id.lblTitleDialogQRCode);
        Lbl.setTypeface(FontManager.GetTypeface(context, FontManager.IranSans));

        QRGEncoder qrgEncoder = new QRGEncoder(mcr.AppUrl, null, QRGContents.Type.TEXT, 300);
        try {
            Bitmap bitmap = qrgEncoder.encodeAsBitmap();
            ImageView img = dialog.findViewById(R.id.qrCodeDialogQRCode);
            img.setImageBitmap(bitmap);
        } catch (WriterException e) {
            Toasty.warning(context, e.getMessage(), Toast.LENGTH_LONG, true).show();
        }

        Button Btn = dialog.findViewById(R.id.btnDialogQRCode);
        Btn.setTypeface(FontManager.GetTypeface(context, FontManager.IranSans));
        Btn.setOnClickListener(v -> {
            dialog.dismiss();
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.app_name) + "\n" + "لینک دانلود:" + "\n" + mcr.AppUrl);
            shareIntent.setType("text/txt");
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(Intent.createChooser(shareIntent, "به اشتراک گزاری با...."));
        });
        if (Drawer != null) {
            Drawer.closeMenu(true);
        }
    }

    private void ClickAbout() {
        context.startActivity(new Intent(context, ActAbout.class));
        if (Drawer != null) {
            Drawer.closeMenu(true);
        }
    }

    private void ClickContact() {
        context.startActivity(new Intent(context, ActSupport.class));
        if (Drawer != null) {
            Drawer.closeMenu(true);
        }
    }

    private void ClickFeedBack() {
        ApplicationScoreRequest request = new ApplicationScoreRequest();
        if (Drawer != null) {
            Drawer.closeMenu(true);
        }
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        window.setLayout(LinearLayoutCompat.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        dialog.setContentView(R.layout.dialog_comment);
        dialog.show();
        TextView Lbl = dialog.findViewById(R.id.lblTitleDialogComment);
        Lbl.setTypeface(FontManager.GetTypeface(context, FontManager.IranSans));
        final EditText Txt = dialog.findViewById(R.id.txtDialogComment);
        Txt.setTypeface(FontManager.GetTypeface(context, FontManager.IranSans));
        Txt.setText(EasyPreference.with(context).getString("RateMessage", ""));
        final MaterialRatingBar Rate = dialog.findViewById(R.id.rateDialogComment);
        Rate.setRating(EasyPreference.with(context).getInt("Rate", 0));
        Rate.setOnRatingChangeListener((ratingBar, rating) -> {
            request.ScorePercent = (int) rating;
            //برای تبدیل به درصد
            request.ScorePercent = request.ScorePercent * 17;
            if (request.ScorePercent > 100)
                request.ScorePercent = 100;
        });
        Button Btn = dialog.findViewById(R.id.btnDialogComment);
        Btn.setTypeface(FontManager.GetTypeface(context, FontManager.IranSans));
        Btn.setOnClickListener(v -> {
            if (Txt.getText().toString().isEmpty()) {
                Toast.makeText(context, "لطفا نظر خود را وارد نمایید", Toast.LENGTH_SHORT).show();
            } else {
                if (AppUtill.isNetworkAvailable(context)) {
                    request.ScoreComment = Txt.getText().toString();

                    RetrofitManager manager = new RetrofitManager(context);
                    IApplication iCore = manager.getCachedRetrofit(new ConfigStaticValue(context).GetApiBaseUrl()).create(IApplication.class);
                    Map<String, String> headers = new ConfigRestHeader().GetHeaders(context);
                    Observable<ApplicationScoreResponse> Call = iCore.SetScoreApplication(headers, request);
                    Call.observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe(new Observer<ApplicationScoreResponse>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onNext(ApplicationScoreResponse applicationScoreResponse) {
                                    if (applicationScoreResponse.IsSuccess) {
                                        dialog.dismiss();
                                        Toasty.success(context, "با موفقیت ثبت شد", Toast.LENGTH_LONG, true).show();
                                    } else {
                                        Toasty.warning(context, "مجددا تلاش کنید", Toast.LENGTH_LONG, true).show();
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Toasty.error(context, "خظا در اتصال به مرکز", Toast.LENGTH_LONG, true).show();
                                }

                                @Override
                                public void onComplete() {

                                }
                            });
                } else {
                    Toast.makeText(context, "عدم دسترسی به اینترنت", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void ClickQuestion() {
        context.startActivity(new Intent(context, ActFaq.class));
        if (Drawer != null) {
            Drawer.closeMenu(true);
        }
    }
}
