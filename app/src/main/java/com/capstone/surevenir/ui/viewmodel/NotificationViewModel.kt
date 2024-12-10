package com.capstone.surevenir.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.capstone.surevenir.data.dao.NotificationDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationDao: NotificationDao
) : ViewModel() {
    val allNotifications = notificationDao.getAllNotifications().asLiveData()
    val unreadCount = notificationDao.getUnreadCount().asLiveData()

    fun markAsRead(notificationId: Long) {
        viewModelScope.launch(kotlinx.coroutines.Dispatchers.IO) {
            notificationDao.markAsRead(notificationId)
        }
    }


    fun markAllAsRead() {
        viewModelScope.launch(kotlinx.coroutines.Dispatchers.IO) {
            notificationDao.markAllAsRead()
        }
    }
}