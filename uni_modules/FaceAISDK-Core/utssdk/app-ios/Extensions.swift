import SwiftUI

// 1. 扩展 Color 类
extension Color {
    // 2. 定义静态属性 faceMain
    // 直接使用 RGB 数值 (对应 #0B4D46)
    static var faceMain: Color {
        return Color(red: 11 / 255.0, green: 77 / 255.0, blue: 70 / 255.0)
    }
}