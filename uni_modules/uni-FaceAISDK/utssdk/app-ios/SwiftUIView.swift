import SwiftUI

struct SwiftUIView: View {
    var body: some View {
        VStack {
            Image(systemName: "globe")
                .imageScale(.large)
                .foregroundStyle(.tint)
            Text("CI CD 发布验证，无功能!")
        }
        .padding()
    }
}   

// // 暴露给UTS的工厂方法
// export function SwiftUIView(params: [String: Any], delegate: SwiftUINavigationDelegate) -> some View {
//     ContentView()
// }