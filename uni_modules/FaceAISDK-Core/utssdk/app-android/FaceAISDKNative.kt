package uts.sdk.modules.uniFaceAISDK;

import android.content.Intent
import android.app.Application
import android.content.Context
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
import com.ai.face.core.engine.FaceAISDKEngine;

 
/**
 *  kotlin 方法集  
 *
 */
object FaceAISDKNative {
	
	
	/**
	 * 「1:1人脸识别」从照片中提取人脸特征
	 * 
	 */
	fun getFaceFeatureByImageNative(context:Context,faceID: String,base64FaceImage: String,callback: (UTSJSONObject) -> Unit){
		
	   val bitmap = BitmapUtils.base64ToBitmap(base64FaceImage)
		
	   Image2FaceFeature.getInstance(context).getFaceFeatureByBitmap(bitmap,faceID,object : Image2FaceFeature.Callback{
	       override fun onFailed(msg: String) {
	           // Toast.makeText(baseContext, msg, Toast.LENGTH_LONG).show()
			   var result: UTSJSONObject = object : UTSJSONObject() {
			   			var code = 0
			   			var msg = "getFaceFeature failed"
			            var faceID = faceID
			    }
			   	callback(result)
	       }
	   
	       override fun onSuccess(
	           bitmap: Bitmap,
	           faceID: String,
	           faceFeature: String
	       ) {
			   FaceAISDKEngine.getInstance(context).saveCroppedFaceImage(bitmap, FaceSDKConfig.CACHE_BASE_FACE_DIR, faceID);
               //保存1:1 人脸识别特征数据，直接以KEY-Value的形式保存在MMKV中
               MMKV.defaultMMKV().encode(faceID, faceFeature); //保存人脸faceID 对应的特征值,SDK 只要这个
			   var result: UTSJSONObject = object : UTSJSONObject() {
			   			var code = 1
			   			var msg = "getFaceFeature Success"
			            var faceID = faceID
						var faceFeature =faceFeature
			    }
			   	callback(result)
	       }
	   })
		

	}
	

	
	/**
	 * 「1:1人脸识别」删除本地人脸特征值，同时缓存的图片也删除
	 * 
	 */
	fun deleteFaceKotlin(context:Application,faceID: String,callback: (UTSJSONObject) -> Unit){
		
       //1:1 的人脸特征清除
       MMKV.defaultMMKV().removeValueForKey(faceID)
       //如果缓存了图片也删除
       Image2FaceFeature.getInstance(context).deleteFaceImage(FaceSDKConfig.CACHE_BASE_FACE_DIR+faceID)
	
	   var result: UTSJSONObject = object : UTSJSONObject() {
			var code = 1
			var msg = "Delete Success"
	        var faceID = faceID
	    }
		callback(result)
	}
	
	
 
	/**
	 * 「1:1人脸识别」判断人脸是否存在
	 */
	fun isFaceExistKotlin(context:Application,faceID: String,callback: (UTSJSONObject) -> Unit){
	    var isExist=true;
		var msg = "Face Feature exist"

        //从本地MMKV读取人脸特征值(2025.11.23版本使用MMKV，老的人脸数据请做好迁移)
        var faceFeature = MMKV.defaultMMKV().decodeString(faceID)
        if (faceFeature.isNullOrEmpty()) {
			isExist=false;
			msg = "Face Feature not exist"
			faceFeature=""
        }else if (faceFeature.length !=1024) {
			isExist=false;
			msg = "Face Feature length should be 1024"
		}

        var result: UTSJSONObject = object : UTSJSONObject() {
			var code = if(isExist) 1 else 0
			var msg = msg
            var faceID = faceID
			var faceFeature = faceFeature
        }
		callback(result)
	}
       
	   
    /**
     * 「1:1人脸识别」同步人脸特征值到SDK
     */
    fun insertFaceKotlin(faceID: String,faceFeature : String,context:Application,callback: (UTSJSONObject) -> Unit){
		
		var msg = "insert Face success"
		var code = 1

		if (TextUtils.isEmpty(faceFeature)) {
			msg = "Face Feature not exist"
			code = 0
		}else if (faceFeature.length !=1024) {
			msg = "Face Feature length should be 1024"
			code = 0
		}else{
			//保存1:1 人脸识别特征数据，直接以KEY-Value的形式保存在MMKV中
			MMKV.defaultMMKV().encode(faceID, faceFeature); //保存人脸faceID 对应的特征值,SDK 只要这个
		}
		
		var result: UTSJSONObject = object : UTSJSONObject() {
			var code = code
			var msg = msg
		    var faceID = faceID
		}
		callback(result)
		
    }

}

