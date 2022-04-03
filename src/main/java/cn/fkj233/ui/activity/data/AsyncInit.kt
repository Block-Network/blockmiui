package cn.fkj233.ui.activity.data

import cn.fkj233.ui.activity.fragment.MIUIFragment

interface AsyncInit {
    /**
     *  Skip auto load view / 跳过自动加载控件
     */
    val skipLoadItem: Boolean

    /**
     *  Asynchronous operation start / 异步操作开始
     *  Operation UI, please use { fragment.handler.post }
     */
    fun onInit(fragment: MIUIFragment)
}