package cn.fkj233.ui.activity.data

import cn.fkj233.ui.activity.fragment.MIUIFragment

interface AsyncInit {
    val skipLoadItem: Boolean

    fun onInit(fragment: MIUIFragment)
}