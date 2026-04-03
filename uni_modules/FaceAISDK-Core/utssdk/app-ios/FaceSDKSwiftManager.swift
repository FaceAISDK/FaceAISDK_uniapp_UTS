import SwiftUI
import UIKit
import FaceAISDK_Core
import Combine 

@objcMembers
public class FaceSDKSwiftManager: NSObject {
    
    private static var addFaceTasks: [String: Any] = [:]

    // MARK: - Base64 提取人脸特征
    @objc public static func addFaceByBase64(_ faceID: String, _ base64Str: String, _ callback: @escaping (NSNumber, String, String) -> Void) {
        
        var cleanBase64 = base64Str
        if let idx = cleanBase64.range(of: "base64,")?.upperBound {
            cleanBase64 = String(cleanBase64[idx...])
        }
        
        guard let data = Data(base64Encoded: cleanBase64, options: .ignoreUnknownCharacters),
              let image = UIImage(data: data) else {
            callback(0, "", "图片Base64解析失败") 
            return
        }
        
        let model = AddFaceByImageModel() 
        let taskID = UUID().uuidString
        var cancellables = Set<AnyCancellable>()
        
        Self.addFaceTasks[taskID] = model // 保持强引用防释放
        
        model.$readyConfirmFace
            .receive(on: DispatchQueue.main)
            .sink { isReady in
                if isReady {
                    if let feature = model.getFaceFeature(faceUIImage: model.croppedFaceImage) {
                        // 强制写入磁盘
                        UserDefaults.standard.set(feature, forKey: faceID)
                        UserDefaults.standard.synchronize() 
                        
                        let safeFeature = String(feature)
                        callback(1, safeFeature, "")
                    } else {
                        callback(0, "", "未能提取到特征")
                    }
                    Self.addFaceTasks.removeValue(forKey: taskID)
                }
            }
            .store(in: &cancellables)
    }
    
    // MARK: - 呼出相机录入人脸
    // 注意：这里的 callback 参数严格定义为 (NSNumber, String)
    // 将 _ performanceMode: Int 改为 _ performanceMode: NSNumber
    @objc public static func showAddFaceByCamera(_ faceID: String, _ performanceMode: NSNumber, _ needConfirm: Bool, _ callback: @escaping (NSNumber, String) -> Void) {
        
        guard let topVC = getTopViewController() else {
            callback(0, "")
            return
        }

        let sdkView = AddFaceByCamera(
            faceID: faceID,
            addFacePerformanceMode: performanceMode.intValue, 
            needShowConfirmDialog: needConfirm,
            onDismiss: { [weak topVC] (resultCode: Int, feature: String?) in
                // 在 Swift 端提前安全解包，保证传给 UTS 的绝对是纯净的 String
                let safeFeature = String(feature ?? "")
                let safeCode = NSNumber(value: resultCode)
                
                DispatchQueue.main.async {
                    ScreenBrightnessHelper.shared.restoreBrightness()
                    topVC?.dismiss(animated: true) {
                         callback(safeCode, safeFeature)
                    }
                }
            }
        )
        sdkView.autoControlBrightness = false

        let hostingController = UIHostingController(rootView: sdkView)
        hostingController.modalPresentationStyle = .fullScreen
        topVC.present(hostingController, animated: true)
    }

    // 临时操作的图片转Base64 编码
    public static func getFaceImageBase64(_ faceName: String) -> String {
        guard let faceImageBase64 = FaceImageManger.faceImageToBase64(fileName: faceName) else {
            print("❌ [Swift] getFaceImageBase64 failed")
            return ""
        }
        return faceImageBase64
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
	



    // MARK: - 特征值管理 (核心修复点)
    // 强制返回 String 而不是 String?，避免 UTS 编译成 Swift 时抛出 unwrapped 错误
    @objc public static func getFaceFeature(_ faceID: String) -> String {
        return UserDefaults.standard.string(forKey: faceID) ?? ""
    }
    
    @objc public static func isFaceFeatureExist(_ faceID: String, _ callback: @escaping (NSNumber) -> Void) {
        let exists = UserDefaults.standard.string(forKey: faceID) != nil
        callback(NSNumber(value: exists ? 1 : 0))
    }
    
    @objc public static func deleteFaceFeature(_ faceID: String) {
        UserDefaults.standard.removeObject(forKey: faceID)
        UserDefaults.standard.synchronize()
    }
    
    @objc public static func insertFaceFeature(_ faceID: String, _ feature: String, _ callback: @escaping (NSNumber) -> Void) {
        UserDefaults.standard.set(feature, forKey: faceID)
        UserDefaults.standard.synchronize()
        callback(NSNumber(value: 1))
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