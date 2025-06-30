
// 这里是原生库的引用
import Foundation
// UTS内置对象的引用
import DCloudUTSFoundation

public class FaceAISDKNative {
  
  
  /// 记录上一次的任务id
  static private var lastTaskId = -1
   
   /// 开启内存监控
  static func onMemoryInfoChangeSwift(_ callback: @escaping (_ res: [Int]) -> Void) {
       
       if lastTaskId != -1 {
           // 避免重复开启
           clearInterval(NSNumber.from(lastTaskId))
       }
       
       lastTaskId = setInterval({ 
           let freeMem = FaceAISDKNative.getFreeMemory()
           let totalMem = FaceAISDKNative.getTotalMemory()
           console.log(freeMem, totalMem)
           callback([freeMem, totalMem])
       }, 2000).toInt()
   }
   
  
  
    /// 同步获取内存信息
    static func getMemInfoSwift() -> [Int] {
        let freeMem = FaceAISDKNative.getFreeMemory()
        let totalMem = FaceAISDKNative.getTotalMemory()
        
        // freeMem 可用内存，单位MB
        // totalMem 设备内存，单位MB
        console.log(freeMem, totalMem)
        return [freeMem, totalMem]
    }
    

    /// 关闭内存监控
    static func offMemoryInfoChangeSwift() {
        if lastTaskId != -1 {
            clearInterval(NSNumber.from(lastTaskId))
			lastTaskId = -1
        }
    }
}

// MARK: - 获取内存工具函数
extension FaceAISDKNative {
    
    /// 获取总内存大小（以MB为单位）
    /// - Returns: 设备总内存
    static func getTotalMemory() -> Int {
        return Int(ProcessInfo.processInfo.physicalMemory / 1024 / 1024)
    }

    
    /// 获取可用内存大小（以MB为单位）
    /// - Returns: 设备可用内存
    static func getFreeMemory() -> Int {
        var vmStats = vm_statistics_data_t()
        var infoCount = mach_msg_type_number_t(MemoryLayout<vm_statistics_data_t>.size / MemoryLayout<integer_t>.size)
        let kernReturn = withUnsafeMutablePointer(to: &vmStats) {
            $0.withMemoryRebound(to: integer_t.self, capacity: Int(infoCount)) {
                host_statistics(mach_host_self(), HOST_VM_INFO, $0, &infoCount)
            }
        }
        
        if kernReturn != KERN_SUCCESS {
            return 0
        }
        
        let vmPageSize = vm_page_size
        let freeMemorySize = Int(vmPageSize) * Int(vmStats.free_count + vmStats.inactive_count)
        return freeMemorySize / 1024 / 1024
    }
}

