package uts.sdk.modules.uniFaceAISDK;

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.app.ActivityManager
import android.content.Context.ACTIVITY_SERVICE
import io.dcloud.uts.UTSAndroid
import io.dcloud.uts.setInterval
import io.dcloud.uts.clearInterval
import io.dcloud.uts.console
import org.json.JSONObject
import io.dcloud.uts.UTSJSONObject


/**
 *  启动一个新的Activity 并监测结果
 */
object FaceAISDKNative {


	/**
	 * 获取参数，返回结果 utsInput: UTSJSONObject,
	 */
	fun callFaceAIKotlin(utsInput: UTSJSONObject,callback: (UTSJSONObject) -> Unit){
		
	   val faceID=utsInput.getString("faceID")
		
       var result: UTSJSONObject = object : UTSJSONObject() {
            var faceID = "faceIDBack"
			var code = "code"
			var msg = "faceIDBack"
			
            var printResult = fun(){
                console.log(faceID)
            }
        }
		callback(result)
		
	}
       



     /**
     * 记录上一次的任务id
     */
    private var lastTaskId:Number = -1

	/**
	 * 开启内存监控
	 */
    fun onMemoryInfoChangeKotlin(callback: (Array<Number>) -> Unit){

        if(lastTaskId != -1){
            // 避免重复开启
            clearInterval(lastTaskId)
        }

		// 延迟1000ms，每2000ms 获取一次内存
        lastTaskId = setInterval({

          val activityManager = UTSAndroid.getUniActivity()?.getSystemService(ACTIVITY_SERVICE) as ActivityManager
          val memoryInfo = ActivityManager.MemoryInfo()
          activityManager.getMemoryInfo(memoryInfo)
          val availMem = memoryInfo.availMem / 1024 / 1024
          val totalMem = memoryInfo.totalMem / 1024 / 1024
		  
          console.log()
          
		  console.log(availMem,totalMem)
		  // 将得到的内存信息，封装为kotlin.Array 单位是MB
          callback(arrayOf(availMem,totalMem))
          
        },1000,2000)
		
    }
	

    
	/**
	 * 关闭内存监控
	 */
    fun offMemoryInfoChangeKotlin(){
        if(lastTaskId != -1){
            // 避免重复开启
            clearInterval(lastTaskId)
        }
    }

}

