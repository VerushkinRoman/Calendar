package com.posse.kotlin1.calendar.feature_calendar.data.repository

import com.posse.kotlin1.calendar.common.domain.model.Response
import com.posse.kotlin1.calendar.common.utils.CoroutineDispatchers
import com.posse.kotlin1.calendar.feature_calendar.domain.model.DayData
import com.posse.kotlin1.calendar.feature_calendar.domain.repository.DatesRepository
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn

class DatesRepositoryImpl(
//    private val firestore: FirebaseFirestore,
//    private val firestoreRepository: FirestoreRepository,
    private val coroutineDispatchers: CoroutineDispatchers
) : DatesRepository {

    override fun getDates(userMail: String) = callbackFlow<Response<List<DayData>>> {
//        send(Response.Loading(null))
//
//        val subscription = firestore
//            .collection(userMail)
//            .document(Documents.Dates.value)
//            .addSnapshotListener { snapshot, _ ->
//                try {
//                    val result = getResult(snapshot!!)
//                    trySend(Response.Success(result.toList()))
//                } catch (e: Exception) {
//                    trySend(Response.Error("Class cast Exception", null)) //TODO standard message
//                }
//            }
//
//        awaitClose { subscription.remove() }
    }.flowOn(coroutineDispatchers.io)

    override suspend fun changeDate(
        userMail: String,
        day: DayData,
        shouldDelete: Boolean
    ): Boolean {
//        return firestoreRepository
//            .changeItem(
//                collection = userMail,
//                document = Documents.Dates,
//                data = day,
//                delete = shouldDelete
//            )
        return true // TODO
    }

//    private fun getResult(snapshot: DocumentSnapshot): List<DayData> {
//        return snapshot.data?.values?.map {
//            @Suppress("UNCHECKED_CAST")
//            (it as Map<String, Any>).toDataClass()
//        } ?: emptyList()
//    }
}