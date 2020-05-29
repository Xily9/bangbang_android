package com.autowheel.bangbang.model.network.service

import com.autowheel.bangbang.model.network.bean.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

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

    @POST("/coach/edit")
    @FormUrlEncoded
    suspend fun editCoach(@Field("help_id") helpId: Int, @Field("declaration") declaration: String): GeneralResponseBean<Any>

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

    @POST("/coach/agree")
    @FormUrlEncoded
    suspend fun agreeCoach(@Field("help_id") helpId: Int, @Field("user_id") userId: Int): GeneralResponseBean<Any>

    @POST("/coach/cancel")
    @FormUrlEncoded
    suspend fun cancelCoach(@Field("help_id") helpId: Int): GeneralResponseBean<Any>

    @POST("/coach/reserve/{id}")
    suspend fun bookCoachAgain(@Path("id") id: Int): GeneralResponseBean<Any>

    @POST("/coach/remove")
    @FormUrlEncoded
    suspend fun removeCoach(@Field("help_id") helpId: Int): GeneralResponseBean<Any>

    @POST("/coach/comment")
    @FormUrlEncoded
    suspend fun commentCoach(@Field("help_id") helpId: Int, @Field("star") star: Float, @Field("text") text: String): GeneralResponseBean<Any>

    @GET("/coach/mycomment")
    suspend fun getCommentHistory(): GeneralResponseBean<List<CoachCommentBean>>

    @POST("/coach/pay")
    @FormUrlEncoded
    suspend fun payCoach(@Field("order_id") orderId: Int): GeneralResponseBean<Any>

    @POST("/note")
    @FormUrlEncoded
    suspend fun publishNote(@Field("title") title: String, @Field("tag") tag: String, @Field("content") content: String): GeneralResponseBean<NotePublishBean>

    @GET("/note/published")
    suspend fun getReleasedNote(): GeneralResponseBean<List<NoteBean>>

    @GET("/note/search")
    suspend fun searchNote(@Query("word") word: String): GeneralResponseBean<List<NoteBean>>

    @GET("/note/{id}")
    suspend fun getNoteDetail(@Path("id") id: Int): GeneralResponseBean<NoteBean>

    @Multipart
    @POST("/note/upload")
    suspend fun uploadNoteFile(@Part file: MultipartBody.Part): GeneralResponseBean<NoteUploadFileBean>

    @GET("/note/categories")
    suspend fun getNoteTags(): GeneralResponseBean<List<String>>

    @GET("/note/index")
    suspend fun getNotes(@Query("tag") tag: String, @Query("page") page: Int, @Query("each_page") eachPage: Int): NoteIndexResponseBean

    @GET("/coach/index")
    suspend fun getCoaches(@Query("page") page: Int, @Query("each_page") eachPage: Int): CoachIndexResponseBean

    @POST("/note/edit")
    @FormUrlEncoded
    suspend fun editNote(@Field("note_id") noteId: Int, @Field("content") content: String): GeneralResponseBean<Any>

    @GET("/coach/showcomments")
    suspend fun getCoachComments(@Query("help_id") helpId: Int): CoachCommentResponseBean

    @GET("/user/star/{uid}")
    suspend fun getUserStar(@Path("uid") uid: Int): GeneralResponseBean<UserStarBean>

    @POST("/note/compliments")
    @FormUrlEncoded
    suspend fun complimentNote(@Field("note_id") noteId: Int): GeneralResponseBean<Any>

    @POST("/note/recomp")
    @FormUrlEncoded
    suspend fun reComplimentNote(@Field("note_id") noteId: Int): GeneralResponseBean<Any>

    @GET("/assist")
    suspend fun getHelpList(): GeneralResponseBean<List<HelpBean>>

    @POST("/assist/apply")
    @FormUrlEncoded
    suspend fun applyHelp(
        @Field("user_id") userId: Int,
        @Field("course") course: String,
        @Field("grade") grade: String,
        @Field("course_token") courseToken: String,
        @Field("note") note: String
    ): GeneralResponseBean<Any>

    @Multipart
    @POST("/assist/pickup")
    suspend fun pickUpHelp(
        @Part("couple_id") id: RequestBody,
        @Part file: MultipartBody.Part
    ): GeneralResponseBean<NoteUploadFileBean>

    @GET("/assist/myassist")
    suspend fun getUserHelp(): GeneralResponseBean<List<UserHelpBean>>

    @GET("/msg/msglist")
    suspend fun getMsgList(): GeneralResponseBean<List<MessageListBean>>

    @GET("/msg/history")
    suspend fun getHistoryMsg(@Query("user_id") uid: Int): GeneralResponseBean<List<MessageBean>>


}