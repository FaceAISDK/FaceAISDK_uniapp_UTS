import SwiftUI
import UIKit
import FaceAISDK_Core
import Combine 

@objcMembers
public class FaceSDKSwiftManager: NSObject {
    


    // MARK: - Base64 提取人脸特征
    public static func addFaceByBase64(_ faceID: String,
                                       _ base64Str: String, 
                                       _ callback: @escaping (NSNumber, String, String) -> Void) {
        
        var cleanBase64 = base64Str
        if let idx = cleanBase64.range(of: "base64,")?.upperBound {
            cleanBase64 = String(cleanBase64[idx...])
        }
        
        // 1. 解析 Base64 为 UIImage
        guard let data = Data(base64Encoded: cleanBase64, options: .ignoreUnknownCharacters),
              let image = UIImage(data: data) else {
            callback(0, "", "图片Base64解析失败") 
            return
        }
        
        // 2. 实例化 Model
        let model = AddFaceByImageModel() 
          
        // 3. 直接传入解析好的 image 进行特征提取 (移除冗余的 Combine 监听)
        // 假设 model.getFaceFeature 支持直接传入 UIImage 并同步/快速返回特征值
        if let feature = model.getFaceFeature(faceUIImage: image) {
            
            // 提取成功，存入本地
            UserDefaults.standard.set(feature, forKey: faceID)
            UserDefaults.standard.synchronize() 
            
            // 回调成功状态和特征值
            callback(1, String(feature), "Base64 img 提取人脸信息成功")
        } else {
            // 提取失败（可能是图片中没有人脸、模糊等原因）
            callback(0, "", "未能提取到人脸特征，请确保图片清晰且包含人脸")
        }
    }


    // MARK: - 呼出相机录入人脸
    public static func showAddFaceByCamera(_ faceID: String,
                                           _ performanceMode: NSNumber,
                                           _ needConfirm: Bool, 
                                           _ callback: @escaping (NSNumber, String) -> Void) {
        
        // ✅ 核心修复：确保所有 UI 相关的初始化和跳转都在主线程执行
        DispatchQueue.main.async {
            guard let topVC = self.getTopViewController() else {
                callback(0, "topVC nil")
                return
            }

            var sdkView = AddFaceByCamera(
                faceID: faceID,
                addFacePerformanceMode: performanceMode.intValue, 
                needShowConfirmDialog: needConfirm,
                onDismiss: { [weak topVC] (resultCode: Int, feature: String) in
                    
                    let safeCode = NSNumber(value: resultCode)
                    
                    DispatchQueue.main.async {
                        ScreenBrightnessHelper.shared.restoreBrightness()
                        topVC?.dismiss(animated: true) {
                             callback(safeCode, feature)
                        }
                    }
                }
            )
            sdkView.autoControlBrightness = false

            let hostingController = UIHostingController(rootView: sdkView)
            hostingController.modalPresentationStyle = .fullScreen
            topVC.present(hostingController, animated: true)
        }
    }
	
	// MARK: - 1:1 人脸识别
	public static func showFaceVerify(_ faceID: String,
	                                  _ threshold: NSNumber,
	                                  _ livenessType: NSNumber,
	                                  _ motionLivenessTypes: String,
	                                  _ motionLivenessTimeOut : NSNumber,
	                                  _ motionLivenessSteps : NSNumber,
	                                  _ callback: @escaping (NSNumber, NSNumber, NSNumber) -> Void) {
	    DispatchQueue.main.async {
	        guard let topVC = self.getTopViewController() else { return }
	        ScreenBrightnessHelper.shared.maximizeBrightness()
	        
	        var sdkView = VerifyFaceView(
	            faceID: faceID,
	            threshold: threshold.floatValue,
	            livenessType: livenessType.intValue,
	            motionLiveness: motionLivenessTypes,
	            motionLivenessTimeOut: motionLivenessTimeOut.intValue,
	            motionLivenessSteps: motionLivenessSteps.intValue,
	            // 修改：接收新增的参数
	            onDismiss: { [weak topVC] (resultCode: Int, similarity: Float, liveness: Float) in
	                DispatchQueue.main.async {
	                    ScreenBrightnessHelper.shared.restoreBrightness()
	                    topVC?.dismiss(animated: true) {
	                        // 修改：回传新增的参数
	                        callback(NSNumber(value: resultCode), NSNumber(value: similarity), NSNumber(value: liveness))
	                    }
	                }
	            }
	        )
	        sdkView.autoControlBrightness = false
	        
	        let hostingController = UIHostingController(rootView: sdkView)
	        hostingController.modalPresentationStyle = .fullScreen
	        topVC.present(hostingController, animated: true)
	    }
	}
	
