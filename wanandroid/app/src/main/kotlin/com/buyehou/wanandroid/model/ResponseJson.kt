package com.buyehou.wanandroid.model

import retrofit2.Response
import java.io.IOException

/**
 * @author Rosen
 */
data class ResponseJson<T>(
    val data: T?,
    val errorCode: Int,
    val errorMsg: String
) {
    companion object {
        fun <T> ServerResponse<T>.toResult(): Result<T> {
            return if (isSuccessful) {
                val data = body()?.data
                if (data != null && body()?.errorCode == 0) {
                    Result.success(data)
                } else {
                    Result.failure(IOException(body()!!.errorMsg))
                }
            } else {
                Result.failure(IOException("响应异常"))
            }
        }
    }
}

typealias ServerResponse<T> = Response<ResponseJson<T>>

