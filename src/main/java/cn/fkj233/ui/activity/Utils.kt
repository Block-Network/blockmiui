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

@file:Suppress("DEPRECATION")

package cn.fkj233.ui.activity

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.util.TypedValue
import android.view.Display
import android.view.View
import android.view.WindowManager
import dalvik.system.BaseDexClassLoader
import dalvik.system.DexFile
import java.util.*
import kotlin.collections.HashMap

fun dp2px(context: Context, dpValue: Float): Int =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.resources.displayMetrics).toInt()

fun getDisplay(context: Context): Display {
    return (context.applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
}

fun isRtl(context: Context): Boolean {
    return TextUtils.getLayoutDirectionFromLocale(context.resources.configuration.locale) == View.LAYOUT_DIRECTION_RTL
}

fun Any.getObjectField(field: String): Any? {
    runCatching {
        return this.javaClass.getDeclaredField(field).get(this)
    }
    return null
}

@Suppress("UNCHECKED_CAST")
fun <T> Any.getObjectFieldAs(field: String): T? {
    runCatching {
        return this.javaClass.getDeclaredField(field).get(this) as T
    }
    return null
}

@Suppress("UNCHECKED_CAST")
fun <T> Any.callMethodAs(methodName: String, vararg args: Any?): T? {
    runCatching {
        return this.javaClass.getDeclaredMethod(methodName).invoke(this, *args) as T
    }
    return null
}

fun ClassLoader.allClassesList(): List<String> {
    var classLoader = this
    while (classLoader !is BaseDexClassLoader) {
        if (classLoader.parent != null) classLoader = classLoader.parent
        else return emptyList()
    }
    return classLoader.getObjectField("pathList")
        ?.getObjectFieldAs<Array<Any>>("dexElements")
        ?.flatMap {
            it.getObjectField("dexFile")?.callMethodAs<Enumeration<String>>("entries")?.toList()
                .orEmpty()
        }.orEmpty()
}

val classCache: HashMap<Class<out Annotation>, List<Class<*>>> = HashMap()

@Suppress("UNCHECKED_CAST")
fun <T> findAnnotation(annotations: Class<out Annotation>, context: Context): List<Class<T>> {
    if (classCache.containsKey(annotations)) {
        return classCache[annotations] as List<Class<T>>
    }
    val result = arrayListOf<Class<T>>()
    context.javaClass.classLoader?.allClassesList()?.forEach {
        try {
            val clazz = Class.forName(it, false, context.javaClass.classLoader)
            if (clazz.isAnnotationPresent(annotations)) {
                result.add(Class.forName(it) as Class<T>)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    if (result.size == 0) {
        DexFile(context.packageCodePath).entries().asSequence().forEach {
            try {
                val clazz = Class.forName(it, false, context.javaClass.classLoader)
                if (clazz.isAnnotationPresent(annotations)) {
                    result.add(clazz as Class<T>)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    classCache[annotations] = result
    return result
}