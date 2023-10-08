package com.buyehou.wanandroid.model

/**
 * @author Rosen
 * @date 2023/10/8 17:38
 */
data class Result<T>(val data: T?, val errorCode: Int, val errorMsg: String)
