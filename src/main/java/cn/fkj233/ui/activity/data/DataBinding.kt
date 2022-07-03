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

package cn.fkj233.ui.activity.data

import android.view.View

object DataBinding {
    fun get(bindingData: ArrayList<BindingData>, defValue: () -> Any, recvCallbacks: (View, Int, Any) -> Unit): BindingData {
        val binding = Binding(defValue, recvCallbacks)
        return BindingData(binding, binding.getSend(), recvCallbacks, {
                i -> binding.getRecv(i)
        }, { data ->
            binding.getSend().send(data)
        }).also { bindingData.add(it) }
    }

    data class BindingData(
        val binding: Binding,
        val bindingSend: Binding.Send,
        val recvCallbacks: (View, Int, Any) -> Unit,
        val getRecv: (Int) -> Binding.Recv,
        val send: (Any) -> Unit
    )

    class Binding(private val defValue: () -> Any, val recvCallbacks: (View, Int, Any) -> Unit) {
        var data: ArrayList<Recv> = arrayListOf()

        inner class Send {
            fun send(any: Any) {
                for (recv in data) {
                    recv.recv(any)
                }
            }
        }

        fun add(recv: Recv) {
            data.add(recv)
        }

        fun getSend(): Send {
            return Send()
        }

        /**
         * Get data reception / 获取数据接收
         */
        fun getRecv(flags: Int): Recv {
            return Recv(flags, defValue).also { add(it) }
        }

        /**
         * Please do not use it directly. / 请不要直接使用.
         * <p>
         * Please use {@link cn.fkj233.ui.activity.data.DataBinding.Binding#getRecv(int)}
         */
        inner class Recv(private val flags: Int, private val defValue: () -> Any) {
            private lateinit var mView: View

            fun setView(view: View) {
                mView = view
                recv(defValue())
            }

            fun recv(any: Any) {
                recvCallbacks(mView, flags, any)
            }
        }
    }
}