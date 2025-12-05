// 插件对外暴露能力的总入口在 interface.uts ，他与Android/ios 目录下的 index.uts的关系是声明和实现的关系。

export type OnGetString = (callback: (res: ResultJSON) => void) => void


//检测人脸是否存在
export type OnCheckFaceExist = (faceID: string, callback: (result: ResultJSON) => void) => void


export type FaceSearch = (callback: (res: ResultJSON) => void) => void


  
/**
 * 录入一张人脸照片
 * 
 * @param faceID 用户ID
 * @param callback 结果回调
 */
export type AddFaceImage = (
	faceID : string,
	addFacePerformanceMode:number,
	callback : (result : ResultJSON) => void) => void


/**
 * 录入一张人脸照片for 人脸搜索
 * 
 * @param faceID 用户ID
 * @param callback 结果回调
 */
export type AddFaceSearchImage = (
	addFacePerformanceMode:number,
	callback : (result : ResultJSON) => void) => void

/**
 * 人脸识别+活体检测
 * 
 * @param param 人脸识别参数
 * @param callback 结果回调
 */
export type FaceVerify = (
	param : FaceVerifyParam,
	callback : (result : ResultJSON) => void) => void
	
	
	
/**
 * 人脸识别,业务方传给FaceAISDK 插件基础参数
 */
export type FaceVerifyParam = {
  faceID: string,
  threshold: number,         // 人脸识别通过的相似度阈值 0.85到0.95
  faceLivenessType: number,  // 活体检测类型 //0 SILENT_MOTION; 1 MOTION（动作活体）; 2 SILENT（RGB图像静默活体）; 3 NONE;
  verifyTimeOut: number,     // 活体检测超时时间 秒
  motionStepSize: number,
  silentThreshold: number   
} 


/**
 * 活体检测,业务方传给FaceAISDK 插件基础参数
 */
export type LivenessParam = {
  faceLivenessType: number,         
  verifyTimeOut: number,           
  motionStepSize: number,  
  silentThreshold:number
} 
 
/**
 * 仅活体检测
 * 
 * @param param 人脸识别参数
 * @param callback 结果回调
 */
export type LivenessVerify = (
	param : LivenessParam,
	callback : (result : ResultJSON) => void) => void	




/**
 * 业务方传给FaceAISDK 插件基础参数
 */
export type ResultJSON = {
  code: number,       //code 含义参考Readme 
  msg: string, 
  faceID: string,     
  faceFeature: string,  //人脸特征值
  faceBase64: string    //人脸图Base64编码
}


/**
 * 同步人脸特征指到SDK，比如用户换设备登陆了，把上次录入到你的业务服务器上的人脸同步就行
 * 
 * @param faceFeature 人脸特征值得长度1024.20251202新版本SDK人脸数据合规处理不在接收人脸图参数
 * @param callback 结果回调
 */
export type InsertFace = (
    faceID : string,
	faceFeature : string,
	callback : (result : ResultJSON) => void) => void




/**
 * 删除设备缓存的人脸信息
 * 
 * @param faceID 人脸faceID
 * @param callback 结果回调
 */
export type DeleteFace = (
    faceID : string,
	callback : (result : ResultJSON) => void) => void