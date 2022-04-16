package cn.fkj233.ui.activity.data

import android.content.SharedPreferences

class SafeSharedPreferences {
    var mSP: SharedPreferences? = null

    fun containsKey(key: String, defValue: Boolean = false): Boolean {
        return if (mSP == null) {
            defValue
        } else {
            mSP!!.all.containsKey(key)
        }
    }

    fun putAny(key: String, any: Any) {
        if (mSP == null) return
        val edit = mSP!!.edit()
        when (any) {
            is Boolean -> edit.putBoolean(key, any)
            is String -> edit.putString(key, any)
            is Int -> edit.putInt(key, any)
            is Float -> edit.putFloat(key, any)
            is Long -> edit.putLong(key, any)
        }
        edit.apply()
    }

    fun getBoolean(key: String, defValue: Boolean): Boolean {
        return if (mSP == null) {
            defValue
        } else {
            mSP!!.getBoolean(key, defValue)
        }
    }

    fun getInt(key: String, defValue: Int): Int {
        return if (mSP == null) {
            defValue
        } else {
            mSP!!.getInt(key, defValue)
        }
    }
}