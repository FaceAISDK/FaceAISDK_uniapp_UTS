import {AddFaceSearchFeature,FaceSearch,InsertFaceSearchFeature,ResultJSON} from '../interface.uts'

import Application from 'android.app.Application';
import Activity from 'android.app.Activity';
import Intent from 'android.content.Intent';
import FaceSDKConfig from "com.faceAI.demo.FaceSDKConfig";
import FaceAISDKNative from "uts.sdk.modules.uniFaceAISDK.FaceAISDKNative";
import AddFaceImageActivity from "com.faceAI.demo.SysCamera.addFace.AddFaceImageActivity";
import FaceVerificationActivity from "com.faceAI.demo.SysCamera.verify.FaceVerificationActivity";
import LivenessDetectActivity from "com.faceAI.demo.SysCamera.verify.LivenessDetectActivity";
import BitmapUtils from "com.faceAI.demo.base.utils.BitmapUtils";
import FaceSearch1NActivity from 'com.faceAI.demo.SysCamera.search.FaceSearch1NActivity';



//*******************************     下面是1:N 人脸搜索识别的方法（仅Android） ************************************************

 /**
  *  人脸搜索人脸特征更新同步
  */
 export const insertFaceSearchFeature : InsertFaceSearchFeature = function (faceID : string, faceFeature : string, tag : string, group : string, 
 callback : (result : ResultJSON) => void) {
 	const context = UTSAndroid.getAppContext() as Application
 	FaceSDKConfig.init(context);	
 	
 	// FaceAISDKNative.deleteFaceKotlin(context, faceID,function (result : UTSJSONObject) {
 	// 	const resultJson:ResultJSON={
 	// 			code:result.getNumber("code") as number,
 	// 			msg:result.getString("msg") as string,
 	// 			faceID:faceID,
 	// 			faceBase64:"?",
 	// 			faceFeature:""
 	// 		}	
 	// 	callback(resultJson)
 	// })
 }



/**
 * 跳转到Android SDK 中原生页面处理人脸录入
 * 
 * @param faceID 用户ID
 * @param addFacePerformanceMode 添加人脸角度检测模式. 1 快速模式   2 精确模式
 * @param callback 结果回调
 */
export const addFaceSearchFeature : AddFaceSearchFeature = function (
	addFacePerformanceMode:number,
	callback : (result : ResultJSON) => void
) {
	const context = UTSAndroid.getUniActivity() as Activity
	FaceSDKConfig.init(context);
	const intent = new Intent(context, AddFaceImageActivity().javaClass)
	intent.putExtra("ADD_FACE_IMAGE_TYPE_KEY", "FACE_SEARCH");
	intent.putExtra(AddFaceImageActivity.ADD_FACE_PERFORMANCE_MODE, addFacePerformanceMode);
	context.startActivityForResult(intent, 10086)

    //语法不熟悉，先保证主流程跑通
	UTSAndroid.onAppActivityResult((requestCode : Int, resultCode : Int, intentAct?: Intent) => {
		if (requestCode == 10086) {
			if(intentAct!=null){
				const codeNow:number = intentAct.getIntExtra("code",0) as number
				const msgNow:string=intentAct.getStringExtra("msg") as string
                const faceFeatureNoew:string=intentAct.getStringExtra("faceFeature") as string
				
				let faceBase64="?"
				if(0!=codeNow){
					//用户取消了就不应该有这个值
					faceBase64=BitmapUtils.bitmapToBase64(FaceSDKConfig.CACHE_BASE_FACE_DIR+faceID)
				}
				 
				const resultJson:ResultJSON={
					code:codeNow,
					msg:msgNow,
					faceFeature: faceFeatureNoew,
					faceID:"",
					faceBase64:faceBase64
				}		
				console.log("添加人脸人脸："+resultJson)
				callback(resultJson)
			}else{
				const resultJson:ResultJSON={
					code:-1,
					msg:"添加失败",
					faceFeature: "",
					faceID:"",
					faceBase64:""
				}
				
				callback(resultJson)
			}
		} 
	});
}



/**
 * 1:N，M：N 人脸搜索，开发测试中 完善一下参数
 * 
 */
export const faceSearch:FaceSearch = function(callback: (res: ResultJSON) => void){
	
	const context = UTSAndroid.getUniActivity() as Activity
	FaceSDKConfig.init(context);
	
	const intent = new Intent(context, FaceSearch1NActivity().javaClass)
	context.startActivity(intent)
	
	const resultJson:ResultJSON={
		code: 1,
		msg: "开发测试中",
		faceID: "faceID8",
		faceBase64: "64",
		faceFeature: ""
	}
    callback(resultJson)
} 


