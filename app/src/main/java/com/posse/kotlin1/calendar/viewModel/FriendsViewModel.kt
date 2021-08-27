package com.posse.kotlin1.calendar.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.posse.kotlin1.calendar.model.Contact
import com.posse.kotlin1.calendar.model.Friend
import com.posse.kotlin1.calendar.model.repository.DOCUMENTS
import com.posse.kotlin1.calendar.model.repository.Repository
import com.posse.kotlin1.calendar.model.repository.RepositoryFirestoreImpl
import com.posse.kotlin1.calendar.utils.toDataClass
import java.util.*

class FriendsViewModel : ViewModel() {
    private val repository: Repository = RepositoryFirestoreImpl.newInstance()
    private val friendsData: HashSet<Friend> = hashSetOf()
    private lateinit var email: String
    private val liveDataToObserve: MutableLiveData<Pair<Boolean, Set<Friend>>> =
        MutableLiveData(Pair(false, hashSetOf()))

    fun getLiveData() = liveDataToObserve

    fun refreshLiveData(email: String, callback: (() -> Unit)? = null) {
        this.email = email
        liveDataToObserve.value = Pair(false, emptySet())
        repository.getData(DOCUMENTS.FRIENDS, email) { friends, isOffline ->
            friendsData.clear()
            friends?.values?.forEach { friendMap ->
                val friend = (friendMap as Map<String, Any>).toDataClass<Friend>()
                if (!friend.blocked) friendsData.add(friend)
            }
            sortPositions(friendsData.toList().sortedBy { it.position })
            liveDataToObserve.value = Pair(true, friendsData)
            if (isOffline) callback?.invoke()
        }
    }

    private fun sortPositions(list: List<Friend>) {
        for (i in list.indices) {
            if (list[i].position != i) {
                list[i].position = i
                repository.saveItem(DOCUMENTS.FRIENDS, email, list[i])
            }
        }
        friendsData.clear()
        friendsData.addAll(list)
    }

    fun friendSelected(friend: Friend) {
        var update = false
        var noOneIsSelected = true
        friendsData.forEach {
            if (it.selected) noOneIsSelected = false
            if (it.email == friend.email) {
                if (it.selected == friend.selected) update = true
                it.name = friend.name
                it.selected = friend.selected
                repository.saveItem(DOCUMENTS.FRIENDS, email, it)
            } else if (friend.selected && it.selected) {
                it.selected = false
                repository.saveItem(DOCUMENTS.FRIENDS, email, it)
                update = true
            }
        }
        if (update || noOneIsSelected) liveDataToObserve.value = Pair(true, friendsData)
    }

    fun itemMoved(fromPosition: Int, toPosition: Int) {
        friendsData.forEach {
            if (it.position == fromPosition) {
                it.position = toPosition
                repository.saveItem(DOCUMENTS.FRIENDS, email, it)
            } else if (it.position == toPosition) {
                it.position = fromPosition
                repository.saveItem(DOCUMENTS.FRIENDS, email, it)
            }
        }
    }

    fun changeName(friend: Friend) = repository.saveItem(DOCUMENTS.FRIENDS, email, friend)

    fun deleteFriend(friend: Friend) {
        if (friend.blocked) {
            repository.saveItem(DOCUMENTS.FRIENDS, email, friend)
        } else repository.removeItem(DOCUMENTS.FRIENDS, email, friend)
        repository.removeItem(DOCUMENTS.SHARE, friend.email, Contact(mutableListOf(), email))
        refreshLiveData(email)
    }
}