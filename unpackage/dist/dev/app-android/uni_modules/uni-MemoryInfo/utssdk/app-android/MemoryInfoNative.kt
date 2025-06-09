package uts.sdk.modules.uniMemoryInfo;

import android.app.ActivityManager
import android.content.Context.ACTIVITY_SERVICE
import io.dcloud.uts.UTSAndroid
import io.dcloud.uts.setInterval
import io.dcloud.uts.clearInterval
import io.dcloud.uts.console

object MemoryInfoNative {

	/**
	 * 同步获取内存信息
	 */
	fun getMemInfoKotlin():Array<Number>{

		val activityManager = UTSAndroid.getUniActivity()?.getSystemService(ACTIVITY_SERVICE) as ActivityManager
		val memoryInfo = ActivityManager.MemoryInfo()
		activityManager.getMemoryInfo(memoryInfo)
		val availMem = memoryInfo.availMem / 1024 / 1024
		val totalMem = memoryInfo.totalMem / 1024 / 1024
	  
		// availMem 可用内存，单位MB
		// totalMem 设备内存，单位MB
		console.log(availMem,totalMem)
		return arrayOf(availMem,totalMem)
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

