<template>
	<view>
		<button @tap="addFaceFeatureBySDKCameraDemo">SDK相机录入人脸</button>
		<button @tap="faceVerifyDemo">人脸识别+活体检测</button>
		<button @tap="livenessVerifyDemo">炫彩｜动作 活体检测</button>
		<button @tap="getFaceFeatureDemo">获取人脸特征信息</button>
		<button @tap="insertFaceFeatureDemo">同步人脸特征信息</button>
		<view class="result-box">
			<view> Email: FaceAISDK.Service@gmail.com</view>
			<scroll-view scroll-y="true" class="scroll-view-box">
				<text class="text-content">{{faceAIResult}}</text>
			</scroll-view>
		</view>
	</view>
</template>

<script>
	// 注意：在普通 UniApp 中，需要确保该插件支持 JS 端的调用方式
	// 请修改manifest.json 中vueVersion字段切换uniapp/uniapp X，重新打自定义基座包生效
	import {
		addFaceBySDKCamera,
		faceVerify,
		livenessVerify,
		getFaceFeature,
		insertFaceFeature
	} from "@/uni_modules/FaceAISDK-Core";

	export default {
		data() {
			return {
				faceID: '18812345678',
				motionLivenessType: '1,2,3,4,5',
				faceFeature: 'faceFeature，1024 length',
				faceAIResult: 'faceAIResult',
				base64FaceImage: ''
			}
		},
		onLoad() {

		},

		methods: {

			/**
			 * 1. 1:1 人脸识别调用SDK相机录入人脸特征值
			 * */
			addFaceFeatureBySDKCameraDemo: function() {
				// 转换说明：移除了 (result: ResultJSON) 中的类型注解
				addFaceBySDKCamera(
					this.faceID,
					1, //1.快速模式，  2.精确模式(人脸品质高)
					true, //是否需要显示确认框，强烈建议需要
					(result) => {
						//录入的人脸
						this.faceAIResult = JSON.stringify(result)
					})
			},



			/**
			 * 2. 「1:1人脸识别」校验是否当前用户
			 */
			faceVerifyDemo: function() {
				faceVerify(
					this.faceID,
					0.85, // 阈值设置，范围限 [0.75,0.95] ,默认0.85
					1,    // 1.动作活体  2.动作+炫彩活体 3.炫彩活体(不能强光环境使用)
					"1,2,3,4,5", //动作活体种类用英文","隔开； 1.张张嘴 2.微笑 3.眨眨眼 4.摇头 5.点头
					7,    //动作活体超时时间共7秒
					2,    //动作活体步骤，2个
					(result) => {
						this.faceAIResult = JSON.stringify(result)
					})
			},


			/**
			 * 3. 活体检测，包含动作+炫彩活体
			 */
			livenessVerifyDemo: function() {
				livenessVerify(
					2, // 1.动作活体  2.动作+炫彩活体 3.炫彩活体(不能强光环境使用)
					"1,2,3,4,5", //动作活体种类用英文","隔开； 1.张张嘴 2.微笑 3.眨眨眼 4.摇头 5.点头
					5, //动作活体超时时间共5秒
					2, //动作活体步骤，2个
					(result) => {
						this.faceAIResult = JSON.stringify(result)
					})
			},



			/**
			 * 4. 1:1人脸识别检测是否本地有faceID人脸特征值
			 */
			getFaceFeatureDemo: function() {
				getFaceFeature(
					this.faceID,
					(result) => {
						this.faceAIResult = "OK：" + JSON.stringify(result)
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
						this.faceAIResult = JSON.stringify(result)
					})
			},

		}

	}
</script>

<style>
	/* 给滚动区域一个固定高度和边框 */
	.result-box {
		margin: 20rpx;
	}

	.scroll-view-box {
		height: 400rpx;
		/* 必须指定高度，否则无法滚动 */
		border: 1px solid #ccc;
		border-radius: 10rpx;
		background-color: #f8f8f8;
		padding: 15rpx;
		box-sizing: border-box;
		/* 确保padding不撑大宽高 */
	}

	.text-content {
		font-size: 28rpx;
		color: #fc0280;
		/* word-break: break-all; 关键：解决长JSON字符串不换行的问题 */
		white-space: pre-wrap;
		/* 保留格式并自动换行 */
	}
</style>

<style>
	.gray-button {
		background-color: #ffffff;
		/* 灰色背景 */
		color: #800080;
		/* 深灰色文字 */
		/* 如果需要，可以覆盖默认的边框 */
		border: none;
	}
</style>