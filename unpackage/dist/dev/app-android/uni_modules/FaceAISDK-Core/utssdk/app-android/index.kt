@file:Suppress("UNCHECKED_CAST", "USELESS_CAST", "INAPPLICABLE_JVM_NAME", "UNUSED_ANONYMOUS_PARAMETER", "NAME_SHADOWING", "UNNECESSARY_NOT_NULL_ASSERTION")
package uts.sdk.modules.FaceAISDKCore
import android.app.Activity
import android.app.Application
import android.content.Intent
import com.faceAI.demo.FaceSDKConfig
import com.faceAI.demo.SysCamera.addFace.AddFaceImageActivity
import com.faceAI.demo.SysCamera.search.FaceSearch1NActivity
import com.faceAI.demo.SysCamera.verify.FaceVerificationActivity
import com.faceAI.demo.SysCamera.verify.LivenessDetectActivity
import com.faceAI.demo.base.utils.BitmapUtils
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import uts.sdk.modules.uniFaceAISDK.FaceAISDKNative
import uts.sdk.modules.uniFaceAISDK.R
typealias OnGetString = (callback: (res: ResultJSON) -> Unit) -> Unit
typealias OnCheckFaceExist = (faceID: String, callback: (result: ResultJSON) -> Unit) -> Unit
typealias FaceSearch = (callback: (res: ResultJSON) -> Unit) -> Unit
typealias AddFaceImage = (faceID: String, addFacePerformanceMode: Number, callback: (result: ResultJSON) -> Unit) -> Unit
typealias AddFaceSearchImage = (addFacePerformanceMode: Number, callback: (result: ResultJSON) -> Unit) -> Unit
typealias FaceVerify = (param: FaceVerifyParam, callback: (result: ResultJSON) -> Unit) -> Unit
open class FaceVerifyParam (
    @JsonNotNull
    open var faceID: String,
    @JsonNotNull
    open var threshold: Number,
    @JsonNotNull
    open var faceLivenessType: Number,
    @JsonNotNull
    open var verifyTimeOut: Number,
    @JsonNotNull
    open var motionStepSize: Number,
    @JsonNotNull
    open var silentThreshold: Number,
) : UTSObject(), IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("FaceVerifyParam", "uni_modules/FaceAISDK-Core/utssdk/interface.uts", 30, 13)
    }
}
open class LivenessParam (
    @JsonNotNull
    open var faceLivenessType: Number,
    @JsonNotNull
    open var verifyTimeOut: Number,
    @JsonNotNull
    open var motionStepSize: Number,
    @JsonNotNull
    open var silentThreshold: Number,
) : UTSObject(), IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("LivenessParam", "uni_modules/FaceAISDK-Core/utssdk/interface.uts", 41, 13)
    }
}
typealias LivenessVerify = (param: LivenessParam, callback: (result: ResultJSON) -> Unit) -> Unit
open class ResultJSON (
    @JsonNotNull
    open var code: Number,
    @JsonNotNull
    open var silentLivenessScore: Number,
    @JsonNotNull
    open var msg: String,
    @JsonNotNull
    open var faceID: String,
    @JsonNotNull
    open var faceBase64: String,
) : UTSObject(), IUTSSourceMap {
    override fun `__$getOriginalPosition`(): UTSSourceMapPosition? {
        return UTSSourceMapPosition("ResultJSON", "uni_modules/FaceAISDK-Core/utssdk/interface.uts", 57, 13)
    }
}
typealias InsertFace = (faceID: String, faceBase64: String, callback: (result: ResultJSON) -> Unit) -> Unit
typealias DeleteFace = (faceID: String, callback: (result: ResultJSON) -> Unit) -> Unit
val onGetString: OnGetString = fun(callback: (res: ResultJSON) -> Unit) {
    val resultJson = ResultJSON(code = 11, msg = "onGetString", faceID = "faceID8", faceBase64 = "64", silentLivenessScore = 0)
    callback(resultJson)
}
val addFaceSearchImage: AddFaceSearchImage = fun(addFacePerformanceMode: Number, callback: (result: ResultJSON) -> Unit) {
    val context = UTSAndroid.getUniActivity() as Activity
    FaceSDKConfig.init(context)
    val intent = Intent(context, AddFaceImageActivity().javaClass)
    intent.putExtra("ADD_FACE_IMAGE_TYPE_KEY", "FACE_SEARCH")
    intent.putExtra(AddFaceImageActivity.ADD_FACE_PERFORMANCE_MODE, addFacePerformanceMode)
    context.startActivityForResult(intent, 10086)
    UTSAndroid.onAppActivityResult(fun(requestCode: Int, resultCode: Int, intentAct: Intent?){
        if (requestCode == 10086) {
            if (intentAct != null) {
                val codeNow: Number = intentAct.getIntExtra("code", 0) as Number
                val msgNow: String = intentAct.getStringExtra("msg") as String
                val resultJson = ResultJSON(code = codeNow, msg = msgNow, silentLivenessScore = 0, faceID = "", faceBase64 = "")
                console.log("添加人脸人脸：" + resultJson, " at uni_modules/FaceAISDK-Core/utssdk/app-android/index.uts:63")
                callback(resultJson)
            } else {
                val resultJson = ResultJSON(code = -1, msg = "添加失败", silentLivenessScore = 0, faceID = "", faceBase64 = "")
                callback(resultJson)
            }
        }
    }
    )
}
val faceSearch: FaceSearch = fun(callback: (res: ResultJSON) -> Unit) {
    val context = UTSAndroid.getUniActivity() as Activity
    FaceSDKConfig.init(context)
    val intent = Intent(context, FaceSearch1NActivity().javaClass)
    context.startActivity(intent)
    val resultJson = ResultJSON(code = 1, msg = "开发测试中", faceID = "faceID8", faceBase64 = "64", silentLivenessScore = 0)
    callback(resultJson)
}
val addFaceImage: AddFaceImage = fun(faceID: String, addFacePerformanceMode: Number, callback: (result: ResultJSON) -> Unit) {
    val context = UTSAndroid.getUniActivity() as Activity
    FaceSDKConfig.init(context)
    val intent = Intent(context, AddFaceImageActivity().javaClass)
    intent.putExtra("ADD_FACE_IMAGE_TYPE_KEY", "FACE_VERIFY")
    intent.putExtra("USER_FACE_ID_KEY", faceID)
    intent.putExtra(AddFaceImageActivity.ADD_FACE_PERFORMANCE_MODE, addFacePerformanceMode)
    context.startActivityForResult(intent, 10086)
    UTSAndroid.onAppActivityResult(fun(requestCode: Int, resultCode: Int, intentAct: Intent?){
        if (requestCode == 10086) {
            if (intentAct != null) {
                var faceBase64 = "?"
                val codeNow: Number = intentAct.getIntExtra("code", 0) as Number
                val msgNow: String = intentAct.getStringExtra("msg") as String
                if (0 != codeNow) {
                    faceBase64 = BitmapUtils.bitmapToBase64(FaceSDKConfig.CACHE_BASE_FACE_DIR + faceID)
                }
                val resultJson = ResultJSON(code = codeNow, msg = msgNow, silentLivenessScore = 0, faceID = faceID, faceBase64 = faceBase64)
                console.log("添加人脸人脸：" + resultJson, " at uni_modules/FaceAISDK-Core/utssdk/app-android/index.uts:143")
                callback(resultJson)
            } else {
                val resultJson = ResultJSON(code = -1, msg = "添加失败", silentLivenessScore = 0, faceID = faceID, faceBase64 = "?")
                callback(resultJson)
            }
        }
    }
    )
}
val faceVerify: FaceVerify = fun(param: FaceVerifyParam, callback: (result: ResultJSON) -> Unit) {
    val context = UTSAndroid.getUniActivity() as Activity
    FaceSDKConfig.init(context)
    val intent = Intent(context, FaceVerificationActivity().javaClass)
    intent.putExtra(FaceVerificationActivity.USER_FACE_ID_KEY, param.faceID)
    intent.putExtra(FaceVerificationActivity.THRESHOLD_KEY, param.threshold)
    intent.putExtra(FaceVerificationActivity.FACE_LIVENESS_TYPE, param.faceLivenessType)
    intent.putExtra(FaceVerificationActivity.MOTION_TIMEOUT, param.verifyTimeOut)
    intent.putExtra(FaceVerificationActivity.MOTION_STEP_SIZE, param.motionStepSize)
    intent.putExtra(FaceVerificationActivity.SILENT_THRESHOLD_KEY, param.silentThreshold)
    context.startActivityForResult(intent, 10087)
    UTSAndroid.onAppActivityResult(fun(requestCode: Int, resultCode: Int, intentAct: Intent?){
        if (requestCode == 10087) {
            if (intentAct != null) {
                val codeNow: Number = intentAct.getIntExtra("code", 0) as Number
                val msgNow: String = intentAct.getStringExtra("msg") as String
                val silent: Number = intentAct.getIntExtra("silentLivenessScore", 0) as Number
                val livefaceBase64: String = BitmapUtils.bitmapToBase64(FaceSDKConfig.CACHE_FACE_LOG_DIR + "verifyBitmap")
                val resultJson = ResultJSON(code = codeNow, msg = msgNow, silentLivenessScore = silent, faceID = param.faceID, faceBase64 = livefaceBase64)
                callback(resultJson)
            } else {
                val resultJson = ResultJSON(code = 1, msg = "12345", silentLivenessScore = 0, faceID = param.faceID, faceBase64 = "faceBase64")
                callback(resultJson)
            }
        }
    }
    )
}
val livenessVerify: LivenessVerify = fun(param: LivenessParam, callback: (result: ResultJSON) -> Unit) {
    val context = UTSAndroid.getUniActivity() as Activity
    FaceSDKConfig.init(context)
    val intent = Intent(context, LivenessDetectActivity().javaClass)
    intent.putExtra(LivenessDetectActivity.FACE_LIVENESS_TYPE, param.faceLivenessType)
    intent.putExtra(LivenessDetectActivity.MOTION_TIMEOUT, param.verifyTimeOut)
    intent.putExtra(LivenessDetectActivity.MOTION_STEP_SIZE, param.motionStepSize)
    intent.putExtra(LivenessDetectActivity.SILENT_THRESHOLD_KEY, param.silentThreshold)
    context.startActivityForResult(intent, 10089)
    UTSAndroid.onAppActivityResult(fun(requestCode: Int, resultCode: Int, intentAct: Intent?){
        if (requestCode == 10089) {
            if (intentAct != null) {
                val codeNow: Number = intentAct.getIntExtra("code", 0) as Number
                val msgNow: String = intentAct.getStringExtra("msg") as String
                val silent: Number = intentAct.getIntExtra("silentLivenessScore", 0) as Number
                val livefaceBase64: String = BitmapUtils.bitmapToBase64(FaceSDKConfig.CACHE_FACE_LOG_DIR + "liveBitmap")
                val resultJson = ResultJSON(silentLivenessScore = silent, code = codeNow, msg = msgNow, faceID = "", faceBase64 = livefaceBase64)
                callback(resultJson)
            } else {
                val resultJson = ResultJSON(code = 1, msg = "data == null", faceID = "", faceBase64 = "", silentLivenessScore = 0)
                callback(resultJson)
            }
        }
    }
    )
}
val onCheckFaceExist: OnCheckFaceExist = fun(faceID: String, callback: (re: ResultJSON) -> Unit) {
    val context = UTSAndroid.getAppContext() as Application
    FaceSDKConfig.init(context)
    FaceAISDKNative.isFaceExistKotlin(context, faceID, fun(result: UTSJSONObject) {
        val resultJson = ResultJSON(code = result.getNumber("code") as Number, msg = result.getString("msg") as String, faceID = faceID, faceBase64 = "?", silentLivenessScore = 0)
        callback(resultJson)
    }
    )
}
val insertFace: InsertFace = fun(faceID: String, faceBase64: String, callback: (result: ResultJSON) -> Unit) {
    val context = UTSAndroid.getAppContext() as Application
    FaceSDKConfig.init(context)
    FaceAISDKNative.insertFaceKotlin(faceID, faceBase64, context, fun(result: UTSJSONObject) {
        val resultJson = ResultJSON(code = result.getNumber("code") as Number, msg = result.getString("msg") as String, faceID = faceID, faceBase64 = "?", silentLivenessScore = 0)
        callback(resultJson)
    }
    )
}
val deleteFace: DeleteFace = fun(faceID: String, callback: (result: ResultJSON) -> Unit) {
    val context = UTSAndroid.getAppContext() as Application
    FaceSDKConfig.init(context)
    FaceAISDKNative.deleteFaceKotlin(context, faceID, fun(result: UTSJSONObject) {
        val resultJson = ResultJSON(code = result.getNumber("code") as Number, msg = result.getString("msg") as String, faceID = faceID, faceBase64 = "?", silentLivenessScore = 0)
        callback(resultJson)
    }
    )
}
