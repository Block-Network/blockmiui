package cn.fkj233.ui.activity.view

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import cn.fkj233.miui.R
import cn.fkj233.ui.activity.MIUIActivity
import cn.fkj233.ui.activity.data.DataBinding
import cn.fkj233.ui.activity.dp2px
import cn.fkj233.ui.activity.fragment.MIUIFragment
import cn.fkj233.ui.activity.isRtl

class RadioViewV(private val key: String, val dataBindingRecv: DataBinding.Binding.Recv? = null, val data: RadioData.() -> Unit) : BaseView {
    data class RadioDataValue(val value: String, val name: String, val dataBindingRecv: DataBinding.Binding.Recv?, val dataBindingSend: DataBinding.Binding.Send?, val callBacks: ((View, Any) -> Unit)?)

    class RadioData {
        val data: ArrayList<RadioDataValue> = arrayListOf()

        fun add(value: String, name: String, dataBindingRecv: DataBinding.Binding.Recv? = null, dataBindingSend: DataBinding.Binding.Send? = null, callBacks: ((View, Any) -> Unit)? = null) {
            data.add(RadioDataValue(value, name, dataBindingRecv, dataBindingSend, callBacks))
        }
    }

    override fun getType(): BaseView = this

    override fun create(context: Context, callBacks: (() -> Unit)?): View {
        val data = RadioData().apply(data)
        val viewId: HashMap<String, Int> = hashMapOf()
        return RadioGroup(context).apply {
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            orientation = LinearLayout.VERTICAL
            for (radioData in data.data) {
                addView(RadioButton(context).apply {
                    id = View.generateViewId()
                    viewId[radioData.value] = id
                    layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
                    text = radioData.name
                    setTextColor(context.getColor(R.color.whiteText))
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        paint.typeface = Typeface.create(null, 500, false)
                    } else {
                        paint.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                    }
                    if (isRtl(context))
                        setPadding(dp2px(context, 35f), dp2px(context, 20f), dp2px(context, 30f), dp2px(context, 20f))
                    else
                        setPadding(dp2px(context, 30f), dp2px(context, 20f), dp2px(context, 35f), dp2px(context, 20f))
                    buttonDrawable = null
                    background = context.getDrawable(R.drawable.ic_click_check)
                    val drawable = context.getDrawable(android.R.drawable.btn_radio).also { it?.setBounds(0,0,60,60) }
                    if (isRtl(context))
                        setCompoundDrawables(drawable, null, null, null)
                    else
                        setCompoundDrawables(null, null, drawable, null)
                    setOnClickListener { view ->
                        radioData.callBacks?.let { it(view, radioData.value) }
                        radioData.dataBindingSend?.send(radioData.value)
                        MIUIActivity.safeSP.putAny(key, radioData.value)
                    }
                    radioData.dataBindingRecv?.setView(this)
                })
            }
            viewId[MIUIActivity.safeSP.getString(key, "")]?.let { check(it) }
            dataBindingRecv?.setView(this)
        }
    }

    override fun onDraw(thiz: MIUIFragment, group: LinearLayout, view: View) {
        thiz.apply {
            group.apply {
                setPadding(0, 0, 0, 0)
                addView(view)
            }
        }
    }
}