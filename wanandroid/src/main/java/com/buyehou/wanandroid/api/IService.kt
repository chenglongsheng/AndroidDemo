package com.buyehou.wanandroid.api

import com.buyehou.wanandroid.model.Result
import com.buyehou.wanandroid.model.UserInfoBean
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * @author Rosen
 * @date 2023/10/8 17:22
 */
interface IService {

    @POST("/user/register")
    fun register(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("repassword") repassword: String
    )

    @POST("/user/login")
    fun login(@Query("username") username: String, @Query("password") password: String)

    @POST("/user/logout/json")
    fun logout()

    @GET("/user/lg/userinfo/json")
    fun userinfo(): Call<Result<UserInfoBean>>

}