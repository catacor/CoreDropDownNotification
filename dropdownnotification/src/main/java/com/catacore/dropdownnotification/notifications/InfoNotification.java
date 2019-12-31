package com.catacore.dropdownnotification.notifications;

import android.content.Context;
import android.view.View;

import com.catacore.dropdownnotification.R;

public class InfoNotification implements INotification {
    private String message;
    private NotificationType type;
    private View root;
    private Context mContext;

    public InfoNotification(Context context, String message) {
        this.message = message;
        this.type = NotificationType.INFO;
        mContext = context;
        root = View.inflate(mContext, R.layout.notification_layout,null);
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public NotificationType getType() {
        return type;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void setType(NotificationType type) {
        this.type = type;
    }

    @Override
    public View getRootView() {
        return root;
    }
}
