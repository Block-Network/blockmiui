/*
 * BlockMIUI
 * Copyright (C) 2022 fkj@fkj233.cn
 * https://github.com/577fkj/BlockMIUI
 *
 * This software is free opensource software: you can redistribute it
 * and/or modify it under the terms of the GNU Lesser General Public License v2.1
 * as published by the Free Software Foundation; either
 * version 3 of the License, or any later version and our eula as published
 * by 577fkj.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * GNU Lesser General Public License v2.1 for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License v2.1
 * and eula along with this software.  If not, see
 * <https://www.gnu.org/licenses/>
 * <https://github.com/577fkj/BlockMIUI/blob/main/LICENSE>.
 */

package cn.fkj233.ui.activity.view


import android.content.Context
import android.view.View
import cn.fkj233.ui.activity.MIUIActivity
import cn.fkj233.ui.activity.data.DataBinding
import cn.fkj233.ui.switch.MIUISwitch

class SwitchV(private val key: String, private val defValue: Boolean = false, private val dataBindingRecv: DataBinding.Binding.Recv? = null, private val dataBindingSend: DataBinding.Binding.Send? = null, private val onClickListener: ((Boolean) -> Unit)? = null): BaseView {

    lateinit var switch: MIUISwitch

    override fun getType(): BaseView = this

    override fun create(context: Context, callBacks: (() -> Unit)?): View {
        return MIUISwitch(context).also {
            switch = it
            dataBindingRecv?.setView(it)
            if (!MIUIActivity.safeSP.containsKey(key)) {
                MIUIActivity.safeSP.putAny(key, defValue)
            }
            it.isChecked = MIUIActivity.safeSP.getBoolean(key, defValue)
            it.setOnCheckedChangeListener { _, b ->
                dataBindingSend?.let { send ->
                    send.send(b)
                }
                callBacks?.let { it1 -> it1() }
                onClickListener?.let { it(b) }
                MIUIActivity.safeSP.putAny(key, b)
            }
        }
    }

    fun click() {
        switch.isChecked = !switch.isChecked
        dataBindingSend?.let { send ->
            send.send(switch.isChecked)
        }
        onClickListener?.let { it(switch.isChecked) }
        MIUIActivity.safeSP.putAny(key, switch.isChecked)
    }
}