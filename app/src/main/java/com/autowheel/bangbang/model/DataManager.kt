package com.autowheel.bangbang.model

import com.autowheel.bangbang.model.db.IDB
import com.autowheel.bangbang.model.db.LitePalImpl
import com.autowheel.bangbang.model.pref.setting.ISettingPref
import com.autowheel.bangbang.model.pref.setting.SettingsPrefImpl
import com.autowheel.bangbang.model.pref.user.IUserPref
import com.autowheel.bangbang.model.pref.user.UserPrefImpl

/**
 * Created by Xily on 2020/3/5.
 */
object DataManager : ISettingPref by SettingsPrefImpl, IUserPref by UserPrefImpl, IDB by LitePalImpl