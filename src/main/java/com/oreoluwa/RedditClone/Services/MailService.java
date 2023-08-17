package com.oreoluwa.RedditClone.Services;

import com.oreoluwa.RedditClone.Entities.NotificationEmail;

public interface MailService {
    void sendMail(NotificationEmail notificationEmail);
    String buildMail(String message);
}
