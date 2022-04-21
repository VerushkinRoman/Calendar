package com.posse.kotlin1.calendar.feature_friends.domain.use_case

import com.posse.kotlin1.calendar.common.data.model.Friend
import com.posse.kotlin1.calendar.common.domain.repository.FriendsRepository

class GetFriends(
    private val friendsRepository: FriendsRepository
) {
    suspend operator fun invoke(email: String): List<Friend> {
        return friendsRepository.getFriends(email)
    }
}