package com.buyehou.wanandroid.api

import com.buyehou.wanandroid.model.Result
import com.buyehou.wanandroid.model.UserInfoBean
import retrofit2.Call
import retrofit2.http.GET

/**
 * @author Rosen
 * @date 2023/10/8 17:22
 */
interface IService {

    @GET("/user/lg/userinfo/json")
    fun userinfo(): Call<Result<UserInfoBean>>

}