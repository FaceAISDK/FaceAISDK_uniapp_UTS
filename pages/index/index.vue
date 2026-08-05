<template>
	<view class="page">
		<view class="page-header">
			<text class="page-eyebrow">FaceAISDK · DEMO</text>
			<text class="page-title">插件调用示例</text>
		</view>

		<view class="button-list">
			<button class="demo-button" @tap="addFaceFeatureBySDKCameraDemo">SDK相机录入人脸</button>
			<button class="demo-button" @tap="faceVerifyDemo">人脸识别 + 活体检测</button>
			<button class="demo-button" @tap="livenessVerifyDemo">检测人脸是否活体</button>
			<button class="demo-button" @tap="getFaceFeatureDemo">查询人脸特征信息</button>
			<button class="demo-button" @tap="insertFaceFeatureDemo">同步人脸特征信息</button>
			<button class="demo-button" @tap="addFaceFeatureByImageDemo">Base64 人脸图提取特征</button>
			<button class="demo-button" @tap="deleteFaceFeatureDemo">删除人脸特征信息</button>
			<!-- <button class="demo-button" @tap="switchCameraDemo">切换摄像头</button> -->
		</view>

		<view class="result-box">
			<view class="result-header">
				<text class="result-title">调用结果</text>
				<text class="result-label">RESULT</text>
			</view>
			<scroll-view scroll-y="true" class="scroll-view-box">
				<text class="text-content">{{faceSDKResult}}</text>
			</scroll-view>
			<text class="contact-text">技术支持 · FaceAISDK.Service@gmail.com</text>
		</view>
	</view>
</template>

<script>
	//uniapp Page 注意：在普通 UniApp 中，需要确保该插件支持 JS 端的调用方式
	import {
		playTTS,
		deleteFaceFeature,
		switchCamera,
		addFaceBySDKImage,
		addFaceBySDKCamera,
		faceVerify,
		livenessVerify,
		getFaceFeature,
		insertFaceFeature
	} from "@/uni_modules/FaceAISDK-Core";
	
    import { base64FaceImage } from './imageData.js';

	export default {
		data() {
			return {
				faceID: 'youFaceID', //你的业务系统定义用户唯一的字符串（手机号/身份证等）
				motionLivenessType:'1,2,3,4,5',  //动作活体种类，见接口
				faceFeature: 'faceFeature，1024 length', //录入的人脸提取的特征值，加密后长度1024
				faceSDKResult: 'faceSDKResult',
				base64FaceImage:base64FaceImage  //640*480 人脸图需要遵守规范：https://i.postimg.cc/RCwNy0kV/add-Face.jpg
			}
		},
		onLoad() {

		},

		methods: {

			/**
			 * 1. 1:1 人脸识别调用SDK相机录入人脸特征值(也可以用于检测人脸后裁剪好后用于自身服务器验证)
			 * 
			 * */
			addFaceFeatureBySDKCameraDemo: function() {
				addFaceBySDKCamera(
					this.faceID,
					1,     //1.快速模式，  2.精确模式(人脸品质高)
					true,  //是否需要显示确认框，强烈建议需要(目前仅Android生效)
					(result) => {
						uni.showToast({title: result.msg, icon: 'none',duration: 2000 });
						
						//录入的人脸
						this.faceSDKResult = `code: ${result.code}\n` +
						                     `msg: ${result.msg}\n` +
						                     `faceFeature: ${result.faceFeature.length}\n` +
						                     `faceBase64: ${result.faceBase64.length}`;
						console.log("【addFaceBySDKCamera】: ***"+this.faceSDKResult);
					})
			},


			/**
			 * 2. 「1:1人脸识别」校验是否当前用户
			 */
			faceVerifyDemo: function() {
				faceVerify(
					this.faceID,
					0.84,  // 阈值设置，范围限 [0.75,0.95] ,默认0.84
					1,     // 1.动作活体 2.动作+炫彩活体 3.炫彩活体(不能强光环境使用) 4.静默活体
					"1,2,3,4,5", //动作活体种类用英文","隔开（最少3种类型）； 1.张张嘴 2.微笑 3.眨眨眼 4.摇头 5.点头
					7,     //动作活体超时时间,低端机应该适当加点时间
					2,     //动作活体步骤，1个或2个随机
					true,     //ALLOW_MULTI_FACES 是否允许多人脸入镜(仅Android)
					(result) => {
						uni.showToast({title: result.msg, icon: 'none',duration: 2000 });
						
						this.faceSDKResult = `code: ${result.code}\n` +
						                     `msg: ${result.msg}\n` +
						                     `similarity: ${result.similarity}\n` +
						                     `liveness: ${result.liveness}`;
						console.log("【faceVerify】: ***"+this.faceSDKResult);
					})
			},


			/**
			 * 3. 活体检测，包含动作+炫彩活体 。静默活体默认都包含
			 * Silent liveness threshold (iOS/Android): 0.85–0.95
			 */
			livenessVerifyDemo: function() {
				livenessVerify(
					1,      // 1.动作活体  2.动作+炫彩活体 3.炫彩活体(不能强光环境使用) 4.静默活体 
					"1,2,3,4,5", //动作活体种类用英文","隔开； 1.张张嘴 2.微笑 3.眨眨眼 4.摇头 5.点头
					7,     //动作活体超时时间 
					2,     //动作活体步骤个数
					true,     //ALLOW_MULTI_FACES 是否允许多人脸入镜(仅Android)
					(result) => {
						uni.showToast({title: result.msg, icon: 'none',duration: 2000 });
						//Silent liveness threshold (iOS/Android): 0.85–0.95
						this.faceSDKResult = `code: ${result.code}\n` +
						                     `msg: ${result.msg}\n` +
						                     `liveness: ${result.liveness}\n` +
						                     `faceBase64: ${result.faceBase64.length}`;
						console.log("【livenessVerify】: ***"+this.faceSDKResult);
						
					})
			},



			/**
			 * 4. 1:1人脸识别检测是否本地有faceID人脸特征值
			 */
			getFaceFeatureDemo: function() {
				getFaceFeature(
					this.faceID,
					(result) => {
						this.faceSDKResult = "OK：" + JSON.stringify(result)
					})
			},

			/**
			 * 5. 演示同步1:1人脸特征值到SDK
			 */
			insertFaceFeatureDemo: function() {
				insertFaceFeature(
					this.faceID,
					this.faceFeature,
					(result) => {
						this.faceSDKResult = JSON.stringify(result)
					})
			},
			
			//6. 仅仅用于Android 提取人脸图片中的特征值。不建议通过此方式录入人脸特征
			// 人脸图需要遵守规范：https://i.postimg.cc/RCwNy0kV/add-Face.jpg
			addFaceFeatureByImageDemo: function () {
				addFaceBySDKImage(
					this.faceID,
					this.base64FaceImage,
					 (result)  => {
						this.faceSDKResult =JSON.stringify(result)
					})
			},
			
			/**
			* 7. 切换摄像头仅仅支持Android，一般0是前置，1是后置。
			* 但是部分Android自定义设备可能不是很标准
			*/
			switchCameraDemo: function () {
				switchCamera(1)
			},
			
			/**
			* 8. 删除人脸特征信息
			*/
			deleteFaceFeatureDemo: function () {
				deleteFaceFeature(this.faceID)
			},

		}

	}
</script>

<style>
@import url("./index.css");
</style>
