import {OnMemoryInfoChange,OffMemoryInfoChange,GetMemoryInfo} from '../interface.uts'

// 开启内存监听
export const onMemoryInfoChange:OnMemoryInfoChange = function(callback: (res: Array<number>) => void){
	MemoryInfoNative.onMemoryInfoChangeKotlin(function(res:kotlin.Array<number>){
		callback(Array.fromNative(res))
	})
}

export const offMemoryInfoChange:OffMemoryInfoChange = function(){
	MemoryInfoNative.offMemoryInfoChangeKotlin()
}


export const getMemoryInfo:GetMemoryInfo = function():Array<number>{
	let kotlinArray = MemoryInfoNative.getMemInfoKotlin()
	return UTSArray.fromNative(kotlinArray)
}


