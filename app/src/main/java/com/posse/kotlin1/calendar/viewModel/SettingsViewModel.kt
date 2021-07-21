package com.posse.kotlin1.calendar.viewModel

import androidx.annotation.StyleRes
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.posse.kotlin1.calendar.app.App
import com.posse.kotlin1.calendar.model.repository.SettingsRepo
import com.posse.kotlin1.calendar.model.repository.SettingsRepoImpl
import com.posse.kotlin1.calendar.utils.THEME
import com.posse.kotlin1.calendar.utils.lightTheme
import com.posse.kotlin1.calendar.utils.themeSwitch

class SettingsViewModel : ViewModel() {
    private val liveData: MutableLiveData<SettingsState> = MutableLiveData()
    private val settingsRepo: SettingsRepo = SettingsRepoImpl()
    private val lastTheme: MutableLiveData<Int> = MutableLiveData(
        if (App.sharedPreferences?.lightTheme == true) {
            THEME.DAY.themeID
        } else {
            THEME.NIGHT.themeID
        }
    )

    var switchState: Boolean
        get() = App.sharedPreferences?.themeSwitch ?: true
        set(value) {
            App.sharedPreferences?.themeSwitch = value
        }

    var lightTheme: Boolean
        get() = App.sharedPreferences?.lightTheme ?: true
        set(value) {
            App.sharedPreferences?.lightTheme = value
            switchTheme()
        }

    fun getLiveData() = liveData

    fun getLastTheme() = lastTheme

    fun getSettingsState() {
        liveData.value = settingsRepo.getSettingsState()
    }

    private fun switchTheme() {
        if (App.sharedPreferences?.lightTheme == true) changeTheme(THEME.DAY.themeID)
        else changeTheme(THEME.NIGHT.themeID)
    }

    private fun changeTheme(@StyleRes theme: Int) {
        if (theme != lastTheme.value) {
            lastTheme.value = theme
        }
    }
}