//*********************************************  下面是1:1 人脸识别+活体检测 的方法  **********************************************************


/**
 * 跳转到Android SDK 中原生页面处理人脸录入
 * 
 * @param faceID 用户ID
 * @param addFacePerformanceMode 添加人脸角度检测模式. 1 快速模式   2 精确模式
 * @param needShowConfirmDialog 是否需要显示确认框,强烈建议需要
 * @param callback 结果回调
 */
export const addFaceFeature : AddFaceFeature = function (
	faceID : string,
	addFacePerformanceMode:number,
	needShowConfirmDialog:boolean,
	callback : (result : ResultJSON) => void
) {
	const context = UTSAndroid.getUniActivity() as Activity
	FaceSDKConfig.init(context);
	const intent = new Intent(context, AddFaceImageActivity().javaClass)
	intent.putExtra("ADD_FACE_IMAGE_TYPE_KEY", "FACE_VERIFY");
	intent.putExtra("USER_FACE_ID_KEY", faceID);
	intent.putExtra("NEED_CONFIRM_ADD_FACE", needShowConfirmDialog);
	intent.putExtra(AddFaceImageActivity.ADD_FACE_PERFORMANCE_MODE, addFacePerformanceMode);
	context.startActivityForResult(intent, 10086)

    //语法不熟悉，先保证主流程跑通
	UTSAndroid.onAppActivityResult((requestCode : Int, resultCode : Int, intentAct?: Intent) => {
		if (requestCode == 10086) {
			if(intentAct!=null){
				const codeNow:number = intentAct.getIntExtra("code",0) as number
				const msgNow:string=intentAct.getStringExtra("msg") as string
				const faceFeatureNoew:string=intentAct.getStringExtra("faceFeature") as string
				
				let faceBase64="?"
				if(0!=codeNow){
					//用户取消了就不应该有这个值
					faceBase64=BitmapUtils.bitmapToBase64(FaceSDKConfig.CACHE_BASE_FACE_DIR+faceID)
				}
				 
				const resultJson:ResultJSON={
					code:codeNow,
					msg:msgNow,
					faceFeature: faceFeatureNoew,
					faceID:faceID,
					faceBase64:faceBase64
				}		
				console.log("添加人脸人脸："+resultJson)
				callback(resultJson)
			}else{
				const resultJson:ResultJSON={
					code:-1,
					msg:"添加失败",
					faceFeature: "",
					faceID:faceID,
					faceBase64:"?"
				}
				
				callback(resultJson)
			}
		} 
	});
}


/**
 * 跳转到Android SDK 中原生页面处理人脸识别+活体检测
 * 
 * @param faceID 用户ID
 * @param callback 结果回调
 */
export const faceVerify : FaceVerify = function (
	param : FaceVerifyParam,
	callback : (result : ResultJSON) => void
) {
	const context = UTSAndroid.getUniActivity() as Activity
	FaceSDKConfig.init(context);
	
	const intent = new Intent(context, FaceVerificationActivity().javaClass)
	intent.putExtra(FaceVerificationActivity.USER_FACE_ID_KEY, param.faceID);
    intent.putExtra(FaceVerificationActivity.THRESHOLD_KEY,param.threshold);
    intent.putExtra(FaceVerificationActivity.FACE_LIVENESS_TYPE,param.faceLivenessType);
	intent.putExtra(FaceVerificationActivity.MOTION_TIMEOUT,param.verifyTimeOut);
    intent.putExtra(FaceVerificationActivity.MOTION_STEP_SIZE,param.motionStepSize);
    intent.putExtra(FaceVerificationActivity.SILENT_THRESHOLD_KEY,param.silentThreshold);
	context.startActivityForResult(intent, 10087)

	UTSAndroid.onAppActivityResult((requestCode : Int, resultCode : Int, intentAct?: Intent) => {
		if (requestCode == 10087) {
			if(intentAct!=null){
				const codeNow:number = intentAct.getIntExtra("code",0) as number
				const msgNow:string=intentAct.getStringExtra("msg") as string		
						
				//这个对应的Java float 类型，转number 会丢失精度
				const silent:number=intentAct.getIntExtra("silentLivenessScore",0) as number
			
			    //活体检测通过后的人脸图，用户可以用这张图做进一步其他处理
		    	const livefaceBase64:string=BitmapUtils.bitmapToBase64(FaceSDKConfig.CACHE_FACE_LOG_DIR+"verifyBitmap")
			
				const resultJson:ResultJSON={
					code:codeNow,
					msg:msgNow,
					faceFeature: "",
					faceID:param.faceID,
					faceBase64:livefaceBase64
				}
				callback(resultJson)
			}else{
				const resultJson:ResultJSON={
					code:1,
					msg:"12345",
					faceFeature: "",
					faceID:param.faceID,
					faceBase64:"faceBase64"
				}	
				callback(resultJson)
			}
		} 
	});
}


