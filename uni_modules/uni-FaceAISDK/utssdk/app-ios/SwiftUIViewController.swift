// // 创建 UIViewController 包装器（SwiftUIViewController.swift）
// import SwiftUI

// class SwiftUIViewController: UIViewController {
//     var onResult: (([String: Any]) -> Void)?
    
//     override func viewDidLoad() {
//         super.viewDidLoad()
        
//         let swiftUIView = MySwiftUIView { result in
//             self.dismiss(animated: true) {
//                 self.onResult?(result) // 回调后关闭页面
//             }
//         }
        
//         let hostingController = UIHostingController(rootView: swiftUIView)
//         addChild(hostingController)
//         view.addSubview(hostingController.view)
        
//         hostingController.view.translatesAutoresizingMaskIntoConstraints = false
//         NSLayoutConstraint.activate([
//             hostingController.view.topAnchor.constraint(equalTo: view.topAnchor),
//             hostingController.view.bottomAnchor.constraint(equalTo: view.bottomAnchor),
//             hostingController.view.leadingAnchor.constraint(equalTo: view.leadingAnchor),
//             hostingController.view.trailingAnchor.constraint(equalTo: view.trailingAnchor)
//         ])
        
//         hostingController.didMove(toParent: self)
//     }
// }