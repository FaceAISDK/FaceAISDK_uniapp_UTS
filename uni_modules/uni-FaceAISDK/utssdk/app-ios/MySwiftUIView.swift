// // 在 UTS 插件的 ios 目录下创建 SwiftUI 视图文件（如 MySwiftUIView.swift）
// import SwiftUI

// struct MySwiftUIView: View {
//     // 回调闭包，用于返回 JSON 数据
//     var onResult: ([String: Any]) -> Void
    
//     var body: some View {
//         VStack {
//             Text("SwiftUI 页面")
//             Button("提交数据") {
//                 // 构造 JSON 数据
//                 let result: [String: Any] = [
//                     "status": "success",
//                     "message": "操作完成",
//                     "data": ["value": 42]
//                 ]
//                 onResult(result) // 触发回调
//             }
//         }
//     }
// }