/**
 * 跳转到Android SDK 中原生页面处理人脸识别活体检测
 * 
 * @param faceID 用户ID
 * @param callback 结果回调
 */
export const livenessVerify : LivenessVerify = function (
	param : LivenessParam,
	callback : (result : ResultJSON) => void
) {
	const context = UTSAndroid.getUniActivity() as Activity
	FaceSDKConfig.init(context);
	const intent = new Intent(context, LivenessDetectActivity().javaClass)
    intent.putExtra(LivenessDetectActivity.FACE_LIVENESS_TYPE,param.faceLivenessType);
	intent.putExtra(LivenessDetectActivity.MOTION_TIMEOUT,param.verifyTimeOut);
    intent.putExtra(LivenessDetectActivity.MOTION_STEP_SIZE,param.motionStepSize);
    intent.putExtra(LivenessDetectActivity.SILENT_THRESHOLD_KEY,param.silentThreshold);
	
	context.startActivityForResult(intent, 10089)

	UTSAndroid.onAppActivityResult((requestCode : Int, resultCode : Int, intentAct?: Intent) => {
		if (requestCode == 10089) {
			if(intentAct!=null){
				const codeNow:number = intentAct.getIntExtra("code",0) as number
				const msgNow:string=intentAct.getStringExtra("msg") as string

				//活体检测通过后的人脸图，用户可以用这张图做进一步其他处理
				const livefaceBase64:string=BitmapUtils.bitmapToBase64(FaceSDKConfig.CACHE_FACE_LOG_DIR+"liveBitmap")
				
				const resultJson:ResultJSON={
					faceFeature:"",
					code:codeNow,
					msg:msgNow,
					faceID:"",
					faceBase64:livefaceBase64
				} 
				callback(resultJson)
			}else{
				const resultJson:ResultJSON={
					code:1,
					msg:"data == null",
					faceID:"",
					faceBase64:"",
					faceFeature:""
				}	
				callback(resultJson)
			}
		} 
	});
}



/**
 * 调用原生的FaceAISDK 检测功能人脸是否存在
 */
export const onCheckFaceExist : OnCheckFaceExist = function (faceID : string, callback : (re : ResultJSON) => void) {
	const context = UTSAndroid.getAppContext() as Application
	FaceSDKConfig.init(context);
	
	FaceAISDKNative.isFaceExistKotlin(context,faceID, function (result : UTSJSONObject) {
			const resultJson:ResultJSON={
				code:result.getNumber("code") as number,
				msg:result.getString("msg") as string,
				faceID:faceID,
				faceBase64:"?",
				faceFeature:result.getString("faceFeature") as string
			}
			
		callback(resultJson)
	})
}
 
/**
 *  同步人脸图片到SDK，比如用户换设备登陆了，把上次录入到你的业务服务器上的人脸同步就行
 */
export const insertFace : InsertFace = function (faceID : string, faceBase64 : string, callback : (result : ResultJSON) => void) {
	const context = UTSAndroid.getAppContext() as Application
	FaceSDKConfig.init(context);
	
	FaceAISDKNative.insertFaceKotlin(faceID,faceBase64,context, function (result : UTSJSONObject) {
		const resultJson:ResultJSON={
				code:result.getNumber("code") as number,
				msg:result.getString("msg") as string,
				faceID:faceID,
				faceBase64:"?",
				faceFeature:""
			}	
		callback(resultJson)
	})
}
 
 
 
 
 /**
  *  删除人脸信息
  */
 export const deleteFaceFeature : DeleteFaceFeature = function (faceID : string, callback : (result : ResultJSON) => void) {
 	const context = UTSAndroid.getAppContext() as Application
 	FaceSDKConfig.init(context);	
 	
 	FaceAISDKNative.deleteFaceKotlin(context, faceID,function (result : UTSJSONObject) {
 		const resultJson:ResultJSON={
 				code:result.getNumber("code") as number,
 				msg:result.getString("msg") as string,
 				faceID:faceID,
 				faceBase64:"?",
 				faceFeature:""
 			}	
 		callback(resultJson)
 	})
 }
  
 
