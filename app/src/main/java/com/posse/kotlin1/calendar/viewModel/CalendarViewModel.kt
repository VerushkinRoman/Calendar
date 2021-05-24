package com.posse.kotlin1.calendar.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.posse.kotlin1.calendar.model.Repository
import com.posse.kotlin1.calendar.model.RepositoryImpl
import java.time.LocalDate
import java.time.Year
import java.time.temporal.ChronoUnit

class CalendarViewModel(
    private val liveDataToObserve: MutableLiveData<Set<LocalDate>> = MutableLiveData(),
    private val repository: Repository = RepositoryImpl()
) : ViewModel() {

    fun getLiveData() = liveDataToObserve

    fun refreshDrankState() = getDataFromLocalSource()

    fun dayClicked(date: LocalDate) {
        repository.changeState(date)
//        mLiveDataToObserve.value = mRepository.getDrankStateFromLocalStorage()
    }

    fun getDrankDaysQuantity() = repository.getDrinkDaysInThisYear()

    fun getThisYearDaysQuantity() = repository.getThisYearDaysQuantity()

    private fun getDataFromLocalSource() {
        repository.init()
        liveDataToObserve.value = repository.getDrankStateFromLocalStorage()
    }
}