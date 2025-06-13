# uni-FaceAISDK
### 开发文档

此插件以[原生混编](https://doc.dcloud.net.cn/uni-app-x/plugin/uts-plugin-hybrid.html)的方式实现了 Android/IOS 同步/异步 获取设备内存使用信息的功能。

使用示例:

```vue
<template>
	<view>
		<button @tap="kotlinMemGetTest">通过kotlin获取内存(同步)</button>
		<button @tap="kotlinMemListenTest">kotlin监听内存并持续回调UTS</button>
		<button @tap="kotlinStopMemListenTest">停止监听</button>
		<text>{{memInfo}}</text>
	</view>
</template>

<script>
	
	import { offFaceAISDKChange,onFaceAISDKChange,getFaceAISDK} from "@/uni_modules/uni-FaceAISDK";
	 
	export default {
		data() {
			return {
				memInfo: '-'
			}
		},
		onLoad() {

		},
		methods: {
			
			kotlinMemGetTest:function () {
			    let array = getMemoryInfo()
				this.memInfo = "可用内存:" + array[0] + "MB--整体内存:" + array[1] + "MB"
			},
			kotlinMemListenTest: function () {
				onMemoryInfoChange((res: Array<number>) => {
					this.memInfo = "可用内存:" + res[0] + "MB--整体内存:" + res[1] + "MB"
				})
			},
			
			kotlinStopMemListenTest:function () {
			    offMemoryInfoChange()
				this.memInfo = "已暂停"
			},
		}
	}
</script>

```
