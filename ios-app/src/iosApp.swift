import SwiftUI
import KotlinModules

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> some UIViewController {
        ViewControllerKt.ViewController()
    }
    func updateUIViewController(_ uiViewController: UIViewControllerType, context: Context) {}
}

struct ContentView: View {
    var body: some View {
        ComposeView().ignoresSafeArea(.all, edges: .bottom)
    }
}

@main
struct iosApp: SwiftUI.App {
    var body: some Scene {
        WindowGroup {
            ContentView()
                .ignoresSafeArea()
        }
    
    }
}
