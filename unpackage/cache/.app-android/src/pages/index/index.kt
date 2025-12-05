@file:Suppress("UNCHECKED_CAST", "USELESS_CAST", "INAPPLICABLE_JVM_NAME", "UNUSED_ANONYMOUS_PARAMETER", "NAME_SHADOWING", "UNNECESSARY_NOT_NULL_ASSERTION")
package uni.UNIA6AD04B
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
import kotlin.properties.Delegates
import uts.sdk.modules.FaceAISDKCore.addFaceSearchImage
import uts.sdk.modules.FaceAISDKCore.faceSearch
import uts.sdk.modules.FaceAISDKCore.deleteFace
import uts.sdk.modules.FaceAISDKCore.addFaceImage
import uts.sdk.modules.FaceAISDKCore.faceVerify
import uts.sdk.modules.FaceAISDKCore.insertFace
import uts.sdk.modules.FaceAISDKCore.FaceVerifyParam
import uts.sdk.modules.FaceAISDKCore.LivenessParam
import uts.sdk.modules.FaceAISDKCore.ResultJSON
import uts.sdk.modules.FaceAISDKCore.onGetString
import uts.sdk.modules.FaceAISDKCore.livenessVerify
import uts.sdk.modules.FaceAISDKCore.onCheckFaceExist
open class GenPagesIndexIndex : BasePage {
    constructor(__ins: ComponentInternalInstance, __renderer: String?) : super(__ins, __renderer) {
        onLoad(fun(_: OnLoadOptions) {}, __ins)
    }
    @Suppress("UNUSED_PARAMETER", "UNUSED_VARIABLE")
    override fun `$render`(): Any? {
        val _ctx = this
        val _cache = this.`$`.renderCache
        return _cE("view", null, _uA(
            _cE("button", _uM("onClick" to _ctx.addFaceImageDemo), "录入人脸信息", 8, _uA(
                "onClick"
            )),
            _cE("button", _uM("onClick" to _ctx.checkFaceExistDemo), "检测人脸是否存在", 8, _uA(
                "onClick"
            )),
            _cE("button", _uM("onClick" to _ctx.insertFaceFeature), "同步人脸特征信息", 8, _uA(
                "onClick"
            )),
            _cE("button", _uM("onClick" to _ctx.deleteFaceDemo), "删除本地人脸信息", 8, _uA(
                "onClick"
            )),
            _cE("button", _uM("onClick" to _ctx.faceVerifyDemo), "人脸识别+活体检测", 8, _uA(
                "onClick"
            )),
            _cE("button", _uM("onClick" to _ctx.livenessVerifyDemo), "仅活体检测", 8, _uA(
                "onClick"
            )),
            _cE("button", _uM("class" to "gray-button", "onClick" to _ctx.faceSearchDemo), "1:N人脸搜索识别", 8, _uA(
                "onClick"
            )),
            _cE("button", _uM("class" to "gray-button", "onClick" to _ctx.addFaceSearchImageDemo), "1:N人脸搜索录入人脸", 8, _uA(
                "onClick"
            )),
            _cE("view", _uM("class" to "result-box"), _uA(
                _cE("view", null, " Email: FaceAISDK.Service@gmail.com"),
                _cE("scroll-view", _uM("scroll-y" to "true", "class" to "scroll-view-box"), _uA(
                    _cE("text", _uM("class" to "text-content"), _tD(_ctx.faceAIResult), 1)
                ))
            ))
        ))
    }
    open var faceID: String by `$data`
    open var faceFeature: String by `$data`
    open var faceAIResult: String by `$data`
    @Suppress("USELESS_CAST")
    override fun data(): Map<String, Any?> {
        return _uM("faceID" to "18812345678", "faceFeature" to "faceFeature is a string with lenth 1024", "faceAIResult" to "faceAIResult")
    }
    open var checkFaceExistDemo = ::gen_checkFaceExistDemo_fn
    open fun gen_checkFaceExistDemo_fn() {
        onCheckFaceExist(this.faceID, fun(result: ResultJSON){
            this.faceAIResult = "OK：" + JSON.stringify(result)
        }
        )
    }
    open var addFaceImageDemo = ::gen_addFaceImageDemo_fn
    open fun gen_addFaceImageDemo_fn() {
        addFaceImage(this.faceID, 1, fun(result: ResultJSON){
            this.faceAIResult = JSON.stringify(result)
        }
        )
    }
    open var faceVerifyDemo = ::gen_faceVerifyDemo_fn
    open fun gen_faceVerifyDemo_fn() {
        var faceVerifyParam = FaceVerifyParam(faceID = this.faceID, threshold = 0.83, faceLivenessType = 3, verifyTimeOut = 7, motionStepSize = 2, silentThreshold = 0.7)
        faceVerify(faceVerifyParam, fun(result: ResultJSON){
            this.faceAIResult = JSON.stringify(result)
        }
        )
    }
    open var livenessVerifyDemo = ::gen_livenessVerifyDemo_fn
    open fun gen_livenessVerifyDemo_fn() {
        var param = LivenessParam(faceLivenessType = 3, verifyTimeOut = 7, motionStepSize = 2, silentThreshold = 0.7)
        livenessVerify(param, fun(result: ResultJSON){
            this.faceAIResult = JSON.stringify(result)
        }
        )
    }
    open var insertFaceFeature = ::gen_insertFaceFeature_fn
    open fun gen_insertFaceFeature_fn() {
        insertFace(this.faceID, this.faceFeature, fun(result: ResultJSON){
            this.faceAIResult = JSON.stringify(result)
        }
        )
    }
    open var deleteFaceDemo = ::gen_deleteFaceDemo_fn
    open fun gen_deleteFaceDemo_fn() {
        deleteFace(this.faceID, fun(result: ResultJSON){
            this.faceAIResult = JSON.stringify(result)
        }
        )
    }
    open var faceSearchDemo = ::gen_faceSearchDemo_fn
    open fun gen_faceSearchDemo_fn() {
        faceSearch(fun(result: ResultJSON){
            this.faceAIResult = JSON.stringify(result)
        }
        )
    }
    open var addFaceSearchImageDemo = ::gen_addFaceSearchImageDemo_fn
    open fun gen_addFaceSearchImageDemo_fn() {
        addFaceSearchImage(1, fun(result: ResultJSON){
            this.faceAIResult = JSON.stringify(result)
        }
        )
    }
    open var getStringTest = ::gen_getStringTest_fn
    open fun gen_getStringTest_fn() {
        onGetString(fun(res: ResultJSON){
            this.faceAIResult = "返回1：" + JSON.stringify(res)
        }
        )
    }
    companion object {
        val styles: Map<String, Map<String, Map<String, Any>>> by lazy {
            _nCS(_uA(
                styles0
            ), _uA(
                GenApp.styles
            ))
        }
        val styles0: Map<String, Map<String, Map<String, Any>>>
            get() {
                return _uM("result-box" to _pS(_uM("marginTop" to "20rpx", "marginRight" to "20rpx", "marginBottom" to "20rpx", "marginLeft" to "20rpx")), "scroll-view-box" to _pS(_uM("height" to "400rpx", "borderTopWidth" to 1, "borderRightWidth" to 1, "borderBottomWidth" to 1, "borderLeftWidth" to 1, "borderTopStyle" to "solid", "borderRightStyle" to "solid", "borderBottomStyle" to "solid", "borderLeftStyle" to "solid", "borderTopColor" to "#cccccc", "borderRightColor" to "#cccccc", "borderBottomColor" to "#cccccc", "borderLeftColor" to "#cccccc", "borderTopLeftRadius" to "10rpx", "borderTopRightRadius" to "10rpx", "borderBottomRightRadius" to "10rpx", "borderBottomLeftRadius" to "10rpx", "backgroundColor" to "#f8f8f8", "paddingTop" to "15rpx", "paddingRight" to "15rpx", "paddingBottom" to "15rpx", "paddingLeft" to "15rpx", "boxSizing" to "border-box")), "text-content" to _pS(_uM("fontSize" to "28rpx", "color" to "#333333", "wordBreak" to "break-all", "whiteSpace" to "pre-wrap")), "gray-button" to _pS(_uM("backgroundColor" to "#ffffff", "color" to "#800080", "borderTopWidth" to "medium", "borderRightWidth" to "medium", "borderBottomWidth" to "medium", "borderLeftWidth" to "medium", "borderTopStyle" to "none", "borderRightStyle" to "none", "borderBottomStyle" to "none", "borderLeftStyle" to "none", "borderTopColor" to "#000000", "borderRightColor" to "#000000", "borderBottomColor" to "#000000", "borderLeftColor" to "#000000")))
            }
        var inheritAttrs = true
        var inject: Map<String, Map<String, Any?>> = _uM()
        var emits: Map<String, Any?> = _uM()
        var props = _nP(_uM())
        var propsNeedCastKeys: UTSArray<String> = _uA()
        var components: Map<String, CreateVueComponent> = _uM()
    }
}
