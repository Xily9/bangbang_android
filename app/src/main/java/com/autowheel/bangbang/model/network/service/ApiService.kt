package com.autowheel.bangbang.model.network.service

import com.autowheel.bangbang.model.network.bean.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by Xily on 2020/3/25.
 */
interface ApiService {
    @POST("/auth/login")
    @FormUrlEncoded
    fun login(@Field("username") username: String, @Field("password") password: String): Call<GeneralResponseBean<Any>>

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
    suspend fun forgetPasswordSend(@Field("email") email: String): GeneralResponseBean<Any>

    @POST("/auth/forget_password")
    @FormUrlEncoded
    suspend fun forgetPassword(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("verify_code") verifyCode: String
    ): GeneralResponseBean<Any>

    @POST("/user/change_password")
    @FormUrlEncoded
    suspend fun changePassword(
        @Field("old_password") oldPassword: String,
        @Field("new_password") newPassword: String
    ): GeneralResponseBean<Any>

    @GET("/user/profile")
    fun getProfile(): Call<GeneralResponseBean<ProfileBean>>

    @GET("/user/profile/{uid}")
    suspend fun getProfile(@Path("uid") uid: Int): GeneralResponseBean<ProfileBean>

    @POST("/user/profile")
    @FormUrlEncoded
    fun editProfile(
        @Field("nickname") nickname: String,
        @Field("signature") signature: String
    ): Call<GeneralResponseBean<Any>>

    @Multipart
    @POST("/user/avatar")
    suspend fun uploadAvatar(@Part avatar: MultipartBody.Part): GeneralResponseBean<Any>

    @GET("/user/grade")
    suspend fun getGrade(): GeneralResponseBean<List<GradeBean>>

    @GET("/note/published")
    suspend fun getUserNote(): GeneralResponseBean<List<UserNoteBean>>

    @POST("/coach")
    @FormUrlEncoded
    suspend fun publishCoach(
        @Field("type") type: String,
        @Field("course") course: String,
        @Field("grade") grade: String,
        @Field("course_token") courseToken: String,
        @Field("skill_name") skillName: String,
        @Field("file_id") fileId: String,
        @Field("declaration") declaration: String,
        @Field("price") price: String
    ): GeneralResponseBean<CoachPublishBean>

    @GET("/coach/{id}")
    suspend fun getCoachDetail(@Path("id") id: Int): GeneralResponseBean<CoachBean>

    @GET("/coach/search")
    suspend fun searchCoach(@Query("word") word: String): GeneralResponseBean<List<CoachBean>>

    @POST("/coach/{id}")
    suspend fun bookCoach(@Path("id") id: Int): GeneralResponseBean<Any>

    @GET("/coach/booklist")
    suspend fun getCoachBookList(): GeneralResponseBean<List<CoachBookListBean>>

    @GET("/coach/record")
    suspend fun getCoachHistory(): GeneralResponseBean<List<CoachBean>>

    @GET("/coach/released")
    suspend fun getReleasedCoach(): GeneralResponseBean<List<CoachBean>>

}