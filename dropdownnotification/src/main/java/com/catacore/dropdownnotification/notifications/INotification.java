package com.catacore.dropdownnotification.notifications;

import android.view.View;

public interface INotification {
    String getMessage();
    NotificationType getType();
    void setMessage(String message);
    void setType(NotificationType type);
    View getRootView();

}
