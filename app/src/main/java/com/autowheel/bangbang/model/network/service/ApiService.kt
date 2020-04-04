package com.autowheel.bangbang.model.network.service

import com.autowheel.bangbang.model.network.bean.GeneralResponseBean
import com.autowheel.bangbang.model.network.bean.LoginBean
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * Created by Xily on 2020/3/25.
 */
interface ApiService {
    @POST("/auth/login")
    @FormUrlEncoded
    fun login(@Field("username") username: String, @Field("password") password: String): Call<GeneralResponseBean<LoginBean>>

    @POST("/auth/register")
    @FormUrlEncoded
    suspend fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("email") email: String,
        @Field("nickname") nickname: String
    ): GeneralResponseBean<Any>

    @POST("/auth/verify")
    @FormUrlEncoded
    fun verify(@Field("number") username: String, @Field("password") password: String): Call<GeneralResponseBean<Any>>

    @POST("/auth/forget_password/send")
    @FormUrlEncoded
    fun forgetPasswordSend(@Field("email") email: String): Call<GeneralResponseBean<Any>>

    @POST("/auth/forget_password")
    @FormUrlEncoded
    fun forgetPassword(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("verify_code") verifyCode: String
    ): Call<GeneralResponseBean<Any>>
}