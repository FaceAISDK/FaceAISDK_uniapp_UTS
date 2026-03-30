import SwiftUI
import UIKit
import FaceAISDK_Core
import Combine 

@objcMembers
public class FaceSDKSwiftManager: NSObject {
	
	
	
	private static var addFaceTasks: [String: Any] = [:]
	
	@objc public static func addFaceByBase64(_ faceID: String, _ base64Str: String, _ callback: @escaping (NSNumber, String) -> Void) {
	    
	    // 1. 剥离可能存在的 Base64 前缀
	    var cleanBase64 = base64Str
	    if let idx = cleanBase64.range(of: "base64,")?.upperBound {
	        cleanBase64 = String(cleanBase64[idx...])
	    }
	    
	    // 2. 将 Base64 转换为 UIImage
	    guard let data = Data(base64Encoded: cleanBase64, options: .ignoreUnknownCharacters),
	          let image = UIImage(data: data) else {
	        callback(0, "") // 0 表示失败
	        return
	    }
	    
	    //  3. 复用 AddFaceByImageModel 作为桥梁
	    let model = AddFaceByImageModel() //
	    let taskID = UUID().uuidString
	    var cancellables = Set<AnyCancellable>()
	    
	    // 清理内存的闭包
	    let cleanup = {
	        Self.addFaceTasks.removeValue(forKey: taskID)
	    }
	    
	    // 4. 监听检测成功状态
	    model.$readyConfirmFace
	        .receive(on: DispatchQueue.main)
	        .sink { isReady in
	            if isReady {
	                // 成功获取对齐图后，调用公开方法提取特征值
	                if let feature = model.getFaceFeature(faceUIImage: model.croppedFaceImage) {
	                    // 保存特征值到本地 (与之前的逻辑保持一致)
	                    UserDefaults.standard.set(feature, forKey: faceID)
	                    callback(1, feature)
	                } else {
	                    callback(0, "")
	                }
	                cleanup() // 执行完毕，清理内存
	            }
	        }
	        .store(in: &cancellables)
	    
	    // 5. 监听异常或失败状态 (例如：未检测到人脸、人脸太小等)
	    model.$sdkInterfaceTips
	        .receive(on: DispatchQueue.main)
	        .sink { tips in
	            // 判断逻辑：如果既不是初始干净状态，也不是准备确认状态，说明遇到了报错拦截[cite: 5]
	            if tips.code != FaceTipsCode.CLEAN_TIPS && tips.code != FaceTipsCode.CONFIRM_ADD_FACE {
	                print("❌ [Swift] AddFaceByBase64 Failed: \(tips.message)")
	                callback(0, "")
	                cleanup() // 执行完毕，清理内存
	            }
	        }
	        .store(in: &cancellables)
	    
	    //  6. 将任务存入静态字典，防止 model 和 cancellables 随作用域结束被销毁
	    Self.addFaceTasks[taskID] = (model, cancellables)
	    
	    // 7. 触发底层的检测逻辑
	    model.addFaceByUIImage(faceUIImage: image) //[cite: 5]
	}
	    
	
	
    
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