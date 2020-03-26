package com.autowheel.bangbang.model.network.service

import com.autowheel.bangbang.model.network.bean.request.LoginRequestBean
import com.autowheel.bangbang.model.network.bean.request.RegisterRequestBean
import com.autowheel.bangbang.model.network.bean.response.GeneralResponseBean
import com.autowheel.bangbang.model.network.bean.response.LoginResponseBean
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by Xily on 2020/3/25.
 */
interface ApiService {
    @POST("/auth/login")
    fun login(@Body loginRequestBean: LoginRequestBean): Call<GeneralResponseBean<LoginResponseBean>>

    @POST("/auth/register")
    fun register(@Body registerRequestBean: RegisterRequestBean): Call<GeneralResponseBean<Nothing>>
}