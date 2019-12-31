package com.catacore.dropdownnotification.notifications;

public enum NotificationType {
    ERROR("Error"),
    INFO("Info"),
    SUCCESS("Success"),
    CONFIRMATION("Confirmation");


    private NotificationType(String text)
    {
        content = text;
    }

    private String content;

    public String getContent(){ return content;}
}
