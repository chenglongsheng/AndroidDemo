package com.buyehou.wanandroid.api

import com.buyehou.wanandroid.model.HotKey
import com.buyehou.wanandroid.model.ServerResponse
import retrofit2.http.GET

/**
 * @author Rosen
 */
interface Service {

    @GET("/hotkey/json")
    suspend fun getHotKey(): ServerResponse<List<HotKey>>

}