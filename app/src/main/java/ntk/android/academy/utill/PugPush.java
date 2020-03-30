package ntk.android.academy.utill;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.view.View;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.greenrobot.eventbus.EventBus;

import ntk.android.academy.Academy;
import ntk.android.academy.R;
import ntk.android.academy.activity.ActSplash;
import ntk.android.academy.event.EvNotify;
import ntk.android.academy.model.Notify;

/**
 * Created by Mehrdad Safari on 20-Mar-17.
 */

public class PugPush {

    public static void ShowNotification(Context context, Notify notification) {
        notification.ID = (int) System.currentTimeMillis() + 1;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("Academy", context.getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
            mBuilder = new NotificationCompat.Builder(context, "Academy");
        } else {
            mBuilder = new NotificationCompat.Builder(context);
        }
        PendingIntent intent = null;
        Intent i = new Intent(context, ActSplash.class);
        i.setData(Uri.parse(notification.Content));
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent = PendingIntent.getActivity(context, 0, i, 0);
        if (notification.ContentType == 0) {
            mBuilder.setSmallIcon(R.drawable.logo)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(notification.Content))
                    .setContentTitle(notification.Title)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setContentIntent(intent)
                    .setContentText(notification.Content);
        } else if (notification.ContentType == 5) {
            mBuilder.setSmallIcon(R.drawable.logo)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setContentIntent(intent)
                    .setStyle(new NotificationCompat.BigPictureStyle()
                            .bigPicture(ImageLoader.getInstance().loadImageSync(notification.BigImageSrc))
                            .setSummaryText(notification.Title)
                            .setBigContentTitle(notification.Content));
        }

        notificationManager.notify(notification.ID, mBuilder.build());
        if (Academy.Inbox) {
            EventBus.getDefault().post(new EvNotify(true));
        }
    }
}
