package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.Notification;
import com.example.entity.vo.response.NotificationVO;

import java.util.List;

public interface NotificationService extends IService<Notification> {
    List<NotificationVO> fineUserNotifications(int uid);
    void deleteUserNotifications(int id, int uid);
    void deleteUserAllNotifications(int uid);
    void addNotification(int uid, String title, String content, String type, String url);
}