	// MARK: - 活体检测 
	public static func showLivenessVerify(_ livenessType: NSNumber,
	                                      _ motionLivenessTypes: String,
	                                      _ motionLivenessTimeOut : NSNumber,
	                                      _ motionLivenessSteps : NSNumber,
	                                      _ callback: @escaping (NSNumber, NSNumber) -> Void) {
	    DispatchQueue.main.async {
	        guard let topVC = self.getTopViewController() else { return }
	        ScreenBrightnessHelper.shared.maximizeBrightness()
	        
	        var sdkView = LivenessDetectView(
	            livenessType: livenessType.intValue,
	            motionLiveness: motionLivenessTypes,
	            motionLivenessTimeOut: motionLivenessTimeOut.intValue,
	            motionLivenessSteps: motionLivenessSteps.intValue,
	            // 修改：接收 liveness 参数
	            onDismiss: { [weak topVC] (resultCode: Int, liveness: Float) in
	                DispatchQueue.main.async {
	                    ScreenBrightnessHelper.shared.restoreBrightness()
	                    topVC?.dismiss(animated: true) {
	                        // 修改：回传 liveness 参数
	                        callback(NSNumber(value: resultCode), NSNumber(value: liveness))
	                    }
	                }
	            }
	        )
	        sdkView.autoControlBrightness = false
	        
	        let hostingController = UIHostingController(rootView: sdkView)
	        hostingController.modalPresentationStyle = .fullScreen
	        topVC.present(hostingController, animated: true)
	    }
	}
	

    // 临时操作的图片转Base64 编码
    public static func getFaceImageBase64(_ faceName: String) -> String {
        guard let faceImageBase64 = FaceImageManger.faceImageToBase64(fileName: faceName) else {
            print("❌ [Swift] getFaceImageBase64 failed")
            return ""
        }
        return faceImageBase64
    }


    // MARK: - 特征值管理 (核心修复点)
    // 强制返回 String 而不是 String?，避免 UTS 编译成 Swift 时抛出 unwrapped 错误
   public static func getFaceFeature(_ faceID: String) -> String {
        return UserDefaults.standard.string(forKey: faceID) ?? "vue2 error?"
    }
    
    public static func isFaceFeatureExist(_ faceID: String, _ callback: @escaping (NSNumber) -> Void) {
        let exists = UserDefaults.standard.string(forKey: faceID) != nil
        callback(NSNumber(value: exists ? 1 : 0))
    }
    
    public static func deleteFaceFeature(_ faceID: String) {
        UserDefaults.standard.removeObject(forKey: faceID)
        UserDefaults.standard.synchronize()
    }
    
    // 插入人脸特征值：增加长度判断拦截
    public static func insertFaceFeature(_ faceID: String, _ feature: String, _ callback: @escaping (NSNumber) -> Void) {
        
        // 1. 拦截：判断字符串是否为空，或者长度是否小于 1024
        // 特征值通常是加密后的超长字符串，如果太短说明数据不完整
        if feature.isEmpty || feature.count < 1024 {
            print("【FaceSDK】插入失败：特征值长度不足 (\(feature.count))")
            callback(NSNumber(value: 0)) // 返回 0 表示失败
            return
        }

        // 2. 校验通过，执行存储
        UserDefaults.standard.set(feature, forKey: faceID)
        UserDefaults.standard.synchronize()
        
        print("【FaceSDK】人脸特征值插入成功，ID: \(faceID)")
        callback(NSNumber(value: 1)) // 返回 1 表示成功
    }
    
    // MARK: - 辅助方法
    private static func getTopViewController() -> UIViewController? {
        let windowScene = UIApplication.shared.connectedScenes
            .first { $0.activationState == .foregroundActive } as? UIWindowScene
        
        guard let keyWindow = windowScene?.windows.first(where: { $0.isKeyWindow }),
              let rootVC = keyWindow.rootViewController else {
            return nil
        }
        
        var topController = rootVC
        while let presentedViewController = topController.presentedViewController {
            topController = presentedViewController
        }
        return topController
    }
}