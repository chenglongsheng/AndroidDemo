package com.loong.multiprocess.constant

/**
 * @author Rosen
 * @date 2023/8/22 18:25
 */
object MsgConst {

    object MAIN2SUB {
        const val YSN_DATA = 0

        /**
         * 用于同步liveData数据
         */
        const val YSN_LIVE_DATA = 1
    }

    object SUB2MAIN {
        const val YSN_DATA = 10

        /**
         * 自杀主进程
         */
        const val KILL_SELF = 11
    }

}