@file:Suppress("FunctionName")

package cn.fkj233.ui.activity.data

import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.widget.TextView
import cn.fkj233.ui.activity.view.*

class InitView(private val datalist: HashMap<String, ItemData>) {
    var isMenu = false

    inner class ItemData(val title: String) {

        val itemList: ArrayList<BaseView> = arrayListOf()

        fun Author(authorHead: Drawable, authorName: String, authorTips: String? = null, round: Float = 30f, onClick: (() -> Unit)? = null, dataBindingRecv: DataBinding.Binding.Recv? = null) {
            itemList.add(AuthorV(authorHead, authorName, authorTips, round, onClick, dataBindingRecv))
        }

        fun Line() {
            itemList.add(LineV())
        }

        fun SeekBar(key: String, min: Int, max: Int, divide: Int = 1, defaultProgress: Int, dataSend: DataBinding.Binding.Send? = null, dataBindingRecv: DataBinding.Binding.Recv? = null, callBacks: ((Int, TextView) -> Unit)? = null) {
            itemList.add(SeekBarV(key, min, max, divide, defaultProgress, dataSend, dataBindingRecv, callBacks))
        }

        fun TextSummaryWithSpinner(textV: TextSummaryV, spinnerV: SpinnerV, dataBindingRecv: DataBinding.Binding.Recv? = null) {
            itemList.add(TextSummaryWithSpinnerV(textV, spinnerV, dataBindingRecv))
        }

        fun Text(text: String? = null, resId: Int? = null, textSize: Float? = null, colorInt: Int? = null, colorId: Int? = null, padding: Padding? = null, dataBindingRecv: DataBinding.Binding.Recv? = null, typeface: Typeface? = null, onClickListener: (() -> Unit)? = null) {
            itemList.add(TextV(text, resId, textSize, colorInt, colorId, padding, dataBindingRecv, typeface, onClickListener))
        }

        fun SeekBarWithText(key: String = "", min: Int, max: Int, defaultProgress: Int = 0, dataBindingRecv: DataBinding.Binding.Recv? = null, dataBindingSend: DataBinding.Binding.Send? = null, callBacks: ((Int, TextView) -> Unit)? = null) {
            itemList.add(SeekBarWithTextV(key, min, max, defaultProgress, dataBindingRecv, dataBindingSend, callBacks))
        }

        fun TextSummaryArrow(textSummaryV: TextSummaryV, dataBindingRecv: DataBinding.Binding.Recv? = null) {
            itemList.add(TextSummaryArrowV(textSummaryV, dataBindingRecv))
        }

        fun TextSummaryWithSwitch(textSummaryV: TextSummaryV, switchV: SwitchV, dataBindingRecv: DataBinding.Binding.Recv? = null) {
            itemList.add(TextSummaryWithSwitchV(textSummaryV, switchV, dataBindingRecv))
        }

        fun TitleText(text: String? = null, resId: Int? = null, dataBindingRecv: DataBinding.Binding.Recv? = null, onClickListener: (() -> Unit)? = null) {
            itemList.add(TitleTextV(text, resId, dataBindingRecv, onClickListener))
        }

        fun TextWithSwitch(textV: TextV, switchV: SwitchV, dataBindingRecv: DataBinding.Binding.Recv? = null) {
            itemList.add(TextWithSwitchV(textV, switchV, dataBindingRecv))
        }

        fun TextWithSpinner(textV: TextV, spinnerV: SpinnerV, dataBindingRecv: DataBinding.Binding.Recv? = null) {
            itemList.add(TextWithSpinnerV(textV, spinnerV, dataBindingRecv))
        }


    }

    fun registerMain(title: String, itemData: ItemData.() -> Unit) {
        datalist["Main"] = ItemData(title).apply(itemData)
    }

    fun registerMenu(title: String, itemData: ItemData.() -> Unit) {
        datalist["Menu"] = ItemData(title).apply(itemData)
        isMenu = true
    }

    fun register(key: String, title: String, itemData: ItemData.() -> Unit){
        datalist[key] = ItemData(title).apply(itemData)
    }
}