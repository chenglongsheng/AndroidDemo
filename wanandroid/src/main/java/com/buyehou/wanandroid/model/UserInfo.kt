package com.buyehou.wanandroid.model

/**
 * @author Rosen
 * @date 2023/10/8 17:26
 */
data class UserInfo(
    val admin: Boolean,
    val chapterTops: List<String>,
    val coinCount: Int,
    val collectIds: List<String>
)
