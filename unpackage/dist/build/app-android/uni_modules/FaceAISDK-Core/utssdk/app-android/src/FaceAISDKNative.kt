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
import android.text.TextUtils;
import com.tencent.mmkv.MMKV;
import com.faceAI.demo.base.utils.VoicePlayer;
import com.faceAI.demo.base.utils.BitmapUtils;
import com.faceAI.demo.FaceSDKConfig;
import com.ai.face.faceSearch.search.Image2FaceFeature;

 
/**
 *  启动一个新的Activity 并监测结果
 */
object FaceAISDKNative {

	
	/**
	 * 删除本地人脸特征值，同时缓存的图片也删除
	 * 
	 */
	fun deleteFaceKotlin(context:Application,faceID: String,callback: (UTSJSONObject) -> Unit){
		
       //1:1 的人脸特征清除
       MMKV.defaultMMKV().removeValueForKey(faceID)
       //如果缓存了图片也删除
       Image2FaceFeature.getInstance(context).deleteFaceImage(FaceSDKConfig.CACHE_BASE_FACE_DIR+faceID)
		
	   // var isSuccess=FaceSDKConfig.deleteFace(context,FaceSDKConfig.CACHE_BASE_FACE_DIR+faceID,faceID)
	
	   var result: UTSJSONObject = object : UTSJSONObject() {
			var code = 1
			var msg = "Delete Success"
	        var faceID = faceID
	    }
		callback(result)
	}
	
	
 
	/**
	 * 判断人脸是否存在
	 */
	fun isFaceExistKotlin(context:Application,faceID: String,callback: (UTSJSONObject) -> Unit){
	    var isExist=true;

        //从本地MMKV读取人脸特征值(2025.11.23版本使用MMKV，老的人脸数据请做好迁移)
        val faceFeature = MMKV.defaultMMKV().decodeString(faceID)
        if (TextUtils.isEmpty(faceFeature)) {
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
    fun insertFaceKotlin(faceID: String,faceFeature : String,context:Application,callback: (UTSJSONObject) -> Unit){
        //保存1:1 人脸识别特征数据，直接以KEY-Value的形式保存在MMKV中
		MMKV.defaultMMKV().encode(faceID, faceFeature); //保存人脸faceID 对应的特征值,SDK 只要这个
    }
	
	
	
	
	/**
	 * 同步Base64人脸到SDK
	 */
	fun insertFaceByImage(faceID: String,faceBase64 : String,context:Application,callback: (UTSJSONObject) -> Unit){
		
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

 
		  }
	}

}

