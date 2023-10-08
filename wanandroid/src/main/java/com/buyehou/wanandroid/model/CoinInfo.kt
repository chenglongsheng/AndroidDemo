package com.buyehou.wanandroid.model

data class CoinInfo(
    val coinCount: Int,
    val level: Int,
    val nickname: String? = null,
    val rank: String? = null,
    val userId: Long,
    val username: String? = null
)