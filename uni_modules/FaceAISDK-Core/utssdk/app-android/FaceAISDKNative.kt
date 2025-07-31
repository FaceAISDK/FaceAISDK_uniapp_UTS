package uts.sdk.modules.uniFaceAISDK;

import android.content.Intent
import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import android.app.ActivityManager
import android.graphics.Bitmap
import io.dcloud.uts.UTSAndroid
import io.dcloud.uts.setInterval
import io.dcloud.uts.clearInterval
import io.dcloud.uts.console
import org.json.JSONObject
import io.dcloud.uts.UTSJSONObject

import com.faceAI.demo.FaceAIConfig
import com.ai.face.base.baseImage.FaceAIUtils
import com.ai.face.base.baseImage.FaceAIUtils.Companion.getInstance
import com.faceAI.demo.base.utils.VoicePlayer 
import com.faceAI.demo.base.utils.BitmapUtils

 
/**
 *  启动一个新的Activity 并监测结果
 */
object FaceAISDKNative {
 
	/**
	 * 判断人脸是否存在
	 */
	fun isFaceExistKotlin(faceID: String,callback: (UTSJSONObject) -> Unit){
	   val isExist=FaceAIConfig.isFaceIDExist(faceID);

       var result: UTSJSONObject = object : UTSJSONObject() {
			var code = if(isExist) 1 else 0
			var msg = if(isExist) "Face exist" else "Face not exist"
            var faceID = faceID
        }
		callback(result)
	}
       
	   
    /**
     * 同步Base64人脸到SDK
     */
    fun insertFaceKotlin(faceID: String,faceBase64 : String,context:Application,callback: (UTSJSONObject) -> Unit){
    	
          val bitmap = BitmapUtils.base64ToBitmap(faceBase64)
		   
		  if (bitmap == null) { 
			  var result: UTSJSONObject = object : UTSJSONObject() {
			  		var code =  0
			  		var msg = "base64ToBitmap 失败"
			        var faceID = faceID
			   }
			  callback(result)
			  return
		  }else {
			  //其他地方同步过来的人脸可能是不规范的没有经过校准的人脸图（证件照，多人脸，过小等）。disposeBaseFaceImage处理
			  getInstance(context)
			      .disposeBaseFaceImage(
			          bitmap,
			          FaceAIConfig.CACHE_BASE_FACE_DIR + faceID,
			          object : FaceAIUtils.Callback {
			              //处理优化人脸成功完成去初始化引擎
			              override fun onSuccess(bitmap: Bitmap) {
							
							  var result: UTSJSONObject = object : UTSJSONObject() {
							  		var code =  1
							  		var msg = "insertFace2SDK 成功"
							        var faceID = faceID
							   }
							  callback(result)
			              } 
			   
			              //底片处理异常的信息回调
			              override fun onFailed(msg: String, errorCode: Int) {
							  var result: UTSJSONObject = object : UTSJSONObject() {
							  		var code =  0
							  		var msg = "$msg-$errorCode"
							        var faceID = faceID
							   }
							  callback(result)
			              }
			          })
		  }
    }

}

