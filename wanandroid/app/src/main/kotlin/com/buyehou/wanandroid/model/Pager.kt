package com.buyehou.wanandroid.model

/**
 * @author Rosen
 */
data class Pager<T>(
    val curPage: Int,
    val datas: List<T>?,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int
)