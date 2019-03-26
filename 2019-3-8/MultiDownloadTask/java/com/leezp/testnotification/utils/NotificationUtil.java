package com.leezp.testnotification.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.leezp.testnotification.R;
import com.leezp.testnotification.ThirdActivity;
import com.leezp.testnotification.entities.FileInfo;
import com.leezp.testnotification.services.NotificationDownloadService;

import java.util.HashMap;
import java.util.Map;

/**
 * 通知工具类
 */
public class NotificationUtil {
    private NotificationManager mNotificationMananger = null;
    private Map<Integer, Notification> mNotifications = null;
    private Context mContext = null;

    public NotificationUtil(Context context) {
        mContext = context;
        //获取通知系统服务
        mNotificationMananger = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        //创建通知的集合
        mNotifications = new HashMap<>();
    }

    /**
     * 显示通知
     * @param fileInfo
     */
    public void showNotification(FileInfo fileInfo) {
        //判断通知是否已经显示了
        if (!mNotifications.containsKey(fileInfo.getId())) {
            //创建通知对象
            Notification notification = new Notification();
            //设置滚动文字
            notification.tickerText = fileInfo.getFileName() + "开始下载";
            //设置显示时间
            notification.when = System.currentTimeMillis();
            //设置图标
            notification.icon = R.mipmap.ic_launcher;
            //设置通知特性
            notification.flags = Notification.FLAG_AUTO_CANCEL;
            //设置点击通知栏的操作
            Intent intent = new Intent(mContext, ThirdActivity.class);
            PendingIntent pintent = PendingIntent.getActivity(mContext, 0, intent, 0);
            notification.contentIntent = pintent;
            //创建RemoteViews对象
            RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.notification);
            //设置开始按钮操作
            Intent intentStart = new Intent(mContext, NotificationDownloadService.class);
            intentStart.setAction(NotificationDownloadService.ACTION_START);
            intentStart.putExtra("fileInfo",fileInfo);
            PendingIntent piStart = PendingIntent.getService(mContext, fileInfo.getId(), intentStart, 0);
            remoteViews.setOnClickPendingIntent(R.id.buttonStart,piStart);
            //设置结束按钮操作
            Intent intentStop = new Intent(mContext, NotificationDownloadService.class);
            intentStop.setAction(NotificationDownloadService.ACTION_STOP);
            intentStop.putExtra("fileInfo",fileInfo);
            PendingIntent piStop = PendingIntent.getService(mContext, fileInfo.getId(), intentStop, 0);
            remoteViews.setOnClickPendingIntent(R.id.buttonStop,piStop);
            //设置TextView
            remoteViews.setTextViewText(R.id.tvFileName, fileInfo.getFileName());
            //设置Notification的视图
            notification.contentView = remoteViews;
            //发出通知
            mNotificationMananger.notify(fileInfo.getId(), notification);
            //把通知加到集合中
            mNotifications.put(fileInfo.getId(), notification);
        }
    }

    /**
     * 取消通知
     */
    public void cancelNotification(int id) {
        mNotificationMananger.cancel(id);
        mNotifications.remove(id);
    }

    /**
     * 更新进度条
     * @param id
     * @param progress
     */
    public void updateNotification(int id, int progress) {
        Notification notification = mNotifications.get(id);
        if (notification != null) {
            //修改进度条
            notification.contentView.setProgressBar(R.id.pbProgress, 100, progress, false);
            mNotificationMananger.notify(id, notification);
        }
    }
}
