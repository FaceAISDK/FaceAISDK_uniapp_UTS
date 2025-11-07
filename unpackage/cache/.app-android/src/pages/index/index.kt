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
            _cE("button", _uM("onClick" to _ctx.addFaceImageDemo), "录入人脸", 8, _uA(
                "onClick"
            )),
            _cE("button", _uM("onClick" to _ctx.checkFaceExistDemo), "检测人脸是否存在", 8, _uA(
                "onClick"
            )),
            _cE("button", _uM("onClick" to _ctx.insertFace), "同步Base64编码人脸图", 8, _uA(
                "onClick"
            )),
            _cE("button", _uM("onClick" to _ctx.deleteFaceDemo), "删除本地人脸信息", 8, _uA(
                "onClick"
            )),
            _cE("button", _uM("onClick" to _ctx.faceVerifyDemo), "人脸识别活体检测", 8, _uA(
                "onClick"
            )),
            _cE("button", _uM("onClick" to _ctx.livenessVerifyDemo), "仅活体检测", 8, _uA(
                "onClick"
            )),
            _cE("button", _uM("onClick" to _ctx.faceSearchDemo), "1:N人脸搜索识别", 8, _uA(
                "onClick"
            )),
            _cE("button", _uM("onClick" to _ctx.addFaceSearchImageDemo), "录入人脸for1:N搜索", 8, _uA(
                "onClick"
            )),
            _cE("text", null, _tD(_ctx.faceAIResult), 1)
        ))
    }
    open var faceID: String by `$data`
    open var faceBase64: String by `$data`
    open var faceAIResult: String by `$data`
    @Suppress("USELESS_CAST")
    override fun data(): Map<String, Any?> {
        return _uM("faceID" to "18812345678", "faceBase64" to "data:image/jpeg;base64,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", "faceAIResult" to "faceAIResult")
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
        var faceVerifyParam = FaceVerifyParam(faceID = this.faceID, threshold = 0.83, faceLivenessType = 3, verifyTimeOut = 7, motionStepSize = 2, silentThreshold = 0.85)
        faceVerify(faceVerifyParam, fun(result: ResultJSON){
            this.faceAIResult = JSON.stringify(result)
        }
        )
    }
    open var livenessVerifyDemo = ::gen_livenessVerifyDemo_fn
    open fun gen_livenessVerifyDemo_fn() {
        var param = LivenessParam(faceLivenessType = 3, verifyTimeOut = 7, motionStepSize = 2, silentThreshold = 0.85)
        livenessVerify(param, fun(result: ResultJSON){
            this.faceAIResult = JSON.stringify(result)
        }
        )
    }
    open var insertFace = ::gen_insertFace_fn
    open fun gen_insertFace_fn() {
        insertFace(this.faceID, this.faceBase64, fun(result: ResultJSON){
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
            _nCS(_uA(), _uA(
                GenApp.styles
            ))
        }
        var inheritAttrs = true
        var inject: Map<String, Map<String, Any?>> = _uM()
        var emits: Map<String, Any?> = _uM()
        var props = _nP(_uM())
        var propsNeedCastKeys: UTSArray<String> = _uA()
        var components: Map<String, CreateVueComponent> = _uM()
    }
}
