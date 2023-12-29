package com.buyehou.wanandroid.data

import com.buyehou.wanandroid.model.HotKey

/**
 * @author Rosen
 */
interface Repository {
    suspend fun getHotKey(): Result<List<HotKey>>
}