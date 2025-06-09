@file:Suppress("UNCHECKED_CAST", "USELESS_CAST", "INAPPLICABLE_JVM_NAME", "UNUSED_ANONYMOUS_PARAMETER", "NAME_SHADOWING", "UNNECESSARY_NOT_NULL_ASSERTION")
package uts.sdk.modules.uniMemoryInfo
import io.dcloud.uniapp.*
import io.dcloud.uniapp.extapi.*
import io.dcloud.uniapp.framework.*
import io.dcloud.uniapp.runtime.*
import io.dcloud.uniapp.vue.*
import io.dcloud.uniapp.vue.shared.*
import io.dcloud.unicloud.*
import io.dcloud.uts.*
import io.dcloud.uts.Map
import io.dcloud.uts.Set
import io.dcloud.uts.UTSAndroid
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
typealias OnMemoryInfoChange = (callback: (res: UTSArray<Number>) -> Unit) -> Unit
typealias OffMemoryInfoChange = () -> Unit
typealias GetMemoryInfo = () -> UTSArray<Number>
val onMemoryInfoChange: OnMemoryInfoChange = fun(callback: (res: UTSArray<Number>) -> Unit) {
    MemoryInfoNative.onMemoryInfoChangeKotlin(fun(res: kotlin.Array<Number>) {
        callback(UTSArray.fromNative(res))
    }
    )
}
val offMemoryInfoChange: OffMemoryInfoChange = fun() {
    MemoryInfoNative.offMemoryInfoChangeKotlin()
}
val getMemoryInfo: GetMemoryInfo = fun(): UTSArray<Number> {
    var kotlinArray = MemoryInfoNative.getMemInfoKotlin()
    return UTSArray.fromNative(kotlinArray)
}
