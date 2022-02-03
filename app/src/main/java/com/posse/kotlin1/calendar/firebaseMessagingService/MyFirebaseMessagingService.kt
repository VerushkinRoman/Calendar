package com.posse.kotlin1.calendar.firebaseMessagingService

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.SharedPreferences
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.posse.kotlin1.calendar.R
import com.posse.kotlin1.calendar.model.User
import com.posse.kotlin1.calendar.model.repository.Documents
import com.posse.kotlin1.calendar.model.repository.Repository
import com.posse.kotlin1.calendar.model.repository.RepositoryFirestoreImpl.Companion.COLLECTION_USERS
import com.posse.kotlin1.calendar.utils.*
import dagger.android.AndroidInjection
import java.time.LocalDate
import javax.inject.Inject

class MyFirebaseMessagingService : FirebaseMessagingService() {
    @Inject
    lateinit var repository: Repository

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private lateinit var channelName: String
    private lateinit var channelDescriptionText: String
    private lateinit var channelID: String

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val remoteMessageData = remoteMessage.data
        if (remoteMessageData.isNotEmpty()) {
            handleDataMessage(remoteMessageData.toMap())
        }
    }

    private fun handleDataMessage(data: Map<String, String>) {
        val title = data[PUSH_KEY_TITLE]
        val message = data[PUSH_KEY_MESSAGE]
        if (!title.isNullOrBlank() && !message.isNullOrBlank()) {
            showNotification(title, message)
        }
    }

    private fun showNotification(title: String, message: String) {
        channelID = getString(R.string.drunk_channel)
        if (message == ADDED_YOU.toString() || message == REMOVED_YOU.toString()) channelID =
            getString(R.string.shared)
        val notificationBuilder = NotificationCompat.Builder(applicationContext, channelID)
            .apply {
                setSmallIcon(R.drawable.ic_splash_screen)
                setContentTitle(getText(title, message))
                priority = NotificationCompat.PRIORITY_DEFAULT
            }

        getSystemService<NotificationManager>()?.let {
            createNotificationChannel(it)
            it.notify(NOTIFICATION_ID, notificationBuilder.build())
        }
    }

    private fun getText(title: String, message: String): String {
        channelName = getString(R.string.shared)
        channelDescriptionText = getString(R.string.shared_notifications)
        val text: String = when (message) {
            ADDED_YOU.toString() -> getString(R.string.shared_with_you)
            REMOVED_YOU.toString() -> getString(R.string.removed_from_friends)
            else -> {
                channelName = getString(R.string.drunk_channel)
                channelDescriptionText = getString(R.string.drunk_notifications)
                val drunk = getString(R.string.drunk)
                when (val date = convertLongToLocalDale(message.toLong())) {
                    LocalDate.now() -> "$drunk ${getString(R.string.today)}!"
                    LocalDate.now().minusDays(1) -> "$drunk ${getString(R.string.yesterday)}!"
                    else -> "$drunk $date!"
                }
            }
        }
        return "$title $text"
    }

    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelID, channelName, importance).apply {
            description = channelDescriptionText
        }
        notificationManager.createNotificationChannel(channel)
    }

    override fun onNewToken(token: String) {
        sharedPreferences.token = token
        sharedPreferences.nickName?.let {
            repository.getData(Documents.Users, COLLECTION_USERS) { users, _ ->
                users?.forEach { userMap ->
                    try {
                        @Suppress("UNCHECKED_CAST")
                        val user = (userMap.value as Map<String, Any>).toDataClass<User>()
                        if (user.nickname == it) repository.saveUser(
                            User(user.email, it, token)
                        )
                    } catch (e: Exception) {
                    }
                }
            }
        }
    }

    companion object {
        const val ADDED_YOU: Long = -1
        const val REMOVED_YOU: Long = -2
        private const val PUSH_KEY_TITLE = "title"
        private const val PUSH_KEY_MESSAGE = "message"
        private const val NOTIFICATION_ID = 68
    }
}