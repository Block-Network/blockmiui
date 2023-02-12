package cn.fkj233.ui.activity.data

import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.TextView
import cn.fkj233.ui.activity.MIUIActivity
import cn.fkj233.ui.activity.fragment.MIUIFragment
import cn.fkj233.ui.activity.view.*

abstract class BasePage {
    val itemList: ArrayList<BaseView> = arrayListOf()
    var bindingData = arrayListOf<DataBinding.BindingData>()
    var skipLoadItem: Boolean = false
    lateinit var activity: MIUIActivity

    abstract fun onCreate()

    open fun asyncInit(fragment: MIUIFragment) {}

    fun showFragment(key: String) {
        activity.showFragment(key)
    }

    fun setTitle(title: String) {
        activity.title = title
    }

    open fun getTitle(): String = ""

    fun getString(id: Int): String = activity.getString(id)
    fun getDrawable(id: Int): Drawable = activity.getDrawable(id)!!
    fun getColor(id: Int): Int = activity.getColor(id)


    fun GetDataBinding(defValue: () -> Any, recvCallbacks: (View, Int, Any) -> Unit): DataBinding.BindingData {
        return DataBinding.get(bindingData, defValue, recvCallbacks)
    }

    fun Author(authorHead: Drawable, authorName: String, authorTips: String? = null, round: Float = 30f, onClickListener: (() -> Unit)? = null, dataBindingRecv: DataBinding.Binding.Recv? = null) {
        itemList.add(AuthorV(authorHead, authorName, authorTips, round, onClickListener, dataBindingRecv))
    }

    fun Line() {
        itemList.add(LineV())
    }

    fun SeekBar(key: String, min: Int, max: Int, defaultProgress: Int, dataSend: DataBinding.Binding.Send? = null, dataBindingRecv: DataBinding.Binding.Recv? = null, callBacks: ((Int, TextView) -> Unit)? = null) {
        itemList.add(SeekBarV(key, min, max, defaultProgress, dataSend, dataBindingRecv, callBacks))
    }

    fun SeekBarWithText(key: String = "", min: Int, max: Int, defaultProgress: Int = 0, dataBindingRecv: DataBinding.Binding.Recv? = null, dataBindingSend: DataBinding.Binding.Send? = null, callBacks: ((Int, TextView) -> Unit)? = null) {
        itemList.add(SeekBarWithTextV(key, min, max, defaultProgress, dataBindingRecv, dataBindingSend, callBacks))
    }

    fun Text(text: String? = null, textId: Int? = null, textSize: Float? = null, colorInt: Int? = null, colorId: Int? = null, padding: Padding? = null, dataBindingRecv: DataBinding.Binding.Recv? = null, typeface: Typeface? = null, onClickListener: (() -> Unit)? = null) {
        itemList.add(TextV(text, textId, textSize, colorInt, colorId, padding, dataBindingRecv, typeface, onClickListener))
    }

    fun TextWithSwitch(textV: TextV, switchV: SwitchV, dataBindingRecv: DataBinding.Binding.Recv? = null) {
        itemList.add(TextWithSwitchV(textV, switchV, dataBindingRecv))
    }

    fun TextSw(text: String? = null, textId: Int? = null, key: String, defValue: Boolean = false, onClickListener: ((Boolean) -> Unit)? = null, dataBindingRecv: DataBinding.Binding.Recv? = null) {
        itemList.add(TextWithSwitchV(TextV(text, textId), SwitchV(key, defValue, onClickListener = onClickListener), dataBindingRecv))
    }

    fun TextWithSpinner(textV: TextV, spinnerV: SpinnerV, dataBindingRecv: DataBinding.Binding.Recv? = null) {
        itemList.add(TextWithSpinnerV(textV, spinnerV, dataBindingRecv))
    }

    fun TextSp(text: String? = null, textId: Int? = null, currentValue: String, data: SpinnerV.SpinnerData.() -> Unit, dataBindingRecv: DataBinding.Binding.Recv? = null) {
        itemList.add(TextWithSpinnerV(TextV(text, textId), SpinnerV(currentValue, data = data), dataBindingRecv))
    }

    fun TextWithArrow(textV: TextV, dataBindingRecv: DataBinding.Binding.Recv? = null) {
        itemList.add(TextWithArrowV(textV, dataBindingRecv))
    }

    fun TextA(text: String? = null, textId: Int? = null, onClickListener: (() -> Unit)? = null, dataBindingRecv: DataBinding.Binding.Recv? = null) {
        itemList.add(TextWithArrowV(TextV(text, textId, onClickListener = onClickListener), dataBindingRecv))
    }

