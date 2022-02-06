package cn.fkj233.blockmiui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Switch
import android.widget.Toast
import cn.fkj233.ui.activity.MIUIActivity
import cn.fkj233.ui.activity.data.MIUIPopupData
import cn.fkj233.ui.activity.view.*
import cn.fkj233.ui.dialog.MIUIDialog

class MainActivity : MIUIActivity() {
    private val handler by lazy { Handler(Looper.getMainLooper()) }

    private val activity = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSP(getPreferences(0))
    }

    override fun mainItems(): ArrayList<BaseView> {
        return arrayListOf<BaseView>().apply {
            add(TextSummaryV("showTest", onClickListener = {
                showFragment("test")
            }))
            add(TextSummaryV("showDialog", onClickListener = {
                MIUIDialog(activity).apply {
                    setTitle("Test")
                    setMessage("TestMessage")
                    setLButton("Cancel") {
                        dismiss()
                    }
                    setRButton("OK") {
                        dismiss()
                    }
                    show()
                }
            }))
            add(TextSummaryV("test", tips = "summary"))
            add(AuthorV(getDrawable(R.mipmap.ic_launcher)!!, "Test", "Test123"))
            add(TextWithSwitchV(TextV("test"), SwitchV("test")))
            add(TextWithSpinnerV(TextV("Spinner"), SpinnerV(arrayListOf<MIUIPopupData>().apply {
                add(MIUIPopupData("test") { showToast("select test") })
                add(MIUIPopupData("test1") { showToast("select test1") })
                add(MIUIPopupData("test2") { showToast("select test2") })
                add(MIUIPopupData("test3") { showToast("select test3") })
            }, "test")))
            add(LineV())
            add(TitleTextV("Title"))
            add(TextSummaryV("test", tips = "summary"))
            add(TextV("SeekbarWithText"))
            add(SeekBarWithTextV("seekbar", 0, 100, 0))
            // TODO: Bug not fix
//            add(TextV("Seekbar"))
//            add(SeekBarV("seekbar1", 0, 100, defaultProgress = 0))
            add(LineV())
            add(TitleTextV("DataBinding"))
            val binding = getDataBinding(false) { view, flags, data ->
                when (flags) {
                    1 -> (view as Switch).isEnabled = data as Boolean
                    2 -> view.visibility = if (data as Boolean) View.VISIBLE else View.GONE
                }
            }
            add(TextWithSwitchV(TextV("data-binding"), SwitchV("binding", dataBindingSend = binding.bindingSend)))
            add(TextWithSwitchV(TextV("test123"), SwitchV("test123", dataBindingRecv = binding.binding.getRecv(1))))
            add(TextSummaryV("test", dataBindingRecv = binding.binding.getRecv(2)))
        }
    }

    override fun mainName(): String {
        return "Home"
    }

    override fun menuItems(): ArrayList<BaseView> {
        return arrayListOf<BaseView>().apply {
            add(TextV("ThisMenu"))
        }
    }

    override fun menuName(): String {
        return "Menu"
    }

    override fun getItems(item: String): ArrayList<BaseView> {
        return when (item) {
            "test" -> arrayListOf<BaseView>().apply {
                add(TextV("thisTest"))
            }

            /** 必须写这两个 不然会出错 */
            menuName() -> menuItems()
            else -> mainItems()
        }
    }

    fun showToast(string: String) {
        handler.post {
            Toast.makeText(this, string, Toast.LENGTH_LONG).show()
        }
    }
}