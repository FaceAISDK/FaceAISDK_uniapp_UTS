import SwiftUI
import UIKit
import FaceAISDK_Core

@objcMembers
public class FaceSDKSwiftManager: NSObject {
	
	
	// 临时操作的图片转Base64 编码
	public static func getFaceImageBase64(_ faceName: String) -> String {
	    guard let faceImageBase64 = FaceImageManger.faceImageToBase64(fileName: faceName) else {
	        print("❌ getFaceImageBase64 failed")
	        return ""
	    }
	    	    
	    return faceImageBase64
	}
	
        
    // 获取并校验人脸特征值 (同步)
    public static func getFaceFeature(_ faceID: String) -> String {
        guard let faceFeature = UserDefaults.standard.string(forKey: faceID) else {
            print("❌ [Swift] isFaceFeatureExist: No data found for \(faceID)")
            return ""
        }
        
        if faceFeature.count != 1024 {
            print("❌ [Swift] isFaceFeatureExist: Invalid Length! Current: \(faceFeature.count), Expected: 1024")
            return ""
        }
        
        // print("✅ [Swift] isFaceFeatureExist: OK (Length 1024)")
        return faceFeature
    }
    
    // faceID 对应的人脸特征是否存在？
    public static func isFaceFeatureExist(_ faceID: String,
                                          _ callback: @escaping (NSNumber) -> Void) {
        guard let faceFeature = UserDefaults.standard.string(forKey: faceID),
              faceFeature.count == 1024 else {
            print("isFaceFeatureExist? : No or Invalid Length!")
            callback(0)
            return
        }
        print("\n😊FaceFeature (Length 1024): OK")
        callback(1)
    }
    
    // 同步人脸特征到SDK
    public static func insertFaceFeature(_ faceID: String,
                                         _ faceFeature: String,
                                         _ callback: @escaping (NSNumber) -> Void) {
        guard !faceFeature.isEmpty, faceFeature.count == 1024 else {
            print("FaceAISDK: 特征值无效，插入失败 (Length: \(faceFeature.count))")
            callback(0)
            return
        }
        UserDefaults.standard.set(faceFeature, forKey: faceID)
        callback(1)
    }


    // MARK: - 1:1 人脸识别 
    public static func showFaceVerify(_ faceID: String,
                                          _ threshold: NSNumber,
                                          _ livenessType: NSNumber,
                                          _ motionLivenessTypes: String,
                                          _ motionLivenessTimeOut : NSNumber,
                                          _ motionLivenessSteps : NSNumber,
                                          _ callback: @escaping (NSNumber) -> Void) {
          DispatchQueue.main.async {
                guard let topVC = getTopViewController() else { return }
                
                 ScreenBrightnessHelper.shared.maximizeBrightness()
                
                var hostingController: UIHostingController<VerifyFaceView>? = nil
                
                var sdkView = VerifyFaceView(
                    faceID: faceID,
                    threshold: threshold.floatValue,
                    livenessType: livenessType.intValue,
                    motionLiveness: motionLivenessTypes,
                    motionLivenessTimeOut: motionLivenessTimeOut.intValue,
                    motionLivenessSteps: motionLivenessSteps.intValue,
                    onDismiss: { (resultCode: Int) in
                        DispatchQueue.main.async {
                            ScreenBrightnessHelper.shared.restoreBrightness()
                            hostingController?.dismiss(animated: true) {
                                callback(NSNumber(value: resultCode))
                            }
                        }
                    }
                )
                
                sdkView.autoControlBrightness = false
                
                hostingController = UIHostingController(rootView: sdkView)
                hostingController?.modalPresentationStyle = .fullScreen
                topVC.present(hostingController!, animated: true)
            }
        }
    
        //  活体检测 
        public static func showLivenessVerify(_ livenessType: NSNumber,
                                              _ motionLivenessTypes: String,
                                              _ motionLivenessTimeOut : NSNumber,
                                              _ motionLivenessSteps : NSNumber,
                                              _ callback: @escaping (NSNumber) -> Void) {
            DispatchQueue.main.async {
                guard let topVC = getTopViewController() else { return }
                
                ScreenBrightnessHelper.shared.maximizeBrightness()
                
                var hostingController: UIHostingController<LivenessDetectView>? = nil
                
                var sdkView = LivenessDetectView(
                    livenessType: livenessType.intValue,
                    motionLiveness: motionLivenessTypes,
                    motionLivenessTimeOut: motionLivenessTimeOut.intValue,
                    motionLivenessSteps: motionLivenessSteps.intValue,
                    onDismiss: { (resultCode: Int) in
                        DispatchQueue.main.async {
                            ScreenBrightnessHelper.shared.restoreBrightness()
                            hostingController?.dismiss(animated: true) {
                                callback(NSNumber(value: resultCode))
                            }
                        }
                    }
                )
                
                sdkView.autoControlBrightness = false
                
                hostingController = UIHostingController(rootView: sdkView)
                hostingController?.modalPresentationStyle = .fullScreen
                topVC.present(hostingController!, animated: true)
            }
        }
        
        // MARK: - 人脸采集 (更新版)
        public static func showAddFaceByCamera(_ faceID: String,
                                               _ mode: NSNumber,
                                               _ showConfirm: Bool,
                                               _ callback: @escaping (NSNumber) -> Void) {
            DispatchQueue.main.async {
                guard let topVC = getTopViewController() else { return }
                
            
                ScreenBrightnessHelper.shared.maximizeBrightness()
                
                var hostingController: UIHostingController<AddFaceByCamera>? = nil
                
                var sdkView = AddFaceByCamera(
                    faceID: faceID,
                    onDismiss: { (resultCode: Int) in
                        DispatchQueue.main.async {
                            ScreenBrightnessHelper.shared.restoreBrightness()
                            
                            hostingController?.dismiss(animated: true) {
                                callback(NSNumber(value: resultCode))
                            }
                        }
                    }
                )
                
                sdkView.autoControlBrightness = false
        
                hostingController = UIHostingController(rootView: sdkView)
                hostingController?.modalPresentationStyle = .fullScreen
                topVC.present(hostingController!, animated: true)
            }
        }
    
        // MARK: - 辅助方法
        private static func getTopViewController() -> UIViewController? {
            let keyWindow = UIApplication.shared.connectedScenes
                .filter { $0.activationState == .foregroundActive }
                .compactMap { $0 as? UIWindowScene }
                .first?.windows
                .filter { $0.isKeyWindow }.first
                ?? UIApplication.shared.keyWindow
            
            guard let rootVC = keyWindow?.rootViewController else { return nil }
            var topController = rootVC
            while let presentedViewController = topController.presentedViewController {
                topController = presentedViewController
            }
            return topController
        }
}
