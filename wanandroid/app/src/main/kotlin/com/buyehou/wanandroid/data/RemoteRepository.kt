package com.buyehou.wanandroid.data

import com.buyehou.wanandroid.api.Service
import com.buyehou.wanandroid.model.HotKey
import com.buyehou.wanandroid.model.ResponseJson.Companion.toResult

/**
 * @author Rosen
 */
class RemoteRepository(private val service: Service) : Repository {
    override suspend fun getHotKey(): Result<List<HotKey>> {
        return service.getHotKey().toResult()
    }
}