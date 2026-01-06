import SwiftUI
import UIKit
import FaceAISDK_Core 

@objcMembers
public class FaceSDKSwiftManager: NSObject {
	
	
	
    // MARK: - 获取并校验人脸特征值 (同步方法)
    // UTS 调用: let feature = SwiftUIManager.getFaceFeature("yourFaceID")
    public static func getFaceFeature(_ faceID: String) -> String {
        
        // 1. 尝试从本地存储获取
        guard let faceFeature = UserDefaults.standard.string(forKey: faceID) else {
            print("❌ [Swift] isFaceFeatureExist: No data found for \(faceID)")
            return "" // 不存在，返回空
        }
        
        // 2. 校验长度 (必须严格等于 1024)
        if faceFeature.count != 1024 {
            print("❌ [Swift] isFaceFeatureExist: Invalid Length! Current: \(faceFeature.count), Expected: 1024")
            // 如果长度不对，视为无效，返回空字符串（或者你可以决定是否要容错）
            return "" 
        }
        
        // 3. 成功，返回特征值
        print("✅ [Swift] isFaceFeatureExist: OK (Length 1024)")
        return faceFeature
    }
	
	
	
    // faceID 对应的人脸特征是否存在？ 长度1024
    public static func isFaceFeatureExist(_ faceID: String,
                                      _ callback: @escaping (NSNumber) -> Void) {
        
        // 同时判断【不为空】且【长度严格等于1024】
        guard let faceFeature = UserDefaults.standard.string(forKey: faceID),
              faceFeature.count == 1024 else {
            
            print("isFaceFeatureExist? : No or Invalid Length!")
            callback(0) // 不存在或格式错误，返回 0
            return
        }
        
        print("\n😊FaceFeature (Length 1024): OK")
        callback(1) // 校验通过，返回 1
    }
	
	
    // 同步人脸特征到SDK
    public static func insertFaceFeature(_ faceID: String,
                                         _ faceFeature: String,
                                         _ callback: @escaping (NSNumber) -> Void) {
        
        // 1. 校验逻辑：如果字符串为空 或者 长度不等于 1024
        guard !faceFeature.isEmpty, faceFeature.count == 1024 else {
            print("FaceAISDK: 特征值无效，插入失败 (Length: \(faceFeature.count))")
            callback(0)
            return // 校验失败，直接返回
        }
        
        // 2. 校验通过，保存数据
        UserDefaults.standard.set(faceFeature, forKey: faceID)
        
        // 3. 回调成功
        callback(1)
    }
	
	
	// MARK: - 活体检测
	public static func showLivenessVerify(_ livenessType: NSNumber,
	                                      _ motionLivenessTypes: String,
	                                      _ callback: @escaping (NSNumber) -> Void) {
	    
	    guard let topVC = getTopViewController() else {
	        print("Error: Could not find top ViewController")
	        return
	    }
	    
	    var hostingController: UIHostingController<LivenessDetectView>? = nil
	    let faceLivenessTypeInt = livenessType.intValue
	    
	    let sdkView = LivenessDetectView(
	        livenessType: faceLivenessTypeInt,
	        motionLiveness: motionLivenessTypes, 
	        onDismiss: { (resultCode: Int) in 
	            
	            DispatchQueue.main.async {
	                hostingController?.dismiss(animated: true) {
	                    let numberCode = NSNumber(value: resultCode)
	                    callback(numberCode)
	                }
	            }
	        }
	    )
	    
	    hostingController = UIHostingController(rootView: sdkView)
	    hostingController?.modalPresentationStyle = .fullScreen
	    
	    topVC.present(hostingController!, animated: true, completion: nil)
	}
	
	

	// 1:1 人脸识别
	public static func showFaceVerify(_ faceID: String,
	                                  _ threshold: NSNumber,
	                                  _ livenessType: NSNumber,
	                                  _ motionLivenessTypes: String,
	                                  _ callback: @escaping (NSNumber) -> Void) {
	    
	    guard let topVC = getTopViewController() else {
	        print("Error: Could not find top ViewController")
	        return
	    }
	    
	    var hostingController: UIHostingController<VerifyFaceView>? = nil
		let floatThreshold = threshold.floatValue
		let faceLivenessTypeInt = livenessType.intValue
		
        let sdkView = VerifyFaceView(
            faceID: faceID,
            threshold: floatThreshold, 
			livenessType: faceLivenessTypeInt,
			motionLiveness: motionLivenessTypes, 
            onDismiss: { (resultCode: Int) in 
                
                DispatchQueue.main.async {
                    hostingController?.dismiss(animated: true) {
                        let numberCode = NSNumber(value: resultCode)
                        callback(numberCode)
                    }
                }
            }
        )
	    
	    hostingController = UIHostingController(rootView: sdkView)
	    hostingController?.modalPresentationStyle = .fullScreen
	    
	    topVC.present(hostingController!, animated: true, completion: nil)
	}
	
	
    
    // MARK: - 录入人脸方法
    public static func showAddFaceByCamera(_ faceID: String, 
                                           _ mode: NSNumber, 
                                           _ showConfirm: Bool, 
                                           _ callback: @escaping (NSNumber) -> Void) {
        
        let modeInt = mode.intValue
        
        guard let topVC = getTopViewController() else {
            print("❌ Error: Could not find top ViewController")
            return
        }
		        
        var hostingController: UIHostingController<AddFaceByCamera>? = nil
        
        let sdkView = AddFaceByCamera(
            faceID: faceID,
            onDismiss: { (resultCode: Int) in 
			    DispatchQueue.main.async {
					hostingController?.dismiss(animated: true) {
						let numberCode = NSNumber(value: resultCode)
					    callback(numberCode) //0用户取消  1添加成功
					}
				}
            }
        )
        
        hostingController = UIHostingController(rootView: sdkView)
        hostingController?.modalPresentationStyle = .fullScreen
        
        topVC.present(hostingController!, animated: true, completion: nil)
    }



    // MARK: - 【辅助方法】获取顶层控制器
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