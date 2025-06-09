@file:Suppress("UNCHECKED_CAST", "USELESS_CAST", "INAPPLICABLE_JVM_NAME", "UNUSED_ANONYMOUS_PARAMETER", "NAME_SHADOWING", "UNNECESSARY_NOT_NULL_ASSERTION")
package uni.UNI9292158
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
import uts.sdk.modules.uniMemoryInfo.offMemoryInfoChange
import uts.sdk.modules.uniMemoryInfo.onMemoryInfoChange
import uts.sdk.modules.uniMemoryInfo.getMemoryInfo
open class GenPagesIndexIndex : BasePage {
    constructor(__ins: ComponentInternalInstance, __renderer: String?) : super(__ins, __renderer) {
        onLoad(fun(_: OnLoadOptions) {}, __ins)
    }
    @Suppress("UNUSED_PARAMETER", "UNUSED_VARIABLE")
    override fun `$render`(): Any? {
        val _ctx = this
        val _cache = this.`$`.renderCache
        return createElementVNode("view", null, utsArrayOf(
            createElementVNode("button", utsMapOf("onClick" to _ctx.kotlinMemGetTest), "通过kotlin获取内存(同步)", 8, utsArrayOf(
                "onClick"
            )),
            createElementVNode("button", utsMapOf("onClick" to _ctx.kotlinMemListenTest), "kotlin监听内存并持续回调UTS", 8, utsArrayOf(
                "onClick"
            )),
            createElementVNode("button", utsMapOf("onClick" to _ctx.kotlinStopMemListenTest), "停止监听", 8, utsArrayOf(
                "onClick"
            )),
            createElementVNode("text", null, toDisplayString(_ctx.memInfo), 1)
        ))
    }
    open var memInfo: String by `$data`
    @Suppress("USELESS_CAST")
    override fun data(): Map<String, Any?> {
        return utsMapOf("memInfo" to "-")
    }
    open var kotlinMemGetTest = ::gen_kotlinMemGetTest_fn
    open fun gen_kotlinMemGetTest_fn() {
        var array = getMemoryInfo()
        this.memInfo = "可用内存:" + array[0] + "MB--整体内存:" + array[1] + "MB"
    }
    open var kotlinMemListenTest = ::gen_kotlinMemListenTest_fn
    open fun gen_kotlinMemListenTest_fn() {
        onMemoryInfoChange(fun(res: UTSArray<Number>){
            this.memInfo = "可用内存:" + res[0] + "MB--整体内存:" + res[1] + "MB"
        }
        )
    }
    open var kotlinStopMemListenTest = ::gen_kotlinStopMemListenTest_fn
    open fun gen_kotlinStopMemListenTest_fn() {
        offMemoryInfoChange()
        this.memInfo = "已暂停"
    }
    companion object {
        val styles: Map<String, Map<String, Map<String, Any>>> by lazy {
            normalizeCssStyles(utsArrayOf(), utsArrayOf(
                GenApp.styles
            ))
        }
        var inheritAttrs = true
        var inject: Map<String, Map<String, Any?>> = utsMapOf()
        var emits: Map<String, Any?> = utsMapOf()
        var props = normalizePropsOptions(utsMapOf())
        var propsNeedCastKeys: UTSArray<String> = utsArrayOf()
        var components: Map<String, CreateVueComponent> = utsMapOf()
    }
}
