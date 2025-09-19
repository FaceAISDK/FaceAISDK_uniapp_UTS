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
import android.graphics.BitmapFactory;

import com.ai.face.base.baseImage.FaceEmbedding;
import com.faceAI.demo.FaceImageConfig
import com.ai.face.base.baseImage.FaceAIUtils
import com.faceAI.demo.base.utils.VoicePlayer 
import com.faceAI.demo.base.utils.BitmapUtils

 
/**
 *  启动一个新的Activity 并监测结果
 */
object FaceAISDKNative {
 
	/**
	 * 判断人脸是否存在
	 */
	fun isFaceExistKotlin(context:Application,faceID: String,callback: (UTSJSONObject) -> Unit){
	    var isExist=true;

        val faceEmbedding = FaceEmbedding.loadEmbedding(context, faceID)

        // 去Path 路径读取有没有faceID 对应的处理好的人脸Bitmap
        val faceFilePath = FaceImageConfig.CACHE_BASE_FACE_DIR + faceID
        val baseBitmap = BitmapFactory.decodeFile(faceFilePath)
 
        if (faceEmbedding.size == 0||baseBitmap==null) {
			isExist=false;
		}

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
			  FaceAIUtils.Companion.getInstance(context)
			      .disposeBaseFaceImage(context, bitmap,object : FaceAIUtils.Callback {
			              /**
						   * 返回裁剪成功后的人脸图和特征向量编码
						   */
			              override 
						  fun onSuccess(bitmap: Bitmap,faceEmbedding: FloatArray) {
							  
							 FaceEmbedding.saveEmbedding(context, faceID, faceEmbedding); //本地保存人脸特征向量，后期可以用这个了
							 BitmapUtils.saveDisposedBitmap(bitmap, FaceImageConfig.CACHE_BASE_FACE_DIR,faceID) //裁剪好的人脸保存本地

							  var result: UTSJSONObject = object : UTSJSONObject() {
							  		var code =  1
							  		var msg = "insertFace2SDK 成功"
							        var faceID = faceID
							   }
							  callback(result)
			              } 
			   
			              //底片处理异常的信息回调
			              override 
						  fun onFailed(msg: String, errorCode: Int) {
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