    fun TextWithSeekBar(textV: TextV, seekBarWithTextV: SeekBarWithTextV, dataBindingRecv: DataBinding.Binding.Recv? = null) {
        itemList.add(TextWithSeekBarV(textV, seekBarWithTextV, dataBindingRecv))
    }

    fun TextSummary(text: String? = null, textId: Int? = null, tips: String? = null, colorInt: Int? = null, colorId: Int? = null, tipsId: Int? = null, dataBindingRecv: DataBinding.Binding.Recv? = null, onClickListener: (() -> Unit)? = null) {
        itemList.add(TextSummaryV(text, textId, tips, colorInt, colorId, tipsId, dataBindingRecv, onClickListener))
    }

    fun TextS(text: String? = null, textId: Int? = null, tips: String? = null, colorInt: Int? = null, colorId: Int? = null, tipsId: Int? = null, dataBindingRecv: DataBinding.Binding.Recv? = null, onClickListener: (() -> Unit)? = null) {
        itemList.add(TextSummaryV(text, textId, tips, colorInt, colorId, tipsId, dataBindingRecv, onClickListener))
    }

    fun TextSummaryWithSwitch(textSummaryV: TextSummaryV, switchV: SwitchV, dataBindingRecv: DataBinding.Binding.Recv? = null) {
        itemList.add(TextSummaryWithSwitchV(textSummaryV, switchV, dataBindingRecv))
    }

    fun TextSSw(text: String? = null, textId: Int? = null, tips: String? = null, tipsId: Int? = null, key: String, defValue: Boolean = false, onClickListener: ((Boolean) -> Unit)? = null, dataBindingRecv: DataBinding.Binding.Recv? = null) {
        itemList.add(TextSummaryWithSwitchV(TextSummaryV(text, textId, tips, tipsId), SwitchV(key, defValue, onClickListener = onClickListener), dataBindingRecv))
    }

    fun TextSummaryWithSpinner(textSummaryV: TextSummaryV, spinnerV: SpinnerV, dataBindingRecv: DataBinding.Binding.Recv? = null) {
        itemList.add(TextSummaryWithSpinnerV(textSummaryV, spinnerV, dataBindingRecv))
    }

    fun TextSSp(text: String? = null, textId: Int? = null, tips: String? = null, tipsId: Int? = null, currentValue: String, data: SpinnerV.SpinnerData.() -> Unit, dataBindingRecv: DataBinding.Binding.Recv? = null) {
        itemList.add(TextSummaryWithSpinnerV(TextSummaryV(text, textId, tips, tipsId), SpinnerV(currentValue, data = data), dataBindingRecv))
    }

    fun TextSummaryWithArrow(textSummaryV: TextSummaryV, dataBindingRecv: DataBinding.Binding.Recv? = null) {
        itemList.add(TextSummaryWithArrowV(textSummaryV, dataBindingRecv))
    }

    fun TextSA(text: String? = null, textId: Int? = null, tips: String? = null, tipsId: Int? = null, onClickListener: (() -> Unit)? = null, dataBindingRecv: DataBinding.Binding.Recv? = null) {
        itemList.add(TextSummaryWithArrowV(TextSummaryV(text, textId, tips, tipsId, onClickListener = onClickListener), dataBindingRecv))
    }

    fun TextSummaryWithSeekBar(textSummaryV: TextSummaryV, seekBarWithTextV: SeekBarWithTextV, dataBindingRecv: DataBinding.Binding.Recv? = null) {
        itemList.add(TextSummaryWithSeekBarV(textSummaryV, seekBarWithTextV, dataBindingRecv))
    }

    fun TitleText(text: String? = null, textId: Int? = null,colorInt: Int? = null, colorId: Int? = null, dataBindingRecv: DataBinding.Binding.Recv? = null, onClickListener: (() -> Unit)? = null) {
        itemList.add(TitleTextV(text, textId,colorInt, colorId,dataBindingRecv, onClickListener))
    }

    fun CustomView(view: View, dataBindingRecv: DataBinding.Binding.Recv? = null) {
        itemList.add(CustomViewV(view, dataBindingRecv))
    }

    fun RadioView(key: String, dataBindingRecv: DataBinding.Binding.Recv? = null, data: RadioViewV.RadioData.() -> Unit) {
        itemList.add(RadioViewV(key, dataBindingRecv, data))
    }

}