# FaceAISDK
### 开发文档

此插件共5个API实现人脸识别活体检测，可在uniApp,uniAppX 中使用. 人脸特征值是个1024 长度的字符串


使用示例:

```
uvue
<template>
	<view>
		<button @tap="addFaceImageDemo">录入人脸信息</button>
		<button @tap="checkFaceExistDemo">检测人脸是否存在</button>
		<button @tap="insertFaceFeature">同步人脸特征信息</button>
		<button @tap="deleteFaceDemo">删除本地人脸信息</button>
		<button @tap="faceVerifyDemo">人脸识别+活体检测</button>
		<button @tap="livenessVerifyDemo">仅活体检测</button>
		<button class="gray-button" @tap="faceSearchDemo">1:N人脸搜索识别</button> 
		<button class="gray-button" @tap="addFaceSearchFeatureDemo">1:N人脸搜索录入人脸</button>
	</view>
</template>



<script> 
	import {addFaceSearchFeature,faceSearch,deleteFace,addFaceImage,faceVerify,insertFace,FaceVerifyParam,LivenessParam,ResultJSON,onGetString, livenessVerify,onCheckFaceExist} from "@/uni_modules/FaceAISDK-Core";
 
	export default {
		  
		data() {
			return {
				faceID: '18812345678',
				faceFeature: 'faceFeature is a string with lenth 1024',
				faceAIResult: 'faceAIResult'
			}
		},
		onLoad() {

		},
		
		methods: {
			
			/**
			* 1. 检测是否本地有faceID人脸特征值
			*/
			checkFaceExistDemo: function () {
			
				onCheckFaceExist(
					this.faceID,
					 (result: ResultJSON) => {
						this.faceAIResult ="OK："+JSON.stringify(result)						
					})
			}, 
			

			/**
			* 2. 演示如何调用SDK录入人脸特征值
			* 
			*/
			addFaceImageDemo: function () {
				addFaceImage(
					this.faceID,
					1,  //1.快速模式，  2.精确模式(人脸品质高)
					true, //是否需要显示确认框，强烈建议需要
					 (result: ResultJSON)  => {
						//录入的人脸
						this.faceAIResult =JSON.stringify(result)
					})
			},
			
			
			/**
			* 3. 演示传参进行人脸识别
			* 
			*/
			faceVerifyDemo: function () {	
				var faceVerifyParam : FaceVerifyParam = {
					faceID: this.faceID,
					threshold: 0.83,         //人脸识别通过的相似度阈值 0.75到0.95. 根据场景自行调整
					faceLivenessType: 3,     //活体检测类型 //0 NONE无活体;  1 SILENT静默活体和摄像头成像能力有关;  2 MOTION动作活体;  3 SILENT_MOTION;  
					verifyTimeOut: 7,        //动作活体检测超时时间[3,22] 秒
					motionStepSize:2,        //动作活体检测步骤个数
					silentThreshold: 0.7    //5 静默活体阈值通过阈值（后期会改为炫彩活体检测）
				}
				
				/**
				 * Code
				 * 0 用户取消
				 * 1 人脸识别成功
				 * 2 活体分数过低
				 * 3 活体检测超时
				 * 4 人脸识别相似度低于阈值
				 * 5 多次检测到画面无人脸
				 */
				faceVerify(
					faceVerifyParam,
					 (result: ResultJSON) => {
						this.faceAIResult =JSON.stringify(result)
					})
			},
			
			
		   /**
			* 4. 活体检测
		    * Code
		    * 0 用户取消
		    * 1 人脸识别成功
		    * 2 活体分数过低
		    * 3 活体检测超时
		    * 4 人脸识别相似度低于阈值
		    * 5 多次检测到画面无人脸
		    */
			livenessVerifyDemo: function () {
				//目前参数不会生效，都是原生SDK默认的		
				var param : LivenessParam = {
					faceLivenessType: 3,  //活体检测类型 //0 NONE无活体;  1 SILENT静默活体和摄像头成像能力有关;  2 MOTION动作活体;  3 SILENT_MOTION;  
                    verifyTimeOut: 7,     // 动作活体检测超时时间[3,22]，仅仅含有MOTION（动作活体）
                    motionStepSize: 2,    //动作活体的步骤个数（1-2个）
                    silentThreshold: 0.7  //5 静默活体阈值
                }  
				 
				livenessVerify(
					param,
					 (result: ResultJSON) => {
						this.faceAIResult =JSON.stringify(result)
					})
			},
			
			
			/**
			* 5. 演示同步人脸特征值到SDK
			*/
			insertFaceFeature: function () {
				insertFace(
				    this.faceID,this.faceFeature,
					(result: ResultJSON) => {
						this.faceAIResult =JSON.stringify(result)
					})
			},
			
			/**
			* 6. 演示删除人脸特征值
			*/
			deleteFaceDemo: function () {
				deleteFace(
				    this.faceID,
					(result: ResultJSON) => {
						this.faceAIResult =JSON.stringify(result)
					})
			},
			
			/**
			* 人脸搜索，插件beta版本
			*/
			faceSearchDemo: function () {
				faceSearch(
					(result: ResultJSON) => {
						this.faceAIResult =JSON.stringify(result)
					})
			},
			
			
			/**
			* 人脸搜索人脸录入，Beta版本仅供演示
			* 
			*/
			addFaceSearchFeatureDemo: function () {
				addFaceSearchFeature(
					 1,  //1.快速模式，  2.精确模式
					 (result: ResultJSON)  => {
						//录入的人脸
						this.faceAIResult =JSON.stringify(result)
					})
			},
			
			getStringTest: function () {
				onGetString(
					(res: ResultJSON) => {
					this.faceAIResult ="返回1：" + JSON.stringify(res)
				})
			}

	   }			

	}
</script>



```
