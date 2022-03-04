package cn.fkj233.ui.activity.view

import android.content.Context
import android.view.View
import cn.fkj233.ui.activity.data.DataBinding

class CustomViewV(val view: View, private val dataBindingRecv: DataBinding.Binding.Recv? = null): BaseView() {

    override fun getType(): BaseView = this

    override fun create(context: Context, callBacks: (() -> Unit)?): View {
        dataBindingRecv?.setView(view)
        return view
    }
}