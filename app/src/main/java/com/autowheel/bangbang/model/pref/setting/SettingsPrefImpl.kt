package com.autowheel.bangbang.model.pref.setting

object SettingsPrefImpl : ISettingPref {
    override var notificationChannel: Boolean by SettingPrefHelper(false)
}