import SwiftUI
import UIKit
import FaceAISDK_Core

@objcMembers
public class FaceSDKSwiftManager: NSObject {
    
    // 临时操作的图片转Base64 编码
    public static func getFaceImageBase64(_ faceName: String) -> String {
        guard let faceImageBase64 = FaceImageManger.faceImageToBase64(fileName: faceName) else {
            print("❌ [Swift] getFaceImageBase64 failed")
            return ""
        }
        return faceImageBase64
    }
    
    // 获取并校验人脸特征值 (同步)
    public static func getFaceFeature(_ faceID: String) -> String {
        guard let faceFeature = UserDefaults.standard.string(forKey: faceID) else {
            print("❌ [Swift] getFaceFeature: No data found for \(faceID)")
            return ""
        }
        
        //只判断是否为空，具体合法性应由核心算法校验
        if faceFeature.isEmpty {
            print("❌ [Swift] getFaceFeature: Invalid Feature (Empty)")
            return ""
        }
        
        return faceFeature
    }
    
    // faceID 对应的人脸特征是否存在？
    public static func isFaceFeatureExist(_ faceID: String,
                                          _ callback: @escaping (NSNumber) -> Void) {
        guard let faceFeature = UserDefaults.standard.string(forKey: faceID),
              !faceFeature.isEmpty else { 
            print("isFaceFeatureExist? : No or Invalid feature!")
            callback(0)
            return
        }
        print("\n😊FaceFeature Exist: OK")
        callback(1)
    }
	
	// faceID 对应的人脸特征是否存在？
	public static func deleteFaceFeature(_ faceID: String) {
        UserDefaults.standard.set(nil, forKey: faceID)
        UserDefaults.standard.synchronize()
	}
    
    /**
     * 同步人脸特征到 SDK
     */
    public static func insertFaceFeature(_ faceID: String,
                                         _ faceFeature: String,
                                         _ callback: @escaping (NSNumber) -> Void) {
        guard !faceFeature.isEmpty, faceFeature.count >= 1024 else {
            if faceFeature.isEmpty {
                print("FaceAISDK: 插入失败，特征值不能为空")
            } else {
                print("FaceAISDK: 插入失败，特征值长度不足")
            }
            callback(0)
            return
        }
        
        UserDefaults.standard.set(faceFeature, forKey: faceID)
        UserDefaults.standard.synchronize()
        
        print("FaceAISDK: 特征值插入成功 (FaceID: \(faceID))")
        callback(1)
    }

    // MARK: - 1:1 人脸识别 
    public static func showFaceVerify(_ faceID: String,
                                      _ threshold: NSNumber,
                                      _ livenessType: NSNumber,
                                      _ motionLivenessTypes: String,
                                      _ motionLivenessTimeOut : NSNumber,
                                      _ motionLivenessSteps : NSNumber,
                                      // 修改：增加 similarity 和 liveness 回调参数
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
    
    // MARK: - 人脸采集 (更新版)
    public static func showAddFaceByCamera(_ faceID: String,
                                               _ mode: NSNumber,
                                               _ showConfirm: Bool,
                                               // 修改 1：将 NSString 改为 String
                                               _ callback: @escaping (NSNumber, String) -> Void) {
            DispatchQueue.main.async {
                guard let topVC = self.getTopViewController() else { return }
                ScreenBrightnessHelper.shared.maximizeBrightness()
                
                var sdkView = AddFaceByCamera(
                    faceID: faceID,
                    onDismiss: { [weak topVC] (resultCode: Int, feature: String?) in
                        DispatchQueue.main.async {
                            ScreenBrightnessHelper.shared.restoreBrightness()
                            topVC?.dismiss(animated: true) {
                                let safeFeature = feature ?? ""
                                // 修改 2：直接传入 safeFeature，不再包一层 NSString()
                                callback(NSNumber(value: resultCode), safeFeature)
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